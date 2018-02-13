package ids.androidsong.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.atributo;
import ids.androidsong.object.cancion;

public class atributosEditarFragment extends Fragment {

    private cancion cancion;
    private int itemId;
    private View root;

    public atributosEditarFragment() {
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

    public void guardar(){
        TextView view = root.findViewById(R.id.atributos_editar_text);
        cancion.setAtributos(new ArrayList<atributo>());
        cancion.llenarAtributos(view.getText().toString());
        cancion.modificarAtributos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.atributos_editar, container, false);
        if (itemId > 0) {
            TextView view = root.findViewById(R.id.atributos_editar_text);
            view.setText(cancion.getAtributosTexto());
        }
        return root;
    }
}