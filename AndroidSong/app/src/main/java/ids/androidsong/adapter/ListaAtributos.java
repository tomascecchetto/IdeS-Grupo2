package ids.androidsong.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.object.Atributo;

public class ListaAtributos extends RecyclerView.Adapter<ListaAtributos.ViewHolder>
{
    Activity contexto;
    private final ArrayList<Atributo> lista;

    public ListaAtributos(ArrayList<Atributo> atributo) {
        super();
        lista = atributo;
    }

    @Override
    public ListaAtributos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                //Layout de la lista
                .inflate(R.layout.atributos_lista_item, parent, false);
        return new ListaAtributos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListaAtributos.ViewHolder holder, int position) {
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
        public Atributo atributo;
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