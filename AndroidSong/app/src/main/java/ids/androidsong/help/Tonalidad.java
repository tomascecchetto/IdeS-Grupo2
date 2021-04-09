package ids.androidsong.help;

/**
 * Clase/Enum que opera sobre las tonalidades
 */
public enum Tonalidad {
    A(0),Bb(1),B(2),C(3),Cs(4),D(5),Eb(6),E(7),F(8),Fs(9),G(10),Ab(11);

    final int t;

    Tonalidad(int i){
        this.t=i;
    }

    private int getValue(){
        return t;
    }

    private static int GetNivel(String tono){
        int nivel = -1;
        if (tono.length() == 2){
            if (tono.charAt(1)=='#'){
                tono = tono.replace('#','s');
            }
        }
        for (Tonalidad ton : Tonalidad.values()){
            if (ton.name().equals(tono)){
                nivel = ton.getValue();
            }
        }
        return nivel;
    }

    private static String GetTono(int nivel){
        String tono = "";
        for (Tonalidad ton : Tonalidad.values()){
            if (ton.getValue() == Mod(nivel,12)){
                tono = ton.name();
            }
        }
        if (tono.length() == 2){
            if ("s".contains(tono.substring(1))){
                tono = tono.replace('s','#');
            }
        }
        return tono;
    }

    private static String GetNuevoTono(String tono, int semitonos){
        String nuevoTono;
        int actual = GetNivel(tono);
        if (actual > -1){
            nuevoTono = GetTono(actual+semitonos);
        }
        else
        {
            nuevoTono = tono;
        }
        return nuevoTono;
    }

    private static int Mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    public static String GetLineaTonos(String linea, int semitonos){
        int i; int j;
        String nuevoTono;
        try {
            for (i= 0; i <= linea.length()-1; i++){
                CharSequence sub1 = linea.subSequence(i,i+1);
                if ("ABCDEFG".contains(sub1)){
                    j = 1;
                    if (i < linea.length()-1){
                        CharSequence sub2 = linea.subSequence(i+1,i+2);
                        if ("b#".contains(sub2)){
                            j = 2;
                        }
                    }
                    String tono = linea.substring(i,i+j);
                    nuevoTono = GetNuevoTono(tono,semitonos);
                    linea = linea.substring(0,i)+nuevoTono+linea.substring(i+j);
                }
            }
        }
        catch (Exception e){
            linea = e.getMessage();
        }
        return linea;
    }
}
