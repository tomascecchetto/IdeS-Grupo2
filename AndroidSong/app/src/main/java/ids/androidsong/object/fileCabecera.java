package ids.androidsong.object;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import ids.androidsong.R;
import ids.androidsong.help.App;

public class fileCabecera implements Serializable {
    public String titulo;
    protected String path;
    private static String MainFolder = "Principal";
    private static String AllFolders = "Todas";
    private static String SongsFolder = Environment.getExternalStorageDirectory() + "/" +
            App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SongsFolder);

    public String getTitulo() {
        return titulo;
    }

    public String getDir() {
        return path + titulo;
    }

    public String getPath() { return path; }

    public void borrar(){
        File archivo = new File(getDir());
        archivo.delete();
    }

    public void renombrar(String nombre){
        File origen = new File(getPath(),getTitulo());
        File destino = new File(getPath(),nombre);
        origen.renameTo(destino);
    }

    public void mover(String carpeta){
        if (carpeta.equals(MainFolder)|| carpeta.equals(AllFolders)){
            carpeta = "";
        }
        String newPath = SongsFolder + "/"+carpeta;
        File origen = new File(getPath(),getTitulo());
        File destino = new File(newPath,getTitulo());
        origen.renameTo(destino);
    }

    public void copiar(String carpeta){
        try {
            File origen = new File(getDir());
            if (carpeta.equals(MainFolder)|| carpeta.equals(AllFolders)){
                carpeta = "";
            }
            String newPath = SongsFolder + "/"+carpeta;
            File destino = new File(newPath,getTitulo());
            InputStream is = new FileInputStream(origen);
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(destino));
            osw.write(is.toString());
            osw.flush();
            osw.close();
        }
        catch (Exception e){}
    }

}