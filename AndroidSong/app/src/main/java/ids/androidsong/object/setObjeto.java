package ids.androidsong.object;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ids.androidsong.R;
import ids.androidsong.help.App;

import static ids.androidsong.help.xml.*;

/**
 * 20/07/2015. Carga y almacenamiento de un set
 */
public class setObjeto implements Serializable, iGrupos{

    public setCabecera set;
    public String name = "";
    public ArrayList<iGrupos> grupos = new ArrayList<iGrupos>();

    public setObjeto Load(setCabecera setSeleccionado){
        Document dom;
        try {
            dom = getDocument(setSeleccionado);
            set = setSeleccionado;
        } catch (Exception e) {
            return GetSetError();
        }
        return BuildSet(dom);
    }

    private setObjeto BuildSet(Document dom) {
        Element[] grupos = GetChilds("slide_group",dom);
        String type;
        int i;
        for (i=0;i<grupos.length;i++){
            Element g = grupos[i];
            type = g.getAttribute("type");
            if (type.equals("song")) {
                getGrupoSong(g);

            } else if (type.equals("custom")) {
                getGrupoCustom(g);

            } else if (type.equals("image")) {
                getGrupoImage(g);

            } else if (type.equals("style")) {
                getGrupoStyle(g);

            }
        }
        return this;
    }

    private void getGrupoStyle(Element g) {

    }

    private void getGrupoImage(Element g) {
        grupoObjeto grupo = new grupoObjeto();
        grupo.title = g.getAttribute("name");
        grupo.type = "image";
        getImageItems(grupo, g);
        grupos.add(grupo);
    }

    private void getImageItems(grupoObjeto grupo, Element g) {
        Element[] slides = GetChilds("slide",g);
        int i,j;
        for (i=0;i<slides.length;i++){
            Element s = slides[i];
            itemObjeto item = new itemObjeto(grupo.type,grupo.title, String.format("%d",i));
            item.estilo.activo = true;
            j=GetValue(s,"description").lastIndexOf("\\");
            String nombreImagen = GetValue(s, "description").substring(j+1);
            fileCabecera img = new fileCabecera();
            img.titulo = nombreImagen;
            img.path = Environment.getExternalStorageDirectory() + "/" +
                    App.getContext().getString(R.string.OpenSongFolder) + "/" +
                    App.getContext().getString(R.string.BackgroundsFolder)+"/"+ nombreImagen;
            item.estilo.imagen = img;
            grupo.items.add(item);
        }
    }

    private void getGrupoCustom(Element g) {
        grupoObjeto grupo = new grupoObjeto();
        grupo.title = g.getAttribute("name");
        grupo.type = "custom";
        getCustomItems(grupo,g);
        grupos.add(grupo);
    }

    private void getCustomItems(grupoObjeto grupo, Element g) {
        Element[] slides = GetChilds("slide",g);
        int i;
        for (i=0;i<slides.length;i++){
            Element s = slides[i];
            itemObjeto item = new itemObjeto(grupo.type,grupo.title, String.format("%d",i));
            item.texto = GetValue(s,"body");
            grupo.items.add(item);
        }
    }

    private void getGrupoSong(Element g) {
        String title; String path; String carpeta;
        title = g.getAttribute("name");
        carpeta = g.getAttribute("path");
        path = Environment.getExternalStorageDirectory() + "/" +
                App.getContext().getString(R.string.OpenSongFolder) + "/" +
                App.getContext().getString(R.string.SongsFolder)+"/"+carpeta+title;
        cancionCabecera song = new cancionCabecera(title,path,carpeta.substring(0,carpeta.length()-2));
        grupos.add(new cancionPantalla().Load(song));
    }

    private setObjeto GetSetError() {
        setObjeto setError = new setObjeto();
        setError.name = "Error";
        return setError;
    }

    private Document getDocument(setCabecera setSeleccionado)
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(new File(setSeleccionado.getDir()));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        return db.parse(is);
    }
    @Override
    public itemObjeto[] getItems() {
        ArrayList<itemObjeto> items = new ArrayList<itemObjeto>();
        items.add(espacio());
        for (iGrupos g: grupos){
            for (itemObjeto i: g.getItems()){
                items.add(i);
            }
            items.add(espacio());
        }
        return items.toArray(new itemObjeto[items.size()]);
    }

    protected itemObjeto espacio(){
        return new itemObjeto("Espacio","Default","D");
    }

    @Override
    public itemObjeto[] getAllItems() {
        return getItems();
    }

    @Override
    public String getType() {
        return "set";
    }


}
