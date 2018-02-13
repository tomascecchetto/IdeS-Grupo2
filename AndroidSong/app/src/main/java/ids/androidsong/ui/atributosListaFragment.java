package ids.androidsong.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ids.androidsong.R;
import ids.androidsong.adapter.listaAtributos;
import ids.androidsong.object.cancion;

/**
 * A placeholder fragment containing a simple view.
 */
public class atributosListaFragment extends Fragment {

    private cancion cancion;
    private int itemId;

    public atributosListaFragment() {
        super();
    }

    public void setItemId(int i) {
        this.itemId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (itemId > 0) {
            cancion = new cancion(itemId);
            cancion.fill();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.atributos_lista, container, false);
        if (itemId > 0) {
            RecyclerView atributos = root.findViewById(R.id.lista_atributos);
            atributos.setAdapter(new listaAtributos(cancion.getAtributos()));
        }
        return root;
    }


}
