package logic.bookscanner;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ms on 15.05.17.
 */
public class BookScannerTest {

    private BookScanner bookScanner;
    private static String filename = "testbook.txt";
    private static String expectedBookString = "This is a sample text";
    private static String path = "src/test/resources/";

    @BeforeClass
    public static void setup() throws IOException {
        File file = new File(path + filename);

        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(new FileOutputStream(path + filename));
        writer.println(expectedBookString);
        writer.close();
    }

    @Before
    public void setupEach() {
        bookScanner = new BookScanner();
    }


    @Test
    public void validPathReturnsAString() {

        try {
            String scannedBook = bookScanner.getBookAsString( path + filename);
            assertThat(scannedBook, is(expectedBookString));
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFilenameGivenThrowException() throws FileNotFoundException {
        String filename = path + "wrongName.jpeg";
        bookScanner.getBookAsString(filename);
    }

    @Test
    public void getCitiesFromBooksFromRegex() throws FileNotFoundException {

        String book = "This is a test, to match cities New York is a fun city.\n Magnus is on vacation in Spain, he visits Barcelona, Madrid and other places";
        String[] expectedWordHits = {"This", "New York", "Magnus", "Spain", "Barcelona", "Madrid"};

        BookScanner bookScannerMock = mock(BookScanner.class);
        when(bookScannerMock.getBookAsString("test")).thenReturn(book);

        ScannedBook scannedBook = new ScannedBook(bookScanner.getCapitalizedWords(bookScannerMock.getBookAsString("test")));

        HashSet<String> wordSet = scannedBook.getWordsFromBook();

        assertThat(wordSet.size(), is(6));
        assertThat(wordSet, hasItems(expectedWordHits));
    }

    @Test
    public void setTitleAuthorFromMetaData() throws IOException, SAXException, ParserConfigurationException {

        String filename = path + "50000.txt";
        ScannedBook book = new ScannedBook(new HashSet<String>());

        book = bookScanner.setMetaDataOnBook(book, filename);
        assertThat(book.getTitle(), is("John Gutenberg, First Master Printer\r"));
        assertThat(book.getAuthors(), hasItem("Franz von Dingelstedt"));
    }

    @Test(expected = IOException.class)
    public void setTitleAuthorFromMetaDataNoFile() throws IOException, SAXException, ParserConfigurationException {

        String filename = path + "500001.txt";
        ScannedBook book = new ScannedBook(new HashSet<String>());

        bookScanner.setMetaDataOnBook(book, filename);
    }


}
