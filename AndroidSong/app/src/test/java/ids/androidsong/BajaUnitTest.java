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
    private String DRIVE_HASH = "DriveHash";
    ArrayList<cancion> canciones;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.setContext(RuntimeEnvironment.application);
        App.getOpenDB();
        canciones = new cancionesDummy().getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (cancion cancion : canciones) {
            cancion.alta();
        }
    }



    @Test
    public void Cancion_BajaNoSincronizado_Status(){
        cancion cancion = canciones.get(0);
        cancion.baja();
        driveStatus status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertEquals(0,status.getItemId());
    }

    @Test
    public void Cancion_BajaSincronizado_Status(){
        cancion cancion = canciones.get(0);
        driveStatus status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.setItem(cancion);
        status.setDriveDT(DRIVE_HASH);
        status.modificacion();
        cancion.baja();
        status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getDriveDT().equals(DRIVE_HASH) && status.getLocalDT() == null);
    }

    @Test
    public void Cancion_Baja_Papelera(){
        cancion cancion0 = canciones.get(0);
        cancion0.baja();
        cancion cancion1 = canciones.get(1);
        cancion1.baja();
        assertTrue((new cancion()).getBajas().size() == 2);
    }

    @Test
    public void Cancion_Baja_Lista(){
        cancion cancion0 = canciones.get(0);
        cancion0.baja();
        cancion cancion1 = canciones.get(1);
        cancion1.baja();
        assertTrue((new cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-2);
    }

    @Test
    public void Cancion_RestaurarNoSincronizado_Status(){
        cancion cancion = canciones.get(0);
        cancion.baja();
        cancion.restaurar();
        driveStatus status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getItemId() > 0 && status.getLocalDT() != null && status.getDriveDT() == null);
    }

    @Test
    public void Cancion_RestaurarSincronizado_Status(){
        cancion cancion = canciones.get(0);
        driveStatus status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.fill();
        status.setDriveDT(DRIVE_HASH);
        status.modificacion();
        cancion.baja();
        cancion.restaurar();
        status = new driveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getLocalDT() != null);
    }

    @Test
    public void Cancion_Restaurar_Papelera(){
        cancion cancion0 = canciones.get(0);
        cancion0.baja();
        cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.restaurar();
        assertTrue((new cancion()).getBajas().size() == 1);
    }

    @Test
    public void Cancion_Restaurar_Lista(){
        cancion cancion0 = canciones.get(0);
        cancion0.baja();
        cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.restaurar();
        assertTrue((new cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-1);
    }

    @Test
    public void Cancion_Eliminar() {
        cancion cancion0 = canciones.get(0);
        cancion0.baja();
        cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.eliminar();
        assertTrue((new cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-2 && (new cancion()).getBajas().size() == 1);
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