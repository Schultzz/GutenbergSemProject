package data;

import data.dto.BookDTO;
import data.dto.CityDTO;

import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public interface IQuery {
    /**
     * Given an authorname, the method lists all books written by that author
     * @param author name of the author of a book
     * @return list of BookDTO's
     */
    List<BookDTO> getBooksByAuthor(String author);

    /**
     * Given a cityname, the methods lists all books that mentions the city
     * @param city name of the city
     * @return list of BookDTO's
     */
    List<BookDTO> getBooksByCity(String city);

    /**
     * Given a geolaction, the method returns a list of all books, inside a given
     * vicinity of the given geolocation
     * @param lon longitude of the location
     * @param lat latitutde of the location
     * @param distance radius of the search distance
     * @return list of BookDTO's
     */
    List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance);

    /**
     * Given a booktitle, the method returns a list of cities mentioned in the book
     * @param bookTitle title of the book
     * @return list of CityDTO's
     */
    List<CityDTO> getCitiesByBookTitle(String bookTitle);
}
