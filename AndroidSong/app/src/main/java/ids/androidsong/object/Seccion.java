package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;
import ids.androidsong.help.Tonalidad;

/**
 * Created by ALAN on 01/10/2017.
 * bjeto para representar la tabla Secciones
 */

public class Seccion {
    private int Id;
    private String nombre;
    private String contenido;

    private Seccion(int i, String n, String c) {
        super();
        this.Id = i;
        this.nombre = n;
        this.contenido = c;
    }

    public Seccion(String n, String c) {
        super();
        this.nombre = n;
        this.contenido = c;
    }

    public Seccion(String n) {
        super();
        this.nombre = n;
    }

    public Seccion() {
        super();
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
            acordes = new Opciones().getBool(AsdbContract.Opciones.OPT_NAME_MOSTRARACORDES);
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

    public void alta(Item i) {

        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Secciones.COLUMN_NAME_ITEMID, i.getId());
        registro.put(AsdbContract.Secciones.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(AsdbContract.Secciones.COLUMN_NAME_CONTENIDO, getContenido());

        App.GetOpenDB().insert(AsdbContract.Secciones.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    public void baja(Item i) {
        App.GetOpenDB().delete(AsdbContract.Secciones.TABLE_NAME, AsdbContract.Secciones.COLUMN_NAME_ITEMID + "=" + i.getId(), null);
        //helper.currentDB.close();
    }

    public ArrayList<Seccion> get(Item item){
        ArrayList<Seccion> secciones = new ArrayList<>();
        String sortOrder = AsdbContract.Secciones.COLUMN_NAME_ID + " ASC";
        String filter = AsdbContract.Secciones.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = App.GetOpenDB().query(AsdbContract.Secciones.TABLE_NAME, null, filter, null, null, null, sortOrder);

        if(c!= null && c.moveToFirst() ) {
            //    c.moveToFirst();
            do {
                secciones.add(new Seccion(
                        c.getInt(c.getColumnIndex(AsdbContract.Secciones.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndex(AsdbContract.Secciones.COLUMN_NAME_NOMBRE)),
                        c.getString(c.getColumnIndex(AsdbContract.Secciones.COLUMN_NAME_CONTENIDO))
                ));
            } while (c.moveToNext());
            c.close();

        }
        return secciones;
    }

    private String formatearContenido(boolean chords, int capo) {
        StringBuilder formated = new StringBuilder();
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
                            linea = Tonalidad.GetLineaTonos(linea, capo);
                            linea = linea.replaceAll(" ", "&nbsp;");
                            formated.append("<b>").append(linea.substring(1)).append("</b><br/>");
                        }
                        break;
                    case ' ':
                        if (linea.length() > 2) {
                            formated.append(linea.substring(1)).append("<br/>");
                        } else {
                            formated.append(linea).append("&nbsp;<br/>");
                        }
                        break;
                    default:
                        formated.append(linea).append("<br/>");
                        break;
                }
            } else {
                formated.append("<br/>");
            }
        }
        catch (Exception e){
            formated.append("Error al cargar.<br/>");
        }
        if (getNombre().charAt(0) == 'C')
            formated = new StringBuilder("<i>" + formated + "</i>");
        return formated.toString();
    }

}
