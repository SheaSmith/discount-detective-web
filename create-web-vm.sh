# Update the APT sources and install Git and Java
apt-get update
apt-get install -y openjdk-11-jdk apache2 dos2unix

# Create the appropriate app folder
mkdir /var/app