package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar las tablas Atributos y AtributosUsuario
 */

public class atributo {
    private int Id;
    private String nombre;
    private String valor;

    public atributo(int i, String n, String v){
        this.Id = i;
        this.nombre = n;
        this.valor = v;
    }

    public atributo(String n, String v){
        this.nombre = n;
        this.valor = v;
    }

    public atributo(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void alta(item i) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Atributos.COLUMN_NAME_ITEMID, i.getId());
        registro.put(aSDbContract.Atributos.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(aSDbContract.Atributos.COLUMN_NAME_VALOR, getValor());

        helper.currentDB.insert(aSDbContract.Atributos.TABLE_NAME, null, registro);
        helper.currentDB.close();
    }

    public void baja(atributo a) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        helper.currentDB.delete(aSDbContract.Atributos.TABLE_NAME, aSDbContract.Atributos.COLUMN_NAME_ID + "=" + a.getId(), null);
        helper.currentDB.close();
    }

    public void modificacion(atributo a) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Atributos.COLUMN_NAME_NOMBRE, a.getNombre());
        registro.put(aSDbContract.Atributos.COLUMN_NAME_VALOR, a.getValor());

        helper.currentDB.update(aSDbContract.Atributos.TABLE_NAME, registro, aSDbContract.Atributos.COLUMN_NAME_ID + "=" + a.getId(), null);
        helper.currentDB.close();
    }

    public ArrayList<atributo> get(item item){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openDataBase();
        ArrayList<atributo> atributos = new ArrayList<>();
        String sortOrder = aSDbContract.Atributos.COLUMN_NAME_NOMBRE + " ASC";
        String filter = aSDbContract.Atributos.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = helper.currentDB.query(aSDbContract.Atributos.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                atributos.add(new atributo(
                        c.getInt(c.getColumnIndex(aSDbContract.Atributos.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndex(aSDbContract.Atributos.COLUMN_NAME_NOMBRE)),
                        c.getString(c.getColumnIndex(aSDbContract.Atributos.COLUMN_NAME_VALOR))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return atributos;
    }

}
