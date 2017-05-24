package control.services;

import control.entities.Book;
import data.IQuery;
import data.StubQuery;
import data.dto.CityDTO;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by ms on 24-05-17.
 */
public class QueryServiceTest {


    private static QueryService queryService;

    @BeforeClass
    public static void setup() {
        IQuery stub = new StubQuery();
        queryService = new QueryService(stub);
    }

    @Test
    public void getBooksByCity_returnsOneBook() {
        List<Book> books = queryService.getBookTitlesByCity("Lyngby");

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getTitle(), is("Mit liv med Guffe"));
    }

    @Test
    public void getBooksByGeolocation_returnsOneBook() {
        List<Book> books = queryService.getBooksByGeoLocation(1, 1, 1);

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getTitle(), is("Kunsten i at indpakke gaver"));
    }

    @Test
    public void getBooksByAuthor_returnsOneBook() {
        List<Book> books = queryService.getBooksByAuthorAndPlotCities("Unknown");

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getTitle(), is("Sagaen om Flashed Hansen"));
    }

    @Test
    public void plotCitiesOnMap_returnsPathToCreatedMap() {
        List<CityDTO> cities = queryService.plotCitiesByBookTitle("Unknown");

        assertThat(cities, hasSize(1));
        assertThat(cities.get(0).getCityName(), is("Lyngby"));
        assertThat(cities.get(0).getLatitude(), is(1.0));
        assertThat(cities.get(0).getLongtitude(), is(1.0));
    }

}
