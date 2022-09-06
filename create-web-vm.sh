# Update the APT sources and install Git and Java
apt-get update
apt-get install -y git openjdk-11-jdk apache2

# Create the app repository and clone the repository.
mkdir /var/app
git clone https://github.com/SheaSmith/discount-detective-web.git /var/app