package ids.androidsong.object;

import android.os.Environment;

import ids.androidsong.R;
import ids.androidsong.help.App;

/**
 * Estilo de la diapositiva
 */
public class estiloObjeto {
    public boolean activo = false;
    public fileCabecera imagen;
    public String fuente;
    public boolean autoTamanio = true;

    public void loadDefault(){
        imagen = new fileCabecera();
        imagen.titulo = "default";
        imagen.path = Environment.getExternalStorageDirectory() + "/" +
                App.getContext().getString(R.string.OpenSongFolder) + "/" +
                App.getContext().getString(R.string.BackgroundsFolder)+"/Cross.jpg";
    }
}
