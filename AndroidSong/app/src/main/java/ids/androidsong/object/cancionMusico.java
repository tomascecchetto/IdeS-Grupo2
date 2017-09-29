package ids.androidsong.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import ids.androidsong.help.tonalidad;

public class cancionMusico extends cancionObjeto{

    public cancionMusico Load(cancionCabecera cancionSeleccionada){
        return (cancionMusico) super.Load(cancionSeleccionada);
    }

    public String ChangeChords(String letras, int semitonos){
        StringReader reader = new StringReader(letras);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        String letra = "";
        try
        {
            while ((linea = br.readLine()) != null){
                if (linea.length()>0){
                    Character caracter = linea.charAt(0);
                    switch (caracter){
                        case '.':
                            letra += tonalidad.getLineaTonos(linea, semitonos)+"\n";
                            break;
                        default:
                            letra += linea+"\n";
                            break;
                    }
                }
                else {
                    letra += linea+"\n";
                }
            }
        }
        catch (IOException e){}
        return letra;
    }

    public String GetFormatedChords(){
        return GetFormated(lyrics, true);
    }

    public String GetFormatedLyrics(){
        return GetFormated(lyrics, false);
    }

    public String GetFormated(String lyrics, boolean chords) {
        String formated = "";
        boolean coro = false;
        StringReader reader = new StringReader(lyrics);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        try
        {
            while ((linea = br.readLine()) != null) if (linea.length() > 0) {
                Character caracter = linea.charAt(0);
                switch (caracter) {
                    case '.':
                        if (chords) {
                            linea = linea.replaceAll(" ", "&nbsp;");
                            formated += "<b>" + linea.substring(1) + "</b><br/>";
                        }
                        break;
                    case '[':
                        caracter = linea.charAt(1);
                        switch (caracter) {
                            case 'C':
                                if (!coro) {
                                    coro = true;
                                    formated += "<i><b>Coro:<br/>";
                                } else {
                                    formated += "Coro:<br/>";
                                }
                                break;
                            case 'V':
                                if (coro) {
                                    formated += "</b>Estrofa " + linea.charAt(2) + ":</i><br/>";
                                } else {
                                    formated += "<i>Estrofa " + linea.charAt(2) + ":</i><br/>";
                                }
                                coro = false;
                                break;
                            case 'B':
                                if (coro) {
                                    formated += "Puente:</i><br/>";
                                } else {
                                    formated += "<i>Puente:</i><br/>";
                                }
                                coro = false;
                                break;
                            case 'T':
                                if (coro) {
                                    formated += "Marca:</i><br/>";
                                } else {
                                    formated += "<i>Marca</i><br/>";
                                }
                                coro = false;
                                break;
                            case 'P':
                                if (coro) {
                                    formated += "Pre-coro:</i><br/>";
                                } else {
                                    formated += "<i>Pre-coro:</i><br/>";
                                }
                                coro = false;
                                break;
                        }
                        break;
                    case ' ':
                        if (linea.length() > 2) {
                            formated += linea.substring(1) + "<br/>";
                        } else {
                            formated += linea + "&nbsp;<br/>";
                        }
                        break;
                    default:
                        formated += linea + "<br/>";
                        break;
                }
            } else {
                formated += "<br/>";
            }
        }
        catch (Exception e){
            formated += "Error al cargar.<br/>";
        }
        return formated;
    }

   public String GetOnlyLyrics(){
        return GetOnlyLyrics(lyrics);
    }

    private String GetOnlyLyrics(String lyrics) {
        String formated = "";
        StringReader reader = new StringReader(lyrics);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        try
        {
            while ((linea = br.readLine()) != null){
                if (linea.length()>0){
                    Character caracter = linea.charAt(0);
                    switch (caracter){
                        case '.':
                            break;
                        case ' ':
                            formated += linea+"\n";
                            break;
                        case '[':
                            formated += linea+"\n";
                            break;
                    }
                } else {
                    formated += linea+"\n";
                }
            }
        }
        catch (Exception e){
            formated += "Error al cargar.";
        };
        return formated;
    }
}
