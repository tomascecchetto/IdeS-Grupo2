package ids.androidsong.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Context Holder
 */
public class App {

    private static Context  APP_CONTEXT;
    private static AsdbHelper DB_HELPER;

    public static Context GetContext(){
        return APP_CONTEXT;
    }

    public static void SetContext(Context pcon){
        APP_CONTEXT = pcon;
    }

    public static synchronized AsdbHelper GetDBHelper(){
        if (DB_HELPER == null){
            DB_HELPER = new AsdbHelper(GetContext());
        }
        return DB_HELPER;
    }

    public static synchronized SQLiteDatabase GetOpenDB(){
        if (GetDBHelper().currentDB == null){
            try {
                GetDBHelper().createDataBase();
                GetDBHelper().openWriteDataBase();
            } catch (Exception e) {
                Log.e("Error",e.getMessage());
            }
        } else if (!GetDBHelper().currentDB.isOpen()){
            GetDBHelper().openWriteDataBase();
        }
        return GetDBHelper().currentDB;
    }

    public static void CloseDB(){
        if (GetDBHelper().currentDB != null)
            if (GetDBHelper().currentDB.isOpen()) GetDBHelper().currentDB.close();
    }
}
