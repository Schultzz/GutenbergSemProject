package data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import data.dto.BookDTO;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import refactormepleasehansen.MongoQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


/**
 * Created by Flashed on 07-05-2017.
 */

public class MongoQueryTest {

    private static MongoQuery mongoQuery;
    private static int port = 27017;
    private static String databaseName;
    private static String user;
    private static String password;
    private static String connectionString;
    private static String bookCollectionName;

    @BeforeClass
    public static void setup() throws IOException {

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //Setup variables
        databaseName = "testDB";
        user = "user";
        password = "password";
        connectionString = "mongodb://localhost";
        bookCollectionName = "books";

        MongoClient _mongoClient = new MongoClient("localhost");
        _mongoClient.getDatabase(databaseName).drop(); //TODO: refactor or whatever. Needed or an exception for collection already exist is thrown.
        _mongoClient.getDatabase(databaseName).createCollection(bookCollectionName);

        //Test setup
        List<Document> documents = new ArrayList<Document>(); //Id's could be ints.
        documents.add(new Document("bookId", "1000").append("title", "Best book").append("author", "Ernest Hemingway"));
        documents.add(new Document("bookId", "1010").append("title", "Good book").append("author", "Ernest Hemingway"));
        documents.add(new Document("bookId", "1020").append("title", "Another book").append("author", "Stephen King"));
        _mongoClient.getDatabase(databaseName).getCollection(bookCollectionName).insertMany(documents);
        mongoQuery = new MongoQuery(connectionString, port+"", user, password, databaseName);
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
