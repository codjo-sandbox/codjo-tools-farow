package net.codjo.tools.farow.command.nexus;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.httpclient.HttpMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
/**
 *
 */
public class DomUtil {

    static NodeList extractNodeByXpath(Document document, String xpathExpression) throws XPathExpressionException {
        XPathFactory xpFactory = XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = xpFactory.newXPath();

        return (NodeList)xpath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);
    }


    static Document buildDocument(HttpMethod httpMethod) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        InputStream response = httpMethod.getResponseBodyAsStream();

        return dbFactory.newDocumentBuilder().parse(response);
    }
}
