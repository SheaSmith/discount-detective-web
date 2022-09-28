# Update the APT sources and install Git and Java
apt-get update
apt-get install -y openjdk-11-jdk dos2unix

# Create the app repository and clone the repository.
mkdir /var/app

# Create the user for running the API.
useradd -m discount-detective

# Copy the new API files to the appropriate place.
cp -r /vagrant/api /var/app/

# Move the systemd service for the API to the correct folder.
cp -f /var/app/api/discount-detective.service /etc/systemd/system

# Mark the gradle wrapper as executable
chmod +x /var/app/api/gradlew

# Create the config directory, and move the properties for the deployed app there.
mkdir /var/app/api/config
cp /var/app/api/application-deployed.properties /var/app/api/config/application.properties

# Convert Windows to Unix line endings.
find /var/app -type f -exec dos2unix {} \;

# Enable service
systemctl enable discount-detective

# Transfer ownership of the app directory to this new user, so they can run, modify and access all subfolders/files.
chown -R discount-detective:discount-detective /var/app

# Run the service
systemctl start discount-detective