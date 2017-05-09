package data.mysql;

import ch.vorburger.mariadb4j.DB;
import data.dto.BookDTO;
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

    private static MySqlConnector mySqlConnector;
    private static String url = "jdbc:mysql://localhost/testdb?serverTimezone=UTC";
    private static String username = "root";
    private static String password = "password";

    @BeforeClass
    public static void setUp() throws Exception {
        DB db = DB.newEmbeddedDB(3306);
        db.start();
        db.source("MOCK_DATA.sql");
        mySqlConnector = new MySqlConnector(url, username, password);
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
