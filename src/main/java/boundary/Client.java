package boundary;

import control.controller.Controller;
import control.controller.IController;
import data.mongo.MongoQuery;
import data.mysql.MySqlConnector;
import data.mysql.MySqlQuery;
import data.neo4j.Neo4jQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by ms on 21-05-17.
 */
public class Client {

    IController controller;

    public Client(IController controller) {
        this.controller = controller;
    }

    public void runQuery(int query) {
        System.out.println(query);
        switch (query) {
            case 1:
                System.out.println("query 1");
                controller.getBookTitlesByCity("London");
                break;
            case 2:
                System.out.println("query 2");
                controller.plotCitiesByBookTitle("Belgians Under the German Eagle");
                break;
            case 3:
                System.out.println("query 3");
                controller.getBooksByGeoLocation(-73.935242, 40.730610, 5);
                break;
            case 4:
                System.out.println("query 4");
                break;
        }
    }

    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("#1 Given a city name your application returns all book titles with corresponding authors that mention this city.");
            System.out.println("#2 Given a book title, your application plots all cities mentioned in this book onto a map.");
            System.out.println("#3 Given an author name your application lists all books written by that author and plots all cities mentioned in any of the books onto a map.");
            System.out.println("#4 Given a geolocation, your application lists all books mentioning a city in vicinity of the given geolocation.");

            String filename = "config.properties";
            input = Client.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            String database = prop.getProperty("database");

            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");

            while (true) {

                System.out.print("Select a query:");
                int query = scanner.nextInt();
                if (database.equals("mysql")) {
                    Client client = new Client(new Controller(new MySqlQuery(new MySqlConnector(url, username, password))));
                    client.runQuery(query);
                } else if (database.equals("mongo")) {
                    Client client = new Client(new Controller(new MongoQuery(url, "", username, password, "")));
                    client.runQuery(query);
                } else if (database.equals("neo4j")) {
                    Client client = new Client(new Controller(new Neo4jQuery(url, username, password)));
                    client.runQuery(query);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
