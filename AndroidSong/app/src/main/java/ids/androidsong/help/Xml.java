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
    Manipulación de Xml
 */
public class Xml {

    public static String GetStringFromDocument(Document dom)
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

    public static boolean ExistsNode(String tag, Document dom) {
        boolean ret = false;
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(tag);
        if (lista.getLength() > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean FindNode(String tag, String valor, Document dom) {
        boolean ret = false;
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(tag);
        if (lista.getLength() > 0){
            lista.item(0).setTextContent(valor);
            ret = true;
        }
        return ret;
    }

    public static String GetValue(Document dom, String tag){
        Element e = dom.getDocumentElement();
        return GetValue(e,tag);
    }

    private static String GetValue(Element e, String tag){
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

    private static Element[] GetChilds(String tag, Element e)
    {
        NodeList lista = e.getElementsByTagName(tag);
        NodeList hijos = lista.item(0).getChildNodes();
        Element[] elementos = new Element[hijos.getLength()];
        int i;
        if (hijos.getLength()>0) {
            for (i = 0; i < elementos.length; i++) {
                elementos[i] = (Element) hijos.item(i);
            }
        }
        return elementos;
    }

    public static Document GetRawDocument(int rawId)
            throws ParserConfigurationException, SAXException, IOException {
        Resources res = App.GetContext().getResources();
        InputStream is = res.openRawResource(rawId);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }

    public static void AddChild(String parent, String name, String value, Document dom){
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(parent);
        Node nodo = lista.item(0);
        Element nuevo = dom.createElement(name);
        nuevo.setTextContent(value);
        nodo.appendChild(nuevo);
    }

    public static void AddRootChild(String name, Document dom){
        Element e = dom.getDocumentElement();
        Element nuevo = dom.createElement(name);
        e.appendChild(nuevo);
    }
}
