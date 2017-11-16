package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
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

import ids.androidsong.R;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.alert;
import ids.androidsong.help.permisos;
import ids.androidsong.help.sincronizar;
import ids.androidsong.object.opciones;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

public class sincronizador extends AppCompatActivity /*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {

    private TextView path;
    private Context con;
    private boolean sobreescritura;
    private sincronizar sincBl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        permisos.solicitarCuenta(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        con = this;
        path = (TextView) findViewById(R.id.sincronizar_text_path);
        ToggleButton sobreescribir = (ToggleButton) findViewById(R.id.sincronizar_button_override);
        try {
            path.setText(new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH, sincronizar.defaultPath));
            sobreescritura = new opciones().getBool(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE);
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSincronizacion();
            }
        });
        sincBl = new sincronizar(this);
        sincBl.getDriveConnection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sincBl.onActivityResult(requestCode, resultCode, data);
    }

    private void iniciarSincronizacion() {
        sincBl.sincronizarEnBackground();
    }


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
                        path.setText((CharSequence) text);
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