package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uffe on 05-05-2017.
 */
public class Neo4jQuery implements IQuery {
    private String URL;
    private String USER;
    private String PASSWORD;
    private Neo4jConnection connection;

    public Neo4jQuery(String URL, String USER, String PASSWORD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.connection = new Neo4jConnection();
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        //String query = "MATCH (b:Book)-[:WRITTENBY]->(a:Author{name:\""+author+"\"}) RETURN a as author, b as book";
        String query = "MATCH (b:Book {title:\"Mit liv i Sverige\"})-[:WRITTENBY]->(a:Author) \n" +
                "MATCH (b)-[:CONTAINS]->(c:City) \n" +
                "RETURN collect(distinct(a)) as authors, collect(distinct(c)) as cities, b as book";
        StatementResult result = session.run(query);
        List<BookDTO> resultList = convertToDTO(result);
        session.close();

        return resultList;
    }


    public List<BookDTO> getBooksByCity(String city) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        String query = "MATCH (b:Book)-[co:CONTAINS]->(c:City{name: \"Name\"})\n" +
                "MATCH (b)-[w:WRITTENBY]->(a:Author)\n" +
                "RETURN b.title, a.name";
        return null;
    }

    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) {
        return null;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        return null;
    }

    private List<BookDTO> convertToDTO(StatementResult res) {
        List<BookDTO> resultList = new ArrayList<BookDTO>();

        while (res.hasNext()) {
            Record record = res.next();

            Value book = record.get("book");
            Value authors = record.get("authors");
            Value cities = record.get("cities");
            BookDTO bk = new BookDTO(book.get("id").asInt(), book.get("title").asString(), "");
            for (Value val : authors.values()) {
                bk.addAuthor((val.get("name").asString()));
            }
            for (Value val : cities.values()) {
                bk.addCity(new CityDTO(val.get("name").asString(), val.get("latitude").asDouble(), val.get("longitude").asDouble()));
            }

            resultList.add(bk);
        }
        return resultList;
    }
}
