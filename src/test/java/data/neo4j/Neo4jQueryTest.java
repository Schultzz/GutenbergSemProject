package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        String query4 = "MERGE (a:Book { title: \"Sverige for Svenskere\", id: 1 }) " +
                "MERGE (b:Book { title: \"Mit liv i Sverige\", id: 2 }) " +
                "MERGE (c:Book { title: \"Det her er jo bare en test\", id: 3 }) " +
                "MERGE (d:Book { title: \"On the Poles\", id: 4 }) " +
                "MERGE (e:Book { title: \"Per i Vildmarken\", id: 5 }) " +
                "MERGE (g:Author { name: \"Villy Soevndal\", id: 10}) " +
                "MERGE (h:Author { name: \"Per\", id: 11}) " +
                "MERGE (i:Author { name: \"Mogens Lykketoft\", id: 12}) " +
                "MERGE (k:City { name: \"Copenhagen\", latitude: 55.676098, longitude: 12.568337}) " +
                "MERGE (l:City { name: \"Stockholm\", latitude: 59.334591, longitude: 18.063240}) " +
                "MERGE (m:City { name: \"Oslo\", latitude: 59.911491, longitude: 10.757933}) " +
                "MERGE (n:City { name: \"Helsinki\", latitude: 60.192059, longitude: 24.945831}) " +
                "MERGE (a)-[:WRITTENBY]->(g)" +
                "MERGE (b)-[:WRITTENBY]->(g)" +
                "MERGE (b)-[:WRITTENBY]->(i)" +
                "MERGE (c)-[:WRITTENBY]->(i)" +
                "MERGE (d)-[:WRITTENBY]->(g)" +
                "MERGE (e)-[:WRITTENBY]->(h)" +
                "MERGE (a)-[:CONTAINS]->(k)" +
                "MERGE (b)-[:CONTAINS]->(l)" +
                "MERGE (c)-[:CONTAINS]->(m)" +
                "MERGE (d)-[:CONTAINS]->(n)" +
                "MERGE (e)-[:CONTAINS]->(k)";
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

    @Ignore
    @Test
    public void testInvalidBooksByAuthorQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Magnus Henriksen");
        assertThat(DTOBooks.size() == 0, is(true));
    }

    @Ignore
    @Test
    public void testValidBooksByCityQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Magnus Henriksen");
        assertThat(DTOBooks.size() == 0, is(true));
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
        //session.runQuery(query1);
        //session.runQuery(query2);
        //session.runQuery(query3);
        //session.runQuery(query4);
        session.close();
        driver.close();
    }
}
