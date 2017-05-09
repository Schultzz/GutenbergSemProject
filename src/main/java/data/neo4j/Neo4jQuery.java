package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uffe on 05-05-2017.
 */
public class Neo4jQuery implements IQuery {
    private String URL;
    private String USER;
    private String PASSWORD;
    private Neo4jConnection connection;

    public Neo4jQuery(String URL, String USER, String PASSWORD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.connection = new Neo4jConnection();
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        Session session = connection.getConnection(URL, USER, PASSWORD);
        String query = "MATCH (a:Book) " +
                " WHERE a.author = \""+ author +
                "\" RETURN a.author AS author, a.title AS title, a.id AS id;";
        StatementResult result = session.run(query);

        List<BookDTO> resultList = new ArrayList<BookDTO>();
        while(result.hasNext()){
            Record record = result.next();
            int id = record.get("id").asInt();
            String au = record.get("author").asString();
            String ti = record.get("title").asString();
            BookDTO bk = new BookDTO(id, ti, au);
            resultList.add(bk);
        }
        session.close();

        return resultList;
    }


}
