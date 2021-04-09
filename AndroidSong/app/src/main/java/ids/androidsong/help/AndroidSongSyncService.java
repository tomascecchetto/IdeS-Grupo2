package ids.androidsong.help;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Alan on 4/12/2018.
 */

public class AndroidSongSyncService extends IntentService {

    public AndroidSongSyncService() {
        super("AndroidSongSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (true) {
            Log.i("SyncUpdate","Servicio de Sync iniciado");
            Sincronizar sincBL = new Sincronizar((AppCompatActivity) App.GetContext());
            sincBL.getDriveConnection();
            sincBL.sincronizarEnBackground(false, false, null);
            Log.i("SyncUpdate","Servicio de Sync en suspensión");
            try {
                Thread.sleep(60 * 60 * 4 * 1000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }
    }
}
