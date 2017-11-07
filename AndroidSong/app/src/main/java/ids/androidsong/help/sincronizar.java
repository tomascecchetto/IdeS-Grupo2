package ids.androidsong.help;

import android.os.Environment;

import ids.androidsong.R;

/**
 * Created by ALAN on 05/11/2017.
 * Clase que gestiona la busqueda y carga de canciones desde disco
 */

public class sincronizar {

    public static String defaultPath = "/" +
            App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SongsFolder);
    boolean existeLocal;
    boolean existeStatus;
    
}
