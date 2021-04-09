package ids.androidsong.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import ids.androidsong.R;
import ids.androidsong.object.CancionShare;

/**
 * An activity representing a single Cancion detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * Item details are presented side-by-side with a list of items
 * in a {@link CancionLista}.
 */
@SuppressWarnings("unused")
public class CancionPre extends AppCompatActivity {

    private CancionPreFragment fragment;
    private int capo = 0;
    private int fontSize = 16;
    private boolean opciones = true;
    private CancionShare cancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_pre);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Show the Up button in the action bar.

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
        if(savedInstanceState ==null)

    {
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        fragment = new CancionPreFragment();
        fragment.uri = getIntent().getParcelableExtra(CancionPreFragment.ARG_ITEM_ID);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cancionmusico_detail_container, fragment)
                .commit();
    }

    mostrarOpciones();
}

    public void modificarAcordesSostenido(View view) {
        capo = capo + 1;
        fragment.modificarAcordesSostenido();
    }

    public void modificarAcordesBemol(View view) {
        capo = capo - 1;
        fragment.modificarAcordesBemol();
    }

    public void tamanioLetraMenor(View view) {
        fontSize = fontSize - 2;
        fragment.tamanioLetraMenor();
    }

    public void tamanioLetraMayor(View view) {
        fontSize = fontSize + 2;
        fragment.tamanioLetraMayor();
    }

    public void mostrarOpciones(View view) {
        mostrarOpciones();
    }

    private void mostrarOpciones() {
        ImageButton modAcordesSostenido = findViewById(R.id.cancion_detalle_sharp);
        ImageButton modAcordesBemol = findViewById(R.id.cancion_detalle_flat);
        ImageButton tamanioLetraMenor = findViewById(R.id.cancion_detalle_menor);
        ImageButton tamanioLetraMayor = findViewById(R.id.cancion_detalle_mayor);

        if (opciones){
            modAcordesSostenido.setVisibility(View.GONE);
            modAcordesBemol.setVisibility(View.GONE);
            tamanioLetraMenor.setVisibility(View.GONE);
            tamanioLetraMayor.setVisibility(View.GONE);

            opciones = false;
        } else {
            modAcordesSostenido.setVisibility(View.VISIBLE);
            modAcordesBemol.setVisibility(View.VISIBLE);
            tamanioLetraMenor.setVisibility(View.VISIBLE);
            tamanioLetraMayor.setVisibility(View.VISIBLE);

            opciones = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
