package bookscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by ms on 16-05-17.
 */
public class CityScanner {

    private String filename;

    public CityScanner(String filename) {
        this.filename = filename;
    }

    public HashMap<String, String> getCitiesFromTxtFile() throws FileNotFoundException {
        HashMap<String, String> map = new HashMap();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNext()) {
            String sentence = scanner.nextLine();
            String[] elements = sentence.split("\t");
            String cityName = elements[1];
            String[] alternateNames = elements[3].split(",");

            for (String alternateName : alternateNames) {
                map.put(alternateName, cityName);
            }
        }
        scanner.close();
        return map;
    }
}
