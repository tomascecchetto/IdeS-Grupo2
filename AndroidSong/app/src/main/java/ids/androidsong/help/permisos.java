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

    public static void SolicitarLocal(Activity activity){
        Solicitar(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE,0);
    }

    public static void SolicitarCuenta(Activity activity){
        Solicitar(activity,Manifest.permission.INTERNET,2);
        Solicitar(activity,Manifest.permission.GET_ACCOUNTS,1);
        Solicitar(activity,Manifest.permission.ACCESS_NETWORK_STATE,3);
    }

    private static void Solicitar(Activity activity, String permission, int id){
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    id);
        }
    }
}
