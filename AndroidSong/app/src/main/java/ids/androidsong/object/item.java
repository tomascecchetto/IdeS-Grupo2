package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ids.androidsong.help.App;
import ids.androidsong.help.Enum;
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

    public item(int i, String t){
        this.Id = i;
        this.title = t;
    }

    public item(){}

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

    public void alta(String tipo) {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Items.COLUMN_NAME_TITULO, getTitle());
        registro.put(aSDbContract.Items.COLUMN_NAME_TIPO, tipo);
        //TODO: Agregar la carpeta
        setId((int)helper.currentDB.insert(aSDbContract.Items.TABLE_NAME, null, registro));
        helper.currentDB.close();

        for (atributo a: atributos) { a.alta(this); }

        for (seccion s: secciones) { s.alta(this); }
    }

    public item get(item item){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        try {
            helper.createDataBase();
            helper.openWriteDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sortOrder = aSDbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter = aSDbContract.Items.COLUMN_NAME_ID + "=" + item.getId();
        Cursor c = helper.currentDB.query(aSDbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        c.moveToFirst();
        item.setTipo(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TIPO)));
        item.setSecciones(new seccion().get(item));
        item.setAtributos(new atributo().get(item));
        return item;
    }

    public ArrayList<item> get(String tipo){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        try {
            helper.createDataBase();
            helper.openWriteDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<item> items = new ArrayList<>();
        String sortOrder = aSDbContract.Items.COLUMN_NAME_TITULO + " ASC";
        //TODO: Agregar filtro por carpeta y excepción para "Todas"
        String filter = aSDbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\"";
        Cursor c = helper.currentDB.query(aSDbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
        do {
            items.add(new item(
                    c.getInt(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_ID)),
                    c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TITULO))
            ));
        } while (c.moveToNext());
        }
        return items;
    }
}
