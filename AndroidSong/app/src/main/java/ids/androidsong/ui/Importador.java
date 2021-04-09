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
import ids.androidsong.help.AsdbContract;
import ids.androidsong.help.Alert;
import ids.androidsong.help.Importar;
import ids.androidsong.help.Permisos;
import ids.androidsong.object.CancionXml;
import ids.androidsong.object.Opciones;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

@SuppressWarnings("unused")
public class Importador extends AppCompatActivity {

    private TextView path;
    private Context con;
    private boolean sobreescritura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        con = this;
        path = findViewById(R.id.importar_text_path);
        ToggleButton sobreescribir = findViewById(R.id.importar_button_override);
        Permisos.SolicitarLocal(this);
        try {
            path.setText(new Opciones().getString(AsdbContract.Opciones.OPT_NAME_IMPORTPATH, Importar.DEFAULT_PATH));
            sobreescritura=new Opciones().getBool(AsdbContract.Opciones.OPT_NAME_IMPORTOVERRIDE);
            sobreescribir.setChecked(sobreescritura);
            sobreescribir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        new Opciones(AsdbContract.Opciones.OPT_NAME_IMPORTOVERRIDE,Boolean.toString(isChecked)).modificacion();
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
        Alert.TextViewAlert(
                con,
                input,
                new Alert.InputRunnable() {
                    @Override
                    public void run(String text) throws Exception {
                        path.setText(text);
                        new Opciones(AsdbContract.Opciones.OPT_NAME_IMPORTPATH,text).modificacion();
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
            ArrayList<CancionXml> canciones = (new Importar()).getCanciones();
            for (CancionXml cancion :
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
                } catch (Exception e) {
                    publishProgress(getLog("conf", "Conflicto con " + cancion.getTitulo() + ": " + e.getMessage()));
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
