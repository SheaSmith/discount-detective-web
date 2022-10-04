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

# GitHub PAT for Amplify
variable "github_pat" {
  type = string
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
  vpc_id                  = aws_vpc.vpc.id
  availability_zone       = "us-east-1a"
  cidr_block              = "10.0.3.0/24"
  map_public_ip_on_launch = true

  depends_on = [
    aws_internet_gateway.igw
  ]

  tags = {
    Name = "DD Public Subnet"
  }
}

# Search & db subnet
resource "aws_subnet" "private" {
  vpc_id                  = aws_vpc.vpc.id
  map_public_ip_on_launch = false
  availability_zone       = "us-east-1a"
  cidr_block              = "10.0.4.0/24"

  depends_on = [
    aws_internet_gateway.igw
  ]

  tags = {
    Name = "DD Private Subnet"
  }
}

# DB second subnet
resource "aws_subnet" "private_2" {
  vpc_id                  = aws_vpc.vpc.id
  map_public_ip_on_launch = false
  availability_zone       = "us-east-1b"
  cidr_block              = "10.0.2.0/24"

  depends_on = [
    aws_internet_gateway.igw
  ]

  tags = {
    Name = "DD Private 2 Subnet"
  }
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

# Create the route table association for the public subnet.
resource "aws_route_table_association" "public_association" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.route_table.id
}

# Create the route table association for the private subnet.
resource "aws_route_table_association" "private_association" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.route_table.id
}

# Create the route table association for the private subnet.
resource "aws_route_table_association" "private_association_2" {
  subnet_id      = aws_subnet.private_2.id
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
    description = "Allow MYSQL"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow ElasticSearch"
    from_port   = 9200
    to_port     = 9200
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow API traffic"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    prefix_list_ids = [data.aws_ec2_managed_prefix_list.cloudfront.id]
  }

  tags = {
    Name = "DD API Security Group"
  }
}

# Prefixes for allowing Cloudfront to access the API.
data "aws_ec2_managed_prefix_list" "cloudfront" {
  name  = "com.amazonaws.global.cloudfront.origin-facing"
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

# Create the security group for allowing search access.
resource "aws_security_group" "search" {
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port       = 9200
    to_port         = 9200
    protocol        = "tcp"
    cidr_blocks     = ["0.0.0.0/0"]
    security_groups = ["${aws_security_group.api.id}"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
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
    Name = "DD Allow Search Access"
  }
}

##################################################################################
# AMI
##################################################################################

# Get the AMI for Ubuntu 20.04
data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-arm64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

##################################################################################
# API
##################################################################################

# Create the API EC2 instance, including the provisioning script.
resource "aws_instance" "api" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "m6gd.large"
  subnet_id     = aws_subnet.public.id
  user_data = templatefile("${path.module}/aws-api-provision.tftpl", {
    db_endpoint     = aws_db_instance.db.endpoint,
    search_endpoint = aws_instance.search.private_ip
  })

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

# CDN for the API, so we don't have to deal with SSL on domain names and stuff
resource "aws_cloudfront_distribution" "api" {
  enabled = true

  origin {
    domain_name = aws_eip.api_eip.public_dns
    origin_id   = aws_eip.api_eip.public_dns

    custom_origin_config {
      http_port = 8080
      # Force HTTP only as there is no HTTPS setup on the EC2 instance
      origin_protocol_policy = "http-only"

      # HTTPS config that is required, but not used since we force HTTP only
      https_port           = 8080
      origin_ssl_protocols = ["TLSv1.2"]
    }
  }

  default_cache_behavior {
    allowed_methods        = ["GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE"]
    cached_methods         = ["GET", "HEAD", "OPTIONS"]
    target_origin_id       = aws_eip.api_eip.public_dns
    viewer_protocol_policy = "redirect-to-https"

    forwarded_values {
      headers      = []
      query_string = true

      cookies {
        forward = "all"
      }
    }
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  tags = {
    Name = "API CDN"
  }
}

##################################################################################
# Database
##################################################################################

# Create the RDS database instance.
resource "aws_db_instance" "db" {
  allocated_storage      = 10
  db_name                = "DiscountDetective"
  engine                 = "mysql"
  engine_version         = "8.0"
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

# Create the subnet group for the database.
resource "aws_db_subnet_group" "db" {
  subnet_ids = [aws_subnet.private.id, aws_subnet.private_2.id]

  tags = {
    Name = "DD DB Subnet Group"
  }
}


##################################################################################
# Elastic Search
##################################################################################

# Create the ElasticSearch EC2 instance, including the provisioning script.
# Using EC2 as Amazon Educate doesn't seem to allow access to ElasticSearch
resource "aws_instance" "search" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = "m6gd.large"
  user_data              = file("${path.module}/create-search-vm.sh")
  vpc_security_group_ids = [aws_security_group.search.id]
  subnet_id              = aws_subnet.public.id

  tags = {
    Name = "DD ElasticSearch"
  }
}

##################################################################################
# Frontend
##################################################################################

# Create the Amplify app for the frontend.
resource "aws_amplify_app" "frontend" {
  name                        = "DD Frontend"
  repository                  = "https://github.com/SheaSmith/discount-detective-web"
  access_token                = var.github_pat
  enable_auto_branch_creation = true
  enable_branch_auto_build    = true

  build_spec = templatefile("${path.module}/amplify-build-spec.tftpl", {
    api_endpoint = aws_cloudfront_distribution.api.domain_name
  })

  # The default rewrites and redirects added by the Amplify Console.
  custom_rule {
    source = "/<*>"
    status = "404"
    target = "/index.html"
  }

  # The default patterns added by the Amplify Console.
  auto_branch_creation_patterns = [
    "*",
    "*/**",
  ]

  auto_branch_creation_config {
    # Enable auto build for the created branch.
    enable_auto_build = true
  }
}

##################################################################################
# Outputs
##################################################################################

output "instance_public_ip" {
  value = aws_eip.api_eip.public_ip
}
