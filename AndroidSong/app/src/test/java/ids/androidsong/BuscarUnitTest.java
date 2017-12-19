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
import ids.androidsong.object.item;

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
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        ArrayList<cancion> canciones = new cancionesDummy().getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (cancion cancion : canciones) {
            cancion.alta();
        }
    }

    @Test
    public void Cancion_BuscarNinguno(){
        ArrayList<item> lista = new cancion().buscar("25");
        assertTrue(lista.size() == 0);
    }

    @Test
    public void Cancion_BuscarUno(){
        ArrayList<item> lista = new cancion().buscar("13");
        assertTrue(lista.size() == 1);
    }

    @Test
    public void Cancion_BuscarVarios(){
        ArrayList<item> lista = new cancion().buscar("5");
        assertTrue(lista.size() == 2);
    }

    @After
    public void finalize(){
        try {
            App.closeDB();
            App.getDBHelper().clearDb();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }
}