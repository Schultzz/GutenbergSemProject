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
                System.out.println("Oouffe");
                break;
            case 2:
                System.out.println("Flæsk");
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "config.properties";
            input = Client.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            String database = prop.getProperty("database");
            int query = Integer.parseInt(prop.getProperty("query"));
            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");


            if (database.equals("mysql")) {
                Client client = new Client(new Controller(new MySqlQuery(new MySqlConnector(url, username, password))));
                client.runQuery(query);
            } else if (database.equals("mongo")) {
                Client client = new Client(new Controller(new MongoQuery(url, "", username, password, "")));
            } else if (database.equals("neo4j")) {
                Client client = new Client(new Controller(new Neo4jQuery(url, username, password)));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
