package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;

/**
 * Created by ALAN on 03/10/2017.
 * Clase que representa la tabla Carpetas
 */

public class carpeta {
    private int Id;
    private String nombre;

    public carpeta(String n) {
        super();
        this.nombre = n;
    }

    public carpeta() {
        super();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int alta(){
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Carpetas.COLUMN_NAME_NOMBRE, getNombre());
        //helper.currentDB.close();
        return (int) App.GetOpenDB().insert(aSDbContract.Carpetas.TABLE_NAME, null, registro);
    }

    public ArrayList<String> get(){
        ArrayList<String> carpetas = new ArrayList<>();
        String[] projection = { aSDbContract.Carpetas.COLUMN_NAME_ID,aSDbContract.Carpetas.COLUMN_NAME_NOMBRE };
        String sortOrder = aSDbContract.Carpetas.COLUMN_NAME_NOMBRE + " DESC";
        Cursor c = App.GetOpenDB().query(aSDbContract.Carpetas.TABLE_NAME, projection, null, null, null, null, sortOrder);
        c.moveToFirst();
        do {
            carpetas.add(c.getString(c.getColumnIndex(aSDbContract.Carpetas.COLUMN_NAME_NOMBRE)));
        } while (c.moveToNext());
        c.close();
        return carpetas;
    }

    public String get(int id){
        String carpeta;
        String[] projection = { aSDbContract.Carpetas.COLUMN_NAME_NOMBRE };
        Cursor c = App.GetOpenDB().query(aSDbContract.Carpetas.TABLE_NAME, projection, aSDbContract.Carpetas.COLUMN_NAME_ID + " = " + Integer.toString(id), null, null, null, null);
        c.moveToFirst();
        carpeta = c.getString(c.getColumnIndex(aSDbContract.Carpetas.COLUMN_NAME_NOMBRE));
        c.close();
        return carpeta;
    }

    public int get(String nombre){
        int carpetaId;
        String[] projection = { aSDbContract.Carpetas.COLUMN_NAME_ID };
        Cursor c = App.GetOpenDB().query(aSDbContract.Carpetas.TABLE_NAME, projection, aSDbContract.Carpetas.COLUMN_NAME_NOMBRE + "=\"" + nombre + "\"", null, null, null, null);
        if (c.moveToFirst())
            carpetaId = c.getInt(c.getColumnIndex(aSDbContract.Carpetas.COLUMN_NAME_ID));
        else {
            setNombre(nombre);
            carpetaId = alta();
        }
        c.close();
        return carpetaId;
    }
}
