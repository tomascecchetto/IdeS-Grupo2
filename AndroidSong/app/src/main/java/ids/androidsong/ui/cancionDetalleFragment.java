package ids.androidsong.ui;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ids.androidsong.R;
import ids.androidsong.adapter.listaSecciones;
import ids.androidsong.object.cancion;

/**
 * A fragment representing a single cancion detail screen.
 * This fragment is either contained in a {@link cancionLista}
 * in two-pane mode (on tablets) or a {@link cancionDetalle}
 * on handsets.
 */
public class cancionDetalleFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_CAPO = "capo";
    public static final String ARG_ITEM_FUENTE = "fuente";
    private CollapsingToolbarLayout appBarLayout;
    private int capo = 0;
    private int fontSize = 16;
    private RecyclerView secciones;

    /**
     * The content this fragment is presenting.
     */
    private cancion cancion;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public cancionDetalleFragment() {
        if (appBarLayout != null) {
            appBarLayout.setTitle(cancion.getTitulo());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            cancion = new cancion(getArguments().getInt(ARG_ITEM_ID));
            cancion.fill();

            Activity activity = this.getActivity();
            appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cancion.getTitulo());
            }
        }
    }

    public void modificarAcordesSostenido() {
        capo = capo + 1;
        secciones.setAdapter(new listaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void modificarAcordesBemol() {
        capo = capo - 1;
        secciones.setAdapter(new listaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void tamanioLetraMenor() {
        fontSize = fontSize - 2;
        secciones.setAdapter(new listaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void tamanioLetraMayor() {
        fontSize = fontSize + 2;
        secciones.setAdapter(new listaSecciones(cancion.getSecciones(),capo,fontSize));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cancion_detalle, container, false);

        // Show the dummy content as text in a TextView.
        secciones = rootView.findViewById(R.id.cancion_detalle_secciones);
        secciones.setAdapter(new listaSecciones(cancion.getSecciones(),capo,fontSize));
        return rootView;
    }

}
