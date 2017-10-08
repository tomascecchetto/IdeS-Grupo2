package ids.androidsong.object;

import android.database.Cursor;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla items
 */

public class item {
    private int Id;
    private String carpeta;
    private String tipo;
    private String title;
    private ArrayList<seccion> secciones = new ArrayList<>();
    private ArrayList<atributo> atributos = new ArrayList<>();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(ArrayList<seccion> secciones) {
        this.secciones = secciones;
    }

    public ArrayList<atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<atributo> atributos) {
        this.atributos = atributos;
    }

    public item get(int itemId){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();
        item item = new item();
        String sortOrder = aSDbContract.Atributos.COLUMN_NAME_NOMBRE + " ASC";
        String filter = aSDbContract.Secciones.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = helper.currentDB.query(aSDbContract.Atributos.TABLE_NAME, null, filter, null, null, null, sortOrder);
        c.moveToFirst();
        item.setId(c.getInt(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_ID)));
        item.setTitle(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TITULO)));
        item.setTipo(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TIPO)));
        item.setSecciones(new seccion().get(item));
        item.setAtributos(new atributo().get(item));
        return item;
    }
}
