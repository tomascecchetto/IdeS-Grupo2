package ids.androidsong.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import ids.androidsong.R;

import ids.androidsong.help.alert;
import ids.androidsong.object.cancion;
import ids.androidsong.object.carpeta;
import ids.androidsong.object.coleccion;
import ids.androidsong.object.item;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of Canciones. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link cancionDetalle} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("unused")
public class cancionLista extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Context con;
    private int itemId = 0;
    private SimpleItemRecyclerViewAdapter adapter;
    private View recyclerView;
    private Spinner spinnerCarpetas;
    private cancionDetalleFragment fragment;
    private int capo = 0;
    private int fontSize = 16;
    private boolean opciones = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_lista);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemId == 0)
                    Snackbar.make(view, "Seleccione una canción primero", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                Intent intent = new Intent(getApplication().getBaseContext(),fullscreenCancion.class);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID,itemId);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_CAPO,capo);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_FUENTE,fontSize);
                startActivity(intent);
                }
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupCarpetas();

        recyclerView = findViewById(R.id.cancion_lista);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        registerForContextMenu(recyclerView);

        mostrarOpciones();
        if (findViewById(R.id.cancionmusico_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        } else {
            findViewById(R.id.cancion_detalle_opciones).setVisibility(View.GONE);
        }
        con=this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cancion_lista, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            adapter = new SimpleItemRecyclerViewAdapter((new cancion()).buscar(query));
            ((RecyclerView)recyclerView).setAdapter(null);
            ((RecyclerView)recyclerView).setAdapter(adapter);
        }
    }

    private void setupCarpetas() {
        spinnerCarpetas = findViewById(R.id.cancion_lista_carpeta);
        ArrayList<String> carpetas = (new carpeta()).get();
        carpetas.add("Todas");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                carpetas);
        spinnerCarpetas.setAdapter(spinnerArrayAdapter);
        spinnerCarpetas.setSelection(carpetas.size()-1);
        spinnerCarpetas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Inicializa la lista
        adapter = new SimpleItemRecyclerViewAdapter((new cancion()).get(spinnerCarpetas.getSelectedItem().toString()));
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //Guarda el array para referencia y linkeo
        private final List<item> canciones;

        public SimpleItemRecyclerViewAdapter(List<item> items) {
            super();
            canciones = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    //Layout de la lista
                    .inflate(R.layout.cancion_lista_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        //Une las partes del objeto a la vista
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = canciones.get(position);
            holder.iTitulo.setText(canciones.get(position).getTitulo());
            holder.iCarpeta.setText(canciones.get(position).getCarpeta());

            // Accion al hacer clic, depende de la pantalla
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemId = holder.mItem.getId();
                    abrirCancion();
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemId = holder.mItem.getId();
                    final cancion cancion = new cancion(itemId);
                    cancion.fill();
                    final CharSequence[] items = {"Abrir", "Añadir a Favoritos", "Copiar a carpeta", "Mover a carpeta", "Renombrar canción", "Eliminar canción"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(con);

                    builder.setTitle(cancion.getTitulo());
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                //TODO: Completar acción de Favorito
                                case 0:
                                    abrirCancion();
                                    break;
                                case 1:
                                    agregarFavorito();
                                    break;
                                case 2:
                                    copiarCancion();
                                    break;
                                case 3:
                                    moverCancion();
                                    break;
                                case 4:
                                    renombrarCancion();
                                    break;
                                case 5:
                                    borrarCancion();
                                    break;
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return canciones.size();
        }

        // Define la vista
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView iTitulo;
            public final TextView iCarpeta;
            public item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                iTitulo = view.findViewById(R.id.cancion_lista_item_titulo);
                iCarpeta = view.findViewById(R.id.cancion_lista_item_carpeta);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + iTitulo.getText() + "'";
            }
        }

        public void abrirCancion() {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(cancionDetalleFragment.ARG_ITEM_ID,
                        itemId);
                fragment = new cancionDetalleFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cancionmusico_detail_container, fragment)
                        .commit();
            } else {
                Context context = con;
                Intent intent = new Intent(context, cancionDetalle.class);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID, itemId);

                context.startActivity(intent);
            }
        }

        public void copiarCancion() {
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            final Spinner input = new Spinner(con);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(con,android.R.layout.simple_spinner_item,(new carpeta()).get().toArray(new String[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            input.setAdapter(adapter);
            alert.SpinnerAlert(con,
                    input,
                    new alert.InputRunnable() {
                        @Override
                        public void run(String text) throws Exception {
                            cancion.setCarpeta(text);
                            cancion.alta();
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    },
                    "Copiar " + cancion.getTitulo() + " a la carpeta:");
        }

        public void moverCancion() {
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            final Spinner input = new Spinner(con);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(con,android.R.layout.simple_spinner_item,(new carpeta()).get().toArray(new String[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            input.setAdapter(adapter);
            alert.SpinnerAlert(con,
                    input,
                    new alert.InputRunnable() {
                        @Override
                        public void run(String text) throws Exception {
                            cancion.setCarpeta(text);
                            cancion.modificacion();
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    },
                    "Mover " + cancion.getTitulo() + " a la carpeta:");
        }

        void renombrarCancion() {
            final EditText input = new EditText(con);
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            input.setText(cancion.getTitulo());
            alert.TextViewAlert(con,
                    input,
                    new alert.InputRunnable() {
                        @Override
                        public void run(String text) throws Exception {
                            if (text.length() > 0) {
                                cancion.setTitulo(text);
                                cancion.modificacion();
                                setupRecyclerView((RecyclerView) recyclerView);
                            } else {
                                throw new Exception("El título no puede estar vacío");
                            }
                        }
                    },
                    "Renombrar " + cancion.getTitulo());

        }

        private void borrarCancion(){
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            alert.SimpleAlert(con,
                    new alert.SimpleRunnable(){
                        @Override
                        public void run() {
                            cancion.baja();
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    },
                "Eliminar " + cancion.getTitulo());
        }

        private void agregarFavorito() {
            new coleccion(0).addItem(itemId);
            Snackbar.make(recyclerView, "Añadido a Favoritos", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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

    public void mostrarOpciones(View view) {
        mostrarOpciones();
    }

    public void mostrarEdicion(View view) {
        Intent intent = new Intent(con, editarCancion.class);
        intent.putExtra("ITEM_ID", itemId);
        startActivity(intent);
    }

    public void mostrarAtributos(View view){
        Intent intent = new Intent(con, atributos.class);
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
}
