#!/bin/bash
echo "CERCO IL SISTEMA OPERATIVO"
DISTRO=`lsb_release -i | cut -f 2-`
echo "TROVATO $DISTRO"
echo "INSTALLO MYSQL"
if [ $DISTRO="Ubuntu" ] 
then
	wget http://dev.mysql.com/get/mysql-apt-config_0.8.9-1_all.deb
	dpkg -i mysql-apt-config_0.8.9-1_all.deb
	rm mysql-apt-config_0.8.9-1_all.deb
	apt-get update
	apt-get -y install mysql-server
	mysql_secure_installation
	mysql_install_db
	service mysqld start
elif [ $DISTRO=="CentOS" ] 
then
	wget http://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
	yum -y localinstall mysql57-community-release-el7-11.noarch.rpm
	rm mysql57-community-release-el7-11.noarch.rpm
	yum  -y install mysql-community-server
	service mysqld start
elif [ $DISTRO=="Fedora" ] 
then
	wget http://dev.mysql.com/get/mysql57-community-release-fc27-10.noarch.rpm
	dnf -y localinstall mysql57-community-release-fc27-10.noarch.rpm
	rm mysql57-community-release-fc27-10.noarch.rpm
	dnf -y install mysql-community-server
	service mysqld start
fi
mysql_secure_installation
mysqld --initialize
echo "Inserisci la password del database (che hai inserito prima) e poi INVIO:"
read password
mysql -u root -p$password -e "CREATE USER 'getoutdb'@'localhost' IDENTIFIED BY '$password';"
mysql -u root -p$password -e "CREATE DATABASE getoutdb;"
mysql -u root -p$password -e "GRANT ALL PRIVILEGES ON getoutdb TO getoutdb;"
mysql -u getoutdb -p$password getoutdb < getout_dump.sql


