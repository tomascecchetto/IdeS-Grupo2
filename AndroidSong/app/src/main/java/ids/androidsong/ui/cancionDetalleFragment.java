package ids.androidsong.ui;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import ids.androidsong.R;
import ids.androidsong.adapter.listaSecciones;
import ids.androidsong.help.App;
import ids.androidsong.object.cancion;
import ids.androidsong.ui.dummy.DummyContent;

/**
 * A fragment representing a single cancionMusico detail screen.
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
    private CollapsingToolbarLayout appBarLayout;

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
            appBarLayout.setTitle(cancion.getTitle());
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
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cancion.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cancion_detalle, container, false);

        // Show the dummy content as text in a TextView.
        RecyclerView secciones = (RecyclerView) rootView.findViewById(R.id.cancion_detalle_secciones);
        secciones.setAdapter(new listaSecciones(cancion.getSecciones()));
        return rootView;
    }
}
