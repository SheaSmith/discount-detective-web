##################################################################################
# Global configuration
##################################################################################

# Configure necessary terraform providers
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

# Specify AWS region, as I'm using a learner academy account (for now), this can only be us-east-1. Ideally as this is a NZ specific app, this should be an AU region (or even NZ once it launches...).
provider "aws" {
  region = "us-east-1"
}

##################################################################################
# Specifying IP address ranges.
##################################################################################

# Setup the VPC so the different components can communicate.
resource "aws_vpc" "vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true

  tags = {
    Name = "DD VPC"
  }
}

# Create a subnet for the VMs/db/search.
resource "aws_subnet" "public" {
  vpc_id            = aws_vpc.vpc.id
  availability_zone = "us-east-1a"
  cidr_block = "10.0.3.0/24"

  depends_on = [
    aws_internet_gateway.igw
  ]

  tags = {
    Name = "DD Public Subnet"
  }
}

# First DB subnet, to ensure better availability
resource "aws_subnet" "db-1" {
  vpc_id                  = aws_vpc.vpc.id
  map_public_ip_on_launch = false
  availability_zone       = "us-east-1a"
  cidr_block = "10.0.1.0/24"
}

# Second DB subnet, to ensure better availability
resource "aws_subnet" "db-2" {
  vpc_id                  = aws_vpc.vpc.id
  map_public_ip_on_launch = false
  availability_zone       = "us-east-1b"
  cidr_block = "10.0.2.0/24"
}

##################################################################################
# Generic networking boilerplate.
##################################################################################

# Create the internet gateway.
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id

  tags = {
    Name = "DD Internet Gateway"
  }
}

# And the route table.
resource "aws_route_table" "route_table" {
  vpc_id = aws_vpc.vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "DD Route Table"
  }
}

# Create the route table association.
resource "aws_route_table_association" "table_association" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.route_table.id
}

##################################################################################
# Security groups
##################################################################################

# Create the security group for securing the API.
resource "aws_security_group" "api" {
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "MYSQL"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "DD API Security Group"
  }
}

# Create the security group for allowing DB access.
resource "aws_security_group" "db" {
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    cidr_blocks     = ["0.0.0.0/0"]
    security_groups = ["${aws_security_group.api.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "DD Allow DB Access"
  }
}

##################################################################################
# API
##################################################################################

# Create the API EC2 instance, including the provisioning script.
resource "aws_instance" "api" {
  ami           = "ami-0bfa1783225ce047b"
  instance_type = "m6gd.large"
  subnet_id     = aws_subnet.public.id
  user_data     = data.template_file.api_provision.rendered

  security_groups = [
    aws_security_group.api.id
  ]

  depends_on = [
    aws_db_instance.db
  ]

  tags = {
    Name = "DD API Server"
  }
}

# Create provisioning script for the API.
data "template_file" "api_provision" {
  template = file("./aws-api-provision.tpl")
  vars = {
    db_endpoint = aws_db_instance.db.endpoint
  }
}

# Create the EIP for the API, so it has a public (as well as fixed private) IP address.
resource "aws_eip" "api_eip" {
  vpc      = true
  instance = aws_instance.api.id

  depends_on = [
    aws_internet_gateway.igw
  ]

  tags = {
    Name = "DD API EIP"
  }
}

##################################################################################
# Database
##################################################################################

resource "aws_db_instance" "db" {
  allocated_storage      = 10
  db_name                = "DiscountDetective"
  engine                 = "mysql"
  engine_version         = "5.7"
  instance_class         = "db.t3.micro"
  username               = "discountdetective"
  password               = "DbUserChangeThis"
  skip_final_snapshot    = true
  vpc_security_group_ids = ["${aws_security_group.db.id}"]
  db_subnet_group_name   = aws_db_subnet_group.db.id

  tags = {
    Name = "DD Database"
  }
}

resource "aws_db_subnet_group" "db" {
  subnet_ids = [aws_subnet.db-1.id, aws_subnet.db-2.id]

  tags = {
    Name = "DD DB Subnet Group"
  }
}

##################################################################################
# Outputs
##################################################################################

output "instance_public_ip" {
  value = aws_eip.api_eip.public_ip
}
