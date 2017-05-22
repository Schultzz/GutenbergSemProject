package boundary;

import control.controller.Controller;
import control.controller.IController;
import control.entities.Book;
import data.IQuery;
import data.mongo.MongoQuery;
import data.neo4j.Neo4jQuery;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by Flashed on 20-05-2017.
 */
public class CLITester {
    public static void main(String[] args) {
        IQuery query = null;

        query = new Neo4jQuery("bolt://localhost","neo4j","test");


        IController controller = new Controller(query);
       // List<Book> books = controller.getBookTitlesByCity("London");
       // System.out.println(controller.plotCitiesByBookTitle("Belgians Under the German Eagle"));
        List<Book> books = controller.getBooksByGeoLocation(13.404954, 52.520008, 1600);
        System.out.println(books.size());
        for (Book b:books) {
            System.out.println(b.getTitle());

        }
    }

}
