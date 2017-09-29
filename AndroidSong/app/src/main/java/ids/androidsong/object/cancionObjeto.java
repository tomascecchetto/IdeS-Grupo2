package ids.androidsong.object;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ids.androidsong.R;

import static ids.androidsong.help.xml.*;


/**
 * Modelo de objeto Canción
 */
public class cancionObjeto implements Serializable {
    public cancionCabecera cabecera;
    public String title = "";
    public String author = "";
    public String copyright = "";
    public String hymn_number = "";
    public String presentation = "";
    public String ccli = "";
    public String capo = "";
    public boolean printCapo = false;
    public String key = "";
    public String aka = "";
    public String key_line = "";
    public String user1 = "";
    public String user2 = "";
    public String user3 = "";
    public String theme = "";
    public String tempo = "";
    public String time_sig = "";
    public String lyrics = "";

    public cancionObjeto Load(cancionCabecera cancionSeleccionada){
        Document dom;
        try {
            dom = getDocument(cancionSeleccionada);
            cabecera = cancionSeleccionada;
        } catch (Exception e) {
            return GetCancionError();
        }
        return BuildCancion(dom);
    }

    private cancionObjeto BuildCancion(Document dom) {
        CargarNodosPrincipales(dom);
        try
        {
            CargarNodosSecundarios(dom);
        }
        catch (Exception e){}
        return this;
    }

    private cancionObjeto GetCancionError() {
        cancionObjeto cancionError = new cancionObjeto();
        cancionError.title = "Error";
        cancionError.author = "";
        cancionError.lyrics = "Error al cargar la canción";
        return cancionError;
    }

    private Document getDocument(fileCabecera cancionSeleccionada)
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(new File(cancionSeleccionada.getDir()));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }

    public void grabar(Document dom) {
        try {
            String nomarchivo = cabecera.titulo;
            String contenido = getStringFromDocument(dom);
            File file = new File(cabecera.getPath(), nomarchivo);
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(contenido);
            osw.flush();
            osw.close();
        } catch (Exception e) {
        }
    }

    public void GuardarSetValue(String tag, String valor)
    {
        Document dom = null;
        try
        {
            dom = getDocument(cabecera);
        }
        catch (Exception e) {}
        Element e = dom.getDocumentElement();
        NodeList lista = e.getElementsByTagName(tag);
        Node nodo = lista.item(0);
        nodo.setTextContent(valor);
        grabar(dom);
    }

    private void CargarNodosPrincipales(Document xml){
        title = GetValue(xml,"title");
        author = GetValue(xml,"author");
        lyrics = GetValue(xml,"lyrics");
    }

    private void CargarNodosSecundarios(Document xml){
        copyright = GetValue(xml,"copyright");
        hymn_number = GetValue(xml,"hymn_number");
        presentation = GetValue(xml,"presentation");
        ccli = GetValue(xml,"ccli");
        capo = GetValue(xml,"capo");
        printCapo = false;
        key = GetValue(xml,"key");
        aka = GetValue(xml,"aka");
        key_line = GetValue(xml,"key_line");
        user1 = GetValue(xml,"user1");
        user2 = GetValue(xml,"user2");
        user3 = GetValue(xml,"user3");
        theme = GetValue(xml,"theme");
        tempo = GetValue(xml,"tempo");
        time_sig = GetValue(xml,"time_sig");
    }

    protected void guardar(){
        try {
            Document dom = getRawDocument(R.raw.nuevacancion);
            LoadCancion(dom);
            grabar(dom);
        }
        catch (Exception e){};

    }

    private void LoadCancion(Document dom){
        SetValue("title",title,dom);
        SetValue("author",author,dom);
        SetValue("key",key,dom);
        SetValue("user1",user1,dom);
        SetValue("lyrics",lyrics,dom);
    }
}
