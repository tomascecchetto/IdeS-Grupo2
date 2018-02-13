package ids.androidsong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageButton;

import ids.androidsong.R;

/**
 * An activity representing a single cancion detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link cancionLista}.
 */
@SuppressWarnings("unused")
public class cancionDetalle extends AppCompatActivity {

    private cancionDetalleFragment fragment;
    private int capo = 0;
    private int fontSize = 16;
    private boolean opciones = true;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_detalle);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        itemId = getIntent().getIntExtra(cancionDetalleFragment.ARG_ITEM_ID,0);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                itemId = getIntent().getIntExtra(cancionDetalleFragment.ARG_ITEM_ID,0);
                Intent intent = new Intent(getApplication().getBaseContext(),fullscreenCancion.class);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID,itemId);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_CAPO,capo);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_FUENTE,fontSize);
                startActivity(intent);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(cancionDetalleFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(cancionDetalleFragment.ARG_ITEM_ID,0));
            fragment = new cancionDetalleFragment();
            fragment.setArguments(arguments);
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

    public void mostrarEdicion(View view) {
        Intent intent = new Intent(this, editarCancion.class);
        intent.putExtra("ITEM_ID", itemId);
        startActivity(intent);
    }

    public void mostrarAtributos(View view){
        Intent intent = new Intent(this, atributos.class);
        intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID, itemId);
        startActivity(intent);
    }

    private void mostrarOpciones() {
        ImageButton modAcordesSostenido = findViewById(R.id.cancion_detalle_sharp);
        ImageButton modAcordesBemol = findViewById(R.id.cancion_detalle_flat);
        ImageButton tamanioLetraMenor = findViewById(R.id.cancion_detalle_menor);
        ImageButton tamanioLetraMayor = findViewById(R.id.cancion_detalle_mayor);
        ImageButton mostrarEdicion = findViewById(R.id.cancion_detalle_editar);
        ImageButton mostrarAtributos = findViewById(R.id.cancion_detalle_info);
        if (opciones){
            modAcordesSostenido.setVisibility(View.GONE);
            modAcordesBemol.setVisibility(View.GONE);
            tamanioLetraMenor.setVisibility(View.GONE);
            tamanioLetraMayor.setVisibility(View.GONE);
            mostrarEdicion.setVisibility(View.GONE);
            mostrarAtributos.setVisibility(View.GONE);
            opciones = false;
        } else {
            modAcordesSostenido.setVisibility(View.VISIBLE);
            modAcordesBemol.setVisibility(View.VISIBLE);
            tamanioLetraMenor.setVisibility(View.VISIBLE);
            tamanioLetraMayor.setVisibility(View.VISIBLE);
            mostrarEdicion.setVisibility(View.VISIBLE);
            mostrarAtributos.setVisibility(View.VISIBLE);
            opciones = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, cancionLista.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
