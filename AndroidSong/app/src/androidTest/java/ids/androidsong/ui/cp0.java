package ids.androidsong.ui;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.cancionesDummy;
import ids.androidsong.help.App;
import ids.androidsong.object.Cancion;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class cp0 { //ya esta - eliminar
    private static int CANTIDAD_CANCIONES_DUMMY = 1;
    private static int CANTIDAD_SECCIONES_DUMMY = 3;

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    @Before
    public void setup() {
        /*Pide canciones dummy y las inserta*/
        cancionesDummy cancionesDummy = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY);
        //   canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY));
        ArrayList<Cancion> canciones = new ArrayList<>(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY, "Pruebas"));
        for (Cancion c : canciones) {
            c.alta();
        }
    }

    @Test
    //da de alta una cancion con 3 secciones en una carpeta llamada "Pruebas"
    public void cp0_1() {

    }
/*
    @After
    public void destroy(){
        try {
       //     App.GetDBHelper().clearDb();
            App.CloseDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
    }

 */
}