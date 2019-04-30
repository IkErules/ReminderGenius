package ch.hslu.appe.reminder.genius.Parser;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ch.hslu.appe.reminder.genius.Model.SearchContact;

public class SearchContactXmlParser {

    private static final String ns = null;

    public ArrayList<SearchContact> parse(InputStream in) throws ParserConfigurationException, SAXException, IOException{
        ArrayList<SearchContact> searchContacts = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(in);
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("entry");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                SearchContact searchContact = getSearchContactFromElement(eElement);
                Log.i("SearchContactXmlParser", searchContact.toString());
                searchContacts.add(searchContact );
            }
        }
        return searchContacts;
    }

    private SearchContact getSearchContactFromElement(Element element) {
        SearchContact searchContact = new SearchContact();

        searchContact.setTitle(getTextContentFromElement(element, "title"));
        searchContact.setContent(getTextContentFromElement(element, "content"));
        searchContact.setType(getTextContentFromElement(element, "tel:type"));
        searchContact.setName(getTextContentFromElement(element, "tel:name"));
        searchContact.setFirstname(getTextContentFromElement(element, "tel:firstname"));
        searchContact.setMaidenname(getTextContentFromElement(element, "tel:maidenname"));
        searchContact.setStreet(getTextContentFromElement(element, "tel:street"));
        searchContact.setZip(getTextContentFromElement(element, "tel:zip"));
        searchContact.setCity(getTextContentFromElement(element, "tel:city"));
        searchContact.setCanton(getTextContentFromElement(element, "tel:canton"));
        searchContact.setPhone(getTextContentFromElement(element, "tel:phone"));

        return searchContact;
    }

    private String getTextContentFromElement(Element element, String tagName) {
        NodeList node = element.getElementsByTagName(tagName);
        return isNodeListEmpty(node) ? "" : node.item(0).getTextContent();
    }

    private boolean isNodeListEmpty(NodeList nodeList) {
        return nodeList.getLength() == 0;
    }
}
