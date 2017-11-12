package ids.androidsong.help;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import ids.androidsong.R;

/**
 * Created by ALAN on 05/11/2017.
 * Clase que gestiona la busqueda y carga de canciones desde disco
 */

public class sincronizar {

    public static String defaultPath = "/" +
            App.getContext().getString(R.string.OpenSongFolder) + "/" +
            App.getContext().getString(R.string.SongsFolder);
    boolean existeLocal;
    boolean existeStatus;
    DriveResourceClient client;
    Activity con;

    public sincronizar(DriveResourceClient c, Activity con){
        this.client = c;
        this.con = con;
    }

    private void crearCarpeta(){
        client
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>() {
                    @Override
                    public Task<DriveFolder> then(@NonNull Task<DriveFolder> task)
                            throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("AndroidSongFolder")
                                .setMimeType(DriveFolder.MIME_TYPE)
                                .setStarred(false)
                                .build();
                        return client.createFolder(parentFolder, changeSet);
                    }
                })
                .addOnSuccessListener(con,
                        new OnSuccessListener<DriveFolder>() {
                            @Override
                            public void onSuccess(DriveFolder driveFolder) {

                            }
                        })
                .addOnFailureListener(con, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage() != null) {

                        }
                    }
                });
    }
}
