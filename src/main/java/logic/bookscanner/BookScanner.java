package logic.bookscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by ms on 15.05.17.
 */
public class BookScanner {

    public String getBookAsString(String path, String fileName) throws FileNotFoundException {

        String bookAsString;

        bookAsString = new Scanner(new File(path + fileName)).useDelimiter("\\Z").next();

        return bookAsString;
    }
}
