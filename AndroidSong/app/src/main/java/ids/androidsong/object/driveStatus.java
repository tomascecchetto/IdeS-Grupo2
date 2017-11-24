package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

/**
 * Created by ALAN on 06/11/2017.
 * Clase que modela la tabla DriveStatus
 * Contiene la informaciòn necesaria para el algoritmo de sincronización
 */

public class driveStatus {
    private int itemId;
    private String localDT;
    private String driveDT;
    private item item;
    private String titulo;
    private String carpeta;
    private int procesado;

    public driveStatus(int id, String local, String drive){
        this.itemId = id;
        this.localDT = local;
        this.driveDT = drive;
    }

    public driveStatus(item i){
        setItem(i);
    }

    public driveStatus(String t, String c){
        this.carpeta = c;
        this.titulo = t;
    }

    public driveStatus(){}

    public int getItemId() {
        return itemId;
    }

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

    public ids.androidsong.object.item getItem() {
        if (item == null) {
            item = new item(getItemId());
            item.fill();
        }
        return item;
    }

    public void setItem(ids.androidsong.object.item item) {
        this.item = item;
        this.itemId = item.getId();
        this.localDT = item.getFechaModificacion();
        this.titulo = item.getTitulo();
        this.carpeta = item.getCarpeta();
    }

    public String getCarpeta() {
        return carpeta;
    }

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

    public void setProcesado(int procesado) {
        this.procesado = procesado;
    }

    public void alta() {
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_ITEMID, getItemId());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_TITULO, getTitulo());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_CARPETA, getCarpeta());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);

        App.getOpenDB().insert(aSDbContract.DriveStatus.TABLE_NAME, null, registro);
        //helper.currentDB.close();
    }

    public void eliminar() {
        App.getOpenDB().delete(aSDbContract.DriveStatus.TABLE_NAME, aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    public void baja() {
        setLocalDT(null);
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);
        App.getOpenDB().update(aSDbContract.DriveStatus.TABLE_NAME, registro, aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    public void modificacion() {
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT());

        App.getOpenDB().update(aSDbContract.DriveStatus.TABLE_NAME, registro, aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        //helper.currentDB.close();
    }

    public void fill(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId();
        Cursor c = App.getOpenDB().query(aSDbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            String local = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT));
            setLocalDT(local);
            String drive = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT));
            setDriveDT(drive);

        }
        c.close();
    }

    public void get(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_TITULO + " = \"" + getTitulo() +
                "\" AND " + aSDbContract.DriveStatus.COLUMN_NAME_CARPETA + " = \"" + getCarpeta() + "\"";
        Cursor c = App.getOpenDB().query(aSDbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            setItemId(c.getInt(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_ITEMID)));
            setLocalDT(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT)));
            setDriveDT(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT)));
            setProcesado(c.getInt(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO)));
        }
        c.close();
    }

    public ArrayList<driveStatus> getNuevos(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT + " is null";
        return get(filter);
    }

    public ArrayList<driveStatus> getBajasDrive(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO + " = 0";
        return get(filter);
    }

    public ArrayList<driveStatus> getBajasLocal(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO + "= 0 AND " +
                aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT + " is null";
        return get(filter);
    }

    @NonNull
    private ArrayList<driveStatus> get(String filter) {
        ArrayList<driveStatus> statuses = new ArrayList<>();
        Cursor c = App.getOpenDB().query(aSDbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                driveStatus status = new driveStatus();
                status.setDriveDT(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT)));
                status.setLocalDT(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT)));
                status.setItemId(c.getInt(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_ITEMID)));;
                status.setTitulo(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_TITULO)));
                status.setCarpeta(c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_CARPETA)));
                statuses.add(status);
            } while (c.moveToNext());
        }
        c.close();
        return statuses;
    }

    public void bajaLocal(){
        fill();
        if (getDriveDT() == null)
            eliminar();
        else {
            baja();
        }
    }

    public void marcarProcesado(){
        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO, 1);
        String filtro = aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + " = " + getItemId();

        App.getOpenDB().update(aSDbContract.DriveStatus.TABLE_NAME, registro, filtro, null);
        //helper.currentDB.close();
    }

    public void borrarProcesado(){
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO + " = 1 AND " +
                aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT + " is null";
        App.getOpenDB().delete(aSDbContract.DriveStatus.TABLE_NAME,filter,null);

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_PROCESADO, 0);
        App.getOpenDB().update(aSDbContract.DriveStatus.TABLE_NAME, registro, null, null);
        //helper.currentDB.close();
    }
}
