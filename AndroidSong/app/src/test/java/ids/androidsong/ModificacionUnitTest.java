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
import ids.androidsong.object.atributo;
import ids.androidsong.object.cancion;
import ids.androidsong.object.driveStatus;
import ids.androidsong.object.seccion;

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
    private int CANTIDAD_SECCIONES_DUMMY = 3;
    ArrayList<cancion> canciones;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        App.getOpenDB();
        canciones = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY).getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (cancion cancion : canciones) {
            cancion.alta();
        }
    }



    @Test
    public void Cancion_ModificarSecciones(){
        cancion cancion = canciones.get(0);
        String letra = cancion.getLetra() +
                "[T]\n Contenido tag de prueba";
        cancion.setSecciones(new ArrayList<seccion>());
        cancion.llenarSecciones(letra);
        cancion.modificacion();
        cancion cancionModificada = new cancion(cancion.getId());
        cancionModificada.fill();
        assertTrue(cancion.getSecciones().size()+1 == cancionModificada.getSecciones().size());
    }

    @Test
    public void Cancion_ModificarSecciones_Status(){
        cancion cancion = canciones.get(0);
        String letra = cancion.getLetra() +
                "[T]\n Contenido tag de prueba";
        cancion.setSecciones(new ArrayList<seccion>());
        cancion.llenarSecciones(letra);
        cancion.modificacion();
        driveStatus status = new driveStatus(cancion);
        status.fill();
        assertTrue(!status.getLocalDT().equals(cancion.getFechaModificacion()));
    }

    @Test
    public void Cancion_ModificarAtributos(){
        cancion cancion = canciones.get(0);
        String atributos = cancion.getAtributosTexto() +
                "Atributo de Prueba, Contenido de prueba";
        cancion.setAtributos(new ArrayList<atributo>());
        cancion.llenarAtributos(atributos);
        cancion.modificarAtributos();
        cancion cancionModificada = new cancion(cancion.getId());
        cancionModificada.fill();
        assertTrue(cancion.getAtributos().size()+1 == cancionModificada.getAtributos().size());
    }

    @Test
    public void Cancion_ModificarAtributos_Status(){
        cancion cancion = canciones.get(0);
        String atributos = cancion.getAtributosTexto() +
                "Atributo de Prueba, Contenido de prueba";
        cancion.setAtributos(new ArrayList<atributo>());
        cancion.llenarAtributos(atributos);
        cancion.modificarAtributos();
        driveStatus status = new driveStatus(cancion);
        status.fill();
        assertTrue(!status.getLocalDT().equals(cancion.getFechaModificacion()));
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