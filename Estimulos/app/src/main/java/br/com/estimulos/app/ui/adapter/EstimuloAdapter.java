package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.dominio.Estimulo;

/**
 * Created by caioc_000 on 25/02/2016.
 */
public class EstimuloAdapter extends BaseAdapter {

    public static final int GRID_ADAPTER = R.layout.grid_item_estimulo;
    public static final int LIST_ADAPTER = R.layout.list_item_estimulo;
    private static final int THUMBNAIL_SIZE = 100;

    private Context context;
    List<Estimulo> estimulos;
    int tipoAdapter;
    private static LayoutInflater inflater = null;

    public EstimuloAdapter(Context context, List<Estimulo> estimulos, int tipoAdapter){
        this.context = context;
        this.estimulos = estimulos;
        this.tipoAdapter = tipoAdapter;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return estimulos.size();
    }

    @Override
    public Object getItem(int position) {
        return estimulos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){

            view = inflater.inflate(tipoAdapter, null);
        }

        final TextView nameText = (TextView) view.findViewById(R.id.tvNmEstimulo);
        final TextView categoria = (TextView) view.findViewById(R.id.tvNmCategoriaEst);
        final ImageView imgEstimulo = (ImageView) view.findViewById(R.id.imageEstimulo);

        final Estimulo estimulo = estimulos.get(position);

        view.setTag(estimulo);


        if(tipoAdapter == GRID_ADAPTER)
            nameText.setText(estimulo.getCategoria().getNome());
        else
            nameText.setText(estimulo.getNome());

        // Testar se encontrou o textView da categoria (significa que eh para o listView)
        if(categoria != null) {
            categoria.setText(estimulo.getCategoria().getNome());

        }

        if(estimulo.getImagem() != null) {
            Util.carregarBitmap(context, estimulo.getImagem().getUri(), imgEstimulo);

        }else {
            // TODO: adicionar um img padrao do sistema
        }

        return view;
    }
}
