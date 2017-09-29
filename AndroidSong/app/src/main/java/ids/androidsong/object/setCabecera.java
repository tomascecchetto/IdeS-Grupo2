package ids.androidsong.object;

/**
 * 19/07/2015. Cabecera de las colecciones
 */
public class setCabecera extends fileCabecera {

    public setCabecera(String pTitulo, String pDir) {
        titulo = pTitulo;
        int f;
        f = pDir.lastIndexOf("/");
        path = pDir.substring(0,f+1);
    }
}
