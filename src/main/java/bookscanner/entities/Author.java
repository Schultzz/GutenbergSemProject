package bookscanner.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public class Author {

    private String id;
    private String name;
    private List<Book> books;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
        books = new ArrayList<Book>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
