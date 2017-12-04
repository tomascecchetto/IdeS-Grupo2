package ids.androidsong.object;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

import static ids.androidsong.help.xml.AddChild;
import static ids.androidsong.help.xml.AddRootChild;
import static ids.androidsong.help.xml.ExistsNode;
import static ids.androidsong.help.xml.FindNode;
import static ids.androidsong.help.xml.GetChilds;
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

    private String APP_ATRIBUTOS_NODO = "AndroidSong_attributes";

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
        int i;
        for (i = 0; i<Enum.atributo.values().length; i++){
            getAtributos().add(new atributo(Enum.atributo.values()[i].name(),GetValue(xml,Enum.atributoXml.values()[i].name())));
        }
        Element[] atributosUsuario = GetChilds(APP_ATRIBUTOS_NODO,xml);
        if (atributosUsuario.length > 0) {
            for (Element element : atributosUsuario) {
                getAtributos().add(new atributo(element.getTagName(), element.getTextContent()));
            }
        }
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
        if (!ExistsNode(APP_ATRIBUTOS_NODO,dom))
            AddRootChild(APP_ATRIBUTOS_NODO,dom);
        for (atributo a:getAtributos()){
            try {
                Enum.atributo atributo = Enum.atributo.valueOf(a.getNombre());
                SetValue(Enum.atributoXml.values()[atributo.ordinal()].name(),a.getValor(),dom);
            } catch (IllegalArgumentException e) {
                if (!FindNode(a.getNombre(),a.getValor(),dom)){
                    AddChild(APP_ATRIBUTOS_NODO,a.getNombre(),a.getValor(),dom);
                }
            }
        }
    }
}
