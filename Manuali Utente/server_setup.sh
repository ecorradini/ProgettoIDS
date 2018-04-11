#!/bin/bash
DISTRO=`lsb_release -i | cut -f 2-`
if [$DISTRO="Ubuntu"] 
then
	wget http://dev.mysql.com/get/mysql-apt-config_0.8.9-1_all.deb
	dpkg -i mysql-apt-config_0.8.9-1_all.deb
	rm mysql-apt-config_0.8.9-1_all.deb
	apt-get update
	apt-get -y install mysql-server
	mysql_secure_installation
	mysql_install_db
	service mysqld start
elif [$DISTRO="CentOS"] 
then
	wget http://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
	yum -y localinstall mysql57-community-release-el7-11.noarch.rpm
	rm mysql57-community-release-el7-11.noarch.rpm
	yum  -y install mysql-community-server
	service mysqld start
elif [$DISTRO="Fedora"] 
then
	wget http://dev.mysql.com/get/mysql57-community-release-fc27-10.noarch.rpm
	dnf -y localinstall mysql57-community-release-fc27-10.noarch.rpm
	rm mysql57-community-release-fc27-10.noarch.rpm
	dnf -y install mysql-community-server
	service mysqld start
fi
