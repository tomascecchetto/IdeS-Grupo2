package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

import static ids.androidsong.help.App.getContext;

/**
 * Created by ALAN on 01/10/2017.
 * bjeto para representar la tabla Secciones
 */

public class seccion {
    private int Id;
    private String nombre;
    private String contenido;

    public seccion(int i, String n, String c) {
        this.Id = i;
        this.nombre = n;
        this.contenido = c;
    }

    public seccion(String n, String c) {
        this.nombre = n;
        this.contenido = c;
    }

    public seccion(String n) {
        this.nombre = n;
    }

    public seccion(){};

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void alta(seccion s, item i) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Secciones.COLUMN_NAME_ITEMID, i.getId());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_NOMBRE, s.getNombre());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_CONTENIDO, s.getContenido());

        helper.currentDB.insert(aSDbContract.Secciones.TABLE_NAME, null, registro);
        helper.currentDB.close();
    }

    public void baja(seccion s) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        helper.currentDB.delete(aSDbContract.Secciones.TABLE_NAME, aSDbContract.Secciones.COLUMN_NAME_ID + "=" + s.getId(), null);
        helper.currentDB.close();
    }

    public void modificacion(seccion s) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Secciones.COLUMN_NAME_NOMBRE, s.getNombre());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_CONTENIDO, s.getContenido());

        helper.currentDB.update(aSDbContract.Secciones.TABLE_NAME, registro, aSDbContract.Secciones.COLUMN_NAME_ID + "=" + s.getId(), null);
        helper.currentDB.close();
    }

    public ArrayList<seccion> get(item item){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();
        ArrayList<seccion> secciones = new ArrayList<>();
        String sortOrder = aSDbContract.Secciones.COLUMN_NAME_ID + " ASC";
        String filter = aSDbContract.Secciones.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = helper.currentDB.query(aSDbContract.Secciones.TABLE_NAME, null, filter, null, null, null, sortOrder);
        c.moveToFirst();
        do {
            secciones.add(new seccion(
                    c.getInt(c.getColumnIndex(aSDbContract.Secciones.COLUMN_NAME_ID)),
                    c.getString(c.getColumnIndex(aSDbContract.Secciones.COLUMN_NAME_NOMBRE)),
                    c.getString(c.getColumnIndex(aSDbContract.Secciones.COLUMN_NAME_CONTENIDO))
            ));
        } while (c.moveToNext());
        c.close();
        return secciones;
    }
}
