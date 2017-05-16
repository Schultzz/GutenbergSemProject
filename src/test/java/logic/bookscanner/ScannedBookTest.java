package logic.bookscanner;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 15.05.17.
 */
public class ScannedBookTest {

    ScannedBook scannedBook;


    @Before
    public void beforeEach(){
        scannedBook = new ScannedBook(new HashSet<String>());
    }


    @Test
    public void setTitleNewlineTest(){
        String title = "test\nTitle";
        scannedBook.setTitle(title);

        assertThat(scannedBook.getTitle(), is("test"));

    }

    @Test
    public void setTitleTest(){
        String title = "testTitle";
        scannedBook.setTitle(title);

        assertThat(scannedBook.getTitle(), is("testTitle"));

    }

    @Test
    public void addAuthorTest(){
        String author = "Magnus";
        scannedBook.addAuthor(author);

        assertThat(scannedBook.getAuthors(), hasItem(author));
    }

    @Test
    public void getCSVStringTest(){
        scannedBook.setTitle("testTitle");
        scannedBook.addAuthor("Magnus");
        scannedBook.addMatchedCity("Hellerup");
        String csvString = scannedBook.asCsvString();
        String expectedString = "\"testTitle\",\"Magnus\",\"Hellerup\"\n";

        assertThat(csvString, is(expectedString));
    }

    @Test
    public void getCSVStringWithMultipleValuesTest(){
        scannedBook.setTitle("testTitle");
        scannedBook.addAuthor("Magnus");
        scannedBook.addAuthor("Klavs");
        scannedBook.addMatchedCity("Hellerup");
        scannedBook.addMatchedCity("Rungsted");
        String csvString = scannedBook.asCsvString();
        String expectedString = "\"testTitle\",\"Magnus,Klavs\",\"Hellerup,Rungsted\"\n";

        assertThat(csvString, is(expectedString));
    }


}