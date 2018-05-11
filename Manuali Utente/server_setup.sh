#!/bin/bash
echo "CERCO IL SISTEMA OPERATIVO"
DISTRO=`lsb_release -i | cut -f 2-`
echo "TROVATO $DISTRO"
dbname = "getoutdb";
username = "getoutdb"
echo "INSTALLO MYSQL"
if [ $DISTRO="Ubuntu" ] 
then
	wget http://dev.mysql.com/get/mysql-apt-config_0.8.9-1_all.deb
	dpkg -i mysql-apt-config_0.8.9-1_all.deb
	rm mysql-apt-config_0.8.9-1_all.deb
	apt-get update
	apt-get -y install mysql-server
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
read rootpasswd
echo "Creo il database..."
mysql -uroot -p${rootpasswd} -e "CREATE DATABASE ${dbname} /*\!40100 DEFAULT CHARACTER SET utf8 */;"
echo "Database creato con successo!"
echo "Creo l'utente..."
mysql -uroot -p${rootpasswd} -e "CREATE USER ${username}@localhost IDENTIFIED BY '${userpass}';"
echo "Utente creato con successo!"
echo ""
echo "Granting ALL privileges on ${dbname} to ${username}!"
mysql -uroot -p${rootpasswd} -e "GRANT ALL PRIVILEGES ON ${dbname}.* TO '${username}'@'localhost';"
mysql -uroot -p${rootpasswd} -e "FLUSH PRIVILEGES;"
mysql -u ${username} -p${password} getoutdb < getout_dump.sql
echo "FATTO."
exit


