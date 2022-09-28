terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region  = "us-east-1"
}

resource "aws_instance" "api_server" {
  ami           = "ami-0bfa1783225ce047b"
  instance_type = "m6gd.xlarge"

  tags = {
    Name = "DiscountDetectiveApiServer"
  }
}