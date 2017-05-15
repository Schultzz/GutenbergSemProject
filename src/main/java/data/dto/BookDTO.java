package data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public class BookDTO {

    private int id;
    private String title;
    private String author;
    private List<CityDTO> cities;

    public BookDTO(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cities = new ArrayList<CityDTO>();
    }

    public List<CityDTO> getCities() {
        return cities;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
