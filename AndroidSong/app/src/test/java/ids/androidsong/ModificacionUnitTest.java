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
 * Pruebas de Modificación de canciones
 * Probar la modificación de los elementos y el impacto en el DriveStatus
 * Verificar modificación de Secciones
 *      El flujo de modificaicón de Secciones tiene tres partes:
 *      1-cancion.getLetra
 *      2-cancion.llenarSecciones
 *      3-cancion.modificacion
 * Verificar Modificación de Atributos
 *      El flujo de modificaicón de atributos tiene tres partes:
 *      1-cancion.getAtributosTexto
 *      2-cancion.llenarAtributos
 *      3-cancion.modificacion
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "ids.androidsong")
public class ModificacionUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 1;

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
    public void Cancion_ModificarSecciones(){
        assertTrue(true);
    }

    @Test
    public void Cancion_ModificarAtributos(){
        assertTrue(true);
    }
}