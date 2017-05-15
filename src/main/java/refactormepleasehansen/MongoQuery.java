package refactormepleasehansen;


import com.mongodb.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flashed on 05-05-2017.
 */
public class MongoQuery implements IQuery {

    private MongoConnection mongoConnection;
    private String databaseName;

    public MongoQuery(String connection, String port, String user, String password, String databaseName) throws ConnectException {
        this.databaseName = databaseName;
        this.mongoConnection = new MongoConnection(connection, port, user, password);//getConnection(connection, port, user, password);
    }



    public List<BookDTO> getBooksByAuthor(String author) {
        String collectionName = "books"; //Find place to store or inject this
        BookDTO tempBook;
        List<BookDTO> books = new ArrayList<BookDTO>();


        MongoCursor<Document> cursor = queryBooksByAuthor(author, collectionName);

        while(cursor.hasNext()){
            Document doc = cursor.next();
            if(doc.containsKey("bookId") && doc.containsKey("title") && doc.containsKey("author")){
                tempBook = new BookDTO(Integer.parseInt(doc.getString("bookId")), doc.getString("title"), doc.getString("author"));
                books.add(tempBook);
            }


        }

        return books;
    }

    public List<BookDTO> getBooksByCity(String city) {
        return null;
    }

    public List<BookDTO> getBooksByGeoLocation(float lon, float lat, float distance) {
        return null;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        return null;
    }

    public MongoCursor<Document> queryBooksByAuthor(String author, String collectionName){

        DBObject query = BasicDBObjectBuilder.start().add("author", author).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked

        return cursor;
    }
}
