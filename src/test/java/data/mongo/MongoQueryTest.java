package data.mongo;

import com.mongodb.MongoClient;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;

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
        //Cities
        Document copenhagenDoc = new Document("name", "Copenhagen").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(12.56553, 55.67594)));
        Document londonDoc = new Document("name", "London").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(-0.12574, 51.50853)));
        Document parisDoc =new Document("name", "Paris").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(2.294694, 48.858093)));
        Document dublinDoc =new Document("name", "Dublin").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(-6.266155, 53.350140)));
        Document moscowDoc =new Document("name", "Moscow").append("location", new Document("type", "Point").append("coordinates", Arrays.asList(37.618423, 55.751244)));
        //Books
        List<Document> documents = new ArrayList<Document>(); //Id's could be ints.
        documents.add(new Document("bookId", 1000).append("title", "Best book").append("authors", Arrays.asList("Ernest Hemingway")).append("cities", Arrays.asList(londonDoc, copenhagenDoc)));
        documents.add(new Document("bookId", 1010).append("title", "Good book").append("authors", Arrays.asList("Ernest Hemingway")).append("cities", Arrays.asList(londonDoc, parisDoc)));
        documents.add(new Document("bookId", 1020).append("title", "Another book").append("authors", Arrays.asList("Stephen King")).append("cities", Arrays.asList(dublinDoc, moscowDoc)));
        _mongoClient.getDatabase(databaseName).getCollection(bookCollectionName).insertMany(documents);


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
        int bookId = 123;
        doc.put("bookId", bookId);
        String author = "Per Laursen";
        doc.put("authors", Arrays.asList(author));
        String title = "Mechanics 101";
        doc.put("title", title);
        ArrayList<Double> coords = new ArrayList<Double>();
        coords.add(12.56553);
        coords.add(55.67594);

        List cities = Arrays.asList(new Document("name", "London").append("location", new Document("type", "Point").append("coordinates", coords)),
                                    new Document("name", "Copenhagen").append("location", new Document("type", "Point").append("coordinates", coords)));
        doc.append("cities", cities);


        BookDTO book = mongoQuery.bookDocumentToBookDTO(doc);
        assertThat(book, is(notNullValue()));
        assertThat(book.getId(), is(bookId));
        assertThat(book.getAuthors().get(0), is(author));
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
    public void queryBooksByGeolocationTest(){
        double lng = 12.56569;
        double lat = 55.67572;
        double distance = 1;
        List<BookDTO> bookDTOs = mongoQuery.queryBooksByGeolocation(lng, lat, distance, bookCollectionName);
        assertThat(bookDTOs, hasSize(1));
    }


    @Test
    public void getBooksByGeoLocationTest(){
        double lng = 12.56569;
        double lat = 55.67572;
        double distance = 5;
        List<BookDTO> books = mongoQuery.getBooksByGeoLocation(lng, lat, distance);
        assertThat(books, hasSize(1));
        assertThat(books.get(0).getTitle(), is("Best book"));
    }
}
