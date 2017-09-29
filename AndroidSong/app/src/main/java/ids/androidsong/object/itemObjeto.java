package ids.androidsong.object;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Unidad de una presentación.
 */
public class itemObjeto {
    public String type = "";
    public String title = "";
    public String id = "";
    public String texto = " ";
    public estiloObjeto estilo = new estiloObjeto();

    public itemObjeto (String tipo, String titulo, String seccion){
        type = tipo;
        title = titulo;
        id = seccion;
    }

    public int lineas(){
        BufferedReader textReader = new BufferedReader(new StringReader(texto));
        int i = 2;
        try {
            while (textReader.readLine() != null) {
                i++;
            }
        }
        catch (Exception e) {}
        return i;
    }

    public int maxCaracteres(){
        BufferedReader textReader = new BufferedReader(new StringReader(texto));
        int i = 0;
        String linea = "";
        try {
            while ((linea = textReader.readLine()) != null) {
                i = linea.length() > i ? linea.length() : i;
            }
        }
        catch (Exception e) {}
        return i;
    }
}
