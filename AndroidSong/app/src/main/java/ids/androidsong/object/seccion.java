package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;
import ids.androidsong.help.tonalidad;

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

    public String getFormateado(int capo) {
        boolean acordes = false;
        try {
            acordes = new opciones().getBool(aSDbContract.Opciones.OPT_NAME_MOSTRARACORDES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatearContenido(acordes, capo);
    }

    public String getFormateado() {
        return getFormateado(0);
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void alta(item i) {

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Secciones.COLUMN_NAME_ITEMID, i.getId());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_CONTENIDO, getContenido());

        App.getOpenDB().insert(aSDbContract.Secciones.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    public void baja(item i) {
        App.getOpenDB().delete(aSDbContract.Secciones.TABLE_NAME, aSDbContract.Secciones.COLUMN_NAME_ITEMID + "=" + i.getId(), null);
        //helper.currentDB.close();
    }

    public void modificacion(seccion s) {
ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Secciones.COLUMN_NAME_NOMBRE, s.getNombre());
        registro.put(aSDbContract.Secciones.COLUMN_NAME_CONTENIDO, s.getContenido());

        App.getOpenDB().update(aSDbContract.Secciones.TABLE_NAME, registro, aSDbContract.Secciones.COLUMN_NAME_ID + "=" + s.getId(), null);
        //helper.currentDB.close();
    }

    public ArrayList<seccion> get(item item){
        ArrayList<seccion> secciones = new ArrayList<>();
        String sortOrder = aSDbContract.Secciones.COLUMN_NAME_ID + " ASC";
        String filter = aSDbContract.Secciones.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = App.getOpenDB().query(aSDbContract.Secciones.TABLE_NAME, null, filter, null, null, null, sortOrder);
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

    public String formatearContenido (boolean chords, int capo) {
        String formated = "";
        StringReader reader = new StringReader(contenido);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        try
        {
            while ((linea = br.readLine()) != null) if (linea.length() > 0) {
                Character caracter = linea.charAt(0);
                switch (caracter) {
                    case '.':
                        if (chords) {
                            linea = tonalidad.getLineaTonos(linea, capo);
                            linea = linea.replaceAll(" ", "&nbsp;");
                            formated += "<b>" + linea.substring(1) + "</b><br/>";
                        }
                        break;
                    case ' ':
                        if (linea.length() > 2) {
                            formated += linea.substring(1) + "<br/>";
                        } else {
                            formated += linea + "&nbsp;<br/>";
                        }
                        break;
                    default:
                        formated += linea + "<br/>";
                        break;
                }
            } else {
                formated += "<br/>";
            }
        }
        catch (Exception e){
            formated += "Error al cargar.<br/>";
        }
        if (getNombre().charAt(0) == 'C')
            formated = "<i>" + formated + "</i>";
        return formated;
    }

    public int maxCaracteres(){
        BufferedReader textReader = new BufferedReader(new StringReader(contenido));
        int i = 0;
        String linea = "";
        try {
            while ((linea = textReader.readLine()) != null) {
                i = linea.length() > i ? linea.length() : i;
            }
        }
        catch (Exception e) {}
        return i;
    }
}
