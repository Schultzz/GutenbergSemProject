package control.controller;

import control.entities.Book;

import java.util.List;

/**
 * Created by Flashed on 20-05-2017.
 */
public interface IController {
    List<Book> getBookTitlesByCity(String city);
    String plotCitiesByBookTitle(String title);
    List<Book> getBooksByGeoLocation(double lon, double lat, double distance);
    List<Book> getBooksByAuthorAndPlotCities(String author);
}
