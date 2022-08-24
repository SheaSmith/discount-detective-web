#!/bin/bash
# Source: https://altitude.otago.ac.nz/cosc349/vagrant-multivm/-/blob/master/build-dbserver-vm.sh

# Fix for an apt lock issue
systemctl disable apt-daily.service
systemctl disable apt-daily.timer

apt-get update
export MYSQL_PWD='RootPasswordChangeThis'

# If you run the `apt-get install mysql-server` command
# manually, it will prompt you to enter a MySQL root
# password. The next two lines set up answers to the questions
# the package installer would otherwise ask ahead of it asking,
# so our automated provisioning script does not get stopped by
# the software package management system attempting to ask the
# user for configuration information.
echo "mysql-server mysql-server/root_password password $MYSQL_PWD" | debconf-set-selections 
echo "mysql-server mysql-server/root_password_again password $MYSQL_PWD" | debconf-set-selections

apt-get -y install mysql-server
service mysql start

echo "CREATE DATABASE DiscountDetective;" | mysql
echo "CREATE USER 'discountdetective'@'%' IDENTIFIED BY 'DbUserChangeThis';" | mysql
echo "GRANT ALL PRIVILEGES ON DiscountDetective.* TO 'discountdetective'@'%'" | mysql

# By default, MySQL only listens for local network requests,
# i.e., that originate from within the dbserver VM. We need to
# change this so that the webserver VM can connect to the
# database on the dbserver VM. Use of `sed` is pretty obscure,
# but the net effect of the command is to find the line
# containing "bind-address" within the given `mysqld.cnf`
# configuration file and then to change "127.0.0.1" (meaning
# local only) to "0.0.0.0" (meaning accept connections from any
# network interface).
sed -i'' -e '/bind-address/s/127.0.0.1/0.0.0.0/' /etc/mysql/mysql.conf.d/mysqld.cnf

# We then restart the MySQL server to ensure that it picks up
# our configuration changes.
service mysql restart