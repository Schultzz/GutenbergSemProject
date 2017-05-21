package bookscanner;

import bookscanner.entities.Author;
import bookscanner.entities.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jonathan on 15.05.17.
 */
public class BookScanner {

    private ArrayList<Author> authors;
    private ArrayList<Book> books;
    private Map<String, Author> authorMap;

    public BookScanner() {
        this.authors = new ArrayList<Author>();
        this.books = new ArrayList<Book>();
        authorMap = new HashMap();
    }

    public Book setMetaDataOnBook(Book book, String filename) throws IOException {
        filename = filename.toLowerCase();
        String[] fileArray = filename.split("/");
        String file = fileArray[fileArray.length - 1];
        String bookName = file.replace(".txt", "");
        String fileString = filename.replace(file, "pg" + file);
        fileString = fileString.replace(".txt", ".rdf");
        fileString = fileString.replace("/books/", "/metadata/" + bookName + "/" );


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(fileString));

            NodeList nList = document.getElementsByTagName("pgterms:name");
            NodeList authorIDList = document.getElementsByTagName("pgterms:agent");
            Node tempNode;
            Node tempNode2;
            for (int i = 0; i < nList.getLength(); i++) {
                tempNode = nList.item(i);
                tempNode2 = authorIDList.item(i);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) tempNode;
                    Element element2 = (Element) tempNode2;
                    String[] authorIDs = element2.getAttribute("rdf:about").split("/");
                    String authorID = authorIDs[authorIDs.length - 1];
                    String authorName = element.getFirstChild().getNodeValue();
                    Author author = new Author(authorID, authorName);
                    addAuthorToMap(authorID, author);
                    book.addAuthor(author);
                    book.setId(file.replace(".txt", ""));
                }

            }
            if(nList.getLength()==0){
                Author author = new Author("49", "Unknown");
                addAuthorToMap("49", author);
                book.addAuthor(author);
                book.setId("49");
            }

            nList = document.getElementsByTagName("dcterms:title");
            if (nList.getLength() > 0) {
                tempNode = nList.item(0);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) tempNode;
                    book.setTitle(element.getFirstChild().getNodeValue());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }catch (FileNotFoundException ex){
            System.out.println("FileNotFound on " + filename);
            return null;
        }
        return book;
    }

    public HashSet<String> getCapitalizedWords(String bookAsString) {

        HashSet<String> matches = new HashSet();

        String regex = "([A-Z][a-z]+[ ]?)+([A-Z][a-z]+)?";

        Matcher matcher = Pattern.compile(regex).matcher(bookAsString);

        while (matcher.find()) {
            matches.add(matcher.group().trim());
        }
        return matches;
    }

    public String getBookAsString(String fileName) throws FileNotFoundException {

        String bookAsString = "";
        try {
            bookAsString = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
        }catch (NoSuchElementException e){
            System.out.println("No such elem ex on " + fileName);
        }
        return bookAsString;
    }

    private void addAuthorToMap(String authorID, Author author) {
        if (!authorMap.containsKey(authorID)) {
            authorMap.put(authorID, author);
        }
    }

    public Map<String, Author> getAuthorMap() {
        return authorMap;
    }

}
