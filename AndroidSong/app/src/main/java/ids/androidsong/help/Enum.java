package ids.androidsong.help;

/**
 * Created by ALAN on 01/10/2017.
 * Clase para almacenar listas de valores enumerados
 */

public final class Enum {

    public enum itemTipo {
        CANCION(0),IMAGEN(1);

        final int t;
        itemTipo(int i){
            this.t=i;
        }
    }

    public enum atributo {
        presentacion(0),autor(1),transporte(2),interprete(3),tono(4),himno_numero(5),titulo_alternativo(6),user1(7),user2(8),user3(9);

        final int a;
        atributo(int i){
            this.a=i;
        }
    }

    public enum atributoXml {
        presentation(0),author(1),capo(2),copyright(3),key(4),hymn_number(5),aka(6),user1(7),user2(8),user3(9);

        final int a;
        atributoXml(int i){
            this.a=i;
        }
    }

}
