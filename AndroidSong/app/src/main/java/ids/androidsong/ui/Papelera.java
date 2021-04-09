package ids.androidsong.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import ids.androidsong.R;
import ids.androidsong.help.Alert;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.Item;

public class Papelera extends AppCompatActivity {

    private int itemId;
    private final Context con = this;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papelera);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciarPapelera(view);
            }
        });
        try {
            Assert.assertNotNull(getSupportActionBar());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Error",e.getMessage());
        }
        recyclerView = findViewById(R.id.papelera_lista);
        assert recyclerView != null;
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @SuppressWarnings("unused")
    public void vaciarPapelera(View view){
        Alert.SimpleAlert(con,
                new Alert.SimpleRunnable(){
                    @Override
                    public void run(){
                        ArrayList<Item> canciones = new Cancion().getBajas();
                        if (canciones.size() > 0)
                            for (Item cancion:canciones){
                                cancion.eliminar();
                            }
                        setupRecyclerView((RecyclerView) recyclerView);
                        Snackbar.make(recyclerView, Integer.toString(canciones.size()) + " eliminados correctamente.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                },
                "¿Eliminar permanente todos los elementos de la Papelera?");

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Inicializa la lista
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(
                new Cancion().getBajas());
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<Papelera.SimpleItemRecyclerViewAdapter.ViewHolder> {

        //Guarda el array para referencia y linkeo
        private final List<Item> canciones;

        public SimpleItemRecyclerViewAdapter(List<Item> items) {
            super();
            canciones = items;
        }

        @Override
        public Papelera.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    //Layout de la lista
                    .inflate(R.layout.papelera_lista, parent, false);
            return new Papelera.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        //Une las partes del objeto a la vista
        public void onBindViewHolder(final Papelera.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = canciones.get(position);
            holder.iTitulo.setText(canciones.get(position).getTitulo());
            holder.iCarpeta.setText(canciones.get(position).getCarpeta());

            // Accion al hacer clic, depende de la pantalla
            holder.iRestaurar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemId = holder.mItem.getId();
                    restaurar();
                }
            });

            holder.iEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemId = holder.mItem.getId();
                    borrar();
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
            public final ImageButton iRestaurar;
            public final ImageButton iEliminar;
            public Item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                iTitulo = view.findViewById(R.id.papelera_lista_item_titulo);
                iCarpeta = view.findViewById(R.id.papelera_lista_item_carpeta);
                iRestaurar = view.findViewById(R.id.papelera_lista_item_restaurar);
                iEliminar = view.findViewById(R.id.papelera_lista_item_borrar);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + iTitulo.getText() + "'";
            }
        }

        public void restaurar() {
            final Cancion cancion = new Cancion(itemId);
            cancion.fill();
            Alert.SimpleAlert(con,
                    new Alert.SimpleRunnable(){
                        @Override
                        public void run() {
                            cancion.restaurar();
                            setupRecyclerView((RecyclerView) recyclerView);
                            Snackbar.make(recyclerView, "Restaurada", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    "¿Restaurar \"" + cancion.getTitulo() + "\" en AndroidSong?");
        }

        private void borrar(){
            final Cancion cancion = new Cancion(itemId);
            cancion.fill();
            Alert.SimpleAlert(con,
                    new Alert.SimpleRunnable(){
                        @Override
                        public void run() {
                            cancion.eliminar();
                            setupRecyclerView((RecyclerView) recyclerView);
                            Snackbar.make(recyclerView, "Eliminada", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    "¿Eliminar \"" + cancion.getTitulo() + "\" de forma permanente?");
        }
    }

}
