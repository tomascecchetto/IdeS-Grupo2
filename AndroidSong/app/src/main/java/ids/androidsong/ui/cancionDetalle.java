package ids.androidsong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import ids.androidsong.R;
import ids.androidsong.adapter.listaSecciones;

/**
 * An activity representing a single cancionMusico detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link cancionLista}.
 */
public class cancionDetalle extends AppCompatActivity {

    private cancionDetalleFragment fragment;
    private int capo = 0;
    private int fontSize = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                int id = getIntent().getIntExtra(cancionDetalleFragment.ARG_ITEM_ID,0);
                Intent intent = new Intent(getApplication().getBaseContext(),FullscreenCancion.class);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID,id);
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
