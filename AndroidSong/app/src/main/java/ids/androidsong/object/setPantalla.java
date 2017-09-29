package ids.androidsong.object;

import java.util.ArrayList;

/**
 * 20/07/2015. Renderiza un set a Items desplegables
 */
public class setPantalla extends setObjeto implements iGrupos{

    @Override
    public setPantalla Load(setCabecera setSeleccionado){
        return (setPantalla)super.Load(setSeleccionado);
    }

    @Override
    public itemObjeto[] getItems() {
        ArrayList<itemObjeto> items = new ArrayList<itemObjeto>();
        items.add(espacio());
        for (iGrupos g: grupos){
            for (itemObjeto i: g.getItems()){
                items.add(i);
            }
            items.add(espacio());
        }
        return items.toArray(new itemObjeto[items.size()]);
    }

    @Override
    public itemObjeto[] getAllItems() {
        return getItems();
    }

    @Override
    public String getType() {
        return "set";
    }
}
