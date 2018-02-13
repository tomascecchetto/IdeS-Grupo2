package ids.androidsong.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.atributo;

public class listaAtributos extends RecyclerView.Adapter<listaAtributos.ViewHolder>
{
    Activity contexto;
    private final ArrayList<atributo> lista;

    public listaAtributos(ArrayList<atributo> atributo) {
        super();
        lista = atributo;
    }

    @Override
    public listaAtributos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                //Layout de la lista
                .inflate(R.layout.atributos_lista_item, parent, false);
        return new listaAtributos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listaAtributos.ViewHolder holder, int position) {
        holder.atributo = lista.get(position);
        holder.nombre.setText(lista.get(position).getNombre());
        holder.valor.setText(lista.get(position).getValor());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public atributo atributo;
        public final TextView nombre;
        public final TextView valor;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nombre = view.findViewById(R.id.atributo_nombre);
            valor = view.findViewById(R.id.atributo_valor);
        }
    }
}