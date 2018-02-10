package ids.androidsong;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.object.cancion;
import ids.androidsong.object.driveStatus;

import static org.junit.Assert.*;

/**
 * Pruebas de Alta de canciones
 * Verificar la correcta creación de todos los elementos en BD
 * Item, atributos, secciones, driveStatus (con marca de Nuevo)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml", packageName = "ids.androidsong")
public class AltaUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 5;
    private int CANTIDAD_SECCIONES_DUMMY = 3;
    private ArrayList<cancion> canciones;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        App.getOpenDB();
        /*Pide canciones dummy y las inserta*/
        canciones = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY).getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);

    }

    @Test
    public void Cancion_Alta(){
        for (cancion cancion : canciones) {
            cancion.alta();
        }
        int cantidad = new cancion().get().size();
        assertTrue( cantidad == CANTIDAD_CANCIONES_DUMMY);
    }

    @Test
    public void Cancion_Secciones(){
        for (cancion cancion : canciones) {
            cancion.alta();
        }
        boolean result = true;
        cancion cancionGuardada;
        for(cancion c : canciones){
            cancionGuardada = new cancion(c.getId());
            cancionGuardada.fill();
            if (c.getSecciones().size() != cancionGuardada.getSecciones().size())
                result = false;
        }
        assertTrue(result);
    }

    @Test
    public void Cancion_Atributos(){
        for (cancion cancion : canciones) {
            cancion.alta();
        }
        boolean result = true;
        cancion cancionGuardada;
        for(cancion c : canciones){
            cancionGuardada = new cancion(c.getId());
            cancionGuardada.fill();
            if (c.getAtributos().size() != cancionGuardada.getAtributos().size())
                result = false;
        }
        assertTrue(result);
    }

    @Test
    public void DriveStatus_getNuevos() throws Exception {
        for (cancion cancion : canciones) {
            cancion.alta();
        }
        ArrayList<driveStatus> lista = new driveStatus().getNuevos();
        assertTrue(lista.size() == CANTIDAD_CANCIONES_DUMMY);
    }

    @After
    public void finalize(){
        try {
            App.getDBHelper().clearDb();
            App.closeDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }
}