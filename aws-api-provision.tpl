#!/bin/bash

# Create the app repository and clone the repository.
mkdir /var/app

# Update the APT sources and install Git and Java
apt-get update
apt-get install -y openjdk-11-jdk

# Create the user for running the API.
useradd -m discount-detective

# Clone repo from Git
git clone https://github.com/SheaSmith/discount-detective-web.git /var/app/

# Move the systemd service for the API to the correct folder.
cp -f /var/app/api/discount-detective.service /etc/systemd/system

# Mark the gradle wrapper as executable
chmod +x /var/app/api/gradlew

# Create the config directory, and move the properties for the deployed app there.
mkdir /var/app/api/config
cp /var/app/api/application-aws.properties /var/app/api/config/application.properties
sed  -e "s/\${db_endpoint}/${db_endpoint}/" /var/app/api/config/application.properties

# Enable service
systemctl enable discount-detective

# Transfer ownership of the app directory to this new user, so they can run, modify and access all subfolders/files.
chown -R discount-detective:discount-detective /var/app

# Run the service
# systemctl start discount-detective