# Stop service (if running)
systemctl stop discount-detective

# Navigate to the app folder
cd /var/app

# Pull latest changes
git pull origin master

# Transfer ownership of the app directory to this new user, so they can run, modify and access all subfolders/files.
chown -R discount-detective:discount-detective /var/app

# Run the service
systemctl start discount-detective