package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ids.androidsong.R;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.alert;
import ids.androidsong.help.sincronizar;
import ids.androidsong.object.opciones;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

import java.util.ArrayList;

public class sincronizador extends AppCompatActivity /*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {

    private TextView path;
    private Context con;
    private boolean sobreescritura;
    private sincronizar sincBl;
    private Spinner spinnerFrecuencia;
    private TextView syncLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        syncLog = findViewById(R.id.sincronizar_log_sync);

        con = this;
        path = findViewById(R.id.sincronizar_text_path);
        /*ToggleButton sobreescribir = findViewById(R.id.sincronizar_button_override);*/
        try {
            path.setText(new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH, sincronizar.DEFAULT_PATH));
            /*sobreescritura = new opciones().getBool(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE);
            sobreescribir.setChecked(sobreescritura);
            sobreescribir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        new opciones(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE, Boolean.toString(isChecked)).modificacion();
                        sobreescritura = isChecked;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupFrecuencia();

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSincronizacion();
            }
        });
        fab.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                forzarSincronizacion();
                return false;
            }
        });
        sincBl = new sincronizar(this);
        sincBl.getDriveConnection();
    }

    private void setupFrecuencia() {
        spinnerFrecuencia = findViewById(R.id.sincronizar_auto_freq);
        String freq = "Diariamente";
        final ArrayList<String> frecuencias = new ArrayList<>();
        frecuencias.add("Diariamente");
        frecuencias.add("Semanalmente");
        frecuencias.add("Mensualmente");
        frecuencias.add("Nunca");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                frecuencias);
        spinnerFrecuencia.setAdapter(spinnerArrayAdapter);
        try {
            freq = new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCFREQUENCE,"Nunca");
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinnerFrecuencia.setSelection(frecuencias.indexOf(freq));
        spinnerFrecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    new opciones(aSDbContract.Opciones.OPT_NAME_SYNCFREQUENCE,frecuencias.get(position)).modificacion();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sincBl.onActivityResult(requestCode, resultCode, data);
    }

    private void iniciarSincronizacion() {
        sincBl.sincronizarEnBackground(syncLog);
    }

    private void forzarSincronizacion() {
        sincBl.sincronizarEnBackground(true,true,syncLog);
    }

    @SuppressWarnings("unused")
    public void changePath(View view) {
        final EditText input = new EditText(con);
        input.setText(path.getText());
        alert.TextViewAlert(
                con,
                input,
                new alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        if (text.charAt(0) == '/')
                            text = text.substring(1);
                        if (text.charAt(text.length()-1) == '/')
                            text = text.substring(0,text.length()-1);
                        path.setText(text);
                        new opciones(aSDbContract.Opciones.OPT_NAME_SYNCPATH, text).modificacion();
                    }
                },
                "Ingrese el path de búsqueda"
        );
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