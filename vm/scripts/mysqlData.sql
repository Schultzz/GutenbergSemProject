#POINT(longitude, latitude)

Use gutenberg;

LOAD DATA LOCAL INFILE '/home/ms/Hentet/csv/author.csv'
INTO TABLE author
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE '/home/ms/Hentet/csv/books.csv'
INTO TABLE book
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE '/home/ms/Hentet/csv/authorRelations.csv'
INTO TABLE author_book_join
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE '/home/ms/Hentet/csv/cities15000.txt'
        INTO TABLE location
        FIELDS TERMINATED BY '\t'
        ENCLOSED BY ''
        LINES TERMINATED BY '\n'
        (@col1, @col2, @col3, @col4, @col5, @col6) SET city_name=@col2, lat=@col5, lng=@col6, geom=ST_GeometryFromText(CONCAT('POINT(', @col6, ' ', @col5, ')'));

LOAD DATA LOCAL INFILE '/home/ms/Hentet/csv/cityRelations.csv'
INTO TABLE location_book_join
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';



