package refactormepleasehansen;


import com.mongodb.MongoClient;
import data.IQuery;
import data.dto.BookDTO;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by Flashed on 05-05-2017.
 */
public class MongoQuery implements IQuery {

    private MongoClient connection;


    public MongoQuery(String connection, String port, String user, String password, String databaseName) throws ConnectException {
        this.connection = setupConnection(connection, port, user, password, databaseName);
    }

    private MongoClient setupConnection(String connection, String port, String user, String password, String databaseName) throws ConnectException {

        MongoClient mongoClient = null;

        try{

        }catch (Exception e){//Figure out what exeption this throws
            throw new ConnectException("Failed to connect to MongoDB");
        }

        return  mongoClient;
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        return null;
    }
}
