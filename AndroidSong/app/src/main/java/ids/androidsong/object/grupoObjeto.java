package ids.androidsong.object;

import java.util.ArrayList;

/**
 * Created by Alan on 22/07/2015.
 */
public class grupoObjeto implements iGrupos{
    public String type;
    public String title;
    public ArrayList<itemObjeto> items;

    public grupoObjeto(){
        items = new ArrayList<itemObjeto>();
    }

    @Override
    public itemObjeto[] getItems() {
        return items.toArray(new itemObjeto[items.size()]) ;
    }

    @Override
    public itemObjeto[] getAllItems() {
        return getItems();
    }

    @Override
    public String getType(){
        return type;
    }
}
