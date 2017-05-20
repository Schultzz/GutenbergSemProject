package data.mongo;


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

    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) {
        List<BookDTO> books = null;
        String bookCollection = "books";
        String cityCollection = "cities";
        List<String> cities = queryCitiesByGeoLocation(lon, lat, distance, cityCollection);
        if(cities!=null){
            books = queryBooksByCities(cities, bookCollection);
        }
        return books;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        String collectionName = "books";
        List<CityDTO> cities = queryCitiesByBookTitle(bookTitle, collectionName);
        return cities;
    }

    public List<BookDTO> queryBooksByCities(List<String> cities, String collectionName){
        //This method is almost identical to the method that queries on a single city, but time is running out.
        BasicDBList cityNames = new BasicDBList();
        cityNames.addAll(cities);
        DBObject inClause = new BasicDBObject("$in", cityNames);
        DBObject query = BasicDBObjectBuilder.start().add("cities", inClause).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked

        List<BookDTO> books = mongoCursorToBookDTOList(cursor);
        return books;
    }

    public List<String> queryCitiesByGeoLocation(double lon, double lat, double distance, String collectionName){
        BasicDBList geoCoord = new BasicDBList();
        geoCoord.add(lon);
        geoCoord.add(lat);

        BasicDBList geoParams = new BasicDBList();
        geoParams.add(geoCoord);
        geoParams.add(distance);

        BasicDBObject query = new BasicDBObject("location",
                new BasicDBObject("$geoWithin",
                        new BasicDBObject("$center", geoParams)));

        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator();

        List<String> cities = mongoCursorToCityStringList(cursor);

        return cities;
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

    public List<CityDTO> queryCitiesByBookTitle(String bookTitle, String collectionName){

        List<BookDTO> booksDTO = null;
        List<CityDTO> citiesDTO = null;
        DBObject query = BasicDBObjectBuilder.start().add("title", bookTitle).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator();
        booksDTO = mongoCursorToBookDTOList(cursor);

        if(booksDTO.size()==1){
            citiesDTO = booksDTO.get(0).getCities();
        }

        return citiesDTO;
    }

    public List<String> mongoCursorToCityStringList(MongoCursor<Document> cursor){
        List<String> cities = new ArrayList<String>();

        while(cursor.hasNext()){
            Document doc = cursor.next();
            if(doc.containsKey("name")){
                cities.add(doc.getString("name"));
            }
        }

        return cities;
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
