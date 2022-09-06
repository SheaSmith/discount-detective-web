# Update the APT sources and install Git and Java
apt-get update
apt-get install -y git openjdk-11-jdk

# Create the app repository and clone the repository.
mkdir /var/app
git clone https://github.com/SheaSmith/discount-detective-web.git /var/app

# Move the systemd service for the API to the correct folder.
cp /var/app/api/discount-detective.service /etc/systemd/system

# Mark the gradle wrapper as executable
chmod +x /var/app/api/gradlew

# Create the config directory, and move the properties for the deployed app there.
mkdir /var/app/api/config
cp /var/app/api/application-deployed.properties /var/app/api/config/application.properties

# Create the user for running the API.
useradd -m discount-detective

# Git ownership exemption
git config --global --add safe.directory /var/app