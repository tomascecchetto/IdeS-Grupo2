package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.IOException;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla Colecciones e ItemsColecciones.
 */

public class coleccion {
    private int Id;
    private String nombre;
    private ArrayList<item> items = new ArrayList<>();

    public coleccion(int i){
        this.Id = i;
    }

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

    public ArrayList<item> getItems() {
        if (items == null || items.size() == 0) {
            getByColeccion();
        }
        return items;
    }

    public void setItems(ArrayList<item> items) {
        this.items = items;
    }

    protected void getByColeccion() {
        aSDbHelper helper = new aSDbHelper(App.getContext());
        try {
            helper.createDataBase();
            helper.openWriteDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<item> itemsdb = new ArrayList<>();
        String[] proyection = {aSDbContract.ItemsColecciones.COLUMN_NAME_ITEMID};
        String sortOrder = aSDbContract.ItemsColecciones.COLUMN_NAME_ORDEN + " ASC";
        String filter = aSDbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID + "= " + getId();
        Cursor c = helper.currentDB.query(aSDbContract.ItemsColecciones.TABLE_NAME, proyection, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
            do {
                item tempItem = new item(c.getInt(c.getColumnIndex(aSDbContract.ItemsColecciones.COLUMN_NAME_ITEMID)));
                tempItem.fill();
                itemsdb.add(tempItem);
            } while (c.moveToNext());
        }
        c.close();
        setItems(itemsdb);
    }

    private int getLastOrden(){
        int size;
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();
        String[] arguments = {
                Integer.toString(getId())
        };
        Cursor c = helper.currentDB.rawQuery("SELECT MAX(orden) AS ORDEN FROM ItemsColecciones where coleccionId = ?", arguments);
        if (c.moveToFirst()) size = c.getInt(c.getColumnIndex("ORDEN"));
        else size = 0;
        c.close();
        return size;
    }

    public void addItem(int itemId){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID, getId());
        registro.put(aSDbContract.ItemsColecciones.COLUMN_NAME_ORDEN, getLastOrden()+1);
        registro.put(aSDbContract.ItemsColecciones.COLUMN_NAME_ITEMID, itemId);
        helper.currentDB.insert(aSDbContract.ItemsColecciones.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    public void removeItem(int itemId){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        helper.currentDB.delete(aSDbContract.ItemsColecciones.TABLE_NAME, aSDbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID + "=" + getId() + " AND " +
                aSDbContract.ItemsColecciones.COLUMN_NAME_ITEMID + "=" + itemId, null);
        //helper.currentDB.close();
    }

}
