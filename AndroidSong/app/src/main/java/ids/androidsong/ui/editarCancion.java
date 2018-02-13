package ids.androidsong.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.alert;
import ids.androidsong.object.cancion;
import ids.androidsong.object.seccion;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

@SuppressWarnings("unused")
public class editarCancion extends AppCompatActivity {
    private cancion cancion;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cancion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        con = this;

        cancion = new cancion(getIntent().getIntExtra("ITEM_ID",0));
        cancion.fill();
        setTitle("Editar \"" + cancion.getTitulo() + "\"");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccion();
            }
        });
        EditText letra = findViewById(R.id.editar_cancion_letra);
        letra.setText(cancion.getLetra());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void seccion(View view){
        seccion();
    }

    private void seccion() {
        EditText input = new EditText(App.GetContext());
        input.setText("C");
        input.setSelection(1);
        alert.TextViewAlert(con,
                input,
                new alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        String nombre;
                        if (text.length() > 0)
                            nombre = "[" + text + "]\n";
                        else
                            nombre = "||\n";
                        EditText et = findViewById(R.id.editar_cancion_letra);
                        int start = Math.max(et.getSelectionStart(), 0);
                        int end = Math.max(et.getSelectionEnd(), 0);
                        et.getText().replace(Math.min(start, end), Math.max(start, end),
                                nombre, 0, nombre.length());
                    }
                },
                "Nueva Seccion");
    }

    public void guardarCambios(View view){
        EditText letra = findViewById(R.id.editar_cancion_letra);
        cancion.setSecciones(new ArrayList<seccion>());
        cancion.llenarSecciones(letra.getText().toString());
        cancion.modificarContenido();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
