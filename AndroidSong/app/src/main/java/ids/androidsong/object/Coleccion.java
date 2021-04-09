package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;
import ids.androidsong.help.AsdbHelper;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla Colecciones e ItemsColecciones.
 */

public class Coleccion {
    private int Id;
    private String nombre;
    private ArrayList<Item> items = new ArrayList<>();

    public Coleccion(int i) {
        super();
        this.Id = i;
    }

    private int getId() {
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

    public ArrayList<Item> getItems() {
        if (items == null || items.size() == 0) {
            getByColeccion();
        }
        return items;
    }

    private void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    private void getByColeccion() {
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.createDataBase();
        helper.openWriteDataBase();
        ArrayList<Item> itemsdb = new ArrayList<>();
        String[] proyection = {AsdbContract.ItemsColecciones.COLUMN_NAME_ITEMID};
        String sortOrder = AsdbContract.ItemsColecciones.COLUMN_NAME_ORDEN + " ASC";
        String filter = AsdbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID + "= " + getId();
        Cursor c = helper.currentDB.query(AsdbContract.ItemsColecciones.TABLE_NAME, proyection, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
            do {
                Item tempItem = new Item(c.getInt(c.getColumnIndex(AsdbContract.ItemsColecciones.COLUMN_NAME_ITEMID)));
                tempItem.fill();
                itemsdb.add(tempItem);
            } while (c.moveToNext());
        }
        c.close();
        setItems(itemsdb);
    }

    private int getLastOrden(){
        int size;
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();
        String sqlQuery = "SELECT MAX(orden) AS ORDEN FROM ItemsColecciones where coleccionId = ?";
        String[] arguments = {
                Integer.toString(getId())
        };
        Cursor c = helper.currentDB.rawQuery(sqlQuery, arguments);
        if (c.moveToFirst()) size = c.getInt(c.getColumnIndex("ORDEN"));
        else size = 0;
        c.close();
        return size;
    }

    public void addItem(int itemId){
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID, getId());
        registro.put(AsdbContract.ItemsColecciones.COLUMN_NAME_ORDEN, getLastOrden()+1);
        registro.put(AsdbContract.ItemsColecciones.COLUMN_NAME_ITEMID, itemId);
        helper.currentDB.insert(AsdbContract.ItemsColecciones.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    public void removeItem(int itemId){
        AsdbHelper helper = new AsdbHelper(App.GetContext());
        helper.openWriteDataBase();

        helper.currentDB.delete(AsdbContract.ItemsColecciones.TABLE_NAME, AsdbContract.ItemsColecciones.COLUMN_NAME_COLECCIONID + "=" + getId() + " AND " +
                AsdbContract.ItemsColecciones.COLUMN_NAME_ITEMID + "=" + itemId, null);
        //helper.currentDB.close();
    }

}
