package ids.androidsong.help;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ids.androidsong.R;
import ids.androidsong.object.cancionDrive;
import ids.androidsong.object.driveStatus;
import ids.androidsong.object.opciones;
import ids.androidsong.object.cancion;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ALAN on 05/11/2017.
 * Clase que gestiona la busqueda y carga de canciones desde disco
 */

public class sincronizar implements EasyPermissions.PermissionCallbacks {

    public static final String defaultPath = App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SongsFolder);
    private boolean existeLocal;
    boolean existeStatus;
    private final Activity con;
    private final GoogleAccountCredential mCredential;
    private final ProgressDialog mProgress;
    private final TextView syncLog;
    private TextView confLog;
    private com.google.api.services.drive.Drive mService = null;
    private final Map<String, String> carpetasDriveId = new HashMap<>();
    private String carpetaPrincipal;

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Drive API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { DriveScopes.DRIVE };

    public sincronizar(Activity context){
        con = context;
        syncLog = con.findViewById(R.id.sincronizar_log_sync);
        mCredential = GoogleAccountCredential.usingOAuth2(
                con.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mProgress = new ProgressDialog(con);
        mProgress.setMessage("Conectando con Google Drive");
    }

    private Drive getDriveService(){
        if (mService == null) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.drive.Drive.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("AndroidSong")
                    .build();
        }
        return mService;
    }



    //Métodos del algoritmo de sincronización
    private String crearCarpeta(String nombre) throws IOException {
        return createFolder(carpetasDriveId.get("Principal"),nombre);
    }

    public void sincronizarEnBackground(){
        new sincronizarBackground().execute();
    }

    private String getFolder(String parent, String path)  throws IOException {
        String folderId;
        String folder;
        String subFolders;
        if (path.contains("/")) {
            folder = path.substring(0, path.indexOf("/"));
            subFolders = path.substring(path.indexOf("/") + 1);
        } else {
            folder = path;
            subFolders = null;
        }
        if (parent == null) parent = "root";
        carpetaPrincipal = folder;
        List<File> fullResult = getFullResult(getDriveService().files().list()
                .setQ("title='" + folder + "' and trashed=false and mimeType='application/vnd.google-apps.folder' and '" + parent + "' in parents")
                .setFields("nextPageToken, items(id, title)"));
        if (fullResult.size() > 0) {
            folderId = fullResult.get(0).getId();
            if (subFolders != null)
                folderId = getFolder(folderId, subFolders);
        } else {
            folderId = createFolder(parent,path);
        }
        return folderId;
    }

    private String createFolder(String parent, String path)  throws IOException {
        String folderId;
        String folder;
        String subFolders;
        if (path.contains("/")) {
            folder = path.substring(0, path.indexOf("/"));
            subFolders = path.substring(path.indexOf("/") + 1);
        } else {
            folder = path;
            subFolders = null;
        }
        if (parent == null) parent = "root";
        carpetaPrincipal = folder;
        File fileMetadata = new File();
        fileMetadata.setTitle(folder);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(
                new ParentReference().setId(parent)));
        File file = getDriveService().files().insert(fileMetadata)
                .setFields("id")
                .execute();
        folderId = file.getId();
        if (subFolders != null)
            folderId = createFolder(folderId,subFolders);
        return folderId;
    }

    //Tarea asincrónica para envolver la sincronización
    private class sincronizarBackground extends AsyncTask<Void, String[], String> {

        private Exception mLastError = null;

        sincronizarBackground() {
            getDriveService();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                sincronizar();
                return "\nSincronización completa.";
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getSongsFolder() throws Exception{
            String folder = getFolder(null,new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH,defaultPath));
            carpetasDriveId.put("Principal",folder);
            List<File> fullResult = getFullResult(getDriveService().files().list()
                    .setQ("trashed=false and mimeType = 'application/vnd.google-apps.folder' and '" + folder + "' in parents")
                    .setFields("nextPageToken, items(id, title, mimeType)"));
            if (fullResult.size() > 0)
                for (File item: fullResult) {
                    String nombreCarpeta = item.getTitle();
                    carpetasDriveId.put(nombreCarpeta, item.getId());
                }
            publishProgress(getLog("sync", "Carpeta correctamente identificada."));
            return folder;
        }

        private void procesarCarpeta(String file) throws IOException {
            String parent;
            if (file == null) parent = "root";
            else parent = file;
            String nombreCarpeta = getDriveService().files().get(parent).execute().getTitle();
            List<File> fullResult = getFullResult(getDriveService().files().list()
                    .setQ("trashed=false and '" + parent + "' in parents")
                    .setFields("nextPageToken, items(id, title, mimeType, modifiedDate, parents)"));
            if (fullResult.size() > 0) {
                for (File item : fullResult) {
                    if (item.getMimeType().equals("application/vnd.google-apps.folder")) {
                        publishProgress(getLog("sync", "\nEncontré la carpeta " + item.getTitle()));
                        procesarCarpeta(item.getId());
                    } else
                        procesarFile(item, nombreCarpeta);
                }
            }
        }

        private void procesarFile(File file, String carpeta) throws IOException {
            cancionDrive cancion = new cancionDrive(file,getDriveService(),carpeta);
            driveStatus status = new driveStatus(cancion.getTitulo(),carpeta);
            status.get();
            if (!existeLocal(status)) {
                nuevoDriveALocal(cancion, status);
                publishProgress(getLog("sync", cancion.getTitulo() + ", Agregada a Android Song"));
            } else {
                if (statusLocalNuevo(status)){
                    resolverConflicto(cancion, status);
                    publishProgress(getLog("sync", cancion.getTitulo() + ", Conflicto detectado"));
                } else {
                    if (statusLocalBorrado(status)){
                        bajaLocalADrive(cancion, status);
                        publishProgress(getLog("sync", cancion.getTitulo() + ", Borrada en Drive"));
                    } else {
                        if (statusDriveModificado(cancion, status)){
                            if (statusLocalModificado(status)){
                                resolverConflicto(cancion,status);
                                publishProgress(getLog("sync", cancion.getTitulo() + ", Conflicto detectado"));
                            } else {
                                actualizarDriveALocal(cancion, status);
                                publishProgress(getLog("sync", cancion.getTitulo() + ", Versión Local actualizada"));
                            }
                        } else {
                            if (statusLocalModificado(status)){
                                actualizarLocalADrive(carpeta, cancion, status);
                                publishProgress(getLog("sync", cancion.getTitulo() + ", Versión de Drive actualizada"));
                            } else {
                                status.marcarProcesado();
                            }
                        }
                    }
                }
            }
        }

        private void procesarDriveBorrado() {
            ArrayList<driveStatus> statuses = new driveStatus().getBajasDrive();
            if (statuses.size() > 0) {
                for (driveStatus status : statuses) {
                    bajaDriveALocal(status);
                    publishProgress(getLog("sync", status.getTitulo() + ", Eliminada de Android Song"));
                }
            }
        }

        private void procesarNuevosLocalADrive() throws IOException {
            ArrayList<driveStatus> statuses = new driveStatus().getNuevos();
            if (statuses.size() > 0) {
                for (driveStatus status : statuses) {
                nuevoLocalADrive(status);
                publishProgress(getLog("sync", status.getTitulo() + ", Subida a Drive"));
                }
            }
        }

        private void procesarBajaSimultanea() {
            ArrayList<driveStatus> statuses = new driveStatus().getBajasLocal();
            if (statuses.size() > 0) {
                for (driveStatus status : statuses) {
                    bajaDriveYLocal(status);
                }
            }
        }

        private void sincronizar() throws Exception {
            String folder = getSongsFolder();
            new driveStatus().borrarProcesado();
            procesarCarpeta(folder);
            App.closeDB();
            procesarNuevosLocalADrive();
            App.closeDB();
            procesarBajaSimultanea();
            App.closeDB();
            procesarDriveBorrado();
            App.closeDB();
        }

        @Override
        protected void onPreExecute() {
            syncLog.setText("");
            mProgress.show();
        }

        @Override
        protected void onProgressUpdate (String[]... values){
            if (values[0][0].equals("sync")) syncLog.setText(
                    String.format("%s\n%s", syncLog.getText(), values[0][1]));
            else confLog.setText(
                    String.format("%s\n%s", confLog.getText(), values[0][1]));
        }

        private String[] getLog(String tipo, String mensaje){
            String[] progreso = new String[2];
            progreso[0] = tipo;
            progreso[1] = mensaje;
            return progreso;
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null) {
                syncLog.setText(R.string.Error_con_Drive);
            } else {
                syncLog.setText(String.format("%s%s", syncLog.getText(), output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                    syncLog.setText(String.format("The following error occurred:\n%s", mLastError.getMessage()));
            } else {
                syncLog.setText(String.format("%s\nRequest cancelled.", syncLog.getText()));
            }
        }
    }

    @NonNull
    private List<File> getFullResult(Drive.Files.List request) throws IOException {
        List<File> fullResult = new ArrayList<>();
        do {
            FileList result = request.execute();
            fullResult.addAll(result.getItems());
            request.setPageToken(result.getNextPageToken());
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);
        return fullResult;
    }

    private void bajaDriveALocal(driveStatus status) {
        status.getItem().baja();
        status.setLocalDT(null);
        status.modificacion();
        status.marcarProcesado();
    }

    private void nuevoLocalADrive(driveStatus status) throws IOException {
        cancionDrive cancion = new cancionDrive(status.getItem());
        cancion.setDriveService(getDriveService());
        cancion.altaDrive(buscarCarpetaId(status));
        status.setDriveDT(cancion.getDriveFile().getModifiedDate().toString());
        status.modificacion();
        status.marcarProcesado();
    }

    private void bajaDriveYLocal(driveStatus status) {
        status.marcarProcesado();
    }

    private void bajaLocalADrive(cancionDrive cancion, driveStatus status) throws IOException {
        cancion.bajaDrive();
        status.marcarProcesado();
    }

    private void actualizarLocalADrive(String carpeta, cancionDrive cancion, driveStatus status) throws IOException {
        String currentParent = buscarCarpetaId(status);
        cancion.copiarDatos(status.getItem());
        cancion.modificarDrive(currentParent);
        status.setDriveDT(cancion.getDriveFile().getModifiedDate().toString());
        status.setLocalDT(status.getItem().getFechaModificacion());
        status.modificacion();
        status.marcarProcesado();
    }

    private String buscarCarpetaId(driveStatus status) throws IOException {
        String currentParent;
        if (!carpetasDriveId.containsKey(status.getItem().getCarpeta())){
            currentParent = crearCarpeta(status.getItem().getCarpeta());
        } else {
            currentParent = carpetasDriveId.get(status.getItem().getCarpeta());
        }
        return currentParent;
    }

    private void actualizarDriveALocal(cancionDrive cancion, driveStatus status) {
        cancion.fill();
        cancion.modificarContenido();
        cancion.modificarAtributos();
        status.setLocalDT(cancion.getFechaModificacion());
        status.setDriveDT(cancion.getDriveFile().getModifiedDate().toString());
        status.modificacion();
        status.marcarProcesado();
    }

    private boolean statusLocalModificado(driveStatus status) {
        return !status.getItem().getFechaModificacion().equals(status.getLocalDT());
    }

    private boolean statusDriveModificado(cancionDrive cancion, driveStatus status) {
        return !cancion.getDriveFile().getModifiedDate().toString().equals(status.getDriveDT());
    }

    private void resolverConflicto(cancionDrive cancion, driveStatus status) {
        cancion cancionLocal = new cancion(status.getItemId());
        cancionLocal.fill();
        cancionLocal.setTitulo(cancionLocal.getTitulo() + " (Conflicto)");
        status.setTitulo(cancionLocal.getTitulo());
        cancionLocal.modificacion();
        status.setDriveDT(null);
        status.setLocalDT(cancionLocal.getFechaModificacion());
        status.modificacion();
        status.marcarProcesado();
        nuevoDriveALocal(cancion,status);
    }

    private boolean statusLocalNuevo(driveStatus status) {
        return status.getDriveDT() == null;
    }

    private boolean existeLocal(driveStatus status) {
        return status.getItemId() != 0;
    }

    private boolean statusLocalBorrado(driveStatus status) {
        return status.getLocalDT() == null;
    }

    private void nuevoDriveALocal(cancionDrive cancion, driveStatus status) {
        cancion.fill();
        cancion.alta();
        status.setItem(cancion);
        status.setDriveDT(cancion.getDriveFile().getModifiedDate().toString());
        status.setLocalDT(cancion.getFechaModificacion());
        status.alta();
        status.marcarProcesado();
    }

    //Métodos del flujo de autenticación
    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                con, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = con.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getDriveConnection();
            } else {
                // Start a dialog from which the user can choose an account
                con.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    con,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void getDriveConnection() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            syncLog.setText(R.string.Error_sin_red);
        } else {
            new MakeRequestTask().execute();
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    syncLog.setText(con.getString(R.string.Error_Google_Services));
                } else {
                    getDriveConnection();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                con.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getDriveConnection();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getDriveConnection();
                }
                break;
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(con);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(con);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                con,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        con.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
        getDriveConnection();
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        chooseAccount();
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * An asynchronous task that handles the Drive API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private Exception mLastError = null;

        MakeRequestTask() {
            getDriveService();
        }

        /**
         * Background task to call Drive API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of up to 10 file names and IDs.
         * @return List of Strings describing files, or an empty list if no files
         *         found.
         * @throws IOException En caso de fallar la descarga.
         */
        private String getDataFromApi() throws IOException {
            String fileInfo = "";
            FileList result = mService.files().list()
                    .setMaxResults(1)
                    .execute();
            List<File> files = result.getItems();
            if (files != null) {
                fileInfo = String.format("%s\n", files.get(0).getTitle());
            }
            return fileInfo;
        }


        @Override
        protected void onPreExecute() {
            syncLog.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null) {
                syncLog.setText(R.string.Error_con_Drive);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    con.startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            sincronizar.REQUEST_AUTHORIZATION);
                } else {
                    syncLog.setText(String.format("The following error occurred:\n%s",
                            mLastError.getMessage()));
                }
            } else {
                syncLog.setText(R.string.Conexion_cancelada);
            }
        }
    }
}
