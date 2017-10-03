package ids.androidsong.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.aSDbHelper;

public class principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Context con = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_principal);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void abrirCanciones() {
        Intent intent = new Intent(con,cancionMusicoListActivity.class);
        startActivity(intent);
    }

    private void abrirCancionNueva() {
        Intent intent = new Intent(con,nuevaCancion.class);
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
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void testDBread(View view){
        aSDbHelper helper = new aSDbHelper(con);
        try {
            helper.createDataBase();
            helper.openWriteDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView v = (TextView)findViewById(R.id.testview);
        ContentValues value = new ContentValues();
        value.put(aSDbContract.Carpetas.COLUMN_NAME_ID,1);
        value.put(aSDbContract.Carpetas.COLUMN_NAME_NOMBRE,"Coros");
        long id = helper.currentDB.insert(aSDbContract.Carpetas.TABLE_NAME,null,value);
        v.setText(String.valueOf(id));
        String[] projection = {
                aSDbContract.Carpetas.COLUMN_NAME_NOMBRE
        };
        Cursor c = helper.currentDB.query(
                aSDbContract.Carpetas.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                "id > 0",                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        v.setText(c.getString(c.getColumnIndex(aSDbContract.Carpetas.COLUMN_NAME_NOMBRE)));
        c.close();
    }*/
}
