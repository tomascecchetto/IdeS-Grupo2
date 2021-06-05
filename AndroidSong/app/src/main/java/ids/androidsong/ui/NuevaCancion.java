package ids.androidsong.ui;

import static ids.androidsong.help.Validar.LongitudMaxima;
import static ids.androidsong.help.Validar.RangoValido;
import static ids.androidsong.help.Validar.ValorNumerico;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ids.androidsong.R;
import ids.androidsong.help.Enum;
import ids.androidsong.help.Alert;
import ids.androidsong.object.Atributo;
import ids.androidsong.object.Cancion;
import ids.androidsong.object.Carpeta;

public class NuevaCancion extends AppCompatActivity {

    private Context con;
    private EditText titulo;
    private EditText autor;
    private EditText presentacion;
    private EditText tono;
    private EditText transporte;
    private EditText letra;
    private TextView error;
    private  static Map<String,Boolean> inputValids = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cancion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        con = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccion();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        setupCarpetas();
        setupValidaciones();
    }

    private void setupValidaciones() {
        inputValids.clear();
        error = findViewById(R.id.nueva_cancion_validacion);
        error.setVisibility(View.GONE);
        titulo = findViewById(R.id.nueva_cancion_titulo);
        titulo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) inputValids.put("titulo",LongitudMaxima((EditText)view,50, null));
            }
        });
        autor = findViewById(R.id.nueva_cancion_autor);
        autor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) inputValids.put("autor",LongitudMaxima((EditText)view,50, null));
            }
        });
        presentacion = findViewById(R.id.nueva_cancion_presentacion);
        presentacion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) inputValids.put("presentacion",LongitudMaxima((EditText)view,50, null));
            }
        });
        tono = findViewById(R.id.nueva_cancion_tono);
        tono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    Boolean tonoValidate = false;
                    if (tonoValidate=LongitudMaxima((EditText) view, 3, null)){
                        tonoValidate=RangoValido((EditText) view, "A,Bb,B,C,C#,D,Eb,E,F,F#,G,Ab,Am,Bbm,Bm,Cm,C#m,Dm,Ebm,Em,Fm,F#m,Gm,Abm", null);
                        inputValids.put("tono",tonoValidate);
                    }
                }
            }
        });
        transporte = findViewById(R.id.nueva_cancion_transporte);
        transporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    Boolean transporteValidate = false;
                    if (transporteValidate=ValorNumerico((EditText) view, null)){
                        transporteValidate=RangoValido((EditText) view, 1, 12, null);
                        inputValids.put("transporte",transporteValidate);
                    }

                }
            }
        });
        letra = findViewById(R.id.nueva_cancion_letra);
    }

    private void setupCarpetas() {
        Spinner spinner = findViewById(R.id.nueva_cancion_carpeta);
        String[] carpetas = (new Carpeta()).get().toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                carpetas);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void GuardarCancion(View view) {
        if (!inputValids.containsValue(false)){
        Cancion cancion = new Cancion();
        Spinner carpeta = findViewById(R.id.nueva_cancion_carpeta);
        cancion.setCarpeta(carpeta.getSelectedItem().toString());
        cancion.setTitulo(titulo.getText().toString());
        cancion.getAtributos().add(new Atributo(Enum.atributo.autor.toString(),autor.getText().toString()));
        cancion.getAtributos().add(new Atributo(Enum.atributo.interprete.toString(),autor.getText().toString()));
        cancion.getAtributos().add(new Atributo(Enum.atributo.presentacion.toString(),presentacion.getText().toString()));
        cancion.getAtributos().add(new Atributo(Enum.atributo.tono.toString(),tono.getText().toString()));
        cancion.getAtributos().add(new Atributo(Enum.atributo.transporte.toString(),transporte.getText().toString()));
        cancion.llenarSecciones(letra.getText().toString());
        cancion.alta();
        Snackbar.make(view, "Canción " + cancion.getTitulo() + "creada con éxito.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        }else{
            Snackbar.make(view,
                    String.format("No se puede guardar la canción, hay campos invalidos"),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void seccion() {
        EditText input = new EditText(this);
        input.setText("C");
        input.setSelection(1);
        Alert.TextViewAlert(con,
                input,
                new Alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        String nombre;
                        if (text.length() > 0)
                            nombre = "[" + text + "]\n";
                        else
                            nombre = "||\n";
                        EditText et = findViewById(R.id.nueva_cancion_letra);
                        int start = Math.max(et.getSelectionStart(), 0);
                        int end = Math.max(et.getSelectionEnd(), 0);
                        et.getText().replace(Math.min(start, end), Math.max(start, end),
                                nombre, 0, nombre.length());
                    }
                },
                "Nueva Seccion");
    }

    @SuppressWarnings("unused")
    public void agregarCarpeta(View view){
        final EditText input = new EditText(con);
        input.setHint("Nombre de la Carpeta");
        Alert.TextViewAlert(con,
                input,
                new Alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        if (LongitudMaxima((EditText)input,50, error)) {
                            if (text.length() > 0) {
                                (new Carpeta(text)).alta();
                                setupCarpetas();
                            } else {
                                throw new Exception("El nombre no puede estar vacío");
                            }
                        } else {
                            throw new Exception(String.format(getString(R.string.Error_longitud_max),50));
                        }
                    }
                },
                "Nueva Carpeta:");
    }
}
