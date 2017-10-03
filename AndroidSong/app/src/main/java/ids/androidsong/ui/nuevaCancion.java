package ids.androidsong.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ids.androidsong.R;
import ids.androidsong.help.Enum;
import ids.androidsong.object.atributo;
import ids.androidsong.object.cancion;
import ids.androidsong.object.carpeta;

public class nuevaCancion extends AppCompatActivity {

    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cancion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        con = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner)findViewById(R.id.nueva_cancion_carpeta);
        String[] carpetas = (new carpeta()).get().toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                carpetas);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void GuardarCancion(View view) {
        cancion cancion = new cancion();
        Spinner carpeta = (Spinner)findViewById(R.id.nueva_cancion_carpeta);
        cancion.setCarpeta(carpeta.toString());
        EditText titulo = (EditText)findViewById(R.id.nueva_cancion_titulo);
        cancion.setTitle(titulo.getText().toString());
        EditText autor = (EditText)findViewById(R.id.nueva_cancion_autor);
        cancion.getAtributos().add(new atributo(Enum.atributo.autor.toString(),autor.getText().toString()));
        cancion.getAtributos().add(new atributo(Enum.atributo.interprete.toString(),autor.getText().toString()));
        EditText presentacion = (EditText)findViewById(R.id.nueva_cancion_presentacion);
        cancion.getAtributos().add(new atributo(Enum.atributo.presentacion.toString(),presentacion.getText().toString()));
        EditText tono = (EditText)findViewById(R.id.nueva_cancion_tono);
        cancion.getAtributos().add(new atributo(Enum.atributo.tono.toString(),tono.getText().toString()));
        EditText transporte = (EditText)findViewById(R.id.nueva_cancion_transporte);
        cancion.getAtributos().add(new atributo(Enum.atributo.transporte.toString(),transporte.getText().toString()));
        EditText letra = (EditText)findViewById(R.id.nueva_cancion_letra);
        cancion.llenarSecciones(letra.getText().toString());
        //TODO: Agregar llamada a método Guardar
    }

}
