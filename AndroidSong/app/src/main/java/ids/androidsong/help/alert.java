package ids.androidsong.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.TextView;

import ids.androidsong.R;


/**
 * Controla los mensajes, dialogos y alertas
 */
public class alert {

    public static void TextViewAlert (final Context context, final TextView input, final InputRunnable aceptar, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setView(input);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    aceptar.run(input.getText().toString());
                } catch (Exception e) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.ErrorGenerico);
                    alert.setMessage(e.getMessage());
                    alert.show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public static void SpinnerAlert (final Context context, final AdapterView input, final InputRunnable aceptar, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setView(input);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    aceptar.run(input.getSelectedItem().toString());
                } catch (Exception e) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.ErrorGenerico);
                    alert.setMessage(e.getMessage());
                    alert.show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public static void SimpleAlert (final Context context, final SimpleRunnable aceptar, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    aceptar.run();
                } catch (Exception e) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.ErrorGenerico);
                    alert.setMessage(e.getMessage());
                    alert.show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public static void SimpleErrorAlert (final Context context, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.ErrorGenerico);
        alert.setMessage(message);
        alert.show();
    }

    public interface InputRunnable {
        void run(String text) throws Exception;
    }

    public interface SimpleRunnable {
        void run();
    }

}
