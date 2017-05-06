package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import data.guffeLaverSovsen.Neo4jConnection;
import data.guffeLaverSovsen.Neo4jQuery;
import org.junit.Test;
import org.neo4j.driver.v1.Session;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Uffe on 06-05-2017.
 */
public class Neo4jConnectionTEST {
    @Test
    public void testValidNeo4jConnection() {
        Neo4jConnection con = new Neo4jConnection();
        Session session = con.getConnection("bolt://localhost:7687", "neo4j", "class");
        assertThat(session, is(notNullValue()));
        session.close();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidNeo4jConnection() {
        Neo4jConnection con = new Neo4jConnection();
        Session session = con.getConnection("urlwithoutdb.com", "neo4j", "class");
        assertThat(session == null, is(true));
        session.close();
    }
}
