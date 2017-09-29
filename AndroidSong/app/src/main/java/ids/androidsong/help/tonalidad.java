package ids.androidsong.help;

/**
 * Clase/Enum que opera sobre las tonalidades
 */
public enum tonalidad {
    A(0),Bb(1),B(2),C(3),Cs(4),D(5),Eb(6),E(7),F(8),Fs(9),G(10),Ab(11);

    int t;

    tonalidad(int i){
        this.t=i;
    }

    public int getValue (){
        return t;
    }

    private static int getNivel (String tono){
        int nivel = -1;
        if (tono.length() == 2){
            if (tono.charAt(1)=='#'){
                tono = tono.replace('#','s');
            }
        }
        for (tonalidad ton : tonalidad.values()){
            if (ton.name().equals(tono)){
                nivel = ton.getValue();
            }
        }
        return nivel;
    }

    private static String getTono(int nivel){
        String tono = "";
        for (tonalidad ton : tonalidad.values()){
            if (ton.getValue() == mod(nivel,12)){
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

    public static String getNuevoTono (String tono, int semitonos){
        String nuevoTono;
        int actual = getNivel(tono);
        if (actual > -1){
            nuevoTono = getTono(actual+semitonos);
        }
        else
        {
            nuevoTono = tono;
        }
        return nuevoTono;
    }

    private static int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    public static String getLineaTonos (String linea, int semitonos){
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
                    nuevoTono = getNuevoTono(tono,semitonos);
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
