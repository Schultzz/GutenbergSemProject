//package logic.bookscanner;
//
//import bookscanner.entities.Author;
//import bookscanner.entities.Book;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.util.HashSet;
//
//import static org.hamcrest.CoreMatchers.hasItem;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//
///**
// * Created by ms on 15.05.17.
// */
//public class ScannedBookTest {
//
//    ScannedBook scannedBook;
//    Book book;
//
//    @Before
//    public void beforeEach() {
//        scannedBook = new ScannedBook(new HashSet<String>());
//        book = new Book();
//    }
//
//
//    @Test
//    public void setTitleNewlineTest() {
//        String title = "test\nTitle";
//        book.setTitle(title);
//
//        assertThat(book.getTitle(), is("test"));
//
//    }
//
//    @Test
//    public void setTitleTest() {
//        String title = "testTitle";
//        book.setTitle(title);
//
//        assertThat(book.getTitle(), is("testTitle"));
//
//    }
//
//    @Test
//    public void addAuthorTest() {
//        Author author = new Author("Id", "Name");
//        book.addAuthor(author);
//
//        assertThat(book.getAuthors(), hasItem(author));
//    }
//
//    //TODO FIX when all csv mapper are done.
//    @Ignore
//    public void getCSVStringTest() {
//        Author author = new Author("Id", "Name");
//        book.setTitle("testTitle");
//        book.addAuthor(author);
//        book.addMatchedCity("Hellerup");
//        String csvString = scannedBook.asCsvString();
//        String expectedString = "\"testTitle\",\"Magnus\",\"Hellerup\"\n";
//
//        assertThat(csvString, is(expectedString));
//    }
//
//    @Ignore
//    public void getCSVStringWithMultipleValuesTest() {
//        scannedBook.setTitle("testTitle");
//        scannedBook.addAuthor("Magnus");
//        scannedBook.addAuthor("Klavs");
//        scannedBook.addMatchedCity("Hellerup");
//        scannedBook.addMatchedCity("Rungsted");
//        String csvString = scannedBook.asCsvString();
//        String expectedString = "\"testTitle\",\"Magnus,Klavs\",\"Hellerup,Rungsted\"\n";
//
//        assertThat(csvString, is(expectedString));
//    }
//
//
//}
