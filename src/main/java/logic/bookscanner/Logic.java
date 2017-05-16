package logic.bookscanner;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ms on 16-05-17.
 */
public class Logic {

    private HashMap<String, String> cities;
    private BookScanner bs;

    public Logic(CityScanner cityScanner) throws FileNotFoundException {
        this.cities = cityScanner.getCitiesFromTxtFile();
        bs = new BookScanner();
    }


    public ScannedBook scanBook(String filename) throws IOException {
        ScannedBook scannedBook = new ScannedBook(bs.getCapitalizedWords(bs.getBookAsString(filename)));
        bs.setMetaDataOnBook(scannedBook, filename);
        HashSet<String> wordsFromBook = scannedBook.getWordsFromBook();

        for (String word : wordsFromBook) {
            if (this.cities.containsKey(word)) {
                scannedBook.addMatchedCity(this.cities.get(word));
            }
        }
        return scannedBook;
    }


    public void appendBookToCsv(List<ScannedBook> bookList) {
        String fileName = "books.csv";
        PrintWriter printWriter = null;
        File file = new File(fileName);
        try {
            if (!file.exists()) file.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(fileName, true));

            for (ScannedBook book : bookList) {
                printWriter.write(book.asCsvString()); //For each csv file/format.
            }

        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    public List<ScannedBook> scanAllBooks() throws IOException {
        ScannedBook scannedBook;
        List<ScannedBook> scannedBooks = new ArrayList<ScannedBook>();
        File folder = new File("books");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            scannedBook = scanBook(listOfFiles[i].getName());
            scannedBooks.add(scannedBook);
        }
        appendBookToCsv(scannedBooks); //Printing to CSV.

        return scannedBooks;
    }

    public static void main(String[] args) {
        System.out.println("null");
    }
}
