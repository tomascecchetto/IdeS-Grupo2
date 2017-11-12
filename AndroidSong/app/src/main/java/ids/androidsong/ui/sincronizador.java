package ids.androidsong.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.function.Consumer;

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
    DriveClient driveClient;
    DriveResourceClient driveResourceClient;
    GoogleSignInClient mGoogleSignInClient;
    //GoogleApiClient mGoogleApiClient;

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
            sobreescritura=new opciones().getBool(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE);
            sobreescribir.setChecked(sobreescritura);
            sobreescribir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        new opciones(aSDbContract.Opciones.OPT_NAME_SYNCOVERRIDE,Boolean.toString(isChecked)).modificacion();
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
                testMethod();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(),0);
    }

    private void testMethod(){
        driveResourceClient
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<MetadataBuffer>>() {
                    @Override
                    public Task<MetadataBuffer> then(@NonNull Task<DriveFolder> task)
                            throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        return driveResourceClient.listChildren(parentFolder);
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<MetadataBuffer>() {
                            @Override
                            public void onSuccess(MetadataBuffer metadata) {
                                TextView view = findViewById(R.id.sincronizar_log_sync);
                                try {
                                    Iterator<Metadata> iterator = metadata.iterator();
                                    while (iterator.hasNext()) {
                                        Metadata next = iterator.next();
                                        if (!next.isTrashed())
                                            view.setText(view.getText() + next.getTitle() + "\n");
                                    }
                                } catch (Exception e) {
                                    view.setText(view.getText() + "SHIT!!!!!" + "\n" + e.getMessage());
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage() != null) {
                            TextView view = findViewById(R.id.sincronizar_log_conflict);
                            view.setText("Carpeta creada\n" + e.getMessage());
                            //finish();
                        }
                    }
                });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Use the last signed in account here since it already have a Drive scope.
            driveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));
            // Build a drive resource client.
            driveResourceClient =
                    Drive.getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));
        }
    }

    private void updateViewWithGoogleSignInAccountTask(Task<GoogleSignInAccount> task) {
        task.addOnSuccessListener(
                new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        // Build a drive client.
                        driveClient = Drive.getDriveClient(getApplicationContext(), googleSignInAccount);
                        // Build a drive resource client.
                        driveResourceClient =
                                Drive.getDriveResourceClient(getApplicationContext(), googleSignInAccount);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
    }

    //OLD conection
    /*@Override
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/
}
