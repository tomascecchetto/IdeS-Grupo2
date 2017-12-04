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
 * Pruebas de Búsqueda de canciones
 * Buscar:
 *      Por título
 *      por carpeta
 *      por estado (activo-baja)
 *      por id (fill)
 *      por atributos (próximamente)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "ids.androidsong")
public class BuscarUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 20;

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
    public void Cancion_Buscar(){
        assertTrue(true);
    }
}