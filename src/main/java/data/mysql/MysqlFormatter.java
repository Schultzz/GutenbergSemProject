package data.mysql;

import data.dto.BookDTO;
import data.dto.CityDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 20-05-17.
 */
public class MysqlFormatter {


    public List<BookDTO> formatBooksWithCities(ResultSet resultSet) throws SQLException {

        ArrayList<BookDTO> booklist = null;
        BookDTO book;

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

    public List<CityDTO> formatCities(ResultSet resultSet) throws SQLException {

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


    public List<BookDTO> formatTitleWithAuthor(ResultSet resultSet) throws SQLException {
        ArrayList<BookDTO> books = null;

        if (resultSet.next()) {
            books = new ArrayList();
            do {
                BookDTO book = new BookDTO(
                        0,
                        resultSet.getString("author"),
                        resultSet.getString("title")
                );
                books.add(book);
            } while (resultSet.next());
        }

        return books;
    }

    public List<BookDTO> formatTitleWithCity(ResultSet resultSet) throws SQLException {
        ArrayList<BookDTO> books = null;

        if (resultSet.next()) {
            books = new ArrayList();
            do {
                BookDTO book = new BookDTO(
                        0,
                        "",
                        resultSet.getString("title")
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
}
