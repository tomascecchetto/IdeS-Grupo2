package ids.androidsong.help;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by ALAN on 30/10/2017.
 * Clase que encapsula las solicitudes de permisos en la app.
 */

public class permisos {

    public static void solicitarLocal(Activity activity){
        solicitar(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE,0);
    }

    public static void solicitarCuenta(Activity activity){
        solicitar(activity,Manifest.permission.INTERNET,2);
        solicitar(activity,Manifest.permission.GET_ACCOUNTS,1);
        solicitar(activity,Manifest.permission.ACCESS_NETWORK_STATE,3);
    }

    private static void solicitar(Activity activity, String permission, int id){
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    id);
        }
    }
}
