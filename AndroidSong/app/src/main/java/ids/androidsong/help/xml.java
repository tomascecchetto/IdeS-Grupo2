package ids.androidsong.help;

import android.content.res.Resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
    Manipulación de xml
 */
public class xml {

    public static String getStringFromDocument(Document dom)
    {
        try
        {
            DOMSource domSource = new DOMSource(dom);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch(TransformerException ex)
        {
            return null;
        }
    }

    public static void SetValue(String tag, String valor, Document dom)
    {
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(tag);
        Node nodo = lista.item(0);
        nodo.setTextContent(valor);
    }

    public static Node FindNode(String tag, String valor, Document dom)
    {
        Node ret = null;
        int i;
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(tag);
        for (i=0; i< lista.getLength(); i++) {
            if (lista.item(i).getTextContent().contentEquals(valor)){
                ret=lista.item(i);
            }
        }
        return ret;
    }

    public static String GetValue(Document dom, String tag){
        Element e = dom.getDocumentElement();
        return GetValue(e,tag);
    }

    public static String GetValue(Element e, String tag){
        NodeList lista = e.getElementsByTagName(tag);
        Node nodo = lista.item(0);
        if (nodo.hasChildNodes())
            return nodo.getFirstChild().getNodeValue();
        else
            return "";
    }

    public static Element[] GetChilds(String tag, Document dom)
    {
        Element e = dom.getDocumentElement();
        return GetChilds(tag, e);
    }

    public static Element[] GetChilds(String tag, Element e)
    {
        NodeList lista = e.getElementsByTagName(tag);
        Element[] elementos = new Element[lista.getLength()];
        int i;
        if (elementos.length>0) {
            for (i = 0; i < elementos.length; i++) {
                elementos[i] = (Element) lista.item(i);
            }
        }
        return elementos;
    }

    public static Document getRawDocument (int rawId)
            throws ParserConfigurationException, SAXException, IOException {
        Resources res = App.getContext().getResources();
        InputStream is = res.openRawResource(rawId);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }

    public static Document GetInternalDocument (String name){
        InputStream is;
        DocumentBuilder db;
        Document dom = null;
        try {
            is = App.getContext().openFileInput(name);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            db = factory.newDocumentBuilder();
            dom = db.parse(is);
        } catch (Exception e) {}
        return dom;
    }
}
