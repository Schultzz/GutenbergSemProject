package logic.bookscanner;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by ms on 16-05-17.
 */
public class CityScannerTest {

    private static String path = "src/test/resources/";
    @Test
    public void cityScannerTest() throws FileNotFoundException {

        String filename = path + "cities.txt";
        CityScanner cityScanner = new CityScanner(filename);
        HashMap<String, String> cityMap = cityScanner.getCitiesFromTxtFile();

        assertThat(cityMap.get("Kusk"), is("Kushk"));
        assertThat(cityMap.get("Kondôz"), is("Kunduz"));
    }

    @Test(expected = FileNotFoundException.class)
    public void cityScannerTestInvalidFile() throws FileNotFoundException {

        String filename = path + "citiesWrong.txt";
        CityScanner cityScanner = new CityScanner(filename);
        cityScanner.getCitiesFromTxtFile();
    }

}
