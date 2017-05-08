package data.mongo;

import com.mongodb.client.MongoCursor;
import data.dto.BookDTO;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import refactormepleasehansen.MongoConnection;
import refactormepleasehansen.MongoQuery;

import java.net.ConnectException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


/**
 * Created by Flashed on 07-05-2017.
 */
public class MongoQueryTests {

    private static MongoQuery mongoQuery;
    private static String dataBaseName = "testDB";

    @BeforeClass
    public static void setup() throws ConnectException {
        mongoQuery = new MongoQuery("mongodb://localhost", "27017", "user", "password", dataBaseName);
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
