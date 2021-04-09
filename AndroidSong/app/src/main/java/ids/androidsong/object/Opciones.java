package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;
import ids.androidsong.help.AsdbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla Opciones y gestionar las mismas.
 */

@SuppressWarnings("ALL")
public class Opciones {
    private String nombre;
    private String tipo;
    private String valor;

    public Opciones(String n, String v){
        this.nombre=n;
        this.valor=v;
    }

    public Opciones(){}

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

        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Opciones.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(AsdbContract.Opciones.COLUMN_NAME_TIPO, getTipo());
        registro.put(AsdbContract.Opciones.COLUMN_NAME_VALOR, getValor());

        helper.currentDB.insert(AsdbContract.Opciones.TABLE_NAME, null, registro);
        helper.currentDB.close();
    }

    public Boolean getBool(String nombre) throws Exception{
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();
        String filter = AsdbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + nombre + "\"";
        Cursor c = helper.currentDB.query(AsdbContract.Opciones.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
                setNombre(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_NOMBRE)));
                setTipo(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_TIPO)));
                setValor(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_VALOR)));
        } else {
            setNombre(nombre);
            setTipo(AsdbContract.Opciones.OPT_TYPE_BOOL);
            setValor("false");
            this.alta();
        }
        if (!getTipo().equals(AsdbContract.Opciones.OPT_TYPE_BOOL))
            throw (new Exception("El valor de configuración no es del tipo esperado."));
        c.close();
        return Boolean.parseBoolean(getValor());
    }

    public void modificacion() throws Exception {
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Opciones.COLUMN_NAME_VALOR, getValor());

        int updated = helper.currentDB.update(AsdbContract.Opciones.TABLE_NAME, registro, AsdbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + getNombre() + "\"", null);
        if (!(updated == 1))
            throw (new Exception("Error en la actualización"));
        helper.currentDB.close();
    }

    public String getString(String nombre, String defaultValue) throws Exception{
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();
        String filter = AsdbContract.Opciones.COLUMN_NAME_NOMBRE + "= \"" + nombre + "\"";
        Cursor c = helper.currentDB.query(AsdbContract.Opciones.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            setNombre(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_NOMBRE)));
            setTipo(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_TIPO)));
            setValor(c.getString(c.getColumnIndex(AsdbContract.Opciones.COLUMN_NAME_VALOR)));
            if (getValor().equals("")){
                setValor(defaultValue);
                modificacion();
            }
        } else {
            setNombre(nombre);
            setTipo(AsdbContract.Opciones.OPT_TYPE_TEXT);
            setValor(defaultValue);
            this.alta();
        }
        if (!getTipo().equals(AsdbContract.Opciones.OPT_TYPE_TEXT))
            throw (new Exception("El valor de configuración no es del tipo esperado."));
        c.close();
        return getValor();
    }
}
