package ids.androidsong.help;

import static java.util.Objects.isNull;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ids.androidsong.R;
import ids.androidsong.object.cancionDrive;
import ids.androidsong.object.driveStatus;
import ids.androidsong.object.opciones;
import ids.androidsong.object.cancion;

/**
 * Created by ALAN on 05/11/2017.
 * Clase que gestiona la busqueda y carga de canciones desde disco
 */

public class sincronizar{

    public static final String
            DEFAULT_PATH = App.GetContext().getString(R.string.OpenSongFolder) + "/" +
            App.GetContext().getString(R.string.SongsFolder);
    private boolean existeLocal;
    boolean existeStatus;
    private final ProgressDialog mProgress;
    private boolean showLog = false;
    private TextView syncLog = null;
    private TextView confLog;
    private final Map<String, String> carpetasDriveId = new HashMap<>();
    private String carpetaPrincipal;
    private com.google.api.services.drive.Drive mService = null;
    private final conectarDrive coneccion;

    private static final String[] SCOPES = { DriveScopes.DRIVE };

    public sincronizar(Activity context) {
        super();
        coneccion = new conectarDrive(context);
        mProgress = new ProgressDialog(context);
    }

    private Drive getDriveService(){
        if (mService == null) {
            mService = coneccion.getDriveService();
        }
        return mService;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        coneccion.onActivityResult(requestCode,resultCode,data);
    }

    public void getDriveConnection() {
        coneccion.getDriveConnection();
    }

    //Métodos del algoritmo de sincronización
    private String crearCarpeta(String nombre) throws IOException {
        return createFolder(carpetasDriveId.get("Principal"),nombre);
    }

    //Hacer Sinc ahora
    public void sincronizarEnBackground(TextView log) {
        sincronizarEnBackground(true,false,log);
    }


    public void sincronizarEnBackground(Boolean ignorarFecha, Boolean ignorarFlag, TextView log) {
        String freq = "";
        String last = "";
        Boolean flag = false;
        if (log != null) {
            showLog = true;
            syncLog = log;
        }

        try {
            freq = new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCFREQUENCE,
                    "Nunca");
            last = new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCLAST, "19900101");
            flag = new opciones().getBool(aSDbContract.Opciones.OPT_NAME_SYNCFLAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag || ignorarFlag) {
            int dias = 0;
            switch (freq) {
                case "Diariamente":
                    dias = 1;
                    break;
                case "Semanalmente":
                    dias = 7;
                    break;
                case "Mensualmente":
                    dias = 30;
                    break;
                case "Nunca":
                    dias = 0;
                    break;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date d = null;
            try {
                d = formatter.parse(last);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar lastDate = Calendar.getInstance();
            lastDate.setTime(d);
            Calendar today = Calendar.getInstance();
            long diff = today.getTimeInMillis() - lastDate.getTimeInMillis();
            long diferencia = diff / (24 * 60 * 60 * 1000); // To force each service run: diferencia = diferencia +2;
            if ((dias != 0 && diferencia >= dias) || ignorarFecha)
                new sincronizarBackground().execute();
        } else {
            if (showLog) {
                syncLog.setText("Sincronización actualmente activa. Espere o mantenga presionado el botón para forzar el inicio.");
            } else {
                Log.i("SyncUpdate","Sincronización actualmente activa. Espere o mantenga presionado el botón para forzar el inicio.");
            }
        }
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
            super();
            getDriveService();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                new opciones(aSDbContract.Opciones.OPT_NAME_SYNCFLAG,Boolean.toString(true)).modificacion();
                Log.i("SyncUpdate","Iniciando sincronización.");
                sincronizar();
                new opciones(aSDbContract.Opciones.OPT_NAME_SYNCFLAG,Boolean.toString(false)).modificacion();
                Log.i("SyncUpdate","Terminando sincronización.");
                String fecha = android.text.format.DateFormat.format("yyyyMMdd", new java.util.Date()).toString();
                new opciones(aSDbContract.Opciones.OPT_NAME_SYNCLAST,fecha).modificacion();
                return "\nSincronización completa.";
            } catch (Exception e) {
                mLastError = e;
                try {
                    new opciones(aSDbContract.Opciones.OPT_NAME_SYNCFLAG,Boolean.toString(false))
                                .modificacion();
                } catch (Exception e1) {
                    Log.e("Error",e1.getMessage());
                }
                cancel(true);
                return null;
            }
        }

