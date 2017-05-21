package bookscanner.entities;

import java.util.ArrayList;

/**
 * Created by ms on 04-05-17.
 */
public class Book {

    private String id;
    private String title;
    private ArrayList<String> cities;
    private ArrayList<Author> authors;

    public Book() {
        this.id = "";
        this.cities = new ArrayList();
        this.authors = new ArrayList();
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        title = title.replace("\"", "'");
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

    public void addMatchedCity(String city) {
        cities.add(city);
    }

    //Should be in a formatter class.
    public String asCsvString() {
        String idString;
        if(getId().length() ==0){
            idString = "0";
        }else {
            idString = getId();
        }
        String csvString = idString + "," + "\"" + title.trim() + "\",\"";

        for (Author author : this.authors) {
            csvString += author.getName() + ",";
        }
        csvString = csvString.trim();
        csvString = csvString.substring(0, csvString.length() - 1) + "\",\"";

        for (String city : this.cities) {
            csvString += city.trim() + ",";
        }
        if(this.cities.size()!=0) {
            csvString = csvString.trim();
            csvString = csvString.substring(0, csvString.length() - 1) + "\"\n";
        }else{
            csvString += "\"\n";
        }

        return csvString;
    }

    public String asCsvAuthor() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Author author : authors) {
            stringBuilder.append("\"" + author.getId() + "\",");
            stringBuilder.append("\"" + author.getName() + "\"\n");
        }

        return stringBuilder.toString();
    }

    public String asCsvBookEntry() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\"" + this.getId() + "\",");
        stringBuilder.append("\"" + this.getTitle().trim() + "\"\n");

        return stringBuilder.toString();
    }

    public String asCsvBookAuthorRelation() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Author author : authors) {
            stringBuilder.append("\"" + this.getId() + "\",");
            stringBuilder.append("\"" + author.getId() + "\"\n");
        }

        return stringBuilder.toString();
    }

    public String asCsvBookCityRelation() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String city : cities) {
            stringBuilder.append("\"" + this.getId() + "\",");
            stringBuilder.append("\"" + city + "\"\n");
        }

        return stringBuilder.toString();
    }

}
