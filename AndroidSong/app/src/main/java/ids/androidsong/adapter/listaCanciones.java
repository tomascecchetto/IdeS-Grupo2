package ids.androidsong.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ids.androidsong.R;
import ids.androidsong.object.cancionCabecera;

/**
 * Created by ALAN on 07/08/2017.
 */

public class listaCanciones  extends ArrayAdapter
{
    Activity contexto;
    cancionCabecera[] lista;


    public listaCanciones (Activity context, cancionCabecera[] pLista)
    {
        super(context, R.layout.lista_nivel_dos,pLista);
        contexto = context;
        lista = pLista;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View item = convertView;
        cancionesHolder holder;

        if (item == null)
        {
            LayoutInflater inflater = contexto.getLayoutInflater();
            item = inflater.inflate(R.layout.lista_nivel_dos, null);

            holder = new cancionesHolder();
            holder.titulo = (TextView) item.findViewById(R.id.titulo);
            holder.carpeta = (TextView) item.findViewById(R.id.carpeta);

            item.setTag(holder);
        }
        else
        {
            holder = (cancionesHolder)item.getTag();
        }

        holder.titulo.setText(lista[position].getTitulo());
        holder.carpeta.setText(lista[position].getCarpeta());

        return(item);
    }
}

class cancionesHolder {
    TextView titulo;
    TextView carpeta;
}