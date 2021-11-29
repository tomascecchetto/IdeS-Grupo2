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
import ids.androidsong.object.Atributo;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.Seccion;

import static org.junit.Assert.*;

/**
 * Pruebas de Alta de canciones
 * Verificar la correcta creación de todos los elementos en BD
 * Item, Atributos, secciones, DriveStatus (con marca de Nuevo)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27, manifest = "AndroidManifest.xml", packageName = "ids.androidsong")
public class ConversionesUnitTest {

    private int CANTIDAD_CANCIONES_DUMMY = 1;
    private int CANTIDAD_SECCIONES_DUMMY = 3;
    private Cancion cancion;

    @Before
    public void setup(){
        //Esto guarda el contexto en la clase estática que maneja el acceso a los recursos.
        App.SetContext(RuntimeEnvironment.application);
        App.GetOpenDB();
        /*Pide canciones dummy y las inserta*/
        ArrayList<Cancion> canciones = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY).getCancionesDummy(CANTIDAD_CANCIONES_DUMMY);
        cancion = canciones.get(0);
    }

    @Test
    public void Cancion_Secciones(){
        String Letra1 = cancion.getLetra();
        cancion.setSecciones(new ArrayList<Seccion>());
        cancion.llenarSecciones(Letra1);
        String Letra2 = cancion.getLetra();
        assertEquals(Letra1,Letra2);
    }

    @Test
    public void Cancion_Atributos(){
        String atributo = "Atributo Prueba, Valor de prueba\n";
        String Atributos1 = cancion.getAtributosTexto();
        cancion.setAtributos(new ArrayList<Atributo>());
        cancion.llenarAtributos(Atributos1 + atributo);
        String Atributos2 = cancion.getAtributosTexto();
        assertEquals(Atributos1.length()+atributo.length(),Atributos2.length());
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