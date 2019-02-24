package ids.androidsong;

import java.util.ArrayList;

import ids.androidsong.help.Enum;
import ids.androidsong.object.atributo;
import ids.androidsong.object.cancion;
import ids.androidsong.object.seccion;

/*Esta clase genera canciones de prueba para insertar en la BD de test
* En el constructor se puede setear la cantidad de elementos variables
* (atributos usuario y secciones) a crear.
*/
public class cancionesDummy {

    private int cantidadInterna;
    private String TITULO_DUMMY = "Canción Dummy ";
    private String CARPETA_DUMMY = "Principal";
    private String ATRIBUTO_DUMMY = "Dummy_";
    private String SECCION_NOMBRE_DUMMY = "V";
    private String SECCION_CONTENIDO_DUMMY = ". C    F#      G\n" +
                                             " Contenido Dummy ";
    private int delta;

    public cancionesDummy(int c){
        cantidadInterna = c;
    }

    public cancionesDummy(){
        cantidadInterna = 0;
    }

    public ArrayList<cancion> getCancionesDummy(int cantidad) {
        return getCancionesDummy(cantidad, CARPETA_DUMMY);
    }

    //El método recibe un parámetro para la cantidad de canciones esperadas
    public ArrayList<cancion> getCancionesDummy(int cantidad, String carpeta){
        ArrayList<cancion> canciones = new ArrayList<>();
        int i;
        for (i=0;i<cantidad;i++){
            cancion cancion = new cancion();
            cancion.setTitulo(TITULO_DUMMY + Integer.toString(i + delta));
            cancion.setCarpeta(carpeta);
            cancion.setAtributos(getAtributosDummy(i));
            cancion.setSecciones(getSeccionesDummy(i));
            canciones.add(cancion);
        }
        delta += cantidad;
        return canciones;
    }

    private ArrayList<atributo> getAtributosDummy(int i){
        ArrayList<atributo> atributos = new ArrayList<>();
        int j;
        //Los atributos predefinidos se cargan todos
        for (j = 0; j< Enum.atributo.values().length; j++){
            atributos.add(new atributo(Enum.atributo.values()[j].name(),ATRIBUTO_DUMMY + Enum.atributo.values()[j].name()));
        }
        //Luego se generan atributos custom par cada canción
        int k = cantidadInterna == 0 ? i : cantidadInterna;
        for (j=0;j<k;j++){
            atributos.add(new atributo(ATRIBUTO_DUMMY + Integer.toString(j + delta),ATRIBUTO_DUMMY + Integer.toString(j + delta)));
        }
        return atributos;
    }

    private ArrayList<seccion> getSeccionesDummy(int i){
        ArrayList<seccion> secciones = new ArrayList<>();
        int j;
        int k = cantidadInterna == 0 ? i : cantidadInterna;
        for (j=1;j<=k;j++){
            String contenidoDummy = "";
            int l;
            for (l=1;l<=j;l++){
                contenidoDummy = contenidoDummy + SECCION_CONTENIDO_DUMMY + Integer.toString(l + delta) + "\n";
            }
            secciones.add(new seccion(SECCION_NOMBRE_DUMMY + Integer.toString(j + delta),contenidoDummy));
        }
        return secciones;
    }
}