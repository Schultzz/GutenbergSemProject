package data.mysql;

import ch.vorburger.mariadb4j.DB;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 08-05-17.
 */
public class MySqlConnectionTest {

    private MySqlConnector mySqlConnector;
    private String expectedUrl = "jdbc:mysql://localhost/testdb?serverTimezone=UTC";
    private String expectedUsername = "root";
    private String expectedPassword = "password";

    @BeforeClass
    public static void setUp() throws Exception {

        DB db = DB.newEmbeddedDB(3306);
        db.start();
        db.source("MOCK_DATA.sql");

    }

    @Before
    public void setUpEach() {

        mySqlConnector = new MySqlConnector(expectedUrl, expectedUsername, expectedPassword);

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