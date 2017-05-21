CREATE DATABASE gutenberg;
USE gutenberg;

DROP TABLE author_book_join;
DROP TABLE location_book_join;
DROP TABLE author;
DROP TABLE book;
DROP TABLE location;

CREATE TABLE author (
  author_id INT NOT NULL PRIMARY KEY ,
  name VARCHAR( 60 ) NOT NULL
);

CREATE INDEX author_id on author (author_id);

CREATE TABLE book (
  book_id INT NOT NULL PRIMARY KEY ,
  title VARCHAR( 100 ) NOT NULL
);

CREATE INDEX book_id on book (book_id);

CREATE TABLE location (
  city_name VARCHAR(50) NOT NULL PRIMARY KEY ,
  lat FLOAT( 10, 6 ) NOT NULL ,
  lng FLOAT( 10, 6 ) NOT NULL ,
  geom GEOMETRY NOT NULL
);

CREATE INDEX city_name on location (city_name);

CREATE TABLE author_book_join (
   book_id INT NOT NULL,
   author_id INT NOT NULL,
  FOREIGN KEY (author_id) REFERENCES author(author_id),
  FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE location_book_join (
  book_id INT NOT NULL,
  city_name VARCHAR(50) NOT NULL,
  FOREIGN KEY (city_name) REFERENCES location(city_name),
  FOREIGN KEY (book_id) REFERENCES book(book_id)
);
