package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;
import ids.androidsong.help.alert;
import ids.androidsong.help.androidSongSyncService;
import ids.androidsong.help.permisos;
import ids.androidsong.help.sincronizar;
import ids.androidsong.object.cancion;
import ids.androidsong.object.coleccion;
import ids.androidsong.object.item;
import ids.androidsong.object.opciones;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context con;
    private NavigationView navigationView;
    private int itemId;
    private View recyclerView;
    sincronizar sincBl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permisos.SolicitarCuenta(this);
        con = this;
        App.SetContext(con);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                abrirCanciones();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.favoritos_lista);
        assert recyclerView != null;

        new DBTask().execute();

        sincBl = new sincronizar(this);
        startService(new Intent(con,androidSongSyncService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sincBl.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void leerOpciones() {
        Menu menu=navigationView.getMenu();
        Switch switchAcordes =
                menu.findItem(R.id.switch_acordes).getActionView().findViewById(R.id.mostrar_acordes_switch);
        try {
            boolean value = (new opciones()).getBool(aSDbContract.Opciones.OPT_NAME_MOSTRARACORDES);
            switchAcordes.setChecked(value);
        } catch (Exception e) {
            alert.SimpleErrorAlert(con,e.getMessage());
        }
        switchAcordes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                opciones opcion = new opciones(aSDbContract.Opciones.OPT_NAME_MOSTRARACORDES,Boolean.toString(isChecked));
                try {
                    opcion.modificacion();
                } catch (Exception e) {
                    alert.SimpleErrorAlert(con,e.getMessage());
                }
            }
        });
    }

    private void abrirCanciones() {
        Intent intent = new Intent(con,cancionLista.class);
        startActivity(intent);
    }

    private void abrirCancionNueva() {
        Intent intent = new Intent(con,nuevaCancion.class);
        startActivity(intent);
    }

    private void abrirImportador() {
        Intent intent = new Intent(con,importador.class);
        startActivity(intent);
    }

    private void abrirSincronizador() {
        Intent intent = new Intent(con,sincronizador.class);
         startActivity(intent);
    }

    private void abrirPapelera() {
        Intent intent = new Intent(con,papelera.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_songs) {
            abrirCanciones();
        } else if (id == R.id.nav_newSong) {
            abrirCancionNueva();
        } else if (id == R.id.nav_import) {
            abrirImportador();
        } else if (id == R.id.nav_sync) {
            abrirSincronizador();
        } else if (id == R.id.nav_trash) {
            abrirPapelera();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Inicializa la lista
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(
                (new coleccion(0)).getItems());
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<principal.SimpleItemRecyclerViewAdapter.ViewHolder> {

        //Guarda el array para referencia y linkeo
        private final List<item> canciones;

        public SimpleItemRecyclerViewAdapter(List<item> items) {
            super();
            canciones = items;
        }

        @Override
        public principal.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    //Layout de la lista
                    .inflate(R.layout.favoritos_lista, parent, false);
            return new principal.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        //Une las partes del objeto a la vista
        public void onBindViewHolder(final principal.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
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
                    borrarFavorito();
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
                iTitulo = view.findViewById(R.id.favoritos_lista_item_titulo);
                iCarpeta = view.findViewById(R.id.favoritos_lista_item_carpeta);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + iTitulo.getText() + "'";
            }
        }

        public void abrirCancion() {
            Context context = con;
            Intent intent = new Intent(context, cancionDetalle.class);
            intent.putExtra(cancionDetalleFragment.ARG_ITEM_ID, itemId);

            context.startActivity(intent);
        }

        private void borrarFavorito(){
            final coleccion favoritos = new coleccion(0);
            final cancion cancion = new cancion(itemId);
            cancion.fill();
            alert.SimpleAlert(con,
                    new alert.SimpleRunnable(){
                        @Override
                        public void run(){
                            favoritos.removeItem(itemId);
                            setupRecyclerView((RecyclerView) recyclerView);
                            Snackbar.make(recyclerView, "Removido de Favoritos", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    "Remover " + cancion.getTitulo() + " de la lista de favoritos");
        }
    }

    private class DBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                aSDbHelper helper = new aSDbHelper(con);
                helper.openWriteDataBase();
                helper.currentDB.close();
            } catch (Exception e) {
                Log.e("Error",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //TODO:Mover a un AsyncTask
            leerOpciones();
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }
}
