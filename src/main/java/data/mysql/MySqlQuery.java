package data.mysql;

import data.IQuery;
import data.dto.BookDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 05-05-17.
 */
public class MySqlQuery implements IQuery {

    private MySqlConnector connector;

    MySqlQuery(MySqlConnector connector) {
        this.connector = connector;
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        ArrayList booklist = null;

        try {
            String sql = "SELECT * FROM books WHERE author = ?";
            connector.open();
            PreparedStatement pstmt = connector.getConnection().prepareStatement(sql);
            pstmt.setString(1, author);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                booklist = new ArrayList();
                do {
                    BookDTO book = new BookDTO(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"));
                    booklist.add(book);
                } while (rs.next());
            }
            connector.close();
        } catch (SQLException ex) {
            //handle exception or add to method.
            ex.printStackTrace();
        }

        return booklist;
    }
}
