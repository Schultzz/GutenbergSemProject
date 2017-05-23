package data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public class BookDTO {

    private int id;
    private String title;
    private List<String> authors;
    private List<CityDTO> cities;

    public BookDTO(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.authors = new ArrayList<String>();
        this.cities = new ArrayList<CityDTO>();
    }

    public void addAuthor(String author) {
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    public List<CityDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
    }

    public void addCity(CityDTO city){
        this.cities.add(city);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}
