package ids.androidsong.object;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static ids.androidsong.help.xml.GetValue;
import static ids.androidsong.help.xml.SetValue;
import static ids.androidsong.help.xml.getRawDocument;
import static ids.androidsong.help.xml.getStringFromDocument;

import ids.androidsong.R;
import ids.androidsong.help.App;
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

    public cancionXml(){

    }

    public cancionXml(String t, String p, String c){
        this.titulo = t;
        this.path = p;
        this.carpeta = c;
    }

    @Override
    public void fill(){
        Document dom;
        try {
            dom = getDocument();
            BuildCancion(dom);
            fillId();
        } catch (Exception e) {
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

    protected Document getDocument()
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

    protected File toXml(){
        Document dom = null;
        try {
            dom = getRawDocument(R.raw.nuevacancion);
        } catch (Exception e) {}
        LoadCancion(dom);
        return grabar(dom);
    }

    public File grabar(Document dom) {
        String nomarchivo = "/tempXml.txt";
        File file = new File(App.getContext().getCacheDir().getAbsolutePath()+nomarchivo);
        try {
            String contenido = getStringFromDocument(dom);
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(contenido);
            osw.flush();
            osw.close();
        } catch (Exception e) {
        }
        return file;
    }

    protected void LoadCancion(Document dom){
        SetValue("title",getTitulo(),dom);
        SetValue("lyrics",getLetra(),dom);
        for (atributo atributo:getAtributos()){
            switch (atributo.getNombre()){
                case "autor":
                    SetValue("author",atributo.getValor(),dom);
                    break;
                case "interprete":
                    SetValue("copyright",atributo.getValor(),dom);
                    break;
                case "presentacion":
                    SetValue("presentation",atributo.getValor(),dom);
                    break;
                case "tono":
                    SetValue("key",atributo.getValor(),dom);
                    break;
                case "transporte":
                    SetValue("capo",atributo.getValor(),dom);
                    break;
            }
        }
    }
}
