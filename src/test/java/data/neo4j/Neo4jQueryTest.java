package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Created by Uffe on 05-05-2017.
 */
public class Neo4jQueryTest {

    private static final String URL = "bolt://localhost";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "test";


    @BeforeClass
    public static void setup() {
        Driver driver = GraphDatabase.driver(
                URL,
                AuthTokens.basic(USER, PASSWORD));
        Session session = driver.session();
        String query1 = "CREATE INDEX ON :Book(id, title);";
        String query2 = "CREATE INDEX ON :City(name, latitude, longitude);";
        String query3 = "CREATE INDEX ON :Author(id, name);";
        String query4 = "MERGE (a:Book { title: \"Sverige for Svenskere\", id: \"1\" }) " +
                "MERGE (b:Book { title: \"Mit liv i Sverige\", id: \"2\"}) " +
                "MERGE (c:Book { title: \"Awesome Book\", id: \"3\" }) " +
                "MERGE (d:Book { title: \"On the Poles\", id: \"4\" }) " +
                "MERGE (e:Book { title: \"Per i Vildmarken\", id: \"5\" }) " +
                "MERGE (g:Author { name: \"Villy Soevndal\", id: \"10\"}) " +
                "MERGE (h:Author { name: \"Per\", id: \"11\"}) " +
                "MERGE (i:Author { name: \"Mogens Lykketoft\", id: \"11\"}) " +
                "MERGE (k:City { name: \"Copenhagen\", latitude: \"55.676098\", longitude: \"12.568337\"}) " +
                "MERGE (l:City { name: \"Stockholm\", latitude: \"59.334591\", longitude: \"18.063240\"}) " +
                "MERGE (m:City { name: \"Oslo\", latitude: \"59.911491\", longitude: \"10.757933\"}) " +
                "MERGE (n:City { name: \"Helsinki\", latitude: \"60.192059\", longitude: \"24.945831\"}) " +
                "MERGE (a)-[:AUTHORED_BY]->(g)" +
                "MERGE (b)-[:AUTHORED_BY]->(g)" +
                "MERGE (b)-[:AUTHORED_BY]->(i)" +
                "MERGE (c)-[:AUTHORED_BY]->(i)" +
                "MERGE (d)-[:AUTHORED_BY]->(g)" +
                "MERGE (e)-[:AUTHORED_BY]->(h)" +
                "MERGE (a)-[:MENTIONS]->(k)" +
                "MERGE (b)-[:MENTIONS]->(l)" +
                "MERGE (b)-[:MENTIONS]->(m)" +
                "MERGE (e)-[:MENTIONS]->(k)";
        session.run(query1);
        session.run(query2);
        session.run(query3);
        session.run(query4);
        session.close();
        driver.close();
    }

    @Test
    public void testValidBooksByAuthorQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Villy Soevndal");
        assertThat(DTOBooks.size() > 0, is(true));
        for (BookDTO bk : DTOBooks) {
            assertThat(bk.getAuthors(), hasItem("Villy Soevndal"));
            assertThat(bk.getCities(), notNullValue());
            assertThat(bk.getId(), notNullValue());
            assertThat(bk.getTitle(), notNullValue());
        }
    }

    @Test
    public void testInvalidBooksByAuthorQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Magnus Henriksen");
        assertThat(DTOBooks.size(), is(0));
    }

    @Test
    public void testValidBooksByCityQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByCity("Copenhagen");
        assertThat(DTOBooks.size() > 0, is(true));
        for (BookDTO bk : DTOBooks) {
            assertThat(bk.getAuthors().size() > 0, is(true));
            assertThat(bk.getId(), notNullValue());
            assertThat(bk.getTitle(), notNullValue());
            assertThat(bk.getCities().get(0).getCityName(), is("Copenhagen"));
        }
    }

    @Test
    public void testInvalidBooksByCityQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByCity("Roskilde");
        assertThat(DTOBooks.size(), is(0));
    }

    @Test
    public void testValidButEmptyBooksByCityQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByCity("Helsinki");
        assertThat(DTOBooks.size(), is(0));
    }

    @Test
    public void testValidCitiesByBookTitleQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<CityDTO> DTOCities = nq.getCitiesByBookTitle("Mit liv i Sverige");
        assertThat(DTOCities.size(), is(2));

        for (CityDTO city : DTOCities) {
            assertThat(city.getCityName().equalsIgnoreCase("Stockholm") || city.getCityName().equalsIgnoreCase("Oslo"), is(true));
        }
    }

    @Test
    public void testInvalidCitiesByBookTitleQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<CityDTO> DTOCities = nq.getCitiesByBookTitle("Wrong Title!");
        assertThat(DTOCities.size(), is(0));
    }

    @Test
    public void testValidButEmptyCitiesByBookTitleQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<CityDTO> DTOCities = nq.getCitiesByBookTitle("Awesome Book");
        assertThat(DTOCities.size(), is(0));
    }

    @Test
    public void testValidBooksByGeoQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByGeoLocation(10.757933, 59.911491, 800000);
        assertThat(DTOBooks.size(), is(4));
        for (BookDTO bk: DTOBooks) {
            assertThat(bk.getAuthors(), notNullValue());
            assertThat(bk.getTitle(), notNullValue());
            assertThat(bk.getCities(), notNullValue());
            assertThat(bk.getId(), notNullValue());
        }
    }

    @Test
    public void testInvalidBooksByGeoQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByGeoLocation(0, 0, 0);
        assertThat(DTOBooks.size(), is(0));
    }


    @AfterClass
    public static void teardown() {
        Driver driver = GraphDatabase.driver(
                URL,
                AuthTokens.basic(USER, PASSWORD));
        Session session = driver.session();
        String query1 = "MATCH (n) DETACH DELETE n;";
        String query2 = "DROP INDEX ON :Book(id, title);";
        String query3 = "DROP INDEX ON :City(name, latitude, longitude);";
        String query4 = "DROP INDEX ON :Author(id, name);";
        session.run(query1);
        session.run(query2);
        session.run(query3);
        session.run(query4);
        session.close();
        driver.close();
    }
}
