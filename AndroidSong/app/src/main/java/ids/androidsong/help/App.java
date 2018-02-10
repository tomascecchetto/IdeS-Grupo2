package ids.androidsong.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Context Holder
 */
public class App {

    private static Context con;
    private static aSDbHelper helper;

    public static Context getContext(){
        return con;
    }

    public static void setContext(Context pcon){
        con = pcon;
    }

    public static synchronized aSDbHelper getDBHelper(){
        if (helper == null){
            helper = new aSDbHelper(getContext());
        }
        return helper;
    }

    public static synchronized SQLiteDatabase getOpenDB(){
        if (getDBHelper().currentDB == null){
            try {
                getDBHelper().createDataBase();
                getDBHelper().openWriteDataBase();
            } catch (Exception e) {
                Log.e("Error",e.getMessage());
            }
        } else if (!getDBHelper().currentDB.isOpen()){
            getDBHelper().openWriteDataBase();
        }
        return getDBHelper().currentDB;
    }

    public static void closeDB(){
        if (getDBHelper().currentDB != null)
            if (getDBHelper().currentDB.isOpen()) getDBHelper().currentDB.close();
    }
}
