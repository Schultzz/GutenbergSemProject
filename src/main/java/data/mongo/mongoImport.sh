echo "Starting import of books"
mongoimport  --db project --collection books --type csv --headerline --file "/synced_folder/project/books.csv"
echo "Books import done"
echo "Starting books post-processing"
mongo project --eval "db.books.find().forEach(function(elem){var cities = []; var authors = []; if(elem.cities.indexOf(',') !== -1){ cities = elem.cities.split(',');} else{cities.push(elem.cities);}elem.cities = cities;if(elem.authors.indexOf(',') !== -1){authors = elem.authors.split(',');}else{authors.push(elem.authors);}elem.authors = authors;db.books.save(elem);});"
echo "Done books post-processing"
echo "Starting import of cities"
#Addding header-values for TSV
sed -i '1s;^;geonameid\tname\tasciiname\talternatenames\tlatitude\tlongtitude\tfeatureClass\tfeatureCode\tcountryCode\tcc2\tadmin1Code\tadmin2Code\tadmin3Code\tadmin4Code\tpopulation\televation\tdem\ttimezone\tmodificationDate \n;' /synced_folder/project/cities15000.txt
mongoimport --drop --db project --collection cities --type tsv --headerline --file /synced_folder/project/cities15000.txt
echo "Cities import done"
echo "Staring cities post-processing"
mongo project --eval "db.cities.find().forEach(function(elem){var loc = {type: 'Point', coordinates: [elem.longtitude, elem.latitude]};elem.location = loc;var alternatenames = [];if(elem.alternatenames.indexOf(',') !== -1){alternatenames = elem.alternatenames.split(',');}else{alternatenames.push(elem.alternatenames);}elem.alternatenames = alternatenames;db.cities.save(elem);});"
echo "Done cities post-processing"