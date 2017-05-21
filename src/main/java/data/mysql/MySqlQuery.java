package data.mysql;

import data.IQuery;
import data.dto.BookDTO;
import data.dto.CityDTO;

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

    public MySqlQuery(MySqlConnector connector) {
        this.connector = connector;
    }

    //3
    public List<BookDTO> getBooksByAuthor(String author) {
        List<BookDTO> booklist = null;
        try {
            connector.open();
            booklist = queryBookByAuthor(author);
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booklist;
    }

    //1
    public List<BookDTO> getBooksByCity(String city) {
        List<BookDTO> booklist = null;
        try {
            connector.open();
            booklist = queryBooksByCity(city);
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booklist;
    }

    //4
    public List<BookDTO> getBooksByGeoLocation(double lon, double lat, double distance) {
        List<BookDTO> booklist = null;
        try {
            connector.open();
            booklist = queryBooksByGeoLocation(lon, lat, distance);
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booklist;
    }

    //2
    public List<CityDTO> getCitiesByBookTitle(String bookTitle) {
        List<CityDTO> cities = null;
        try {
            connector.open();
            cities = queryCitiesByBookTitle(bookTitle);
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    //Below could be in a new class ---

    protected List<BookDTO> queryBookByAuthor(String author) throws SQLException {
        String sql = "SELECT DISTINCT b.book_id, b.title, l.city_name, l.lng, l.lat\n" +
                "FROM location l\n" +
                "  INNER JOIN location_book_join\n" +
                "    ON l.city_name = location_book_join.city_name\n" +
                "  INNER JOIN book b\n" +
                "    ON location_book_join.book_id = b.book_id\n" +
                "  INNER JOIN author_book_join\n" +
                "    ON b.book_id = author_book_join.book_id\n" +
                "  INNER JOIN author ON author_book_join.author_id = author.author_id\n" +
                "  WHERE author.name = ? ;";
        ArrayList<BookDTO> booklist = null;
        BookDTO book;

        PreparedStatement pstmt = connector.getConnection().prepareStatement(sql);
        pstmt.setString(1, author);
        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            booklist = new ArrayList();
            do {
                int bookId = resultSet.getInt("book_id");
                book = new BookDTO(
                        bookId,
                        resultSet.getString("title"),
                        "");
                book.addCity(new CityDTO(
                        resultSet.getString("city_name"),
                        Double.parseDouble(resultSet.getString("lng")),
                        Double.parseDouble(resultSet.getString("lat"))
                ));

                while (resultSet.next() && resultSet.getInt("book_id") == bookId) {
                    book.addCity(new CityDTO(
                            resultSet.getString("city_name"),
                            Double.parseDouble(resultSet.getString("lng")),
                            Double.parseDouble(resultSet.getString("lat"))
                    ));
                }
                booklist.add(book);
                resultSet.previous();

            } while (resultSet.next());
        }

        return booklist;
    }

    protected List<BookDTO> queryBooksByCity(String city) throws SQLException {

        String sql = "SELECT a.name AS author, b.title\n" +
                "FROM author a\n" +
                "  INNER JOIN author_book_join ab\n" +
                "    ON a.author_id = ab.author_id\n" +
                "  INNER JOIN book b\n" +
                "    ON ab.book_id = b.book_id\n" +
                "  INNER JOIN location_book_join c\n" +
                "    ON b.book_id = c.book_id\n" +
                "WHERE city_name = ?;";

        PreparedStatement pstmt = connector.getConnection().prepareStatement(sql);
        pstmt.setString(1, city);
        ResultSet resultSet = pstmt.executeQuery();

        ArrayList<BookDTO> books = null;

        if (resultSet.next()) {
            books = new ArrayList();
            do {
                BookDTO book = new BookDTO(
                        0,
                        resultSet.getString("title"),
                        resultSet.getString("author")
                );
                books.add(book);
            } while (resultSet.next());
        }

        return books;
    }

    protected List<BookDTO> queryBooksByGeoLocation(double lon, double lat, double distance) throws SQLException {

        String sql = "SELECT DISTINCT b.title, location.city_name, location.lng, location.lat\n" +
                "FROM book b\n" +
                "  INNER JOIN location_book_join\n" +
                "  ON b.book_id = location_book_join.book_id\n" +
                "  INNER JOIN location\n" +
                "  ON location_book_join.city_name = location.city_name" +
                "  WHERE st_distance_sphere(geom, point(?, ?)) <= ? * 1000;"; //in kilometers

        PreparedStatement pstmt = connector.getConnection().prepareStatement(sql);
        pstmt.setString(1, lon + "");
        pstmt.setString(2, lat + "");
        pstmt.setDouble(3, distance);
        ResultSet resultSet = pstmt.executeQuery();

        ArrayList<BookDTO> books = null;

        if (resultSet.next()) {
            books = new ArrayList();
            do {
                BookDTO book = new BookDTO(
                        0,
                        resultSet.getString("title"),
                        ""
                );
                book.addCity(new CityDTO(
                        resultSet.getString("city_name"),
                        resultSet.getDouble("lng"),
                        resultSet.getDouble("lat")));
                books.add(book);
            } while (resultSet.next());
        }

        return books;
    }

    protected List<CityDTO> queryCitiesByBookTitle(String booktitle) throws SQLException {


        String sql = "SELECT l.city_name, l.lat, l.lng\n" +
                "FROM location l\n" +
                "  INNER JOIN location_book_join lb\n" +
                "    ON l.city_name = lb.city_name\n" +
                "  INNER JOIN book b\n" +
                "    ON b.book_id = lb.book_id\n" +
                "  WHERE title = ?;";

        PreparedStatement pstmt = connector.getConnection().prepareStatement(sql);
        pstmt.setString(1, booktitle);
        ResultSet resultSet = pstmt.executeQuery();

        ArrayList<CityDTO> cities = null;

        if (resultSet.next()) {
            cities = new ArrayList();
            do {
                CityDTO city = new CityDTO(
                        resultSet.getString("city_name"),
                        resultSet.getDouble("lng"),
                        resultSet.getDouble("lat")
                );
                cities.add(city);
            } while (resultSet.next());
        }

        return cities;
    }


}


