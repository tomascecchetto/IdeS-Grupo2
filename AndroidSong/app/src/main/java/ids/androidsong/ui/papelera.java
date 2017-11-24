package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ids.androidsong.R;
import ids.androidsong.help.alert;
import ids.androidsong.object.cancion;
import ids.androidsong.object.coleccion;
import ids.androidsong.object.item;

public class papelera extends AppCompatActivity {

    protected int itemId;
    protected Context con = this;
    protected SimpleItemRecyclerViewAdapter adapter;
    protected View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papelera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciarPapelera(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.papelera_lista);
        assert recyclerView != null;
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    public void vaciarPapelera(View view){
        alert.SimpleAlert(con,
                new alert.SimpleRunnable(){
                    @Override
                    public void run() throws Exception {
                        ArrayList<item> canciones = new cancion().getBajas();
                        if (canciones.size() > 0)
                            for (item cancion:canciones){
                                cancion.eliminar();
                            }
                        setupRecyclerView((RecyclerView) recyclerView);
                        Snackbar.make(recyclerView, Integer.toString(canciones.size()) + " eliminados correctamente.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                },
                "¿Eliminar permanente todos los elementos de la papelera?");

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Inicializa la lista
        adapter = new papelera.SimpleItemRecyclerViewAdapter(new cancion().getBajas());
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<papelera.SimpleItemRecyclerViewAdapter.ViewHolder> {

        //Guarda el array para referencia y linkeo
        private final List<item> canciones;

        public SimpleItemRecyclerViewAdapter(List<item> items) {
            canciones = items;
        }

        @Override
        public papelera.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    //Layout de la lista
                    .inflate(R.layout.papelera_lista, parent, false);
            return new papelera.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        //Une las partes del objeto a la vista
        public void onBindViewHolder(final papelera.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
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
            public item mItem;

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
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            alert.SimpleAlert(con,
                    new alert.SimpleRunnable(){
                        @Override
                        public void run() throws Exception {
                            cancion.restaurar();
                            setupRecyclerView((RecyclerView) recyclerView);
                            Snackbar.make(recyclerView, "Restaurada", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    "¿Restaurar \"" + cancion.getTitulo() + "\" en AndroidSong?");
        }

        private void borrar(){
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            alert.SimpleAlert(con,
                    new alert.SimpleRunnable(){
                        @Override
                        public void run() throws Exception {
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
