# Update the APT sources and install Git and Java
apt-get update
apt-get install -y openjdk-11-jdk dos2unix

# Create the app repository and clone the repository.
mkdir /var/app

# Create the user for running the API.
useradd -m discount-detective