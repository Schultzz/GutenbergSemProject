package logic.bookscanner;

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
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jonathan on 15.05.17.
 */
public class BookScanner {

    public String getBookAsString(String fileName) throws FileNotFoundException {

        String bookAsString;

        bookAsString = new Scanner(new File(fileName)).useDelimiter("\\Z").next();

        return bookAsString;
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


    public ScannedBook setMetaDataOnBook(ScannedBook book, String filename) throws IOException {
        String file = filename.substring(19);
        String path = filename.substring(0,19);
        String fileString = path + "pg" + file;
        fileString = fileString.replace(".txt", ".rdf");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(fileString));

            NodeList nList = document.getElementsByTagName("pgterms:name");
            Node tempNode;
            for (int i = 0; i < nList.getLength(); i++) {
                tempNode = nList.item(i);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) tempNode;
                    book.addAuthor(element.getFirstChild().getNodeValue());
                }
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
        }
        return book;
    }
}
