CREATE INDEX ON :Book(id);

CREATE INDEX ON :City(name);

CREATE INDEX ON :Author(id);

LOAD CSV WITH HEADERS FROM "file:///realcities15000.csv" AS row
MERGE (c:City {name:row.name, latitude: row.latitude, longitude: row.longitude});

LOAD CSV WITH HEADERS FROM "file:///author.csv" AS row
MERGE (a:Author {id: row.id, name:row.name});

LOAD CSV WITH HEADERS FROM "file:///books.csv" AS row
MERGE (b:Book {id: row.id, title:row.title});

MATCH (n:City)
WITH n.name AS name, collect(n) AS nodes
WHERE size(nodes) > 1
FOREACH (n in tail(nodes) | DELETE n);

LOAD CSV WITH HEADERS FROM "file:///authorRelations.csv" AS row
MATCH (b:Book {id: row.bookid})
MATCH (a:Author {id: row.authorid})
WITH a, b
MERGE (b)-[:AUTHORED_BY]->(a);

LOAD CSV WITH HEADERS FROM "file:///cityRelations.csv" AS row
MATCH (b:Book {id: row.bookid})
MATCH (c:City {name: row.cityName})
WITH b, c
MERGE (b)-[:MENTIONS]->(c);
