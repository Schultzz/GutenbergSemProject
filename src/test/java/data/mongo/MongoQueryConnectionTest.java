package data.mongo;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import mongo.MongoConnection;

import java.io.IOException;
import java.net.ConnectException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by Flashed on 05-05-2017.
 */

public class MongoQueryConnectionTest {


    private static MongoConnection mongoConnection;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static int port = 27017;
    private static String databaseName;
    private static String user;
    private static String password;
    private static String connectionString;
    private static String collectionName;

    @BeforeClass
    public static void setup() throws IOException {


        //Setup variables
        databaseName = "testDB";
        user = "user";
        password = "password";
        connectionString = "mongodb://localhost";
        collectionName = "testCollection";

        //Test setup
        mongoConnection = new MongoConnection(connectionString, port + "", user, password);
        mongoClient = mongoConnection.getConnection(connectionString, port + "", user, password);


        //Database preperation
        Document doc = new Document("name", "MongoDB");
        mongoClient.getDatabase(databaseName).drop(); //TODO: refactor or whatever. Needed or an exception for collection already exist is thrown.
        mongoClient.getDatabase(databaseName).createCollection(collectionName);
        mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, databaseName);

    }


    @Test
    public void validConnectionTest() throws ConnectException {
        MongoClient connection = mongoConnection.getConnection(connectionString, port + "", user, password);
        assertThat(connection, is(notNullValue()));
        connection.close();
    }

    @Ignore
    @Test(expected = ConnectException.class)
    public void invalidConnectionTest() throws ConnectException {
        String wrongConnectionString = "mongodb://clearlywrongpath";
        MongoClient connection = mongoConnection.getConnection(wrongConnectionString, port + "", user, password);

    }

    @Test
    public void validDatabaseTest() throws ConnectException {
        assertThat(mongoClient, is(notNullValue()));
        MongoDatabase mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, databaseName);
        assertThat(mongoDatabase, is(notNullValue()));

    }

    @Test
    public void invalidDatabaseTest() throws ConnectException {
        String wrongDatabaseName = "wrongdatabase";
        assertThat(mongoClient, is(notNullValue()));
        MongoDatabase mongoDatabase = mongoConnection.getMongoDatabase(mongoClient, wrongDatabaseName);
        assertThat(mongoDatabase, is(nullValue()));
    }

    @Test
    public void getValidCollectionTest() {
        assertThat(mongoDatabase, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getMongoCollection(mongoDatabase, collectionName); //Get real collection name...
        assertThat(mongoCollection, is(notNullValue()));
    }

    @Test
    public void getInvalidCollectionTest() {
        String wrongCollectionName = "wrongcollection";
        assertThat(mongoDatabase, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getMongoCollection(mongoDatabase, wrongCollectionName); //Get real collection name...
        assertThat(mongoCollection, is(nullValue()));
    }

    @Test
    public void getWorkableCollectionTest() {
        assertThat(mongoConnection, is(notNullValue()));
        MongoCollection mongoCollection = mongoConnection.getWorkableMongoCollection(databaseName, collectionName);
        assertThat(mongoCollection, is(notNullValue()));
    }

    @AfterClass
    public static void close() {
        mongoClient.close();
    }

}
