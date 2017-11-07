package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import ids.androidsong.R;
import ids.androidsong.help.aSDbContract;
import ids.androidsong.help.alert;
import ids.androidsong.help.permisos;
import ids.androidsong.help.sincronizar;
import ids.androidsong.object.opciones;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED;

public class sincronizador extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView path;
    Context con;
    boolean sobreescritura;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        con = this;
        path = (TextView) findViewById(R.id.sincronizar_text_path);
        ToggleButton sobreescribir = (ToggleButton) findViewById(R.id.sincronizar_button_override);
        permisos.solicitar(this);
        try {
            path.setText(new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH, sincronizar.defaultPath));
            sobreescritura=new opciones().getBool(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case RESOLUTION_REQUIRED:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
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
                        path.setText((CharSequence) text);
                        new opciones(aSDbContract.Opciones.OPT_NAME_SYNCPATH,text).modificacion();
                    }
                },
                "Ingrese el path de búsqueda"
        );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
