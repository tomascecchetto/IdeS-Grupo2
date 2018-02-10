package ids.androidsong.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ids.androidsong.R;

public class atributos extends AppCompatActivity {

    private Fragment fragment;
    private boolean lista;
    private int itemId;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atributos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemId = getIntent().getIntExtra(cancionDetalleFragment.ARG_ITEM_ID,0);
        con = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista){
                    mostrarEditar();
                    ((FloatingActionButton) view).setImageResource(R.drawable.ic_menu_songs);
                } else {
                    ((atributosEditarFragment) fragment).guardar();
                    View focus = ((Activity)con).getCurrentFocus();
                    if (focus != null){
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                        assert imm != null;
                        imm.hideSoftInputFromWindow(focus.getWindowToken(),0);
                    }
                    mostrarLista();
                    ((FloatingActionButton) view).setImageResource(R.drawable.ic_action_edit);
                }
            }
        });
        try {
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Error",e.getMessage());
        }

        if (savedInstanceState == null) {
            mostrarLista();
        }

    }

    private void mostrarEditar() {
        fragment = new atributosEditarFragment();
        ((atributosEditarFragment)fragment).setItemId(itemId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.atributos_fragment, fragment)
                .commit();
        lista = false;
    }

    private void mostrarLista() {
        fragment = new atributosListaFragment();
        ((atributosListaFragment)fragment).setItemId(itemId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.atributos_fragment, fragment)
                .commit();
        lista = true;
    }

}
