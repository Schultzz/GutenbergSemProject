package control.controller;

import control.entities.Book;
import control.services.QueryService;
import data.IQuery;
import data.mongo.MongoQuery;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flashed on 20-05-2017.
 */
public class Controller implements IController {

    private IQuery _query;
    private QueryService queryService;

    public Controller(IQuery query){
        _query = query;
        this.queryService = new QueryService(_query);
    }



    public List<Book> getBookTitlesByCity(String city) {
        return queryService.getBookTitlesByCity(city);
    }

    public String plotCitiesByBookTitle(String title) {
        return queryService.plotCitiesByBookTitle(title);
    }

    public List<Book> getBooksByGeoLocation(double lon, double lat, double distance) {
        return queryService.getBooksByGeoLocation(lon,lat,distance);
    }
}
