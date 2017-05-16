package refactormepleasehansen;


import com.mongodb.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
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
        this.mongoConnection = new MongoConnection(connection, port, user, password);
    }



    public List<BookDTO> getBooksByAuthor(String author) {
        String collectionName = "books"; //Find place to store or inject this
        List<BookDTO> books = queryBooksByAuthor(author, collectionName);

        return books;
    }

    public List<BookDTO> getBooksByCity(String city) {
        String collectionName = "books"; //Find place to store or inject this
        List<BookDTO> books = queryBooksByCity(city, collectionName);


        return books;
    }

    public List<BookDTO> getBooksByGeoLocation(float lon, float lat, float distance) {
        return null;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        return null;
    }


    public List<BookDTO> queryBooksByAuthor(String author, String collectionName){

        DBObject query = BasicDBObjectBuilder.start().add("author", author).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked


        List<BookDTO> books = mongoCursorToBookDTOList(cursor);
        return books;
    }

    public List<BookDTO> queryBooksByCity(String city, String collectionName){
        BasicDBList cityNames = new BasicDBList();
        cityNames.add(city);
        DBObject inClause = new BasicDBObject("$in", cityNames);
        DBObject query = BasicDBObjectBuilder.start().add("cities", inClause).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked

        List<BookDTO> books = mongoCursorToBookDTOList(cursor);
        return books;
    }

    public List<CityDTO> queryCitiesByList(List<String> cities, String collectionName){
        List<CityDTO> cityDTOs = null;
        BasicDBList cityNames = new BasicDBList();
        cityNames.addAll(cities);
        DBObject inClause = new BasicDBObject("$in", cityNames);
        DBObject query = BasicDBObjectBuilder.start().add("name", inClause).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator();
        cityDTOs = mongoCursorToCityDTOList(cursor);


        return cityDTOs;
    }

    //UNTESTED, CRIME CRIME CRIME. Not sure how to create the mongocursor in the test.
    public List<CityDTO> mongoCursorToCityDTOList(MongoCursor<Document> cursor){
        List<CityDTO> cityDTOs = new ArrayList<CityDTO>();
        CityDTO tempCity = null;


        while(cursor.hasNext()){
            Document doc = cursor.next();
            tempCity = cityDocumentToCityDTO(doc);
                if(tempCity!=null) {
                    cityDTOs.add(tempCity);
                }
            }

        return cityDTOs;
    }

    //UNTESTED, CRIME CRIME CRIME. Not sure how to create the mongocursor in the test.
    public List<BookDTO> mongoCursorToBookDTOList(MongoCursor<Document> cursor){

        BookDTO tempBook;
        List<BookDTO> books = new ArrayList<BookDTO>();

        while(cursor.hasNext()){
            Document doc = cursor.next();

            tempBook = bookDocumentToBookDTO(doc);
            if(tempBook != null) {
                books.add(tempBook);
            }

        }
        return books;
    }

    public CityDTO cityDocumentToCityDTO(Document doc){
        CityDTO tempCity = null;
        if(doc.containsKey("name") && doc.containsKey("location")) {
            Document locationDoc = (Document) doc.get("location");
            ArrayList<Double> coordinates = (ArrayList<Double>) locationDoc.get("coordinates");
            tempCity = new CityDTO(doc.getString("name"), coordinates.get(0), coordinates.get(1));
        }
        return tempCity;
    }

    public BookDTO bookDocumentToBookDTO(Document doc){
        BookDTO tempBook = null;
        if(doc.containsKey("bookId") && doc.containsKey("title") && doc.containsKey("author")){
            tempBook = new BookDTO(Integer.parseInt(doc.getString("bookId")), doc.getString("title"), doc.getString("author"));
            List<String> cities = (List<String>) doc.get("cities");
            tempBook.setCities(queryCitiesByList(cities, "cities")); //Cities is hardcoded..
        }
        return tempBook;
    }
}
