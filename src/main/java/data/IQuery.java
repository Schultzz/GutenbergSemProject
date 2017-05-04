package data;

import data.dto.BookDTO;

import java.util.List;

/**
 * Created by ms on 04-05-17.
 */
public interface IQuery {
    List<BookDTO> getBooksByAuthor(String author);
}
