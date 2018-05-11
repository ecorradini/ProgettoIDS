dbname="getoutdb";
username="getoutdb"
wget http://dev.mysql.com/get/mysql57-community-release-fc27-10.noarch.rpm
dnf -y localinstall mysql57-community-release-fc27-10.noarch.rpm
rm mysql57-community-release-fc27-10.noarch.rpm
dnf -y install mysql-community-server
service mysqld start
mysql_secure_installation
mysqld --initialize
echo "Inserisci la password del database (che hai inserito prima) e poi INVIO:"
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
mysql -u$username -p$password getoutdb < getout_dump.sql
echo "FATTO."
exit
