package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

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

    public driveStatus(int i){
        this.itemId = i;
    }

    public driveStatus(int id, String local, String drive){
        this.itemId = id;
        this.localDT = local;
        this.driveDT = drive;
    }

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

    public void alta() {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_ITEMID, getItemId());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT() == null ? null : getLocalDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT() == null ? null : getDriveDT());

        helper.currentDB.insert(aSDbContract.DriveStatus.TABLE_NAME, null, registro);
        helper.currentDB.close();
    }

    public void baja() {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        helper.currentDB.delete(aSDbContract.DriveStatus.TABLE_NAME, aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        helper.currentDB.close();
    }

    public void modificacion() {

        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openWriteDataBase();

        ContentValues registro = new ContentValues();
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT, getLocalDT() == null ? null : getLocalDT());
        registro.put(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT, getDriveDT() == null ? null : getDriveDT());

        helper.currentDB.update(aSDbContract.DriveStatus.TABLE_NAME, registro, aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId(), null);
        helper.currentDB.close();
    }

    public void fill(){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openDataBase();
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_ITEMID + "=" + getItemId();
        Cursor c = helper.currentDB.query(aSDbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            String local = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT));
            setLocalDT(local);
            String drive = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT));
            setDriveDT(drive);
        }
        c.close();
    }

    public ArrayList<driveStatus> getNuevos(){
        aSDbHelper helper = new aSDbHelper(App.getContext());
        helper.openDataBase();
        ArrayList<driveStatus> statuses = new ArrayList<>();
        String filter = aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT + "= NULL";
        Cursor c = helper.currentDB.query(aSDbContract.DriveStatus.TABLE_NAME, null, filter, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String local = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_LOCALDT));
                String drive = c.getString(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_DRIVEDT));
                statuses.add(new driveStatus(
                        c.getInt(c.getColumnIndex(aSDbContract.DriveStatus.COLUMN_NAME_ITEMID)),
                        local,
                        drive
                ));
            } while (c.moveToNext());
        }
        c.close();
        return statuses;
    }

    public void bajaLocal(){
        fill();
        if (getDriveDT() == null)
            baja();
        else {
            setLocalDT(null);
            modificacion();
        }
    }

}
