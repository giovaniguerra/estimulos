package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.dominio.Nivel;

/**
 * Created by caioc_000 on 13/03/2016.
 */
public class NivelAdapter extends BaseAdapter {

    private List<Nivel> niveis;
    private static LayoutInflater inflater = null;
    private Context context;

    public NivelAdapter(Context context, List<Nivel> nivel) {

        this.niveis = nivel;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return niveis.size();
    }

    @Override
    public Object getItem(int position) {
        return niveis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item_nivel, null);
        }

//        TextView nameText = (TextView) view.findViewById(R.id.tvNmEstimulo);
        ImageView imgNivel = (ImageView) view.findViewById(R.id.imageEstimulo);

        Nivel nivel = niveis.get(position);

        if (nivel.getUriImagem() != null) {
            Util.carregarBitmap(context, nivel.getUriImagem(), imgNivel);
        }

//        nameText.setText("");
        view.setTag(nivel);

        return view;
    }
}