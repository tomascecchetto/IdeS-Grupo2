package ids.androidsong.object;

import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ids.androidsong.help.App;

/**
 * Created by Alan on 25/2/2019.
 * Modela y encapsula el comportamiento de una canción para la funcionalidad de Compartir
 */

public class CancionShare extends CancionXml implements Serializable {

    CancionShare() {
        super();
    }

    public CancionShare(String t, String c) {
        super();
        this.titulo = t;
        this.carpeta = c;
    }

    public CancionShare(Cancion c) {
        this.setId(c.getId());
        this.setCarpeta(c.getCarpeta());
        this.setTitulo(c.getTitulo());
        this.setSecciones(c.getSecciones());
        this.setAtributos(c.getAtributos());
    }

    public void fill(Uri uri){
        Document dom;
        try {
            dom = getDocument(uri);
            BuildCancion(dom);
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
    }

    public boolean existeCancion(){
        fillId();
        return getId() != 0;
    }

    Document getDocument(Uri uri)
            throws ParserConfigurationException, SAXException, IOException {
        //InputStream is = new FileInputStream(new File(path));
        InputStream is = App.GetContext().getContentResolver().openInputStream(uri);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }

    public File toXmlForShare() {
        File tempFile = super.toXml(getTitulo()+".androidSong");
        return tempFile;
    }

}
