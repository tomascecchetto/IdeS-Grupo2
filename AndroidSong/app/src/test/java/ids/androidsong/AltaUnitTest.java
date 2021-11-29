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
import ids.androidsong.object.DriveStatus;

import static org.junit.Assert.*;

/**
 * Pruebas de Alta de canciones
 * Verificar la correcta creación de todos los elementos en BD
 * Item, Atributos, secciones, DriveStatus (con marca de Nuevo)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27, manifest = "AndroidManifest.xml", packageName = "ids.androidsong")

public class AltaUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 5;
    private int CANTIDAD_SECCIONES_DUMMY = 3;
    private ArrayList<Cancion> canciones;

    @Before public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.SetContext(RuntimeEnvironment.application);
        App.GetOpenDB();
        /*Pide canciones dummy y las inserta*/
        canciones = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY).getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);

    }

    @Test
    public void Cancion_Alta(){
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
        int cantidad = new Cancion().get().size();
        assertTrue( cantidad == CANTIDAD_CANCIONES_DUMMY);
    }

    @Test
    public void Cancion_Secciones(){
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
        boolean result = true;
        Cancion cancionGuardada;
        for(Cancion c : canciones){
            cancionGuardada = new Cancion(c.getId());
            cancionGuardada.fill();
            if (c.getSecciones().size() != cancionGuardada.getSecciones().size())
                result = false;
        }
        assertTrue(result);
    }

    @Test
    public void Cancion_Atributos(){
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
        boolean result = true;
        Cancion cancionGuardada;
        for(Cancion c : canciones){
            cancionGuardada = new Cancion(c.getId());
            cancionGuardada.fill();
            if (c.getAtributos().size() != cancionGuardada.getAtributos().size())
                result = false;
        }
        assertTrue(result);
    }

    @Test
    public void DriveStatus_getNuevos() throws Exception {
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
        ArrayList<DriveStatus> lista = new DriveStatus().getNuevos();
        assertEquals(lista.size(), CANTIDAD_CANCIONES_DUMMY);
    }

    @After public void finalize(){
        try {
            App.GetDBHelper().clearDb();
            App.CloseDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }
}