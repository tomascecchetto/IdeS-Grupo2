package ids.androidsong.object;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ids.androidsong.R;

import static ids.androidsong.help.xml.*;

public class cancionCabecera extends fileCabecera {
    private String carpeta;

    public String getCarpeta() {
        return carpeta;
    }

    public cancionCabecera(String pTitulo, String pDir, String pCarpeta){
        titulo = pTitulo;
        int f;
        f = pDir.lastIndexOf("/");
        path = pDir.substring(0,f+1);
        carpeta = pCarpeta;
    }

    public Element getFavorito(){
        Document dom = null;
        try {
            dom = getRawDocument(R.raw.atributo);
            SetValue("titulo",titulo,dom);
            SetValue("dir",getDir(),dom);
            SetValue("carpeta",carpeta,dom);
        }
        catch (Exception e) {}
        Element base = dom.getDocumentElement();
        return base;
    }

}