        private String getSongsFolder() throws Exception{
            String folder = getFolder(null,new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH,
                    DEFAULT_PATH));
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

        private String getUltimaCarpeta(String path) {
            int i,f;
            f = path.lastIndexOf("/");
            i = path.substring(0,f-1).lastIndexOf("/");
            return path.substring(i+1,f);
        }

        private void procesarCarpeta(String file) throws Exception {
            String parent;
            if (file == null) parent = "root";
            else parent = file;
            String nombreCarpetaDrive = getDriveService().files().get(parent).execute().getTitle();
            String nombreCarpeta = nombreCarpetaDrive.equals(getUltimaCarpeta(new opciones().getString(aSDbContract.Opciones.OPT_NAME_SYNCPATH,
                    DEFAULT_PATH))) ? "Principal" : nombreCarpetaDrive ;
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
            App.CloseDB();
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
            App.CloseDB();
            procesarNuevosLocalADrive();
            App.CloseDB();
            procesarBajaSimultanea();
            App.CloseDB();
            procesarDriveBorrado();
            App.CloseDB();
        }

        @Override
        protected void onPreExecute() {
            if (showLog) {
                syncLog.setText("");
                mProgress.show();
            }
        }

        @Override
        protected void onProgressUpdate (String[]... values){
            if (values[0][0].equals("sync")) if (showLog) syncLog.setText(
                    String.format("%s\n%s", syncLog.getText(), values[0][1]));
            else Log.i("SyncUpdate",values[0][1]);
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
            if (showLog && mProgress.isShowing()) mProgress.hide();
            if (output == null) {
                if (showLog) syncLog.setText(R.string.Error_con_Drive);
            } else {
                if (showLog) syncLog.setText(String.format("%s%s", syncLog.getText(), output));
            }
        }

        @Override
        protected void onCancelled() {
            if (showLog && mProgress.isShowing()) mProgress.hide();
            if (mLastError != null) {
                    if (showLog) syncLog.setText(String.format("The following error occurred:\n%s", mLastError.getMessage()));
            } else {
                if (showLog) syncLog.setText(String.format("%s\nRequest cancelled.", syncLog.getText()));
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
        status.setDriveDT(cancion.getFechaDrive());
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

    @SuppressWarnings("unused")
    private void actualizarLocalADrive(String carpeta, cancionDrive cancion, driveStatus status) throws IOException {
        String currentParent = buscarCarpetaId(status);
        cancion.copiarDatos(status.getItem());
        cancion.modificarDrive(currentParent);
        status.setDriveDT(cancion.getFechaDrive());
        status.setLocalDT(status.getItem().getFechaModificacion());
        status.modificacion();
        status.marcarProcesado();
    }

    private String buscarCarpetaId(driveStatus status) throws IOException {
        String currentParent;
        if (!carpetasDriveId.containsKey(status.getItem().getCarpeta())){
            currentParent = crearCarpeta(status.getItem().getCarpeta());
            carpetasDriveId.put(status.getItem().getCarpeta(),currentParent);
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
        status.setDriveDT(cancion.getFechaDrive());
        status.modificacion();
        status.marcarProcesado();
    }

    private boolean statusLocalModificado(driveStatus status) {
        return !status.getItem().getFechaModificacion().equals(status.getLocalDT());
    }

    private boolean statusDriveModificado(cancionDrive cancion, driveStatus status) {
        return !cancion.getFechaDrive().equals(status.getDriveDT());
    }

    private void resolverConflicto(cancionDrive cancion, driveStatus status) {
        cancion cancionLocal = new cancion(status.getItemId());
        cancionLocal.fill();
        cancionLocal.setTitulo(cancionLocal.getTitulo() + " (Conflicto)");
        status.setTitulo(cancionLocal.getTitulo());
        cancionLocal.modificacion();
        status.setDriveDT(cancion.getFechaDrive());
        status.setLocalDT(cancionLocal.getFechaModificacion());
        status.modificacion();
        status.marcarProcesado();
        driveStatus nuevoStatus = new driveStatus();
        nuevoDriveALocal(cancion,nuevoStatus);
    }

    private boolean statusLocalNuevo(driveStatus status) {
        return (status.getDriveDT() == null || status.getDriveDT().equals("null") || status.getDriveDT().equals(""));
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
        status.setDriveDT(cancion.getFechaDrive());
        status.modificacion();
        status.marcarProcesado();
    }
}
