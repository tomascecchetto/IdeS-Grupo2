package ids.androidsong.object;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;

import ids.androidsong.help.Enum;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto Canción
 */

public class cancion extends item implements Serializable {

    public cancion(int i, String t) {
        super(i, t);
    }

    public cancion(int i) {
        super(i);
    }

    public cancion(){}

    public void llenarSecciones(String letra){
        String nombre = "";
        String contenido = "";
        seccion seccion = new seccion(nombre,contenido);
        StringReader reader = new StringReader(letra);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        boolean primeralinea = true;
        try
        {
            while ((linea = br.readLine()) != null){
                if (linea.length()>0){
                    Character caracter = linea.charAt(0);
                    switch (caracter){
                        case '.':
                            contenido += linea + "\n";
                            break;
                        case '[':
                            nombre = linea.substring(1,linea.indexOf("]"));
                            if (primeralinea){
                                seccion = new seccion(nombre);
                                primeralinea = false;
                            }
                            else {
                                seccion.setContenido(contenido);
                                this.getSecciones().add(seccion);
                                seccion = new seccion(nombre);
                                contenido = "";
                            }
                            break;
                        case ' ':
                            if (linea.equals(" ||")) {
                                seccion.setContenido(contenido);
                                this.getSecciones().add(seccion);
                                seccion = new seccion(nombre);
                                contenido = "";
                            }else {
                                contenido += linea + "\n";
                                break;
                            }
                        default:
                            contenido += " " + linea + "\n";
                            break;
                    }
                }
            }
            if (seccion.getNombre().equals(""))
                seccion.setNombre("V1");
            seccion.setContenido(contenido);
            this.getSecciones().add(seccion);
        }
        catch (Exception e){
            contenido += "Error al cargar.";
            seccion.setContenido(contenido);
            this.getSecciones().add(seccion);
        }
    }

    public ArrayList<item> get(){
        return super.get(Enum.itemTipo.CANCION.name());
    }

    public ArrayList<item> get(String carpeta){
        return getByCarpeta(Enum.itemTipo.CANCION.name(),carpeta,true);
    }

    public ArrayList<item> getBajas(){
        return getByCarpeta(Enum.itemTipo.CANCION.name(),FILTRO_CARPETA_TODAS,false);
    }

    public void alta(){
        super.alta(Enum.itemTipo.CANCION.name());
    }

    public String getLetra(){
        String letra = "";
        for (seccion sec : getSecciones()){
            letra = letra + "[" + sec.getNombre() + "]\n";
            letra = letra + sec.getContenido() + "\n";
        }
        return letra;
    }
}
