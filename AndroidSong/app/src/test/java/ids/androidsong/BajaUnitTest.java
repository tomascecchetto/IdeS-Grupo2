package ids.androidsong;

import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.object.cancion;
import ids.androidsong.object.driveStatus;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.*;

/**
 * Pruebas de Alta de canciones
 * Verificar los eventos de una Baja
 * Validar la papelera y la eliminación desde papelera
 * Validar la restauración (marca el status como nuevo)
 * Validar DriveStatus:
 *      con marca de Baja (para canciones ya sincronizadas, hay que simular la sincronización pegando una fecha en status.DriveDT)
 *      o su ausencia (para canciones que sólo existen localmente)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "ids.androidsong")
public class BajaUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 5;

    @Before
    //Esto se ejecuta SIEMPRE antes de los test
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        App.getOpenDB();
        //Dar de alta algunas canciones para la prueba acá.
    }

    @After
    /*Esto se ejecuta SIEMPRE después del último test
    * Acá borramos la BD para el siguiente test*/
    public void finalize(){
        try {
            App.closeDB();
            App.getDBHelper().clearDb();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }

    @Test
    public void Cancion_Eliminar(){
        assertTrue(true);
    }
}