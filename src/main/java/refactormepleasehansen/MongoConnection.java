package refactormepleasehansen;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.net.ConnectException;

/**
 * Created by Flashed on 05-05-2017.
 */
public class MongoConnection {


    private MongoClient connection;

    public MongoConnection(String connection, String port, String user, String password) throws ConnectException {
        this.connection = getConnection(connection, port, user, password);
    }


    public MongoClient getConnection(String connection, String port, String user, String password) throws ConnectException {

        MongoClient mongoClient = null;

        try{
            String uriString = connection + ":" + port;
            MongoClientURI connStr = new MongoClientURI(uriString);
            mongoClient = new MongoClient(connStr);
            mongoClient.getAddress();
        }catch (Exception e){//Figure out what exception this throws
            //e.printStackTrace();
            throw new ConnectException("Failed to connect to MongoDB");
        }

        return  mongoClient;
    }

    public MongoDatabase getMongoDatabase(MongoClient connection, String databaseName){

        MongoDatabase mongoDatabase = null;

        try{
            MongoIterable<String> iterable = connection.listDatabaseNames();
            MongoCursor<String> it = iterable.iterator();
            while (it.hasNext()) {
                if (it.next().equalsIgnoreCase(databaseName)) {
                    mongoDatabase = connection.getDatabase(databaseName);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            //throw new ConnectException("Failed to connect to MongoDB");
        }

        return mongoDatabase;
    }

    public MongoCollection getMongoCollection(MongoDatabase mongoDatabase, String collectionName){
        MongoCollection mongoCollection = null;

        try{
            MongoIterable<String> iterable = mongoDatabase.listCollectionNames();
            MongoCursor<String> it = iterable.iterator();
            while (it.hasNext()) {
                if (it.next().equalsIgnoreCase(collectionName)) {
                    mongoCollection = mongoDatabase.getCollection(collectionName);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return mongoCollection;
    }

    public MongoCollection getWorkableMongoCollection(String dataBaseName, String collectionName){ //Figure out how to handle the used connection. If DI, then the caller has to write alot of code..
        MongoCollection mongoCollection = null;

        MongoDatabase mongoDatabase = getMongoDatabase(this.connection, dataBaseName);
        if(mongoDatabase!=null){
            mongoCollection = getMongoCollection(mongoDatabase, collectionName);
        }
        return mongoCollection;
    }
}
