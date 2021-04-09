package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;

import java.util.GregorianCalendar;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla items
 */

public class Item {
    private int Id;
    String carpeta;
    private int carpetaId;
    private String tipo;
    String titulo;
    private ArrayList<Seccion> secciones = new ArrayList<>();
    private ArrayList<Atributo> atributos = new ArrayList<>();
    static final String FILTRO_CARPETA_TODAS = "Todas";
    private static final String FILTRO_ITEM_ACTIVO = " AND " + AsdbContract.Items.COLUMN_NAME_FECHABAJA + " is null";
    private static final String FILTRO_ITEM_BAJA = " AND " + AsdbContract.Items.COLUMN_NAME_FECHABAJA + " is not null";
    private String fechaModificacion;

    private Item(int i, String t, int c) {
        super();
        this.Id = i;
        this.titulo = t;
        this.carpetaId = c;
    }

    public Item(int i) {
        super();
        this.Id = i;
    }

    Item() {
        super();
    }

    public int getId() {
        return Id;
    }

    void setId(int id) {
        Id = id;
    }

    public String getCarpeta() {
        if (carpeta == null)
            carpeta = new Carpeta().get(carpetaId);
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getTipo() {
        return tipo;
    }

    void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(ArrayList<Seccion> secciones) {
        this.secciones = secciones;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    private void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    void alta(String tipo) {

        int carpeta = (new Carpeta()).get(getCarpeta());
        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Items.COLUMN_NAME_TITULO, getTitulo());
        registro.put(AsdbContract.Items.COLUMN_NAME_TIPO, tipo);
        registro.put(AsdbContract.Items.COLUMN_NAME_CARPETAID, carpeta);
        registro.put(AsdbContract.Items.COLUMN_NAME_FECHAMODIFICACION, getFechaModificacion());

        setId((int)App.GetOpenDB().insert(AsdbContract.Items.TABLE_NAME, null, registro));
        //helper.currentDB.close();

        new DriveStatus(this).alta();

        for (Atributo a: atributos) { a.alta(this); }

        for (Seccion s: secciones) { s.alta(this); }


    }

    void fillId(){
        String filter = AsdbContract.Items.COLUMN_NAME_TITULO + "=\"" + getTitulo() + "\" AND " +
                AsdbContract.Items.COLUMN_NAME_CARPETAID + "=" + new Carpeta().get(getCarpeta()) + FILTRO_ITEM_ACTIVO;
        Cursor c = App.GetOpenDB().query(AsdbContract.Items.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst())
            setId(c.getInt(c.getColumnIndex(AsdbContract.Carpetas.COLUMN_NAME_ID)));
        else
            setId(0);
        c.close();
    }

    public void fill(){
        String sortOrder = AsdbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter = AsdbContract.Items.COLUMN_NAME_ID + "=" + Integer.toString(getId());
        Cursor c = App.GetOpenDB().query(AsdbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        c.moveToFirst();
        setTitulo(c.getString(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_TITULO)));
        setTipo(c.getString(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_TIPO)));
        setCarpeta((new Carpeta()).get(c.getInt(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_CARPETAID))));
        setSecciones(new Seccion().get(this));
        setAtributos(new Atributo().get(this));
        setFechaModificacion(c.getString(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_FECHAMODIFICACION)));
        c.close();
    }

    ArrayList<Item> get(String tipo){
        return getByCarpeta(tipo,FILTRO_CARPETA_TODAS, true);
    }

    @NonNull
    ArrayList<Item> getByCarpeta(String tipo, String carpeta, boolean activo) {
        ArrayList<Item> items = new ArrayList<>();
        String sortOrder = AsdbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter;
        if (carpeta.equals(FILTRO_CARPETA_TODAS))
            filter = AsdbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\"";
        else
            filter = AsdbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\" AND " + AsdbContract.Items.COLUMN_NAME_CARPETAID + "=" + (new Carpeta().get(carpeta));
        if (activo)
            filter = filter + FILTRO_ITEM_ACTIVO;
        else
            filter = filter + FILTRO_ITEM_BAJA;
        Cursor c = App.GetOpenDB().query(AsdbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
        do {
            items.add(new Item(
                    c.getInt(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_ID)),
                    c.getString(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_TITULO)),
                    c.getInt(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_CARPETAID))
            ));
        } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    @NonNull
    ArrayList<Item> getByTitulo(String tipo, String filtro) {
        ArrayList<Item> items = new ArrayList<>();
        String sortOrder = AsdbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter;
        filter = AsdbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\"";
        filter = filter + FILTRO_ITEM_ACTIVO;
        filter = filter + " AND " + AsdbContract.Items.COLUMN_NAME_TITULO + " LIKE \"%" + filtro + "%\"";
        Cursor c = App.GetOpenDB().query(AsdbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
            do {
                items.add(new Item(
                        c.getInt(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_TITULO)),
                        c.getInt(c.getColumnIndex(AsdbContract.Items.COLUMN_NAME_CARPETAID))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    public void modificacion() {
        int carpeta = (new Carpeta()).get(getCarpeta());
        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Items.COLUMN_NAME_TITULO, getTitulo());
        registro.put(AsdbContract.Items.COLUMN_NAME_TIPO, tipo);
        registro.put(AsdbContract.Items.COLUMN_NAME_CARPETAID, carpeta);
        registro.put(AsdbContract.Items.COLUMN_NAME_FECHAMODIFICACION, fechaModificacion);
        App.GetOpenDB().update(AsdbContract.Items.TABLE_NAME, registro, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        //helper.currentDB.close();
    }

    public void eliminar() {
        App.GetOpenDB().delete(AsdbContract.Items.TABLE_NAME, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
    }

    public void restaurar(){
        ContentValues registro = new ContentValues();
        registro.putNull(AsdbContract.Items.COLUMN_NAME_FECHABAJA);
        App.GetOpenDB().update(AsdbContract.Items.TABLE_NAME, registro, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);

        DriveStatus status = new DriveStatus(getTitulo(),getCarpeta());
        status.get();
        if (status.getItemId() != 0){
            if (status.getProcesado() != 0) {
                status.setDriveDT(null);
            }
            status.setLocalDT(getFechaModificacion());
            status.modificacion();
        } else {
            status.setItem(this);
            status.alta();
        }

    }

    public void baja() {
        String fechaBaja = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Items.COLUMN_NAME_FECHABAJA, fechaBaja);
        App.GetOpenDB().update(AsdbContract.Items.TABLE_NAME, registro, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        new DriveStatus(this).bajaLocal();
        //helper.currentDB.close();
    }

    public void modificarContenido(){
        new Seccion().baja(this);
        for (Seccion s: secciones) { s.alta(this); }

        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Items.COLUMN_NAME_FECHAMODIFICACION, fechaModificacion);
        App.GetOpenDB().update(AsdbContract.Items.TABLE_NAME, registro, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        //helper.currentDB.close();
    }

    public void modificarAtributos(){
        new Atributo().baja(this);
        for (Atributo a: atributos) { a.alta(this); }

        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Items.COLUMN_NAME_FECHAMODIFICACION, fechaModificacion);
        App.GetOpenDB().update(AsdbContract.Items.TABLE_NAME, registro, AsdbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        //helper.currentDB.close();
    }
}
