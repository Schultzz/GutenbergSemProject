package control.controller;

import control.entities.Book;
import control.services.MapService;
import control.services.QueryService;

import java.util.List;

/**
 * Created by Flashed on 20-05-2017.
 */
public class Controller implements IController {
    
    private QueryService queryService;
    private MapService mapService;

    public Controller(QueryService queryService, MapService mapService) {
        this.mapService = mapService;
        this.queryService = queryService;
    }

    public List<Book> getBookTitlesByCity(String city) {
        return queryService.getBookTitlesByCity(city);
    }

    public String plotCitiesByBookTitle(String title) {
        return mapService.plotCitiesOnMap(queryService.plotCitiesByBookTitle(title));
    }

    public List<Book> getBooksByGeoLocation(double lon, double lat, double distance) {
        return queryService.getBooksByGeoLocation(lon, lat, distance);
    }

    public List<Book> getBooksByAuthorAndPlotCities(String author) {
        return queryService.getBooksByAuthorAndPlotCities(author);
    }
}
