package data.mysql;

import data.dto.BookDTO;
import data.dto.CityDTO;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 05-05-17.
 */
public class MySqlQueryTest {

    private static MySqlConnector mySqlConnector;
    private MySqlQuery query;
    private String path = "src/test/resources/mysql/";


    @BeforeClass
    public static void setUp() throws Exception {
        String expectedUrl = "jdbc:mysql://127.0.0.1/gutenberg_test";
        String expectedUsername = "tester";
        String expectedPassword = "pwd";
        mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);
    }

    @Before
    public void setUpEach() throws SQLException, DatabaseUnitException, FileNotFoundException {
        mySqlConnector.open();
        Connection connection = mySqlConnector.getConnection();
        IDatabaseConnection dbConnection = new DatabaseConnection(connection, "gutenberg_test");
        dbConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

        IDataSet authorBook = new FlatXmlDataSetBuilder().build(new FileInputStream(path + "authorBook.xml"));
        IDataSet locationBook = new FlatXmlDataSetBuilder().build(new FileInputStream(path + "locationBook.xml"));

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, authorBook);
        DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, locationBook);
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(path + "author.xml")));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(path + "book.xml")));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(path + "location.xml")));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, authorBook);
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, locationBook);
        mySqlConnector.close();

        //arrange
        query = new MySqlQuery(mySqlConnector);
    }

    @Test
    public void getBooksByValidAuthorFromQuery() throws SQLException {
        //act
        List<BookDTO> books = query.getBooksByAuthor("Various");

        //assert
        assertThat(books.size(), is(4));
    }

    @Test
    public void returnNullWhenAuthorNotExist() throws SQLException {
        //act
        List<BookDTO> books = query.getBooksByAuthor("I don't exist");

        //assert
        assertThat(books, nullValue());
    }

    @Test
    public void getBooksByValidCity() throws SQLException {
        //act
        List<BookDTO> books = query.getBooksByCity("Kent");

    }

    @Test
    public void getBooksByInvalidCity() throws SQLException {
        List<BookDTO> books = query.getBooksByCity("I don't exist");
        //assert
        assertThat(books, nullValue());
    }

    @Test
    public void getBooksWithin10kmFromNewYork() {
        List<BookDTO> books = query.getBooksByGeoLocation(-73.935242, 40.730610, 10);
    }

    @Test
    public void getBooksGeolocationNoHit() {
        List<BookDTO> books = query.getBooksByGeoLocation(0, 0, 0);
        assertThat(books, nullValue());
    }

    @Test
    public void getCitiesByBookTitle() {
        List<CityDTO> cities = query.getCitiesByBookTitle("Torpedo War, and Submarine Explosions");

    }

    @Test
    public void getCitiesByTitleNoHits() {
        List<CityDTO> cities = query.getCitiesByBookTitle("I don't exist");
        assertThat(cities, nullValue());
    }

}
