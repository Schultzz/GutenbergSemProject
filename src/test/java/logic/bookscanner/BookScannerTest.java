package logic.bookscanner;

import logic.entities.Book;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

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
            String scannedBook = bookScanner.getBookAsString(path + filename);
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

//    @Test
//    public void getCitiesFromBooksFromRegex() throws FileNotFoundException {
//
//        String book = "This is a test, to match cities New York is a fun city.\n Magnus is on vacation in Spain, he visits Barcelona, Madrid and other places";
//        String[] expectedWordHits = {"This", "New York", "Magnus", "Spain", "Barcelona", "Madrid"};
//
//        BookScanner bookScannerMock = mock(BookScanner.class);
//        when(bookScannerMock.getBookAsString("test")).thenReturn(book);
//
//        ScannedBook scannedBook = new ScannedBook(bookScanner.getCapitalizedWords(bookScannerMock.getBookAsString("test")));
//
//        HashSet<String> wordSet = scannedBook.getWordsFromBook();
//
//        assertThat(wordSet.size(), is(6));
//        assertThat(wordSet, hasItems(expectedWordHits));
//    }

    @Test
    public void setTitleAuthorFromMetaData() throws IOException {

        String filename = path + "50000.txt";
        Book book = new Book();


        book = bookScanner.setMetaDataOnBook(book, filename);
        assertThat(book.getTitle(), is("John Gutenberg, First Master Printer\r"));
        assertThat(book.getAuthors().size(), is(2));
    }

    @Test(expected = IOException.class)
    public void setTitleAuthorFromMetaDataNoFile() throws IOException {

        String filename = path + "500001.txt";
        Book book = new Book();
        bookScanner.setMetaDataOnBook(book, filename);
    }


}
