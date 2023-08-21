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
import br.com.estimulos.dominio.Fase;

/**
 * Created by Giovani on 11/03/2016.
 */
public class FaseAdapter extends BaseAdapter {

    private List<Fase> fases;
    private Context context;
    private static LayoutInflater inflater = null;

    public FaseAdapter(Context context, List<Fase> fases){
        this.fases = fases;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return fases.size();
    }

    @Override
    public Object getItem(int position) {
        return fases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.grid_item_estimulo, null);
        }

        TextView nameText = (TextView) view.findViewById(R.id.tvNmEstimulo);
        ImageView imgFase = (ImageView) view.findViewById(R.id.imageEstimulo);

        Fase fase = fases.get(position);

        if(fase.getUriImagem() != null) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            Bitmap bm = BitmapFactory.decodeFile(fase.getUriImagem(), options);
//            imgFase.setImageBitmap(bm);
            Util.carregarBitmap(context, fase.getUriImagem(), imgFase);
        }

        nameText.setText(fase.getNome());
        view.setTag(fase.getNomeClasseHerdade());

        return view;
    }

}
