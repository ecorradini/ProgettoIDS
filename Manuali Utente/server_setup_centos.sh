dbname="getoutdb";
username="getoutdb"
wget http://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
yum -y localinstall mysql57-community-release-el7-11.noarch.rpm
rm mysql57-community-release-el7-11.noarch.rpm
yum  -y install mysql-community-server
sudo systemctl start mysqld
mysqld --initialize
grep 'A temporary password is generated for root@localhost' /var/log/mysqld.log
echo "Inserisci la password stampata qui sopra"
mysql_secure_installation
echo "Inserisci la nuova password del database che hai inserito prima"
read rootpasswd
echo "Creo il database..."
mysql -uroot -p$rootpasswd -e "CREATE DATABASE $dbname;"
echo "Database creato con successo!"
echo "Creo l'utente..."
mysql -uroot -p$rootpasswd -e "CREATE USER '$username'@'localhost' IDENTIFIED BY '$rootpasswd';"
echo "Utente creato con successo!"
echo ""
echo "Granting ALL privileges on $dbname to $username!"
mysql -uroot -p$rootpasswd -e "GRANT ALL PRIVILEGES ON $dbname.* TO '$username'@'localhost';"
mysql -uroot -p$rootpasswd -e "FLUSH PRIVILEGES;"
mysql -u$username -p$rootpasswd getoutdb < getout_dump.sql
echo "FATTO."
exit
