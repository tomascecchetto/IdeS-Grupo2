package ids.androidsong.help;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by ALAN on 30/10/2017.
 */

public class permisos {
    final static int PERMISO_ESCRITURA_LOCAL = 0;
    final static int PERMISO_CUENTAS_USUARIO = 1;

    public static void solicitar(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISO_ESCRITURA_LOCAL);
        }
    }

    public static void solicitarCuenta(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISO_CUENTAS_USUARIO);
        }
    }
}