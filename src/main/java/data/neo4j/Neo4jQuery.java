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
        String query = "MATCH (b:Book)-[:AUTHORED_BY]->(:Author {name:\"" + author + "\"}) " +
                "MATCH (b)-[:AUTHORED_BY]->(a:Author) " +
                "MATCH (b)-[:MENTIONS]->(c:City) " +
                "RETURN collect(distinct(a)) as authors, collect(distinct(c)) as cities, b as book";
        StatementResult result = session.run(query);
        List<BookDTO> resultList = convertToBookDTO(result);
        session.close();

        return resultList;
    }


    public List<BookDTO> getBooksByCity(String city) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        String query = "MATCH (b:Book)-[:MENTIONS]->(:City{name: \"" + city + "\"}) " +
                "MATCH (b)-[:AUTHORED_BY]->(a:Author)" +
                "MATCH (b)-[:MENTIONS]->(c:City) " +
                "RETURN collect(distinct(a)) as authors, collect(distinct(c)) as cities, b as book";
        StatementResult result = session.run(query);
        List<BookDTO> resultList = convertToBookDTO(result);
        session.close();

        return resultList;

    }

    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        String query = "MATCH (c:City) " +
                "WITH point({longitude: toFloat(c.longitude), latitude: toFloat(c.latitude)}) AS aPoint, point({latitude: "+ lat +", longitude: "+ lon +"}) AS bPoint, c " +
                "WITH DISTINCT round(distance(aPoint, bPoint)) AS distance, c " +
                "ORDER BY distance DESC " +
                "WHERE distance < "+ distance +" " +
                "MATCH (b:Book)-[:MENTIONS]->(c) " +
                "MATCH (b)-[:AUTHORED_BY]->(a:Author) "+
                "RETURN distance, c.name, b as book, collect(DISTINCT(c)) as cities, collect(DISTINCT(a)) as authors";
        StatementResult result = session.run(query);
        List<BookDTO> resultList = convertToBookDTO(result);
        session.close();

        return resultList;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        String query = "MATCH (b:Book{title:\"" + bookTitle + "\"})-[:MENTIONS]->(c:City) " +
                "RETURN collect(distinct(c)) as cities";
        StatementResult result = session.run(query);
        List<CityDTO> resultList = convertToCityDTO(result);
        session.close();

        return resultList;
    }

    private List<CityDTO> convertToCityDTO(StatementResult res) {
        List<CityDTO> resultList = new ArrayList();

        while (res.hasNext()) {
            Record record = res.next();

            Value cities = record.get("cities");
            for (Value val : cities.values()) {
                try{
                    resultList.add(new CityDTO(val.get("name").asString(), Double.parseDouble(val.get("longitude").asString()),Double.parseDouble(val.get("latitude").asString())));
                }
                catch(org.neo4j.driver.v1.exceptions.value.Uncoercible e){
                    System.out.println("Error in converting values to correct types: City: "+val.get("name").asString() +" \nLatitude: " + val.get("latitude").asString() + ", Longitude: " + val.get("longitude").asString());
                }
            }
        }
        return resultList;
    }

    private List<BookDTO> convertToBookDTO(StatementResult res) {
        List<BookDTO> resultList = new ArrayList<BookDTO>();

        while (res.hasNext()) {
            Record record = res.next();

            Value book = record.get("book");
            Value authors = record.get("authors");
            Value cities = record.get("cities");
            BookDTO bk = new BookDTO(Integer.parseInt(book.get("id").asString()), book.get("title").asString(), "");
            for (Value val : authors.values()) {
                bk.addAuthor((val.get("name").asString()));
            }
            for (Value val : cities.values()) {
                bk.addCity(new CityDTO(val.get("name").asString(), Double.parseDouble(val.get("longitude").asString()),Double.parseDouble(val.get("latitude").asString())));
            }

            resultList.add(bk);
        }
        return resultList;
    }
}
