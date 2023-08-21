package br.com.estimulos.app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by caioc_000 on 08/03/2016.
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> objetos;
    private static LayoutInflater inflater = null;

    public SpinnerAdapter(Context context, List<String> objetos){
        this.context = context;
        this.objetos = objetos;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objetos.size();
    }

    @Override
    public Object getItem(int position) {
        return objetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView view = (TextView) convertView;
//        if(view == null){
//            view = (TextView) inflater.inflate(android.R.layout.simple_spinner_item, null);
//        }

        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(objetos.get(position));

        return view;

    }

}
