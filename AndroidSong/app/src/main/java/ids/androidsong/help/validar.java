package ids.androidsong.help;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.*;

import ids.androidsong.R;

/**
 * Created by Alan on 20/2/2018.
 * Clase que encapsula los métodos genéricos de validación para interfaz
 */

public class validar {

    public static boolean LongitudMaxima(EditText view, int limite, TextView error) {
        if (view.getText().length() > limite) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(R.color.colorAccent));
            }
            if (error != null) {
                error.setText(String.format(App.GetContext().getString(R.string.Error_longitud_max), limite));
                error.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(view,
                        String.format(App.GetContext().getString(R.string.Error_longitud_max),
                                limite),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(R.color.common_google_signin_btn_text_light_default));
            }
            if (error != null) {
                error.setVisibility(View.GONE);
            }
            return true;
        }
    }

    public static boolean RangoValido (EditText view, int min, int max, TextView error){
        if (view.getText().length() > 0) {
            int valor = Integer.parseInt(view.getText().toString());
            if (valor < min || valor > max) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setTextColor(App.GetContext().getColor(R.color.colorAccent));
                }
                if (error != null) {
                    error.setText(
                            String.format(App.GetContext().getString(R.string.Error_valor_rango),
                                    String.valueOf(min), String.valueOf(max)));
                    error.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(view,
                            String.format(App.GetContext().getString(R.string.Error_valor_rango),
                                    String.valueOf(min), String.valueOf(max)),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return false;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setTextColor(App.GetContext().getColor(
                            R.color.common_google_signin_btn_text_light_default));
                }
                if (error != null) {
                    error.setVisibility(View.GONE);
                }
                return true;
            }
        }
        return true;
    }

    public static boolean RangoValido (EditText view, String rango, TextView error){
        String valor = view.getText().toString();
        if (!rango.contains(valor)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(R.color.colorAccent));
            }
            if (error != null) {
                error.setText(String.format(App.GetContext().getString(R.string.Error_valor_rango),
                        rango.charAt(0), rango.charAt(rango.length() - 2)));
                error.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(view,
                        String.format(App.GetContext().getString(R.string.Error_valor_rango),
                                rango.charAt(0), rango.charAt(rango.length() - 2)),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(R.color.common_google_signin_btn_text_light_default));
            }
            if (error != null) {
                error.setVisibility(View.GONE);
            }
            return true;
        }
    }

    public static boolean ValorNumerico (EditText view, TextView error){
        try {
            if (view.getText().length() > 0)
                Integer.parseInt(view.getText().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(
                        R.color.common_google_signin_btn_text_light_default));
            }
            if (error != null) {
                error.setVisibility(View.GONE);
            }
            return true;
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(App.GetContext().getColor(R.color.colorAccent));
            }
            if (error != null) {
                error.setText(String.format(App.GetContext().getString(R.string.Error_valor_numerico), view.getText().toString()));
                error.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(view,
                        String.format(App.GetContext().getString(R.string.Error_valor_numerico),
                                view.getText().toString()),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            return false;
        }
    }
}
