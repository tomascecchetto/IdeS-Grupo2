package ids.androidsong.object;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * 03/01/2015. Manipula las canciones para presentar.
 */
public class cancionPantalla extends cancionObjeto implements iGrupos {

    private final String type = "song";

    public cancionPantalla Load(cancionCabecera cancionSeleccionada){
        return (cancionPantalla) super.Load(cancionSeleccionada);
    }

    @Override
    public itemObjeto[] getAllItems(){
        ArrayList<itemObjeto> items = new ArrayList<itemObjeto>();
        String id = "";
        String texto = "";
        itemObjeto item = new itemObjeto(type,title,"");
        StringReader reader = new StringReader(lyrics);
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
                            break;
                        case '[':
                            id = linea.substring(1,linea.indexOf("]"));
                            if (primeralinea){
                                item = new itemObjeto(type,title,id);
                                primeralinea = false;
                            }
                            else {
                                item.texto = texto;
                                items.add(item);
                                item = new itemObjeto(type,title,id);
                                texto = "";
                            }
                            break;
                        case ' ':
                            if (linea.equals(" ||")) {
                                item.texto = texto;
                                items.add(item);
                                item = new itemObjeto(type,title,id);
                                texto = "";
                            }else {
                                texto += linea + "\n";
                                break;
                            }
                    }
                }
            }
            item.texto = texto;
            items.add(item);
        }
        catch (Exception e){
            texto += "Error al cargar.";
            item.texto = texto;
            items.add(item);
        }
        return items.toArray(new itemObjeto[items.size()]);
    }

    private String[] getOrden(){
        ArrayList<String> orden = new ArrayList<String>();
        int i; int j = 0;
        char c;
        for (i=0;i<=presentation.length()-1;i++){
            c = presentation.charAt(i);
            if (c==' '){
                orden.add(presentation.substring(j,i));
                j=i+1;
            }
        }
        orden.add(presentation.substring(j,presentation.length()));
        return orden.toArray(new String[orden.size()]);
    }

    @Override
    public itemObjeto[] getItems(){
        itemObjeto[] items = getAllItems();
        if (presentation.length()>0){
            String[] orden = getOrden();
            ArrayList<itemObjeto> orderedItems = new ArrayList<itemObjeto>();
            for (String id: orden){
                for (itemObjeto item: items){
                    if (item.id.equals(id)){
                        orderedItems.add(item);
                    }
                }
            }
            return orderedItems.toArray(new itemObjeto[orderedItems.size()]);
        }
        else {
            return items;
        }

    }

    @Override
    public String getType(){
        return type;
    }
}
