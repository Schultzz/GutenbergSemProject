package data.mysql;

import data.dto.BookDTO;
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 05-05-17.
 */
public class MySqlQueryTest {


    private static MySqlConnector mySqlConnector;
    private static String expectedUrl = "jdbc:mysql://127.0.0.1/gutenberg_test";
    private static String expectedUsername = "root";
    private static String expectedPassword = "pwd";
    private Connection connection;

    private IDataSet xmlDataSet;
    private IDatabaseConnection dbConnection;


    @BeforeClass
    public static void setUp() throws Exception {
        mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);
    }

    @Before
    public void setUpEach() throws SQLException, DatabaseUnitException, FileNotFoundException {

        mySqlConnector.open();
        connection = mySqlConnector.getConnection();
        dbConnection = new DatabaseConnection(connection, "gutenberg_test");
        dbConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        xmlDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("scripts/mysqldataset.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, xmlDataSet);
        mySqlConnector.close();
    }



    @Test
    public void getValidBooksValidAuthor() {
        //arrange
        MySqlQuery query = new MySqlQuery(mySqlConnector);

        //act
        List<BookDTO> books = query.getBooksByAuthor("Gery Errigo");

        //assert
        assertThat(books, notNullValue());
        assertThat(books.get(0).getId(), is(1));
        assertThat(books.get(0).getTitle(), is("Aluminum Zirconium Tetrachlorohydrex Gly"));
        assertThat(books.get(0).getAuthor(), is("Gery Errigo"));
    }

    @Test
    public void returnNullWhenAuthorNotExist() {
        //arrange
        MySqlQuery query = new MySqlQuery(mySqlConnector);

        //act
        List<BookDTO> books = query.getBooksByAuthor("I don't exist");

        //assert
        assertThat(books, nullValue());
    }


}
