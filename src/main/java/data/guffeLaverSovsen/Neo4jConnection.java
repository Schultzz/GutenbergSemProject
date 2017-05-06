package data.guffeLaverSovsen;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

/**
 * Created by Uffe on 06-05-2017.
 */
public class Neo4jConnection {
    public Session getConnection(String url, String user, String password) {
        Driver driver = GraphDatabase.driver(
                url,
                AuthTokens.basic(user, password));
        return driver.session();
    }
}
