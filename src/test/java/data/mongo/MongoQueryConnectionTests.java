package data.mongo;



import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import refactormepleasehansen.MongoConnection;

import java.net.ConnectException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Flashed on 05-05-2017.
 */
public class MongoQueryConnectionTests {

    private static MongoConnection mongoConnection;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    @BeforeClass
    public static void setup() throws ConnectException {
        mongoConnection = new MongoConnection("mongodb://localhost", "27017", "user", "password");
        mongoClient = mongoConnection.getConnection("mongodb://localhost", "27017", "user", "password");
        mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, "storhest");
    }

    @Test
    public void validConnectionTest() throws ConnectException {
        MongoClient connection = mongoConnection.getConnection("mongodb://localhost", "27017", "user", "password");
        assertThat(connection, is(notNullValue()));
        connection.close();
    }


    @Test(expected = ConnectException.class)
    public void invalidConnectionTest() throws ConnectException {
        MongoClient connection = mongoConnection.getConnection("mongodb://clearlywrongpath", "27017", "user", "password");

    }

    @Test
    public void validDatabaseTest() throws ConnectException {
        assertThat(mongoClient, is(notNullValue()));
        MongoDatabase mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, "storhest");
        assertThat(mongoDatabase, is(notNullValue()));

    }

    @Test
    public void invalidDatabaseTest() throws ConnectException {
        assertThat(mongoClient, is(notNullValue()));
        MongoDatabase mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, "wrongdatabase");
        assertThat(mongoDatabase, is(nullValue()));
    }

    @Test
    public void getValidCollectionTest(){
        assertThat(mongoDatabase, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getMongoCollection(mongoDatabase, "megetstorhest"); //Get real collection name...
        assertThat(mongoCollection, is(notNullValue()));
    }

    @Test
    public void getInvalidCollectionTest(){
        assertThat(mongoDatabase, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getMongoCollection(mongoDatabase, "wrongcollection"); //Get real collection name...
        assertThat(mongoCollection, is(nullValue()));
    }

    @Test
    public void getWorkableCollectionTest(){
        assertThat(mongoConnection, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getWorkableMongoCollection("storhest", "megetstorhest");
        assertThat(mongoCollection, is(notNullValue()));
    }

    @AfterClass
    public static void close(){
        mongoClient.close();
    }

}
