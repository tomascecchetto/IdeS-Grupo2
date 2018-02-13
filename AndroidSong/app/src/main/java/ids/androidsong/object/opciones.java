package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla opciones y gestionar las mismas.
 */

@SuppressWarnings("ALL")
public class opciones {
    private String nombre;
    private String tipo;
    private String valor;

    public opciones(String n, String v){
        this.nombre=n;
        this.valor=v;
    }

    public opciones(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void alta() {

        aSDbHelper helper = new aSDbHelper(App.GetContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Opciones.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(aSDbContract.Opciones.COLUMN_NAME_TIPO, getTipo());
        registro.put(aSDbContract.Opciones.COLUMN_NAME_VALOR, getValor());

        helper.currentDB.insert(aSDbContract.Opciones.TABLE_NAME, null, registro);
        helper.currentDB.close();
    }

    public Boolean getBool(String nombre) throws Exception{
        aSDbHelper helper = new aSDbHelper(App.GetContext());
        helper.openWriteDataBase();
        String filter = aSDbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + nombre + "\"";
        Cursor c = helper.currentDB.query(aSDbContract.Opciones.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
                setNombre(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_NOMBRE)));
                setTipo(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_TIPO)));
                setValor(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_VALOR)));
        } else {
            setNombre(nombre);
            setTipo(aSDbContract.Opciones.OPT_TYPE_BOOL);
            setValor("false");
            this.alta();
        }
        if (!getTipo().equals(aSDbContract.Opciones.OPT_TYPE_BOOL))
            throw (new Exception("El valor de configuración no es del tipo esperado."));
        c.close();
        return Boolean.parseBoolean(getValor());
    }

    public void modificacion() throws Exception {
        aSDbHelper helper = new aSDbHelper(App.GetContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Opciones.COLUMN_NAME_VALOR, getValor());

        int updated = helper.currentDB.update(aSDbContract.Opciones.TABLE_NAME, registro, aSDbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + getNombre() + "\"", null);
        if (!(updated == 1))
            throw (new Exception("Error en la actualización"));
        helper.currentDB.close();
    }

    public String getString(String nombre, String defaultValue) throws Exception{
        aSDbHelper helper = new aSDbHelper(App.GetContext());
        helper.openWriteDataBase();
        String filter = aSDbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + nombre + "\"";
        Cursor c = helper.currentDB.query(aSDbContract.Opciones.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            setNombre(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_NOMBRE)));
            setTipo(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_TIPO)));
            setValor(c.getString(c.getColumnIndex(aSDbContract.Opciones.COLUMN_NAME_VALOR)));
            if (getValor().equals("")){
                setValor(defaultValue);
                modificacion();
            }
        } else {
            setNombre(nombre);
            setTipo(aSDbContract.Opciones.OPT_TYPE_TEXT);
            setValor(defaultValue);
            this.alta();
        }
        if (!getTipo().equals(aSDbContract.Opciones.OPT_TYPE_TEXT))
            throw (new Exception("El valor de configuración no es del tipo esperado."));
        c.close();
        return getValor();
    }
}
