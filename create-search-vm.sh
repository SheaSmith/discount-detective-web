# Fix for an apt lock issue
systemctl disable apt-daily.service
systemctl disable apt-daily.timer

# Get the GPG key for ElasticSearch and add it the APT key cache
curl -fsSL https://artifacts.elastic.co/GPG-KEY-elasticsearch | apt-key add -

# Add the elastic search APT repo to the APT sources list
echo "deb https://artifacts.elastic.co/packages/7.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-7.x.list

apt-get update

# Install ElasticSearch
apt-get install -y elasticsearch

# Start the ElasticSearch service
systemctl start elasticsearch

# ...and enable it to boot on startup
systemctl enable elasticsearch