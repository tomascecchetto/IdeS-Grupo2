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
import java.lang.reflect.Field;
import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.help.aSDbHelper;
import ids.androidsong.object.cancion;
import ids.androidsong.object.driveStatus;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
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

    @Before
    //Esto se ejecuta SIEMPRE antes de los test
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        /*Pide canciones dummy y las inserta*/
        ArrayList<cancion> canciones = new cancionesDummy().getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (cancion cancion : canciones) {
            cancion.alta();
        }
    }

    @Test
    public void Cancion_Alta(){
        int cantidad = new cancion().get().size();
        assertTrue( cantidad == CANTIDAD_CANCIONES_DUMMY);
    }

    @Test
    public void DriveStatus_getNuevos() throws Exception {
        ArrayList<driveStatus> lista = new driveStatus().getNuevos();
        assertTrue(lista.size() == CANTIDAD_CANCIONES_DUMMY);
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
/*
    private void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }*/
}