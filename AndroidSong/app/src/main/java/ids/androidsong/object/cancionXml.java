package ids.androidsong.object;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static ids.androidsong.help.xml.GetValue;
import ids.androidsong.help.Enum;

/**
 * Created by ALAN on 30/10/2017.
 * Encapsula métodos de la interfaz XML
 */

public class cancionXml extends cancion {
    protected String path;
    protected String author = "";
    protected String copyright = "";
    protected String hymn_number = "";
    protected String presentation = "";
    protected String ccli = "";
    protected String capo = "";
    protected boolean printCapo = false;
    protected String key = "";
    protected String aka = "";
    protected String key_line = "";
    protected String user1 = "";
    protected String user2 = "";
    protected String user3 = "";
    protected String theme = "";
    protected String tempo = "";
    protected String time_sig = "";
    protected String lyrics = "";

    public cancionXml(String t, String p, String c){
        this.titulo = t;
        this.path = p;
        this.carpeta = c;
    }

    public void load(){
        Document dom;
        try {
            dom = getDocument();
            BuildCancion(dom);
        } catch (Exception e) {
            GetCancionError();
        }
    }

    private void BuildCancion(Document dom) {
        try
        {
            CargarNodos(dom);
        }
        catch (Exception e){}
    }

    private void GetCancionError() {
        titulo = "Error";
        author = "";
        lyrics = "Error al cargar la canción";
    }

    private Document getDocument()
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(new File(path));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }


    private void CargarNodos(Document xml){
        llenarSecciones(GetValue(xml,"lyrics"));
        setTipo(Enum.itemTipo.CANCION.name());
        getAtributos().add(new atributo("autor",GetValue(xml,"author")));
        getAtributos().add(new atributo("interprete",GetValue(xml,"copyright")));
        getAtributos().add(new atributo("numeroHimno",GetValue(xml,"hymn_number")));
        getAtributos().add(new atributo("presentacion",GetValue(xml,"presentation")));
        getAtributos().add(new atributo("transporte",GetValue(xml,"capo")));
        getAtributos().add(new atributo("tono",GetValue(xml,"key")));
        getAtributos().add(new atributo("tempo",GetValue(xml,"tempo")));
    }
}
