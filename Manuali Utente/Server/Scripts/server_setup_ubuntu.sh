dbname="getoutdb";
username="getoutdb"
wget http://dev.mysql.com/get/mysql-apt-config_0.8.9-1_all.deb
dpkg -i mysql-apt-config_0.8.9-1_all.deb
rm mysql-apt-config_0.8.9-1_all.deb
apt-get update
apt-get -y install mysql-server
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
wget https://www.dropbox.com/s/nh0oxf3fuyfh4bn/getout_dump.sql
mysql -u$username -p$rootpasswd getoutdb < getout_dump.sql
rm getout_dump.sql
wget https://www.dropbox.com/s/shquf455ttlzibj/Server.tar.xz
tar xf Server.tar.xz
rm Server.tar.xz
sudo java -jar ServerProject.jar
echo "FATTO."
exit
