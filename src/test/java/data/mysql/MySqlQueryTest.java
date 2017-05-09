package data.mysql;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import data.dto.BookDTO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 05-05-17.
 */
public class MySqlQueryTest {

    private static DB db;

    private MySqlConnector mySqlConnector;
    private String url = "jdbc:mysql://localhost/testdb?serverTimezone=UTC";
    private String username = "root";
    private String password = "password";

    @BeforeClass
    public static void setUp() throws Exception {
        db = DB.newEmbeddedDB(3306);
        db.start();
        db.source("MOCK_DATA.sql");
    }

    @Before
    public void setUpEach() {

        mySqlConnector = new MySqlConnector(url, username, password);

    }

    @AfterClass
    public static void tearDown() throws ManagedProcessException {
        db.stop();
    }

    @Test
    public void getValidBooksValidAuthor() {
        //arrange
        MySqlQuery query = new MySqlQuery(mySqlConnector);

        //act
        List<BookDTO> books = query.getBooksByAuthor("Gery Errigo");

        //assert
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
