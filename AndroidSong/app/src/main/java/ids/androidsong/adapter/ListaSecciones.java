package ids.androidsong.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.Seccion;

/**
 * Created by ALAN on 19/10/2017.
 * Pantalla para mostrar el contenido de una canción dividida en secciones.
 */

public class ListaSecciones extends RecyclerView.Adapter<ListaSecciones.ViewHolder>
{
    Activity contexto;
    private final ArrayList<Seccion> lista;
    private final int fontSize;
    private final int capo;

    public ListaSecciones(ArrayList<Seccion> secciones, int c, int f) {
        super();
        lista = secciones;
        this.capo = c;
        this.fontSize = f;
    }

    private String getTitulo(String nombre){
        String title = nombre;
        char caracter = nombre.charAt(0);
        switch (caracter) {
            case 'C':
                title = "Coro:";
                break;
            case 'V':
                title = "Estrofa";
                String number = nombre.substring(1);
                title = title + " " + number + ":";
                break;
            case 'B':
                title = "Puente:";
                break;
            case 'T':
                title = "Marca:";
                break;
            case 'P':
                title = "Pre-coro:";
                break;
        }
        return title;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                //Layout de la lista
                .inflate(R.layout.lista_secciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.seccion = lista.get(position);
        holder.titulo.setText(getTitulo(lista.get(position).getNombre()));
        holder.nombre.setText(lista.get(position).getNombre());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            holder.contenido.setText(Html.fromHtml(lista.get(position).getFormateado(capo), Html.FROM_HTML_MODE_COMPACT));
        else
            holder.contenido.setText(Html.fromHtml(lista.get(position).getFormateado(capo)));
        holder.contenido.setTextSize(2,fontSize);
//        int preferedSize = getTextSize(holder.Seccion,holder.mView);
//        fontSize = preferedSize < fontSize ? preferedSize : fontSize;
//        holder.contenido.setTextSize(2,fontSize);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public Seccion seccion;
        public final TextView titulo;
        public final TextView nombre;
        public final TextView contenido;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titulo = view.findViewById(R.id.seccion_titulo);
            nombre = view.findViewById(R.id.seccion_nombre);
            contenido = view.findViewById(R.id.seccion_contenido);
        }
    }
}