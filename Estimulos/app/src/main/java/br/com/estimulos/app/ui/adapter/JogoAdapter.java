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
import br.com.estimulos.dominio.Jogo;

/**
 * Created by Giovani on 29/02/2016.
 */
public class JogoAdapter extends BaseAdapter {

    /**
     */
    // Constantes
    public static final int GRID_ADAPTER = R.layout.grid_item_jogos;
    public static final int LIST_ADAPTER = R.layout.list_item_jogos;

    /**
     */
    // Variaveis
    Context context;
    List<Jogo> jogos;
    int tipoAdapter;
    private static LayoutInflater inflater = null;

    int finalHeight, finalWidth;

    public JogoAdapter(Context context, List<Jogo> jogos, int tipoAdapter){
        this.context = context;
        this.jogos = jogos;
        this.tipoAdapter = tipoAdapter;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return jogos.size();
    }

    @Override
    public Object getItem(int position) {
        return jogos.get(position);
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

        TextView nameText = (TextView) view.findViewById(R.id.tvTema);
        TextView pacienteText = (TextView) view.findViewById(R.id.tvNmPaciente);
        ImageView imgJogo = (ImageView) view.findViewById(R.id.imageJogo);

        Jogo jogo = jogos.get(position);

        if(jogo.getImagem() != null) {
            Util.carregarBitmap(context, jogo.getImagem().getUri(), imgJogo);
        }

        nameText.setText(jogo.getTema());
        pacienteText.setText(jogo.getPaciente().getNome());

        view.setTag(jogo.getID());


        return view;
    }


}
