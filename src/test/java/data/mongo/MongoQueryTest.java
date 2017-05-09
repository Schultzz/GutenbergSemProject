package data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import data.dto.BookDTO;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import refactormepleasehansen.MongoConnection;
import refactormepleasehansen.MongoQuery;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


/**
 * Created by Flashed on 07-05-2017.
 */
public class MongoQueryTest {

    private static MongoQuery mongoQuery;
    private static int port;
    private static String databaseName;
    private static String user;
    private static String password;
    private static String connectionString;
    private static String bookCollectionName;

    @BeforeClass
    public static void setup() throws IOException {
        //Setup variables
        databaseName = "testDB";
        user = "user";
        password = "password";
        connectionString = "mongodb://localhost";
        bookCollectionName = "books";

        //In-memory DB setup.
        MongodStarter starter = MongodStarter.getDefaultInstance();
        port = Network.getFreeServerPort();
        MongodExecutable _mongodExe = starter.prepare(createMongodConfig());
        _mongodExe.start();
        MongoClient _mongoClient = new MongoClient("localhost", port);
        _mongoClient.getDatabase(databaseName).createCollection(bookCollectionName);


        //Test setup
        List<Document> documents = new ArrayList<Document>(); //Id's could be ints.
        documents.add(new Document("bookId", "1000").append("title", "Best book").append("author", "Ernest Hemingway"));
        documents.add(new Document("bookId", "1010").append("title", "Good book").append("author", "Ernest Hemingway"));
        documents.add(new Document("bookId", "1020").append("title", "Another book").append("author", "Stephen King"));
        _mongoClient.getDatabase(databaseName).getCollection(bookCollectionName).insertMany(documents);
        mongoQuery = new MongoQuery(connectionString, port+"", user, password, databaseName);
    }

    private static IMongodConfig createMongodConfig() throws IOException {
        return createMongodConfigBuilder().build();
    }

    private static MongodConfigBuilder createMongodConfigBuilder() throws IOException {
        return new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()));
    }

    @Test
    public void queryBooksByAuthor(){

        MongoCursor<Document> cursor = mongoQuery.queryBooksByAuthor("Ernest Hemingway", "books");

        int counter = 0;

        while (cursor.hasNext()){
            cursor.next();
            counter++;
        }

        assertThat(counter, is(2));
    }

    @Test
    public void getBooksByAuthorTest(){
        List<BookDTO> books = mongoQuery.getBooksByAuthor("Ernest Hemingway");
        assertThat(books, hasSize(2));

    }

    @Test
    public void getBooksByUnknownAuthorTest(){
        List<BookDTO> books = mongoQuery.getBooksByAuthor("Morten Laursen");
        assertThat(books, hasSize(0));

    }
}
