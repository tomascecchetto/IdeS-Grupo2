package ids.androidsong.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.alert;
import ids.androidsong.help.importar;
import ids.androidsong.help.permisos;
import ids.androidsong.object.cancionXml;
import ids.androidsong.object.opciones;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

public class importador extends AppCompatActivity {

    TextView path;
    Context con;
    boolean sobreescritura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        con = this;
        path = findViewById(R.id.importar_text_path);
        ToggleButton sobreescribir = findViewById(R.id.importar_button_override);
        permisos.solicitarLocal(this);
        try {
            path.setText(new opciones().getString(aSDbContract.Opciones.OPT_NAME_IMPORTPATH,importar.defaultPath));
            sobreescritura=new opciones().getBool(aSDbContract.Opciones.OPT_NAME_IMPORTOVERRIDE);
            sobreescribir.setChecked(sobreescritura);
            sobreescribir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        new opciones(aSDbContract.Opciones.OPT_NAME_IMPORTOVERRIDE,Boolean.toString(isChecked)).modificacion();
                        sobreescritura = isChecked;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    new ImportarCanciones().execute();
                Snackbar.make(view, "Importación iniciada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    public void changePath(View view){
        final EditText input = new EditText(con);
        input.setText(path.getText());
        alert.TextViewAlert(
                con,
                input,
                new alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        path.setText(text);
                        new opciones(aSDbContract.Opciones.OPT_NAME_IMPORTPATH,text).modificacion();
                    }
                },
                "Ingrese el path de búsqueda"
        );
    }

    private class ImportarCanciones extends AsyncTask<Void, String[], Void>{

        private TextView syncLog;
        private TextView confLog;

        @Override
        protected void onPreExecute() {
            syncLog = findViewById(R.id.importar_log_sync);
            confLog = findViewById(R.id.importar_log_conflict);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<cancionXml> canciones = (new importar()).getCanciones();
            for (cancionXml cancion :
                    canciones) {
                isCancelled();
                try {
                    cancion.fill();
                    if (cancion.getSecciones() != null) {
                        if (cancion.getId() > 0) {
                            if (sobreescritura) {
                                cancion.modificacion();
                                publishProgress(getLog("sync", cancion.getTitulo() + " sobreescrita correctamente."));
                            } else {
                                publishProgress(getLog("conf", "Conflicto con " + cancion.getTitulo() + ", exceptuado."));
                            }
                        } else {
                            cancion.alta();
                            publishProgress(getLog("sync", cancion.getTitulo() + " importada correctamente."));
                        }
                    }
                } catch (Exception E) {
                    publishProgress(getLog("conf", "Conflicto con " + cancion.getTitulo() + ": " + E.getMessage()));
                }
            }
            return null;
        }

        private String[] getLog(String tipo, String mensaje){
            String[] progreso = new String[2];
            progreso[0] = tipo;
            progreso[1] = mensaje;
            return progreso;
        }

        @Override
        protected void onProgressUpdate (String[]... values){
            if (values[0][0].equals("sync")) syncLog.setText(
                    String.format("%s\n%s", syncLog.getText(), values[0][1]));
            else confLog.setText(
                    String.format("%s\n%s", confLog.getText(), values[0][1]));
        }

        @Override
        protected void onPostExecute(Void result) {
            Snackbar.make(syncLog, "Importación completa", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        protected void onCancelled() {
            Snackbar.make(syncLog, "Importación cancelada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
