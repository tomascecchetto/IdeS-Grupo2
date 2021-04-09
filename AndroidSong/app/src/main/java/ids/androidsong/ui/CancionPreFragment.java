package ids.androidsong.ui;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ids.androidsong.R;
import ids.androidsong.adapter.ListaSecciones;
import ids.androidsong.help.App;
import ids.androidsong.object.CancionShare;

/**
 * A fragment representing a single Cancion detail screen.
 * This fragment is either contained in a {@link CancionLista}
 * in two-pane mode (on tablets) or a {@link CancionDetalle}
 * on handsets.
 */
public class CancionPreFragment extends Fragment {
    /**
     * The fragment argument representing the Item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "Cancion";
    public static final String ARG_ITEM_CAPO = "capo";
    public static final String ARG_ITEM_FUENTE = "fuente";
    private CollapsingToolbarLayout appBarLayout;
    private int capo = 0;
    private int fontSize = 16;
    private RecyclerView secciones;

    /**
     * The content this fragment is presenting.
     */
    private CancionShare cancion;
    public Uri uri;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CancionPreFragment() {
        super();
        if (appBarLayout != null) {
            appBarLayout.setTitle(cancion.getTitulo());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            getCancionFromUri();
        if (uri != null) {
            Activity activity = this.getActivity();
            appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(cancion.getTitulo());
            }
        }
    }

    private void getCancionFromUri() {
        Cursor returnCursor =
                App.GetContext().getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String title = returnCursor.getString(nameIndex);
        title = title.substring(0,title.indexOf("."));
        returnCursor.close();
        cancion = new CancionShare(title,"");
        cancion.fill(uri);
    }

    public void modificarAcordesSostenido() {
        capo = capo + 1;
        secciones.setAdapter(new ListaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void modificarAcordesBemol() {
        capo = capo - 1;
        secciones.setAdapter(new ListaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void tamanioLetraMenor() {
        fontSize = fontSize - 2;
        secciones.setAdapter(new ListaSecciones(cancion.getSecciones(),capo,fontSize));
    }

    public void tamanioLetraMayor() {
        fontSize = fontSize + 2;
        secciones.setAdapter(new ListaSecciones(cancion.getSecciones(),capo,fontSize));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cancion_pre, container, false);

        // Show the dummy content as text in a TextView.
        secciones = rootView.findViewById(R.id.cancion_detalle_secciones);
        secciones.setAdapter(new ListaSecciones(cancion.getSecciones(),capo,fontSize));
        return rootView;
    }

}
