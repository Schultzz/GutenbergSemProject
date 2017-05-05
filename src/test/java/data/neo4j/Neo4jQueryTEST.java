package data.neo4j;

import data.IQuery;
import data.dto.BookDTO;
import data.guffeLaverSovsen.Neo4jQuery;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Created by Uffe on 05-05-2017.
 */
public class Neo4jQueryTEST {

    @Test
    public void TestNeo4jQuery(){
        IQuery nq = new Neo4jQuery();
        List<BookDTO> DTOBooks = nq.getBooksByAuthor("Villy Soevndal");
        for (BookDTO bk : DTOBooks) {
            assertThat(bk.getAuthor(), is("Villy Soevndal"));
            assertThat(bk.getTitle(), notNullValue());
            assertThat(bk.getId(), notNullValue());
        }
    }
}
