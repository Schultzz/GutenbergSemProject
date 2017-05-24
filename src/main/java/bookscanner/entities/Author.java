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
        this.name = rebuildAuthorName(name);
        books = new ArrayList<Book>();
    }

    public String rebuildAuthorName(String author){
        author = author.replace("\"", "'");
        String[] names = author.split(",");

        if(names.length>1) {
            String fullName = names[1].substring(1) + " " + names[0];
            return fullName;
        }
        else
            return author;


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
