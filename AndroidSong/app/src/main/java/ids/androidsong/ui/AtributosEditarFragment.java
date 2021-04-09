package ids.androidsong.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.Atributo;
import ids.androidsong.object.Cancion;

public class AtributosEditarFragment extends Fragment {

    private Cancion cancion;
    private int itemId;
    private View root;

    public AtributosEditarFragment() {
        super();
    }

    public void setItemId(int i) {
        this.itemId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (itemId > 0) {
            cancion = new Cancion(itemId);
            cancion.fill();
        }
    }

    public void guardar(){
        TextView view = root.findViewById(R.id.atributos_editar_text);
        cancion.setAtributos(new ArrayList<Atributo>());
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