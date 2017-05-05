package data.guffeLaverSovsen;

import data.IQuery;
import data.dto.BookDTO;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uffe on 05-05-2017.
 */
public class Neo4jQuery implements IQuery {
    private final String URL = "bolt://localhost:7687";
    private final String USER = "neo4j";
    private final String PASSWORD = "class";

    public List<BookDTO> getBooksByAuthor(String author) {
        Session session = getConnection();
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

    private Session getConnection(){
        Driver driver = GraphDatabase.driver(
                URL,
                AuthTokens.basic(USER, PASSWORD));
        return driver.session();
    }
}
