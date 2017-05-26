package control.Controller;

import control.controller.Controller;
import control.entities.Book;
import control.services.MapService;
import control.services.QueryService;
import data.mysql.MySqlConnector;
import data.mysql.MySqlQuery;
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

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 26-05-17.
 */
public class ControllerSystemIT {

    private static Controller controller;
    private static MySqlConnector mySqlConnector;
    private MySqlQuery query;
    private String pathSetup = "src/test/resources/mysql/setup/";

    @BeforeClass
    public static void setUp() throws Exception {
        String expectedUrl = "jdbc:mysql://127.0.0.1/gutenberg_test";
        String expectedUsername = "tester";
        String expectedPassword = "pwd";
        mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);
        controller = new Controller(new QueryService(new MySqlQuery(mySqlConnector)), new MapService());
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
    }

    @Test
    public void getBooksByAuthorAndPlotCitiesSystemTest(){
        //act
        List<Book> books = controller.getBooksByAuthorAndPlotCities("Various");

        //assert
        assertThat(books.size(), is(4));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
        assertThat(books.get(1).getTitle(), is("Pick a Crime"));
    }
    @Test
    public void getBookTitlesByCitySystemTest(){
        //act
        List<Book> books = controller.getBookTitlesByCity("Maliana");

        //assert
        assertThat(books.size(), is(1));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
    }
    @Test
    public void plotCitiesByBookTitleSystemTest(){
        //act
        String path = controller.plotCitiesByBookTitle("Pick a Crime");
        assertThat(path, endsWith("map.html"));
    }
    @Ignore
    public void getBooksByGeoLocationSystemTest(){
        //act
        List<Book> books = controller.getBooksByGeoLocation(125.21972, -8.99167, 10);

        //assert
        assertThat(books.size(), is(1));
        assertThat(books.get(0).getTitle(), is("Wenonah's Stories for Children"));
    }


}
