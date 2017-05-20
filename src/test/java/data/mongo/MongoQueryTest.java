package data.mongo;

import com.mongodb.MongoClient;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import mongo.MongoQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    private static String cityCollectionName;

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
        cityCollectionName = "cities";


        MongoClient _mongoClient = new MongoClient("localhost");
        _mongoClient.getDatabase(databaseName).drop(); //TODO: refactor or whatever. Needed or an exception for collection already exist is thrown.
        _mongoClient.getDatabase(databaseName).createCollection(bookCollectionName);
        _mongoClient.getDatabase(databaseName).createCollection(cityCollectionName);

        //Test setup
        //Books
        List<Document> documents = new ArrayList<Document>(); //Id's could be ints.
        documents.add(new Document("bookId", "1000").append("title", "Best book").append("author", "Ernest Hemingway").append("cities", Arrays.asList("London", "Copenhagen")));
        documents.add(new Document("bookId", "1010").append("title", "Good book").append("author", "Ernest Hemingway").append("cities", Arrays.asList("London", "Paris")));
        documents.add(new Document("bookId", "1020").append("title", "Another book").append("author", "Stephen King").append("cities", Arrays.asList("Dublin", "Moscow")));
        _mongoClient.getDatabase(databaseName).getCollection(bookCollectionName).insertMany(documents);
        //Cities
        documents = new ArrayList<Document>();
        documents.add(new Document("name", "Copenhagen").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(12.56553, 55.67594))));
        documents.add(new Document("name", "London").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(-0.12574, 51.50853))));
        _mongoClient.getDatabase(databaseName).getCollection(cityCollectionName).insertMany(documents);

        mongoQuery = new MongoQuery(connectionString, port+"", user, password, databaseName);
    }

    @Test
    public void queryBooksByAuthor(){

        List<BookDTO> books = mongoQuery.queryBooksByAuthor("Ernest Hemingway", "books");
        assertThat(books, hasSize(2));
    }

    @Test
    public void bookDocumentToBookDTOTest(){
        Document doc = new Document();
        String bookId = "123";
        doc.put("bookId", bookId);
        String author = "Per Laursen";
        doc.put("author", author);
        String title = "Mechanics 101";
        doc.put("title", title);
        List cities = Arrays.asList("London", "Copenhagen");
        doc.put("cities", cities);


        BookDTO book = mongoQuery.bookDocumentToBookDTO(doc);
        assertThat(book, is(notNullValue()));
        assertThat(book.getId(), is(Integer.parseInt(bookId)));
        assertThat(book.getAuthor(), is(author));
        assertThat(book.getTitle(), is(title));
        assertThat(book.getCities(), hasSize(2));

    }

    @Test
    public void cityDocumentToCityDTOTest(){
        String city = "Lyngby";
        double lat = 0.00;
        double lng = 2.22;
        ArrayList<Double> cords = new ArrayList<Double>();
        cords.add(lng);
        cords.add(lat);
        Document doc = new Document("name", city).append("location", new Document("type", "Point").append("coordinates", cords));
        CityDTO cityDTO = mongoQuery.cityDocumentToCityDTO(doc);
        assertThat(cityDTO, is(notNullValue()));
        assertThat(cityDTO.getCityName(), is(city));
        assertThat(cityDTO.getLatitude(), is(lat));
        assertThat(cityDTO.getLongtitude(), is(lng));
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


    @Test
    public void getBooksByCityTest(){
        List<BookDTO> books = mongoQuery.getBooksByCity("London");
        assertThat(books, hasSize(2));
    }

    @Test
    public void queryBooksByCityTest(){
        List<BookDTO> books = mongoQuery.queryBooksByCity("London", bookCollectionName);
        assertThat(books, hasSize(2));
    }

    @Test
    public void queryCitiesByList(){
        List<String> cities = Arrays.asList("London", "Copenhagen");
        List<CityDTO> cityDTOs = mongoQuery.queryCitiesByList(cities, cityCollectionName);
        assertThat(cityDTOs, hasSize(cities.size()));
    }

    @Test
    public void getCitiesByBookTitleTest(){
        String bookTitle = "Best book";
        List<CityDTO> cities = mongoQuery.getCitiesByBookTitle(bookTitle);
        assertThat(cities, hasSize(2));
    }

    @Test
    public void queryCitiesByBookTitleTest(){
        String bookTitle = "Best book";
        List<CityDTO> cities = mongoQuery.queryCitiesByBookTitle(bookTitle, bookCollectionName);
        assertThat(cities, hasSize(2));
    }

    @Test
    public void queryCitiesByGeoLocation(){
        double lng = 12.56569;
        double lat = 55.67572;
        double distance = 5;
        List<String> cityStrings = mongoQuery.queryCitiesByGeoLocation(lng, lat, distance, cityCollectionName);
        assertThat(cityStrings, hasSize(1));
        assertThat(cityStrings.get(0), is("Copenhagen"));
    }

    @Test
    public void getBooksByGeoLocation(){
        double lng = 12.56569;
        double lat = 55.67572;
        double distance = 5;
        List<BookDTO> books = mongoQuery.getBooksByGeoLocation(lng, lat, distance);
        assertThat(books, hasSize(1));
        assertThat(books.get(0).getTitle(), is("Best book"));
    }
}
