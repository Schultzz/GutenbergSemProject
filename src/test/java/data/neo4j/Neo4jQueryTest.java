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
        String query1 = "CREATE INDEX ON :Book(author);";
        String query2 = "CREATE CONSTRAINT ON (b:Book) ASSERT b.id IS UNIQUE;";
        String query3 = "CREATE (a:Book { author: \"Hans Andersen\", title: \"Sverige for Svenskere\", id: 1 });";
        String query4 = "CREATE (a:Book { author: \"Hans Andersen\", title: \"Mit liv i Sverige\", id: 2 });";
        String query5 = "CREATE (a:Book { author: \"Hans Andersen\", title: \"Det her er jo bare en test\", id: 3 });";
        String query6 = "CREATE (a:Book { author: \"Villy Soevndal\", title: \"On the Poles\", id: 4 });";
        String query7 = "CREATE (a:Book { author: \"Villy Soevndal\", title: \"I will do this in English\", id: 5 });";
        String query8 = "CREATE (a:Book { author: \"Per Henriksen\", title: \"Per i Vildmarken\", id: 6 });";
        session.run(query1);
        session.run(query2);
        session.run(query3);
        session.run(query4);
        session.run(query5);
        session.run(query6);
        session.run(query7);
        session.run(query8);
        session.close();
        driver.close();
    }

    @Ignore
    @Test
    public void testValidNeo4jQuery() {
        IQuery nq = new Neo4jQuery(URL, USER, PASSWORD);
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Villy Soevndal");
        assertThat(DTOBooks.size() > 0, is(true));
        for (BookDTO bk : DTOBooks) {
            assertThat(bk.getAuthors().get(0), is("Villy Soevndal"));
            assertThat(bk.getTitle(), notNullValue());
            assertThat(bk.getId(), notNullValue());
        }
    }

    @Test
    public void testInvalidNeo4jQuery() {
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
        String query2 = "DROP INDEX ON :Book(author);";
        session.run(query1);
        session.run(query2);
        session.close();
        driver.close();
    }
}
