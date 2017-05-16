package logic.bookscanner;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.*;

/**
 * Created by ms on 15.05.17.
 */
public class BookScannerTest {

    private BookScanner bookScanner;
    private static String filename = "testbook.txt";
    private static String expectedBookString = "This is a sample tekst";

    @BeforeClass
    public static void setup() throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(new FileOutputStream(filename));
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
            String scannedBook = bookScanner.getBookAsString("", filename);
            assertThat(scannedBook, is(expectedBookString));
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFilenameGivenThrowException() throws FileNotFoundException {
        String filename = "wrongName.jpeg";
        bookScanner.getBookAsString("", filename);
    }

    @Test
    public void getCitiesFromBooksFromRegex() throws FileNotFoundException {

        String book = "This is a test, to match cities New York is a fun city.\n Magnus is on vacation in Spain, he visits Barcelona, Madrid and other places";
        //String[] expectedWordHits =

        BookScanner bookScannerMock = mock(BookScanner.class);
        when(bookScannerMock.getBookAsString("", filename)).thenReturn("Shite");

        //bookScanner.getScannedBook(filename);


    }


}
