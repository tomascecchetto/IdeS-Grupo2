package ids.androidsong.help;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import ids.androidsong.R;
import ids.androidsong.object.cancionXml;
import ids.androidsong.object.setCabecera;

import static ids.androidsong.help.xml.*;

public class directorios {

    private static String MainFolder = "Principal";
    private static String AllFolders = "Todas";
    private static String SongsFolder = "Songs";
    private static String SongsRoot = Environment.getExternalStorageDirectory() + "/" +
            App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SongsFolder);
    private static String SetsFolder = Environment.getExternalStorageDirectory() + "/" +
            App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SetsFolder);
    private static File f = new File(SongsRoot);
    private static File[] archivos = f.listFiles();
    private static File s = new File(SetsFolder);
    private static File[] sets = s.listFiles();

    public static ArrayList<String> generarListaCarpetas(){
        return generarListaCarpetas(true);
    }

    public static ArrayList<String> generarListaCarpetas(boolean allFolders){
        ArrayList<String> carpetas = new ArrayList<String>();

        if (allFolders) {carpetas.add(AllFolders);}
        carpetas.add(MainFolder);

        for (File archivo : archivos){
            if (archivo.isDirectory()){
                carpetas.add(archivo.getName());
                for (File archivoIn : archivo.listFiles()){
                    if (archivoIn.isDirectory()){
                        carpetas.add(archivo.getName());
                    }
                }
            }
        }
        return carpetas;
    }

    private static ArrayList<setCabecera> generarListaSet(){
        ArrayList<setCabecera> lista = new ArrayList<setCabecera>();
        for (File archivo : sets){
            if (archivo.isFile()){
                lista.add(new setCabecera(archivo.getName(),archivo.getAbsolutePath()));
            }
        }
        return lista;
    }

    public static setCabecera[] generarListaSets(){
        ArrayList<setCabecera> temp = generarListaSet();
        setCabecera[] sets = temp.toArray(new setCabecera[temp.size()]);
        try {
            Arrays.sort(sets, new Comparator<setCabecera>() {
                @Override
                public int compare(final setCabecera entry1, final setCabecera entry2) {
                    final String cancion1 = entry1.getTitulo();
                    final String cancion2 = entry2.getTitulo();
                    return cancion1.compareTo(cancion2);
                }
            });
        }
        catch (Exception e){
            return  new setCabecera[]{new setCabecera(e.getMessage(),"Error al Ordenar")};
        }
        return sets;
    }

    public static cancionXml[] generarListaCanciones (){
        return generarListaCanciones(AllFolders);
    }

    private static ArrayList<cancionXml> getCabeceras(File[] archivos, String carpetaSeleccionada){
        ArrayList<cancionXml> resultado = new ArrayList<cancionXml>();
        String ultimaCarpeta;
        try {
            for (File archivo : archivos){
                if (archivo.isDirectory()){
                    resultado.addAll(getCabeceras(archivo.listFiles(),carpetaSeleccionada));
                }
                else {
                    if (!(".".contains(archivo.getName().subSequence(0,1)))){
                                ultimaCarpeta = getUltimaCarpeta(archivo.getAbsolutePath());
                                ultimaCarpeta = ultimaCarpeta.equals(SongsFolder) ? MainFolder : ultimaCarpeta;
                                if (carpetaSeleccionada.equalsIgnoreCase(AllFolders) || carpetaSeleccionada.equalsIgnoreCase(ultimaCarpeta)) {
                                    resultado.add(new cancionXml(archivo.getName(), archivo.getAbsolutePath(), ultimaCarpeta));
                        }
                    }
                }
            }
        }
        catch (Exception e){
            resultado = new ArrayList<cancionXml>();
            resultado.add(new cancionXml(e.getMessage(),"Error al parsear",""));
        }
        return resultado;
    }

    public static cancionXml[] generarListaCanciones (String carpeta){
        ArrayList<cancionXml> temp = getCabeceras(archivos,carpeta);
        cancionXml[] canciones = temp.toArray(new cancionXml[temp.size()]);
        try {
            Arrays.sort(canciones, new Comparator<cancionXml>() {
                @Override
                public int compare(final cancionXml entry1, final cancionXml entry2) {
                    final String cancion1 = entry1.getTitulo();
                    final String cancion2 = entry2.getTitulo();
                    return cancion1.compareTo(cancion2);
                }
            });
        }
        catch (Exception e){
            return  new cancionXml[]{new cancionXml(e.getMessage(),"Error al Ordenar","")};
        }
        return canciones;
    }

    public static String getUltimaCarpeta(String path) {
        int i,f;
        f = path.lastIndexOf("/");
        i = path.substring(0,f-1).lastIndexOf("/");
        return path.substring(i+1,f);
    }

    /*public static cancionXml[] generarListaFavoritos() {
        ArrayList<cancionXml> temp = generarListaFavorito();
        cancionXml[] favoritos = temp.toArray(new cancionXml[temp.size()]);
        try {
            Arrays.sort(favoritos, new Comparator<cancionXml>() {
                @Override
                public int compare(final cancionXml entry1, final cancionXml entry2) {
                    final String cancion1 = entry1.getTitulo();
                    final String cancion2 = entry2.getTitulo();
                    return cancion1.compareTo(cancion2);
                }
            });
        }
        catch (Exception e){
            return  new cancionXml[]{new cancionXml(e.getMessage(),"","Error al Ordenar")};
        }
        return favoritos;
    }

    private static ArrayList<cancionXml> generarListaFavorito() {
        ArrayList<cancionXml> canciones = new ArrayList<cancionXml>();
        Document fs = GetInternalDocument("favoritos.xml");
        Element[] f = GetChilds("fav",fs);
        for (Element e:f){
            canciones.add(new cancionXml(GetValue(e,"titulo"),GetValue(e,"dir"),GetValue(e,"carpeta")));
        }
        if (canciones.size()==0){
            canciones.add(new cancionXml("Aww! No hay Favoritos =(","",""));
            canciones.add(new cancionXml("Hay que agregarlos desde la lista ;-)","",""));
        }
        return canciones;
    }

    public static void addFavorito(cancionXml fav){
        Document fs = GetInternalDocument("favoritos.xml");
        Element f = fs.getDocumentElement();
        try{
            Node fE = fav.getFavorito();
            Node domFE = fE.cloneNode(true);
            domFE = fs.importNode(domFE,true);
            f.appendChild(domFE);
            OutputStreamWriter osw = new OutputStreamWriter(App.getContext().openFileOutput("favoritos.xml", App.getContext().MODE_PRIVATE));
            osw.write(getStringFromDocument(fs));
            osw.flush();
            osw.close();
        }
        catch (Exception e) {
            String tmp = e.getMessage();
            tmp = e.getLocalizedMessage();
        }
    }

    public static void removeFavorito(cancionXml fav){
        Document fs = GetInternalDocument("favoritos.xml");
        Element f = fs.getDocumentElement();
        try{
            Node fE = FindNode("titulo",fav.titulo,fs);
            f.removeChild(fE.getParentNode());
            OutputStreamWriter osw = new OutputStreamWriter(App.getContext().openFileOutput("favoritos.xml", App.getContext().MODE_PRIVATE));
            osw.write(getStringFromDocument(fs));
            osw.flush();
            osw.close();
        }
        catch (Exception e) {
            String tmp = e.getMessage();
            tmp = e.getLocalizedMessage();
        }
    }*/
}