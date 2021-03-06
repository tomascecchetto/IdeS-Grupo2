package ids.androidsong.help;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ALAN on 10/09/2017.
 * Clase para gestinar la carga y actualización de la BD en Assets
 */

public class aSDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "androidSongDB";
    private static final String DATABASE_PATH = String.valueOf(App.GetContext().getDatabasePath(DATABASE_NAME));
    private final Context con;
    public SQLiteDatabase currentDB;

    public aSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        con = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
                db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void createDataBase(){

        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.e("Info", "Acceso a una BD existente.");
        } else {
            // Llamando a este método se crea la base de datos vacía en la ruta
            // por defecto del sistema de nuestra aplicación por lo que
            // podremos sobreescribirla con nuestra base de datos.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copiando database");
            }
        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        Cursor cur = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            String[] projection = {
                    aSDbContract.Carpetas.COLUMN_NAME_NOMBRE
            };
            cur = checkDB.query(
                    aSDbContract.Carpetas.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

        } catch (SQLiteException e) {
            // Base de datos no creada todavia
        }

        boolean ret = false;

        if (checkDB != null) {
            checkDB.close();
            if (cur != null){
                cur.close();
                ret = true;
            }
        }

        return ret;
    }

    public void openWriteDataBase() throws SQLException {

        // Open the database
        currentDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        currentDB.execSQL(aSDbContract.CONSTRAINS_ON);
    }

    @Override
    public synchronized void close() {

        if (currentDB != null)
            currentDB.close();

        super.close();
    }

    private void copyDataBase() throws IOException {

        OutputStream databaseOutputStream = new FileOutputStream("" + DATABASE_PATH);
        InputStream databaseInputStream;

        byte[] buffer = new byte[1024];

        databaseInputStream = con.getAssets().open("androidSongDB.db");
        while (databaseInputStream.read(buffer) > 0) {
            databaseOutputStream.write(buffer);
        }

        databaseInputStream.close();
        databaseOutputStream.flush();
        databaseOutputStream.close();
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clearDb() throws IOException {
        copyDataBase();
    }
}
