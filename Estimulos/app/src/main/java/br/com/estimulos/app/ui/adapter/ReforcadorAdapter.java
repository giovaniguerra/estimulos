package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.dominio.Reforcador;

/**
 * Created by Caio Gustavo on 25/03/2016.
 */
public class ReforcadorAdapter extends BaseAdapter {

    private Context context;
    private List<Reforcador> reforcadors;
    private static LayoutInflater inflater = null;

    public ReforcadorAdapter(Context context, List<Reforcador> estimulos){
        this.context = context;
        this.reforcadors = estimulos;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return reforcadors.size();
    }

    @Override
    public Object getItem(int position) {
        return reforcadors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            // layout do item
            view = inflater.inflate( R.layout.list_item_reforcador ,null);
        }

        TextView nameText = (TextView) view.findViewById(R.id.tvNmReforcador);

        final Reforcador reforcador = reforcadors.get(position);

        nameText.setText(reforcador.getNome());
        view.setTag(reforcador);

        return view;
    }
}
