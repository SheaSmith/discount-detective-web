apt-get update
apt-get install -y git openjdk-11-jdk
mkdir /var/app
git clone https://github.com/SheaSmith/discount-detective-web.git /var/app
cp /var/app/api/discount-detective.service /etc/systemd/system
chmod +x /var/app/api/gradlew
mkdir /var/app/api/config
cp /var/app/api/application-deployed.properties /var/app/api/config/application.properties
useradd -m discount-detective
chown -R discount-detective:discount-detective /var/app
systemctl enable discount-detective
systemctl start discount-detective