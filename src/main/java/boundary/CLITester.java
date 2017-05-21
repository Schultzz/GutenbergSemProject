package boundary;

import control.controller.Controller;
import control.controller.IController;
import data.IQuery;
import data.mysql.MySqlConnector;
import data.mysql.MySqlQuery;

/**
 * Created by Flashed on 20-05-2017.
 */
public class CLITester {

    public static void main(String[] args) {
        IQuery query = null;
        try {
            //query = new MongoQuery("mongodb://localhost", "27017", "user", "password", "project");
            query = new MySqlQuery(new MySqlConnector("jdbc:mysql://127.0.0.1/gutenberg", "tester", "pwd"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        IController controller = new Controller(query);
        //List<Book> books = controller.getBookTitlesByCity("London");
        System.out.println(controller.plotCitiesByBookTitle("Belgians Under the German Eagle"));
        //List<Book> books2 = controller.getBooksByGeoLocation(13.404954, 52.520008, 1);
    }

}
