USE gutenberg;

#1 Given a city name your application returns all book titles with corresponding authors that mention this city.
EXPLAIN SELECT DISTINCT b.title, c.city_name
FROM author a
  INNER JOIN author_book_join ab
    ON a.author_id = ab.author_id
  INNER JOIN book b
    ON ab.book_id = b.book_id
  INNER JOIN location_book_join c
    ON b.book_id = c.book_id
WHERE city_name = "New York";

#2 Given a book title, your application plots all cities mentioned in this book onto a map.
SELECT l.city_name, l.lat, l.lng
FROM location l
  INNER JOIN location_book_join lb
    ON l.city_name = lb.city_name
  INNER JOIN book b
    ON b.book_id = lb.book_id
  WHERE title = "Belgians Under the German Eagle";

#3 Given an author name your application lists all books written by that author and plots all cities mentioned in any of the books onto a map.
SELECT DISTINCT b.book_id, b.title, l.city_name, l.lng, l.lat, author.author_id
FROM location l
  INNER JOIN location_book_join
    ON l.city_name = location_book_join.city_name
  INNER JOIN book b
    ON location_book_join.book_id = b.book_id
  INNER JOIN author_book_join
    ON b.book_id = author_book_join.book_id
  INNER JOIN author ON author_book_join.author_id = author.author_id
  WHERE author.name = "Jean Massart";

#4 Given a geolocation, your application lists all books mentioning a city in vicinity of the given geolocation.
EXPLAIN SELECT DISTINCT b.title, location.city_name
FROM book b
  INNER JOIN location_book_join
  ON b.book_id = location_book_join.book_id
  INNER JOIN location
  ON location_book_join.city_name = location.city_name
  WHERE st_distance_sphere(geom, point(-73.935242, 40.730610)) <= 5 * 1000;


#---------------------------------------------------------------------------------------------------------#

SELECT * FROM location where st_distance_sphere(geom, point(-73.935242, 40.730610)) <= 50 * 1000;

SELECT city_name FROM location where st_distance_sphere(geom, point(12.568337, 55.676098)) <= 50 * 1000;

SELECT
    city_name, lat, lng, st_astext(geom) as geom
FROM
    location;

SELECT * FROM location;

SELECT * FROM book
WHERE book_id = 29624;

SELECT *
FROM book
WHERE title = "mein kamf";

SELECT *
FROM author
WHERE name = "unknown";

SELECT DISTINCT count(title)
FROM book;


SELECT count(book_id) as cnt, book_id
FROM location_book_join
GROUP BY book_id
ORDER BY cnt DESC;

SELECT * FROM book WHERE book_id = 40900;

SELECT * FROM location WHERE city_name = "Hamburg"