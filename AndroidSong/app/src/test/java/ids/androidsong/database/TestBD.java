package ids.androidsong.database;



import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Test;

import ids.androidsong.help.aSDbHelper;
import ids.androidsong.object.carpeta;


/**
 * Created by Brian on 27/11/2017.
 */

public class TestBD extends AndroidTestCase {
    aSDbHelper bd;
    public SQLiteDatabase currentDB;
    SQLiteDatabase db;
    carpeta carpeta1;



    protected void setUp() throws Exception {
        super.setUp();
        bd = new aSDbHelper(this.getContext());
        carpeta1 = new carpeta();
        bd.openDataBase();
        bd.openWriteDataBase();
        carpeta1.setNombre("carpeta1");
        carpeta1.setId(1);
        carpeta1.alta();


    }
    protected void tearDown() throws Exception {
        super.tearDown();

    }

}
