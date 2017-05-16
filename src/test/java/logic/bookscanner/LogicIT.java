package logic.bookscanner;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ms on 16-05-17.
 */
public class LogicIT {

    private static CityScanner cityScanner;
    private static Logic logic;
    private static String path = "src/test/resources/";


    @BeforeClass
    public static void setup(){
        cityScanner = new CityScanner(path + "cities.txt");
        try {
            logic = new Logic(cityScanner);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scanBookTest() throws IOException {
        ScannedBook scannedBook = logic.scanBook(path + "1001.txt");

        assertThat(scannedBook.getAuthors(), hasItems("Dante Alighieri", "Henry Wadsworth Longfellow"));
        assertThat(scannedBook.getAuthors().size(), is(2));
        assertThat(scannedBook.getTitle(), is("Divine Comedy, Longfellow's Translation, Hell"));
        assertThat(scannedBook.getMatchedCities(), hasItems("Kunduz", "Kushk"));
        assertThat(scannedBook.getMatchedCities().size(), is(2));
    }

}
