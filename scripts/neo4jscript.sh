echo starting neo4j script
cd /
cd var/lib/neo4j/import

echo removing old files
sudo rm full.csv
sudo rm cityRelations.csv
sudo rm authorRelations.csv
sudo rm books.csv
sudo rm author.csv
sudo rm realcities15000.csv
sudo rm csv.rar
sudo rm csv
sudo rm neo4jScript.cql

echo downloading files
wget https://github.com/guffeluffe/travisEx/raw/master/csv.rar
wget https://cdn.discordapp.com/attachments/315148671601606656/315506643662143488/realcities15000.csv

echo unpacking files
sudo apt-get install unrar
unrar e csv.rar

echo setting headers
sed -i '1s;^;name,latitude,longitude\n;' realcities15000.csv
sed -i '1s;^;id,name\n;' author.csv
sed -i '1s;^;id,title\n;' books.csv
sed -i '1s;^;bookid,authorid\n;' authorRelations.csv
sed -i '1s;^;bookid,cityName\n;' cityRelations.csv

echo resetting db
sudo neo4j stop
sudo rm -Rf ../data/databases/graph.db

echo inserting data - can take up to 10 minutes
cat neo4jcypherScript.cypher | cypher-shell -u neo4j -p test

echo starting neo4j
sudo neo4j start
