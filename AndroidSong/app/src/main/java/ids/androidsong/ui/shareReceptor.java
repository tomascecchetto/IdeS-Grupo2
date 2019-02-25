package ids.androidsong.ui;

import static ids.androidsong.help.validar.LongitudMaxima;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ids.androidsong.R;
import ids.androidsong.help.App;
import ids.androidsong.help.Enum;
import ids.androidsong.help.alert;
import ids.androidsong.object.cancionShare;
import ids.androidsong.object.carpeta;

public class shareReceptor extends AppCompatActivity {

    Context con = this;
    cancionShare cancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_receptor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        App.SetContext(this);

        getCancionFromIntent();

        setupCarpetas();

        setupView();
    }

    private void getCancionFromIntent() {
        Intent intent = getIntent();
        Uri returnUri = intent.getData();
        Cursor returnCursor =
                getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String title = returnCursor.getString(nameIndex);
        title = title.substring(0,title.indexOf("."));
        returnCursor.close();
        cancion = new cancionShare(title,"");
        try {
            cancion.fill(returnUri);
        } catch (Exception e) {
            alert.SimpleErrorAlert(this,
                    "Error al cargar archivo, no es una canción de AndroidSong.");
        }
    }

    private void setupView(){
        TextView titulo = findViewById(R.id.cancion_pre_titulo);
        TextView autor = findViewById(R.id.cancion_pre_autor);
        titulo.setText(cancion.getTitulo());
        autor.setText(cancion.getAtributo(Enum.atributo.autor.toString()).getValor());
    }

    private void setupCarpetas() {
        Spinner spinner = findViewById(R.id.cancion_pre_carpeta);
        final String[] carpetas = (new carpeta()).get().toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                carpetas);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cancion.setCarpeta(carpetas[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
        cancion.setCarpeta(carpetas[0]);
    }

    public void agregarCarpeta(View view){
        final EditText input = new EditText(con);
        input.setHint("Nombre de la carpeta");
        alert.TextViewAlert(con,
                input,
                new alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                           if (text.length() > 0) {
                                (new carpeta(text)).alta();
                                setupCarpetas();
                            } else {
                                throw new Exception("El nombre no puede estar vacío");
                            }
                        }
                    },
                "Nueva carpeta:");
    }

    public void previsualizarCancion(View view){
        Intent preview = new Intent(this,cancionPre.class);
        preview.putExtra(cancionPreFragment.ARG_ITEM_ID,getIntent().getData());
        startActivity(preview);
    }

    public void importarCancion(View view){
        if (cancion.existeCancion()){
            alert.SimpleAlert(this,
                    new alert.SimpleRunnable() {
                        @Override
                        public void run() {
                            cancion.modificarContenido();
                            cancion.modificarAtributos();
                        }
                    },
                    "La canción ya existe en esa carpeta. ¿Sobreescribir?");
        } else {
            cancion.alta();
        }
        onBackPressed();
    }
}
