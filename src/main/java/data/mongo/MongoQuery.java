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

    public MongoQuery(String connection, String port, String user, String password, String databaseName) {
        this.databaseName = databaseName;
        try {
            this.mongoConnection = new MongoConnection(connection, port, user, password);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }


    public List<BookDTO> getBooksByAuthor(String author) { //Should be ok


        String collectionName = "books"; //Find place to store or inject this
        List<BookDTO> books = queryBooksByAuthor(author, collectionName);

        return books;
    }

    public List<BookDTO> getBooksByCity(String city) { //OK
        String collectionName = "books"; //Find place to store or inject this
        List<BookDTO> books = queryBooksByCity(city, collectionName);


        return books;
    }

    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) { //Ok
        List<BookDTO> books = null;
        String bookCollection = "books";
        String cityCollection = "cities";
        books = queryBooksByGeolocation(lon, lat, distance, bookCollection);

        return books;
    }

    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {//Ok
        String collectionName = "books";
        List<CityDTO> cities = queryCitiesByBookTitle(bookTitle, collectionName);
        return cities;
    }


    public List<BookDTO> queryBooksByGeolocation(double lon, double lat, double distance, String collectionName){

        //Kilometers to miles
        distance = distance * 0.62137;

        BasicDBList geoCoord = new BasicDBList();
        geoCoord.add(lon);
        geoCoord.add(lat);

        BasicDBList geoParams = new BasicDBList();
        geoParams.add(geoCoord);
        geoParams.add(distance/3963.2);

        BasicDBObject queryInner = new BasicDBObject("location",
                new BasicDBObject("$geoWithin",
                        new BasicDBObject("$centerSphere", geoParams)));
        //DBObject nameClause = new BasicDBObject("location", queryInner);
        DBObject elemMatch = new BasicDBObject("$elemMatch", queryInner);
        DBObject query = BasicDBObjectBuilder.start().add("cities", elemMatch).get();

        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator();

        List<BookDTO> books = mongoCursorToBookDTOList(cursor);

        return books;
    }

    public List<BookDTO> queryBooksByAuthor(String author, String collectionName){


        DBObject query = BasicDBObjectBuilder.start().add("authors", author).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked


        List<BookDTO> books = mongoCursorToBookDTOList(cursor);
        return books;
    }


    public List<BookDTO> queryBooksByCity(String city, String collectionName){
        DBObject nameClause = new BasicDBObject("name", city);
        DBObject elemMatch = new BasicDBObject("$elemMatch", nameClause);
        DBObject query = BasicDBObjectBuilder.start().add("cities", elemMatch).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator(); //This could be mocked

        List<BookDTO> books = mongoCursorToBookDTOList(cursor);
        return books;
    }


    public List<CityDTO> queryCitiesByBookTitle(String bookTitle, String collectionName) {

        List<BookDTO> booksDTO = null;
        List<CityDTO> citiesDTO = null;
        DBObject query = BasicDBObjectBuilder.start().add("title", bookTitle).get();
        MongoCollection collection = mongoConnection.getWorkableMongoCollection(this.databaseName, collectionName);
        MongoCursor<Document> cursor = collection.find((Bson) query).iterator();
        booksDTO = mongoCursorToBookDTOList(cursor);

        if (booksDTO.size() == 1) {
            citiesDTO = booksDTO.get(0).getCities();
        }

        return citiesDTO;
    }

    //UNTESTED, CRIME CRIME CRIME. Not sure how to create the mongocursor in the test.
    public List<BookDTO> mongoCursorToBookDTOList(MongoCursor<Document> cursor) {

        BookDTO tempBook;
        List<BookDTO> books = new ArrayList<BookDTO>();
        Document doc;
        while(cursor.hasNext()){
            doc = cursor.next();

            tempBook = bookDocumentToBookDTO(doc);
            if (tempBook != null) {
                books.add(tempBook);
            }

        }
        return books;
    }


    public CityDTO cityDocumentToCityDTO(Document doc){

        CityDTO tempCity = null;

            if (doc.containsKey("name") && doc.containsKey("location")) {
                Document locationDoc = (Document) doc.get("location");
                ArrayList<Double> coordinates = (ArrayList<Double>) locationDoc.get("coordinates");
                tempCity = new CityDTO(doc.getString("name"), coordinates.get(0), coordinates.get(1));
            }

        return tempCity;
    }

    public BookDTO bookDocumentToBookDTO(Document doc) {
        BookDTO tempBook = null;
        if(doc.containsKey("bookId") && doc.containsKey("title") && doc.containsKey("authors")){
            String titleString;
            Object title = doc.get("title");
            List<CityDTO> citiesDTO = new ArrayList<CityDTO>();
            CityDTO tempCityDTO;
            Document locationDoc;
            if(title.getClass() == Integer.class) titleString = title+"";
            else titleString = ((String) title);
            tempBook = new BookDTO(doc.getInteger("bookId"), titleString, "");
            List<String> authors = (List<String>) doc.get("authors");
            tempBook.setAuthors(authors);
            List<Document> cities = (List<Document>) doc.get("cities");
            for(Document cityDoc : cities){
                if (cityDoc.containsKey("name") && cityDoc.containsKey("location")) {
                    locationDoc = (Document) cityDoc.get("location");
                    ArrayList<Double> coordinates = (ArrayList<Double>) locationDoc.get("coordinates");
                    tempCityDTO = new CityDTO(cityDoc.getString("name"), Double.parseDouble(coordinates.get(0)+""),
                            Double.parseDouble(coordinates.get(1)+""));
                    citiesDTO.add(tempCityDTO);
                }
            }

            tempBook.setCities(citiesDTO);

        }
        return tempBook;
    }
}
