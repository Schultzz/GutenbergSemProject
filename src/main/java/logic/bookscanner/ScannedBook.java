package logic.bookscanner;

import java.util.HashSet;

/**
 * Created by ms on 15.05.17.
 */
public class ScannedBook {
    private String title = "";
    private HashSet<String> wordsFromBook;
    private HashSet<String> matchedCities;
    private HashSet<String> authors;
    
    public ScannedBook(HashSet<String> wordsFromBook){
        this.wordsFromBook = wordsFromBook;
        matchedCities = new HashSet<String>();
        authors = new HashSet<String>();
    }
    public String asCsvString() {
        return null;
    }

    public void setTitle(String title) {
        if (title.contains("\n")) {
            this.title = title.substring(0, title.indexOf("\n"));
        }
        else{
            this.title = title;
        }
    }

    public void addAuthor(String author) {
        String[] names = author.split(",");
        if (names.length > 1){
            String fullName = names[1].substring(1) + " " + names[0];
            this.authors.add(fullName);
        }
        else{
            this.authors.add(author);
        }
    }
    

    
    public String getTitle() {
        return title;
    }

    public HashSet<String> getWordsFromBook() {
        return wordsFromBook;
    }

    public HashSet<String> getAuthors() {
        return authors;
    }

    public void addMatchedCity(String str) {
        matchedCities.add(str);
    }
}
