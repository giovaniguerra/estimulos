package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.util.Util;

/**
 * Created by caioc_000 on 08/03/2016.
 */
public class VinculaEstimuloAdapter extends BaseAdapter {

    Context context;
    List<EstimuloJogoHelper> estimulosHelper;
    private static LayoutInflater inflater = null;

    public VinculaEstimuloAdapter(Context context, List<EstimuloJogoHelper> estimulos){
        this.context = context;
        this.estimulosHelper = estimulos;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return estimulosHelper.size();
    }

    @Override
    public Object getItem(int position) {
        return estimulosHelper.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){

            view = inflater.inflate(R.layout.list_item_vincula_estimulo, null);
        }

        TextView nameText = (TextView) view.findViewById(R.id.tvNmEstimulo);
        ImageView imgEstimulo = (ImageView) view.findViewById(R.id.imageEstimulo);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkboxEstimulo);

        EstimuloJogoHelper estimuloJogoHelper = estimulosHelper.get(position);

        if(estimuloJogoHelper.getEstimulo().getImagem() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(estimuloJogoHelper.getEstimulo().getImagem().getUri(), options);
            imgEstimulo.setImageBitmap(bm);

//            Util.carregarBitmap(context, estimuloJogoHelper.getEstimulo().getImagem().getUri(), imgEstimulo);
        }

        checkBox.setChecked(estimuloJogoHelper.isFlagVinculado());
        checkBox.setTag(estimuloJogoHelper.getEstimulo().getID());

        checkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
        nameText.setText(estimuloJogoHelper.getEstimulo().getNome());

        return view;
    }

    public void updateResults(List<EstimuloJogoHelper> results){
        estimulosHelper = results;
        notifyDataSetChanged();
    }
}
