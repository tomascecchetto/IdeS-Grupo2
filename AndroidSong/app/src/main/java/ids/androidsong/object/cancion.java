package ids.androidsong.object;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ALAN on 01/10/2017.
 */

public class cancion extends item {

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
                    }
                }
            }
            seccion.setContenido(contenido);
            this.getSecciones().add(seccion);
        }
        catch (Exception e){
            contenido += "Error al cargar.";
            seccion.setContenido(contenido);
            this.getSecciones().add(seccion);
        }
    }
}
