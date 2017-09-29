package ids.androidsong.help;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.OutputStreamWriter;

import ids.androidsong.R;

/**
 * Context Holder
 */
public class App {

    private static Context con;
    private static boolean set = false;

    public static Context getContext(){
        return con;
    }

    public static void setContext(Context pcon){
        con = pcon;
    }

    public static void load(Context pcon){
        if (!set) {
            try {
                setContext(pcon);
                checkFolders();
                checkFavoritos();
                set = true;
            } catch (Exception e) {
            }
        }
    }

    private static void checkFolders(){
        final String OpenSong = Environment.getExternalStorageDirectory() + "/" + con.getString(R.string.OpenSongFolder);
        File dir = new File(OpenSong);
        File subs;

        if(!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
            subs = new File(OpenSong + "/Songs");
            subs.mkdir();
            subs = new File(OpenSong + "/Sets");
            subs.mkdir();
            subs = new File(OpenSong + "/Backgrounds");
            subs.mkdir();
            subs = new File(OpenSong + "/Settings");
            subs.mkdir();
        }
    }

    private static void checkFavoritos() {
        boolean fav = true;
        try {
            con.openFileInput("favoritos.xml");
        }
        catch (Exception e){
            fav=false;
        }
        if (!fav){
            try{
                OutputStreamWriter osw = new OutputStreamWriter(con.openFileOutput("favoritos.xml", con.MODE_PRIVATE));
                osw.write("<favoritos></favoritos>");
                osw.flush();
                osw.close();
            }
            catch (Exception e) {}
        }
    }
}
