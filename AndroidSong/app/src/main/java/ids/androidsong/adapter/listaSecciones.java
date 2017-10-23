package ids.androidsong.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ids.androidsong.R;
import ids.androidsong.object.cancion;
import ids.androidsong.object.cancionCabecera;
import ids.androidsong.object.item;
import ids.androidsong.object.seccion;
import ids.androidsong.ui.cancionLista;

/**
 * Created by ALAN on 19/10/2017.
 */

public class listaSecciones  extends RecyclerView.Adapter<listaSecciones.ViewHolder>
{
    Activity contexto;
    ArrayList<seccion> lista;

    public listaSecciones(ArrayList<seccion> secciones) {
        lista = secciones;
    }

/*    public listaSecciones (Activity context, ArrayList<seccion> canciones)
    {
        super(context, R.layout.lista_secciones,canciones);
        contexto = context;
        lista = canciones;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View item = convertView;
        seccionesHolder holder;

        if (item == null)
        {
            LayoutInflater inflater = contexto.getLayoutInflater();
            item = inflater.inflate(R.layout.lista_secciones, null);

            holder = new seccionesHolder();
            holder.titulo = (TextView) item.findViewById(R.id.seccion_titulo);
            holder.nombre = (TextView) item.findViewById(R.id.seccion_nombre);
            holder.contenido = (TextView) item.findViewById(R.id.seccion_contenido);

            item.setTag(holder);
        }
        else
        {
            holder = (seccionesHolder)item.getTag();
        }

        holder.titulo.setText(getTitulo(lista.get(position).getNombre()));
        holder.nombre.setText(lista.get(position).getNombre());
        holder.contenido.setText(lista.get(position).getContenido());

        return(item);
    }*/

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
        holder.contenido.setText(lista.get(position).getContenido());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public seccion seccion;
        public final TextView titulo;
        public final TextView nombre;
        public final TextView contenido;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titulo = (TextView) view.findViewById(R.id.seccion_titulo);
            nombre = (TextView) view.findViewById(R.id.seccion_nombre);
            contenido = (TextView) view.findViewById(R.id.seccion_contenido);
        }
    }
}