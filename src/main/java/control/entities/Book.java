package control.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public class Book {
    private String id;
    private String title;
    private List<String> cities;
    private List<Author> authors;

    public Book(String title, List<String> authors) {
        this.id = "";
        this.title = title;
        this.cities = new ArrayList();
        this.authors = new ArrayList();
        for(String tempAuthor: authors){
            this.authors.add(new Author("", tempAuthor));
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        if (title.contains("\n")) {
            this.title = title.substring(0, title.indexOf("\n"));
        } else {
            this.title = title;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
