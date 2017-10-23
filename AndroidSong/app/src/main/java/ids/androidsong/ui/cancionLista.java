package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import ids.androidsong.R;

import ids.androidsong.object.cancion;
import ids.androidsong.object.carpeta;
import ids.androidsong.object.item;
import ids.androidsong.ui.dummy.DummyContent;

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
public class cancionLista extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public int itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion_lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemId == 0) {
                    Snackbar.make(view, "Seleccione una canción primero", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                Intent intent = new Intent(getApplication().getBaseContext(),FullscreenCancion.class);
                intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID,itemId);
                startActivity(intent);
                }
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Spinner spinner = (Spinner)findViewById(R.id.cancion_lista_carpeta);
        String[] carpetas = (new carpeta()).get().toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                carpetas);
        spinner.setAdapter(spinnerArrayAdapter);

        View recyclerView = findViewById(R.id.cancion_lista);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.cancionmusico_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter((new cancion()).get()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //Guarda el array para referencia y linkeo
        private final List<item> canciones;

        public SimpleItemRecyclerViewAdapter(List<item> items) {
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
            holder.mIdView.setText(Integer.toString(canciones.get(position).getId()));
            holder.mContentView.setText(canciones.get(position).getTitle());

            // Accion al hacer clic, depende de la pantalla
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        itemId = holder.mItem.getId();
                        Bundle arguments = new Bundle();
                        arguments.putInt(cancionDetalleFragment.ARG_ITEM_ID,
                                holder.mItem.getId());
                        cancionDetalleFragment fragment = new cancionDetalleFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.cancionmusico_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, cancionDetalle.class);
                        intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
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
            public final TextView mIdView;
            public final TextView mContentView;
            public item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
