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
 * Verificar los eventos de una Baja
 * Validar la Papelera y la eliminación desde Papelera
 * Validar la restauración (marca el status como nuevo)
 * Validar DriveStatus:
 *      con marca de Baja (para canciones ya sincronizadas, hay que simular la sincronización pegando una fecha en status.DriveDT)
 *      o su ausencia (para canciones que sólo existen localmente)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.Xml", packageName = "ids.androidsong")
public class BajaUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 5;
    private String DRIVE_HASH = "DriveHash";
    ArrayList<Cancion> canciones;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.SetContext(RuntimeEnvironment.application);
        App.GetOpenDB();
        canciones = new cancionesDummy().getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        for (Cancion cancion : canciones) {
            cancion.alta();
        }
    }



    @Test
    public void Cancion_BajaNoSincronizado_Status(){
        Cancion cancion = canciones.get(0);
        cancion.baja();
        DriveStatus status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertEquals(0,status.getItemId());
    }

    @Test
    public void Cancion_BajaSincronizado_Status(){
        Cancion cancion = canciones.get(0);
        DriveStatus status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.setItem(cancion);
        status.setDriveDT(DRIVE_HASH);
        status.modificacion();
        cancion.baja();
        status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getDriveDT().equals(DRIVE_HASH) && status.getLocalDT() == null);
    }

    @Test
    public void Cancion_Baja_Papelera(){
        Cancion cancion0 = canciones.get(0);
        cancion0.baja();
        Cancion cancion1 = canciones.get(1);
        cancion1.baja();
        assertTrue((new Cancion()).getBajas().size() == 2);
    }

    @Test
    public void Cancion_Baja_Lista(){
        Cancion cancion0 = canciones.get(0);
        cancion0.baja();
        Cancion cancion1 = canciones.get(1);
        cancion1.baja();
        assertTrue((new Cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-2);
    }

    @Test
    public void Cancion_RestaurarNoSincronizado_Status(){
        Cancion cancion = canciones.get(0);
        cancion.baja();
        cancion.restaurar();
        DriveStatus status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getItemId() > 0 && status.getLocalDT() != null && status.getDriveDT() == null);
    }

    @Test
    public void Cancion_RestaurarSincronizado_Status(){
        Cancion cancion = canciones.get(0);
        DriveStatus status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.fill();
        status.setDriveDT(DRIVE_HASH);
        status.modificacion();
        cancion.baja();
        cancion.restaurar();
        status = new DriveStatus(cancion.getTitulo(),cancion.getCarpeta());
        status.get();
        assertTrue(status.getLocalDT() != null);
    }

    @Test
    public void Cancion_Restaurar_Papelera(){
        Cancion cancion0 = canciones.get(0);
        cancion0.baja();
        Cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.restaurar();
        assertTrue((new Cancion()).getBajas().size() == 1);
    }

    @Test
    public void Cancion_Restaurar_Lista(){
        Cancion cancion0 = canciones.get(0);
        cancion0.baja();
        Cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.restaurar();
        assertTrue((new Cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-1);
    }

    @Test
    public void Cancion_Eliminar() {
        Cancion cancion0 = canciones.get(0);
        cancion0.baja();
        Cancion cancion1 = canciones.get(1);
        cancion1.baja();
        cancion1.eliminar();
        assertTrue((new Cancion()).get().size() == CANTIDAD_CANCIONES_DUMMY-2 && (new Cancion()).getBajas().size() == 1);
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