package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;

/**
 * Created by ALAN on 06/11/2017.
 * Clase que modela la tabla DriveStatus
 * Contiene la informaciòn necesaria para el algoritmo de sincronización
 */

public class DriveStatus {
    private int itemId;
    private String localDT;
    private String driveDT;
    private Item item;
    private String titulo;
    private String carpeta;
    private int procesado;

    public DriveStatus(Item i) {
        super();
        setItem(i);
    }

    public DriveStatus(String t, String c) {
        super();
        this.carpeta = c;
        this.titulo = t;
    }

    public DriveStatus() {
        super();
    }

    public int getItemId() {
        return itemId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getLocalDT() {
        return localDT;
    }

    public void setLocalDT(String localDT) {
        this.localDT = localDT;
    }

    public String getDriveDT() {
        return driveDT;
    }

    public void setDriveDT(String driveDT) {
        this.driveDT = driveDT;
    }

    public Item getItem() {
        if (item == null) {
            item = new Item(getItemId());
            item.fill();
        }
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        this.itemId = item.getId();
        this.localDT = item.getFechaModificacion();
        this.titulo = item.getTitulo();
        this.carpeta = item.getCarpeta();
    }

    @SuppressWarnings("WeakerAccess")
    public String getCarpeta() {
        return carpeta;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getProcesado() {
        return procesado;
    }

    @SuppressWarnings("WeakerAccess")
    public void setProcesado(int procesado) {
        this.procesado = procesado;
    }

    public void alta() {
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_ITEMID, getItemId());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_TITULO, getTitulo());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_CARPETA, getCarpeta());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);

        App.GetOpenDB().insert(AsdbContract.DriveStatus.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    private void eliminar() {
        App.GetOpenDB().delete(AsdbContract.DriveStatus.TABLE_NAME, AsdbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    private void baja() {
        setLocalDT(null);
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);
        App.GetOpenDB().update(AsdbContract.DriveStatus.TABLE_NAME, registro, AsdbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    public void modificacion() {
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        if (getDriveDT() == null)
            registro.putNull(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT);
        else
            registro.put(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT());

        App.GetOpenDB().update(AsdbContract.DriveStatus.TABLE_NAME, registro, AsdbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    public void fill(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId();
        Cursor c = App.GetOpenDB().query(AsdbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            String local = c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT));
            setLocalDT(local);
            String drive = c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT));
            setDriveDT(drive);

        }
        c.close();
    }

    public void get(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_TITULO + " = \"" + getTitulo() +
                "\" AND " + AsdbContract.DriveStatus.COLUMN_NAME_CARPETA + " = \"" + getCarpeta() + "\"";
        Cursor c = App.GetOpenDB().query(AsdbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            setItemId(c.getInt(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_ITEMID)));
            setLocalDT(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT)));
            setDriveDT(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT)));
            setProcesado(c.getInt(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO)));
        }
        c.close();
    }

    public ArrayList<DriveStatus> getNuevos(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT + " is null";
        return get(filter);
    }

    public ArrayList<DriveStatus> getBajasDrive(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO + " = 0";
        return get(filter);
    }

    public ArrayList<DriveStatus> getBajasLocal(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO + "= 0 AND " +
                AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT + " is null";
        return get(filter);
    }

    @NonNull
    private ArrayList<DriveStatus> get(String filter) {
        ArrayList<DriveStatus> statuses = new ArrayList<>();
        Cursor c = App.GetOpenDB().query(AsdbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                DriveStatus status = new DriveStatus();
                status.setDriveDT(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_DRIVEDT)));
                status.setLocalDT(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT)));
                status.setItemId(c.getInt(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_ITEMID)));
                status.setTitulo(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_TITULO)));
                status.setCarpeta(c.getString(c.getColumnIndex(AsdbContract.DriveStatus.COLUMN_NAME_CARPETA)));
                statuses.add(status);
            } while (c.moveToNext());
        }
        c.close();
        return statuses;
    }

    public void bajaLocal(){
        fill();
        if (getDriveDT() == null || getDriveDT().equals("NULL"))
            eliminar();
        else {
            baja();
        }
    }

    public void marcarProcesado(){
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO, 1);
        String filtro = AsdbContract.DriveStatus.COLUMN_NAME_ITEMID + " = " + getItemId();

        App.GetOpenDB().update(AsdbContract.DriveStatus.TABLE_NAME, registro, filtro, null);
        //helper.currentDB.close();
    }

    public void borrarProcesado(){
        String filter = AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO + " = 1 AND " + AsdbContract.DriveStatus.COLUMN_NAME_LOCALDT + " is null";
        App.GetOpenDB().delete(AsdbContract.DriveStatus.TABLE_NAME,filter,null);

        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);
        App.GetOpenDB().update(AsdbContract.DriveStatus.TABLE_NAME, registro, null, null);
        //helper.currentDB.close();
    }
}
