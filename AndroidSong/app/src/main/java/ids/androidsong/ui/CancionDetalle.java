package ids.androidsong.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.util.List;

import ids.androidsong.R;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.CancionShare;

/**
 * An activity representing a single Cancion detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * Item details are presented side-by-side with a list of items
 * in a {@link CancionLista}.
 */
@SuppressWarnings("unused")
public class CancionDetalle extends AppCompatActivity {

    private CancionDetalleFragment fragment;
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
        itemId = getIntent().getIntExtra(CancionDetalleFragment.ARG_ITEM_ID,0);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                itemId = getIntent().getIntExtra(CancionDetalleFragment.ARG_ITEM_ID,0);
                Intent intent = new Intent(getApplication().getBaseContext(),FullscreenCancion.class);
                intent.putExtra(CancionDetalleFragment.ARG_ITEM_ID,itemId);
                intent.putExtra(CancionDetalleFragment.ARG_ITEM_CAPO,capo);
                intent.putExtra(CancionDetalleFragment.ARG_ITEM_FUENTE,fontSize);
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
            arguments.putInt(CancionDetalleFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(CancionDetalleFragment.ARG_ITEM_ID,0));
            fragment = new CancionDetalleFragment();
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
        Intent intent = new Intent(this, EditarCancion.class);
        intent.putExtra("ITEM_ID", itemId);
        startActivity(intent);
    }

    public void mostrarAtributos(View view){
        Intent intent = new Intent(this, Atributos.class);
        intent.putExtra(CancionDetalleFragment.ARG_ITEM_ID, itemId);
        startActivity(intent);
    }

    public void compartirCancion(View view){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        Cancion cancion = new Cancion(itemId);
        cancion.fill();
        Uri uri = FileProvider.getUriForFile(this,"ids.androidsong.fileprovider",(new CancionShare(cancion)).toXmlForShare());
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Compartiendo canción: "+cancion.getTitulo());

        //la cancion se comparte con el formato "nombre.ids"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,cancion.getTitulo()+".ids");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, cancion.getTitulo());
        startActivity(Intent.createChooser(sharingIntent, "Compartir con:"));
    }

    private void mostrarOpciones() {
        ImageButton modAcordesSostenido = findViewById(R.id.cancion_detalle_sharp);
        ImageButton modAcordesBemol = findViewById(R.id.cancion_detalle_flat);
        ImageButton tamanioLetraMenor = findViewById(R.id.cancion_detalle_menor);
        ImageButton tamanioLetraMayor = findViewById(R.id.cancion_detalle_mayor);
        ImageButton mostrarEdicion = findViewById(R.id.cancion_detalle_editar);
        ImageButton mostrarAtributos = findViewById(R.id.cancion_detalle_info);
        ImageButton compartirCancion = findViewById(R.id.cancion_detalle_share);
        if (opciones){
            modAcordesSostenido.setVisibility(View.GONE);
            modAcordesBemol.setVisibility(View.GONE);
            tamanioLetraMenor.setVisibility(View.GONE);
            tamanioLetraMayor.setVisibility(View.GONE);
            mostrarEdicion.setVisibility(View.GONE);
            mostrarAtributos.setVisibility(View.GONE);
            compartirCancion.setVisibility(View.GONE);
            opciones = false;
        } else {
            modAcordesSostenido.setVisibility(View.VISIBLE);
            modAcordesBemol.setVisibility(View.VISIBLE);
            tamanioLetraMenor.setVisibility(View.VISIBLE);
            tamanioLetraMayor.setVisibility(View.VISIBLE);
            mostrarEdicion.setVisibility(View.VISIBLE);
            mostrarAtributos.setVisibility(View.VISIBLE);
            compartirCancion.setVisibility(View.VISIBLE);
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
            navigateUpTo(new Intent(this, CancionLista.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
