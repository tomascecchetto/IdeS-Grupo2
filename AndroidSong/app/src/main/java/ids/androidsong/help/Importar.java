package ids.androidsong.help;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.CancionXml;
import ids.androidsong.object.Opciones;

/**
 * Created by ALAN on 01/11/2017.
 * Clase que gestiona la busqueda y carga de canciones desde disco
 */

public class Importar {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/" + App.GetContext().getString(R.string.OpenSongFolder) + "/" + App.GetContext().getString(R.string.SongsFolder);

    public ArrayList<CancionXml> getCanciones (){
        return getCabeceras((new File(getImportPath())).listFiles());
    }

    private String getImportPath(){
        String path = "";
        try {
            path =(new Opciones()).getString(AsdbContract.Opciones.OPT_NAME_IMPORTPATH, DEFAULT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return path;
    }

    private ArrayList<CancionXml> getCabeceras(File[] archivos){
        ArrayList<CancionXml> resultado = new ArrayList<>();
        String ultimaCarpeta;
        String nombre=""; //para guardar el nombre del archivo sin la extension
        try {
            for (File archivo : archivos){
                if (archivo.isDirectory()){
                    resultado.addAll(getCabeceras(archivo.listFiles()));
                }
                else {
                    if (!(".".contains(archivo.getName().subSequence(0,1)))){
                        ultimaCarpeta = getUltimaCarpeta(archivo.getAbsolutePath());

                        //solo tiene en cuenta los archivos que terminan con la extension ".ids"
                        //si encuentra archivos los guarda el nombre sin la extension
                        //ver si esto se puede hacer mejor
                        if (archivo.getName().endsWith(".ids")) {
                            if (archivo.getName().indexOf(".") > 0) {
                                nombre = archivo.getName().substring(0, archivo.getName().lastIndexOf("."));
                            }
                            resultado.add(new CancionXml(nombre, archivo.getAbsolutePath(), ultimaCarpeta));
                        }
                    }
                }
            }
        }
        //si no encuentra ningun archivo devuelve el array vacio
        catch (NullPointerException d){
            return resultado;
        }
        catch (Exception e){
            resultado = new ArrayList<>();
            resultado.add(new CancionXml(e.getMessage(),"Error al parsear",""));
        }
        return resultado;
    }

    private String getUltimaCarpeta(String path) {
        int i,f;
        f = path.lastIndexOf("/");
        i = path.substring(0,f-1).lastIndexOf("/");
        return path.substring(i+1,f);
    }
}
