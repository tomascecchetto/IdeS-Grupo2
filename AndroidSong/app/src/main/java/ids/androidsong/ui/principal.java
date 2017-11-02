package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.alert;
import ids.androidsong.help.directorios;
import ids.androidsong.help.permisos;
import ids.androidsong.object.cancion;
import ids.androidsong.object.cancionCabecera;
import ids.androidsong.object.cancionXml;
import ids.androidsong.object.opciones;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Context con = this;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        App.setContext(con);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                abrirCanciones();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        leerOpciones();
        permisos.solicitar(this);
    }

    private void leerOpciones() {
        Menu menu=navigationView.getMenu();
        Switch switchAcordes =(Switch) MenuItemCompat.getActionView(menu.findItem(R.id.switch_acordes)).findViewById(R.id.mostrar_acordes_switch);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_songs) {
            abrirCanciones();
        } else if (id == R.id.nav_newSong) {
            abrirCancionNueva();
        } else if (id == R.id.nav_import) {
            abrirImportador();
        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void testMethod(){
        cancionXml[] canciones = directorios.generarListaCanciones();
        cancionXml cancion = canciones[8];
        cancion.load();
        cancion.alta();
        int algo = canciones.length;
    }

}
