package logic.bookscanner;

import org.junit.BeforeClass;

import java.io.FileNotFoundException;

/**
 * Created by ms on 16-05-17.
 */
public class LogicIT {

    private static CityScanner cityScanner;
    private static BookScannerManager bookScannerManager;
    private static String path = "src/test/resources/";


    @BeforeClass
    public static void setup() {
        cityScanner = new CityScanner(path + "cities.txt");
        try {
            bookScannerManager = new BookScannerManager(cityScanner, new BookScanner());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //TODO: Fix test
//    @Test
//    public void scanBookTest() throws IOException {
//        Book book = logic.mapCities(path + "1001.txt");
//
//        assertThat(book.getAuthors(), hasItems("Dante Alighieri", "Henry Wadsworth Longfellow"));
//        assertThat(book.getAuthors().size(), is(2));
//        assertThat(book.getTitle(), is("Divine Comedy, Longfellow's Translation, Hell"));
//        assertThat(book.getMatchedCities(), hasItems("Kunduz", "Kushk"));
//        assertThat(book.getMatchedCities().size(), is(2));
//    }

}
