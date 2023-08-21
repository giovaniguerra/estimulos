package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.dominio.Reforcador;

/**
 * Created by Caio Gustavo on 27/03/2016.
 */
public class VinculaReforcadorAdapter extends BaseAdapter{

    private Context context;
    private List<ReforcadorJogoHelper> reforcadorJogoHelpers;
    private static LayoutInflater inflater = null;

    public VinculaReforcadorAdapter (Context context, List<ReforcadorJogoHelper> reforcadores ){
        this.context = context;
        this.reforcadorJogoHelpers = reforcadores;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return reforcadorJogoHelpers.size();
    }

    @Override
    public Object getItem(int position) {
        return reforcadorJogoHelpers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){

            view = inflater.inflate(R.layout.list_item_vincula_reforcador, null);
        }

        TextView nameText = (TextView) view.findViewById(R.id.tvNmReforcador);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkboxReforcador);

        ReforcadorJogoHelper reforcadorHelper = reforcadorJogoHelpers.get(position);

        checkBox.setChecked(reforcadorHelper.isFlagVinculado());
        checkBox.setTag(reforcadorHelper.getReforcador().getID());

        checkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
        nameText.setText(reforcadorHelper.getReforcador().getNome());

        return view;
    }

}
