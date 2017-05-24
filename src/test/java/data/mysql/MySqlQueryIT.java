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
import org.junit.Ignore;
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
public class MySqlQueryIT {

    private static MySqlConnector mySqlConnector;
    private MySqlQuery query;
    private String pathSetup = "src/test/resources/mysql/setup/";

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

        IDataSet authorBook = new FlatXmlDataSetBuilder().build(new FileInputStream(pathSetup + "authorBook.xml"));
        IDataSet locationBook = new FlatXmlDataSetBuilder().build(new FileInputStream(pathSetup + "locationBook.xml"));

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, authorBook);
        DatabaseOperation.TRUNCATE_TABLE.execute(dbConnection, locationBook);
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(pathSetup + "author.xml")));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(pathSetup + "book.xml")));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, new FlatXmlDataSetBuilder().build(new FileInputStream(pathSetup + "location.xml")));
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
        assertThat(books.get(0).getId(), is(51653));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
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
        List<BookDTO> books = query.getBooksByCity("Maliana");

        //assert
        assertThat(books.size(), is(1));
        assertThat(books.get(0).getId(), is(0));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
    }

    @Test
    public void getBooksByInvalidCity() throws SQLException {
        List<BookDTO> books = query.getBooksByCity("I don't exist");
        //assert
        assertThat(books, nullValue());
    }

    @Ignore //Mysql version on travis to old for st_sphere function.
    public void getBooksWithin10kmFromMaliana() {
        List<BookDTO> books = query.getBooksByGeoLocation(125.21972, -8.99167, 10);
        //assert
        assertThat(books.size(), is(1));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
        assertThat(books.get(0).getCities().get(0).getCityName(), is("Maliana"));
    }

    @Ignore
    public void getBooksGeolocationNoHit() {
        List<BookDTO> books = query.getBooksByGeoLocation(0, 0, 0);
        assertThat(books, nullValue());
    }

    @Test
    public void getCitiesByBookTitle() {
        List<CityDTO> cities = query.getCitiesByBookTitle("Pick a Crime");
        //assert
        assertThat(cities.size(), is(1));
        assertThat(cities.get(0).getCityName(), is("Malilipot"));
    }

    @Test
    public void getCitiesByTitleNoHits() {
        List<CityDTO> cities = query.getCitiesByBookTitle("I don't exist");
        assertThat(cities, nullValue());
    }

}
