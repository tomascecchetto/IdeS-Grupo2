package ids.androidsong.object;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import static ids.androidsong.help.Xml.AddChild;
import static ids.androidsong.help.Xml.AddRootChild;
import static ids.androidsong.help.Xml.ExistsNode;
import static ids.androidsong.help.Xml.FindNode;
import static ids.androidsong.help.Xml.GetChilds;
import static ids.androidsong.help.Xml.GetValue;
import static ids.androidsong.help.Xml.SetValue;
import static ids.androidsong.help.Xml.GetRawDocument;
import static ids.androidsong.help.Xml.GetStringFromDocument;

import android.util.Log;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.Enum;

/**
 * Created by ALAN on 30/10/2017.
 * Encapsula métodos de la interfaz XML
 */

public class CancionXml extends Cancion {
    private String path;

    private static final String APP_ATRIBUTOS_NODO = "AndroidSong_attributes";

    CancionXml() {
        super();

    }

    public CancionXml(String t, String p, String c) {
        super();
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
            Log.e("Error",e.getMessage());
        }
    }

    protected void BuildCancion(Document dom) {
        try
        {
            CargarNodos(dom);
        }
        catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }

    Document getDocument()
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
            getAtributos().add(new Atributo(Enum.atributo.values()[i].name(),GetValue(xml,Enum.atributoXml.values()[i].name())));
        }
        Element[] atributosUsuario = GetChilds(APP_ATRIBUTOS_NODO,xml);
        if (atributosUsuario.length > 0) {
            for (Element element : atributosUsuario) {
                getAtributos().add(new Atributo(element.getTagName(), element.getTextContent()));
            }
        }
    }

    File toXml(){
        return toXml("tempXml.txt");
    }

    File toXml(String fileName){
        Document dom = null;
        try {
            dom = GetRawDocument(R.raw.nuevacancion);
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
        LoadCancion(dom);
        return grabar(dom,fileName);
    }

    File grabar(Document dom, String fileName) {
        String nomarchivo = "/"+fileName;
        File file = new File(App.GetContext().getCacheDir().getAbsolutePath()+nomarchivo);
        try {
            String contenido = GetStringFromDocument(dom);
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            assert contenido != null;
            osw.write(contenido);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
        return file;
    }

    void LoadCancion(Document dom){
        SetValue("title",getTitulo(),dom);
        SetValue("lyrics",getLetra(),dom);
        if (!ExistsNode(APP_ATRIBUTOS_NODO,dom))
            AddRootChild(APP_ATRIBUTOS_NODO,dom);
        for (Atributo a:getAtributos()){
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
