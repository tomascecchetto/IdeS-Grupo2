package ids.androidsong;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.Item;

import static org.junit.Assert.*;

/**
 * Pruebas de Búsqueda de canciones
 * Buscar:
 *      Por título
 *      por Carpeta
 *      por estado (activo-baja)
 *      por id (fill)
 *      por Atributos (próximamente)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.Xml", packageName = "ids.androidsong")
public class BuscarUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 20;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.SetContext(RuntimeEnvironment.application);
        ArrayList<Cancion> canciones = new cancionesDummy().getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
    }

    @Test
    public void Cancion_BuscarNinguno(){
        ArrayList<Item> lista = new Cancion().buscar("25");
        assertTrue(lista.size() == 0);
    }

    @Test
    public void Cancion_BuscarUno(){
        ArrayList<Item> lista = new Cancion().buscar("13");
        assertTrue(lista.size() == 1);
    }

    @Test
    public void Cancion_BuscarVarios(){
        ArrayList<Item> lista = new Cancion().buscar("5");
        assertTrue(lista.size() == 2);
    }

    @After
    public void finalize(){
        try {
            App.CloseDB();
            App.GetDBHelper().clearDb();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }
}