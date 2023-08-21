package br.com.estimulos.app.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.dominio.EnumObjetoTela;

import static br.com.estimulos.app.R.color.dodgeBlue;
import static br.com.estimulos.app.R.color.orange;

/**
 * Created by Caio Gustavo on 03/04/2016.
 */
public class ListPosicaoAdapter extends BaseAdapter {

    private Context context;
    List<String> estimulos;
    int tipoAdapter;
    private static LayoutInflater inflater = null;

    public ListPosicaoAdapter(Context context, List<String> estimulos){
        this.context = context;
        this.estimulos = estimulos;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){

            view = inflater.inflate(R.layout.list_item_conf_posicao, null);
        }


        final TextView nameText = (TextView) view.findViewById(R.id.tvNmEstimulo);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageEstimulo);

        imageView.setOnClickListener((View.OnClickListener) context);

        final String estimulo = estimulos.get(position);

        if(estimulo.equals("Estímulo Principal")) {

            //noinspection ResourceType
            imageView.setBackground(context.getResources().getDrawable(orange));
            imageView.setTag(R.id.id_estimulo, EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        }

        else if(estimulo.equals("Estímulo Alvo")) {
            //noinspection ResourceType
            imageView.setBackground(context.getResources().getDrawable(dodgeBlue));
            imageView.setTag(R.id.id_estimulo, EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        }
        else{
            imageView.setTag(R.id.id_estimulo, EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        }

        nameText.setText(estimulo);
        return view;
    }
}
