package experiment;

import control.controller.Controller;
import control.services.MapService;
import control.services.QueryService;
import data.IQuery;
import data.mongo.MongoQuery;
import data.mysql.MySqlConnector;
import data.mysql.MySqlQuery;
import data.neo4j.Neo4jQuery;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 22-05-17.
 */
@Ignore
public class Experiment {
    List<IQuery> databases;
    Controller controller;
    String[] authors = {"Oscar D. (Oscar Douglas) Skelton", "Abraham Lincoln", "Jeremiah Curtin", "Thomas Roscoe", "Friedrich Heinrich Karl La Motte-Fouqué"};
    String[] cities = {"London", "Copenhagen", "Berlin", "Rome", "Paris"};
    String[] books = {"Belgians Under the German Eagle", "The 2010 CIA World Factbook", "The 2004 CIA World Factbook", "People's Handy Atlas of the World", "A Literary and Historical Atlas of Asia"};
    Experiment.Geo[] locations = {
            new Experiment.Geo(13.41053, 52.52437, 5.0),
            new Experiment.Geo(12.56553, 55.67594, 5.0),
            new Experiment.Geo(-81.23304, 42.98339, 5.0),
            new Experiment.Geo(2.3488, 48.85341, 5.0),
            new Experiment.Geo(10.01534, 53.57532, 5.0)};

    public Experiment() {
        databases = new ArrayList<IQuery>();
        databases.add(new MySqlQuery(new MySqlConnector("jdbc:mysql://127.0.0.1/gutenberg", "tester", "pwd")));
        databases.add(new MongoQuery("mongodb://localhost", "27017", "user", "password", "project"));
        databases.add(new Neo4jQuery("bolt://localhost", "neo4j", "test"));
    }
    @Test
    public void getBooksByAuthorAndPlotCities(){
        for (IQuery database : databases) {
            double avg = 0;
            controller = new Controller(new QueryService(database), new MapService());
            for (String author : authors) {
                long startTime = System.currentTimeMillis();
                controller.getBooksByAuthorAndPlotCities(author);
                long stopTime = System.currentTimeMillis();
                avg += stopTime - startTime;
                System.out.printf("Database: [%s] - City: [%s] - Time: %d \n", database.getClass(), author, stopTime - startTime);
            }
            System.out.printf("Database: [%s] - Avg: [%.2f] ms\n", database.getClass(), avg / 5);
        }
    }

    @Test
    public void getBookTitlesByCityName() {
        for (IQuery database : databases) {
            controller = new Controller(new QueryService(database), new MapService());
            double avg = 0;
            for (String city : cities) {
                long startTime = System.currentTimeMillis();
                controller.getBookTitlesByCity(city);
                long stopTime = System.currentTimeMillis();
                avg += stopTime - startTime;
                System.out.printf("Database: [%s] - City: [%s] - Time: %d \n", database.getClass(), city, stopTime - startTime);
            }
            System.out.printf("Database: [%s] - Avg: [%.2f] ms\n", database.getClass(), avg / 5);
        }
    }

    @Test
    public void getCitiesByBookTitle() {
        for (IQuery database : databases) {
        double avg = 0;
            controller = new Controller(new QueryService(database), new MapService());
            for (String title : books) {
                long startTime = System.currentTimeMillis();
                controller.plotCitiesByBookTitle(title);
                long stopTime = System.currentTimeMillis();
                avg += stopTime - startTime;
                System.out.printf("Database: [%s] - Title: [%s] - Time: %d \n", database.getClass(), title, stopTime - startTime);
            }
            System.out.printf("Database: [%s] - Avg: [%.2f] ms\n", database.getClass(), avg / 5);
        }
    }

    @Test
    public void getCitiesByGeolocation() {
        for (IQuery database : databases) {
            controller = new Controller(new QueryService(database), new MapService());
        double avg = 0;
            for (Geo geo : locations) {
                long startTime = System.currentTimeMillis();
                controller.getBooksByGeoLocation(geo.lon, geo.lat, geo.distance);
                long stopTime = System.currentTimeMillis();
                avg += stopTime - startTime;
                System.out.printf("Database: [%s] - Geo: [%f, %f - distance: %f] - Time: %d \n", database.getClass(), geo.lat
                        , geo.lon, geo.distance, stopTime - startTime);
            }
            System.out.printf("Database: [%s] - Avg: [%.2f] ms \n", database.getClass(), avg / 5);
        }
    }


    public class Geo {
        public double lon, lat, distance;

        public Geo(double lon, double lat, double distance) {
            this.lon = lon;
            this.lat = lat;
            this.distance = distance;
        }
    }

}
