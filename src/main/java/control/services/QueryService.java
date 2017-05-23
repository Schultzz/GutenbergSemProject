package control.services;

import control.entities.Author;
import control.entities.Book;
import control.entities.City;
import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flashed on 20-05-2017.
 */
public class QueryService {

    private IQuery _query;
    private MapService mapService;

    public QueryService(IQuery query){
        _query = query;
        this.mapService = new MapService();
    }

    public List<Book> getBookTitlesByCity(String city){
        List<BookDTO> bookDTOs = _query.getBooksByCity(city);
        List<Book> books = new ArrayList<Book>();
        for (BookDTO bookDTO: bookDTOs) {

            books.add(bookDTOToBook(bookDTO));
        }

        return books;
    }

    public String plotCitiesByBookTitle(String title){
        List<CityDTO> cities = _query.getCitiesByBookTitle(title);
        String path = mapService.plotCitiesOnMap(cities);
        return path;
    }

    public List<Book> getBooksByGeoLocation(double lon, double lat, double distance){
        List<BookDTO> bookDTOs = _query.getBooksByGeoLocation(lon, lat, distance);
        List<Book> books = new ArrayList<Book>();
        for (BookDTO bookDTO: bookDTOs) {

            books.add(bookDTOToBook(bookDTO));
        }

        return books;
    }

    public List<Book> getBooksByAuthorAndPlotCities(String authorName){
        List<BookDTO> bookDTOs = _query.getBooksByAuthor(authorName);
        List<Book> books = new ArrayList<Book>();
        if (bookDTOs==null) return null;
        for (BookDTO bookDTO: bookDTOs) {

            books.add(bookDTOToBook(bookDTO));
        }
        mapService.plotCitiesOnMap(getCitiesByBookDTOList(bookDTOs));
        return books;
    }

    public List<CityDTO> getCitiesByBookDTOList(List<BookDTO> bookDTOs){
        List<CityDTO> cityDTOs = new ArrayList<CityDTO>();

        for(BookDTO bookDTO: bookDTOs){
            cityDTOs.addAll(bookDTO.getCities());
        }
        return cityDTOs;
    }

    public Book bookDTOToBook(BookDTO bookDTO){

        Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthors());
        return book;
    }


}
