package ids.androidsong.help;

/**
 * Created by ALAN on 01/10/2017.
 * Clase para almacenar listas de valores enumerados
 */

public final class Enum {

    public enum itemTipo {
        CANCION(0),IMAGEN(1);

        int t;
        itemTipo(int i){
            this.t=i;
        }
    }

    public enum atributo {
        presentacion(0),autor(1),transporte(2),interprete(3),tono(4);

        int a;
        atributo(int i){
            this.a=i;
        }
    }

}
