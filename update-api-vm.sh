# Stop service (if running)
systemctl stop discount-detective

# Delete old API files
rm -r /var/app/api

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