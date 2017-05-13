package data.mysql;

import org.dbunit.DatabaseUnitException;
import org.junit.*;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 08-05-17.
 */
public class MySqlConnectionTest {

    private static MySqlConnector mySqlConnector;
    private static String expectedUrl = "jdbc:mysql://127.0.0.1/gutenberg_test";
    private static String expectedUsername = "tester";
    private static String expectedPassword = "pwd";


    @BeforeClass
    public static void setUp() throws Exception {
        mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);
    }

    @Before
    public void setUpEach() throws SQLException, DatabaseUnitException, FileNotFoundException {

    }

    @After
    public void tearDownEach() {

    }

    @AfterClass
    public static void tearDown() {

    }

    @Test
    public void constructorShouldSetProperties() throws SQLException {
        //arrange

        //act

        //assert
        assertThat(mySqlConnector.getUrl(), is(expectedUrl));
        assertThat(mySqlConnector.getUsername(), is(expectedUsername));
        assertThat(mySqlConnector.getPassword(), is(expectedPassword));
    }

    @Test
    public void returnValidDatabaseConnection() throws SQLException {
        //arrange
        MySqlConnector mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);

        //act
        mySqlConnector.open();
        Connection connection = mySqlConnector.getConnection();

        //assert
        assertThat(connection, notNullValue());
    }

    @Test(expected = Exception.class)
    public void throwExceptionInvalidDatabaseConnection() throws SQLException {
        //arrange
        MySqlConnector mySqlConnector = new MySqlConnector("invalid", expectedUsername, expectedPassword);

        //act
        mySqlConnector.open();

    }

    @Test
    public void validateConnectionGetClosed() throws SQLException {

        //arrange
        MySqlConnector mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);

        //act
        mySqlConnector.open();
        mySqlConnector.close();
        Connection connection = mySqlConnector.getConnection();

        //assert
        assertThat(connection, is(nullValue()));

    }

}

//arrange

//act

//assert
