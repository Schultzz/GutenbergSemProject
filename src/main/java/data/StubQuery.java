package data;

import data.dto.BookDTO;
import data.dto.CityDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 24-05-17.
 */
public class StubQuery implements IQuery {

    public List<BookDTO> getBooksByAuthor(String author) {
        List<BookDTO> bookDTOS = new ArrayList<BookDTO>();
        bookDTOS.add(new BookDTO(1, "Sagaen om Flashed Hansen", "Stefan Duro"));
        return bookDTOS;
    }

    public List<BookDTO> getBooksByCity(String city) {
        List<BookDTO> bookDTOS = new ArrayList<BookDTO>();
        bookDTOS.add(new BookDTO(1, "Mit liv med Guffe", "Stefan Duro"));
        return bookDTOS;
    }

    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) {
        List<BookDTO> bookDTOS = new ArrayList<BookDTO>();
        bookDTOS.add(new BookDTO(1, "Kunsten i at indpakke gaver", "Stefan Duro"));
        return bookDTOS;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        List<CityDTO> cityDTOS = new ArrayList<CityDTO>();
        cityDTOS.add(new CityDTO("Lyngby", 1.0, 1.0));
        return cityDTOS;
    }
}
