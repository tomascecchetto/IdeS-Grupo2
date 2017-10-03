package ids.androidsong.object;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ids.androidsong.help.aSDbHelper;

import static ids.androidsong.help.App.getContext;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar las tablas Atributos y AtributosUsuario
 */

public class atributo {
    private int itemId;
    private String presentacion;
    private String autor;
    private int transporte;
    private String interprete;
    private String tono;

    public int getItemId(){
        return itemId;
    }
    public void setItemId(int i) {
        this.itemId = i;
    }

    public String getPresentacion() {
        return presentacion;
    }
    public void setPresentacion(String p) {
        this.presentacion = p;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String a) {
        this.autor = a;
    }

    public int getTransporte(){
        return transporte;
    }
    public void setTransporte(int t){
        this.transporte = t;
    }

    public String getInterprete() {
        return interprete;
    }
    public void setInterprete(String i) {
        this.interprete = i;
    }

    public String getTono() {
        return tono;
    }
    public void setTono(String t) {
        this.tono = t;
    }


    public void alta(atributo a) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());

        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("presentacion", a.getPresentacion());
        registro.put("autor", a.getAutor());
        registro.put("transporte", a.getTransporte());
        registro.put("interprete", a.getInterprete());
        registro.put("tono", a.getTono());

        bd.insert("Atributo", null, registro);
        bd.close();
    }

    public void baja(atributo a) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());
        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        bd.delete("Atributos", "itemId=" + a.getItemId(), null);
        bd.close();
    }

    public void modificacion(atributo a) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());
        SQLiteDatabase bd = dbHelper.getWritableDatabase();


        ContentValues registro = new ContentValues();
        registro.put("presentacion", a.getPresentacion());
        registro.put("autor", a.getAutor());
        registro.put("transporte", a.getTransporte());
        registro.put("interprete", a.getInterprete());
        registro.put("tono", a.getTono());

        bd.update("Atributos", registro, "itemId=" + a.getItemId(), null);
        bd.close();

    }

}
