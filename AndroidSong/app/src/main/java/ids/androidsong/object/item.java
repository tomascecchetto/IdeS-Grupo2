package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.format.Time;

import java.io.IOException;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla items
 */

public class item {
    protected int Id;
    protected String carpeta;
    private int carpetaId;
    protected String tipo;
    protected String titulo;
    private ArrayList<seccion> secciones = new ArrayList<>();
    private ArrayList<atributo> atributos = new ArrayList<>();
    protected final String FILTRO_CARPETA_TODAS = "Todas";
    protected final String FILTRO_ITEM_ACTIVO = " AND " + aSDbContract.Items.COLUMN_NAME_FECHABAJA + " is null";
    protected final String FILTRO_ITEM_BAJA = " AND " + aSDbContract.Items.COLUMN_NAME_FECHABAJA + " is not null";
    private String fechaModificacion;

    public item(int i, String t, int c){
        this.Id = i;
        this.titulo = t;
        this.carpetaId = c;
    }

    public item(int i, String t){
        this.Id = i;
        this.titulo = t;
    }

    public item(int i){
        this.Id = i;
    }

    public item(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCarpeta() {
        if (carpeta == null)
            carpeta = new carpeta().get(carpetaId);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public void alta(String tipo) {

        int carpeta = (new carpeta()).get(getCarpeta());
        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Items.COLUMN_NAME_TITULO, getTitulo());
        registro.put(aSDbContract.Items.COLUMN_NAME_TIPO, tipo);
        registro.put(aSDbContract.Items.COLUMN_NAME_CARPETAID, carpeta);
        registro.put(aSDbContract.Items.COLUMN_NAME_FECHAMODIFICACION, getFechaModificacion());

        setId((int)App.getOpenDB().insert(aSDbContract.Items.TABLE_NAME, null, registro));
        //helper.currentDB.close();

        new driveStatus(this).alta();

        for (atributo a: atributos) { a.alta(this); }

        for (seccion s: secciones) { s.alta(this); }


    }

    public void fillId(){
        String filter = aSDbContract.Items.COLUMN_NAME_TITULO + "=\"" + getTitulo() + "\" AND " +
                aSDbContract.Items.COLUMN_NAME_CARPETAID + "=" + new carpeta().get(getCarpeta()) + FILTRO_ITEM_ACTIVO;
        Cursor c = App.getOpenDB().query(aSDbContract.Items.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst())
            setId(c.getInt(c.getColumnIndex(aSDbContract.Carpetas.COLUMN_NAME_ID)));
        else
            setId(0);
        c.close();
    }

    public void fill(){
        String sortOrder = aSDbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter = aSDbContract.Items.COLUMN_NAME_ID + "=" + Integer.toString(getId());
        Cursor c = App.getOpenDB().query(aSDbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        c.moveToFirst();
        setTitulo(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TITULO)));
        setTipo(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TIPO)));
        setCarpeta((new carpeta()).get(c.getInt(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_CARPETAID))));
        setSecciones(new seccion().get(this));
        setAtributos(new atributo().get(this));
        setFechaModificacion(c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_FECHAMODIFICACION)));
        c.close();
    }

    protected ArrayList<item> get(String tipo){
        return getByCarpeta(tipo,FILTRO_CARPETA_TODAS, true);
    }

    protected ArrayList<item> getBajas(String tipo, String carpeta){
        return getByCarpeta(tipo,carpeta,false);
    }

    @NonNull
    protected ArrayList<item> getByCarpeta(String tipo, String carpeta, boolean activo) {
        ArrayList<item> items = new ArrayList<>();
        String sortOrder = aSDbContract.Items.COLUMN_NAME_TITULO + " ASC";
        String filter;
        if (carpeta.equals(FILTRO_CARPETA_TODAS))
            filter = aSDbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\"";
        else
            filter = aSDbContract.Items.COLUMN_NAME_TIPO + "= \"" + tipo + "\" AND " + aSDbContract.Items.COLUMN_NAME_CARPETAID + "=" + (new carpeta().get(carpeta));
        if (activo)
            filter = filter + FILTRO_ITEM_ACTIVO;
        else
            filter = filter + FILTRO_ITEM_BAJA;
        Cursor c = App.getOpenDB().query(aSDbContract.Items.TABLE_NAME, null, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
        do {
            items.add(new item(
                    c.getInt(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_ID)),
                    c.getString(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_TITULO)),
                    c.getInt(c.getColumnIndex(aSDbContract.Items.COLUMN_NAME_CARPETAID))
            ));
        } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    public void modificacion() {
        int carpeta = (new carpeta()).get(getCarpeta());
        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Items.COLUMN_NAME_TITULO, getTitulo());
        registro.put(aSDbContract.Items.COLUMN_NAME_TIPO, tipo);
        registro.put(aSDbContract.Items.COLUMN_NAME_CARPETAID, carpeta);
        registro.put(aSDbContract.Items.COLUMN_NAME_FECHAMODIFICACION, fechaModificacion);
        App.getOpenDB().update(aSDbContract.Items.TABLE_NAME, registro, aSDbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        //helper.currentDB.close();
    }

    public void eliminar() {
        App.getOpenDB().delete(aSDbContract.Items.TABLE_NAME, aSDbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
    }

    public void restaurar(){
        ContentValues registro = new ContentValues();
        registro.putNull(aSDbContract.Items.COLUMN_NAME_FECHABAJA);
        App.getOpenDB().update(aSDbContract.Items.TABLE_NAME, registro, aSDbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);

        driveStatus status = new driveStatus(getTitulo(),getCarpeta());
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
        registro.put(aSDbContract.Items.COLUMN_NAME_FECHABAJA, fechaBaja);
        App.getOpenDB().update(aSDbContract.Items.TABLE_NAME, registro, aSDbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        new driveStatus(this).bajaLocal();
        //helper.currentDB.close();
    }

    public void modificarContenido(){
        new seccion().baja(this);
        for (seccion s: secciones) { s.alta(this); }

        fechaModificacion = new GregorianCalendar().getTime().toString();
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.Items.COLUMN_NAME_FECHAMODIFICACION, fechaModificacion);
        App.getOpenDB().update(aSDbContract.Items.TABLE_NAME, registro, aSDbContract.Items.COLUMN_NAME_ID + "=" + getId(), null);
        //helper.currentDB.close();
    }
}
