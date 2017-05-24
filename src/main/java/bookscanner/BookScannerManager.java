package bookscanner;

import bookscanner.entities.Author;
import bookscanner.entities.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ms on 19-05-17.
 */
public class BookScannerManager {

    private HashMap<String, String> cities;
    private BookScanner bookScanner;


    public BookScannerManager(CityScanner cityScanner, BookScanner bookScanner) throws FileNotFoundException {
        this.bookScanner = bookScanner;
        this.cities = cityScanner.getCitiesFromTxtFile();
    }

    public Book mapCities(String filename) throws IOException {
        Book book = new Book();

        String bookAsString = bookScanner.getBookAsString(filename);
        if(bookAsString.length()==0) return null;
        HashSet<String> matchCapitalizedWords = bookScanner.getCapitalizedWords(bookAsString);

        for (String word : matchCapitalizedWords) {
            if (this.cities.containsKey(word)) {
                book.addMatchedCity(this.cities.get(word));
            }
        }

        if (filename.equals("pgcityRelations.csv") || filename.equals("pgcityRelations.csv/pgcityRelations.csv")) {
            filename = "1";
        }

        //Find and set metadata.
        book = bookScanner.setMetaDataOnBook(book, filename);

        return book;
    }


    public List<Book> scanAllBooks(String filePath) throws IOException {
        List<Book> books = new ArrayList<Book>();
        Book book;
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        long estimatedTime;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < listOfFiles.length; i++) {
            book = mapCities(filePath + listOfFiles[i].getName());
            if(book!=null) {
                books.add(book);
                if (i % 1000 == 0) {
                    estimatedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Time was: " + estimatedTime + ". " + i + "/" + listOfFiles.length);
                }
            }
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Reading done. Time was: " + estimatedTime + ".");
        return books;
    }

    //Should be in the Data persistance layer. Not the prettiest solution.
    public void saveBooksAsCsv(List<Book> bookList, String filePath) {

        String[] csvFiles = {filePath + "full.csv", filePath + "books.csv", filePath + "author.csv", filePath + "authorRelations.csv", filePath + "cityRelations.csv"};
        PrintWriter[] pws = createFiles(csvFiles);

        pws[0].write("bookId,title,authors,cities\n");

        for (Author au : bookScanner.getAuthorMap().values()) {
            pws[2].write("\"" + au.getId() + "\"," + "\"" + au.getName() + "\"\n");
        }
        for (Book bk : bookList) {
            pws[0].write(bk.asCsvString());
            pws[1].write(bk.asCsvBookEntry());
            pws[3].write(bk.asCsvBookAuthorRelation());
            pws[4].write(bk.asCsvBookCityRelation());
        }
        for (PrintWriter pw : pws) {
            pw.flush();
            pw.close();
        }
    }

    private PrintWriter[] createFiles(String[] csvFiles) {

        PrintWriter[] printWriters = new PrintWriter[csvFiles.length];

        for (int i = 0; i < csvFiles.length; i++) {
            String filepath = csvFiles[i];
            File file = new File(filepath);
            if (file.exists()) file.delete();
            try {
                file.createNewFile();
                printWriters[i] = new PrintWriter(new FileOutputStream(file, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return printWriters;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "resources/bookscanner/";
        String booksFilePath = filePath + "books/";
        String csvFiles = filePath + "csv/";
        String citiesFilePath = filePath + "cities15000.txt";

        BookScanner bookScanner = new BookScanner();
        CityScanner cityScanner = new CityScanner(citiesFilePath);

        BookScannerManager bookScannerManager = new BookScannerManager(cityScanner, bookScanner);
        long startTime = System.currentTimeMillis();
        List<Book> books = bookScannerManager.scanAllBooks(booksFilePath);
        bookScannerManager.saveBooksAsCsv(books, csvFiles); //Printing to CSV.
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time was: " + estimatedTime);

    }

}
