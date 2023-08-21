package br.com.estimulos.app.ui.configuracao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estimulos.app.R;

public class ConfDadosTerapeutaFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conf_dados_terapeuta, container, false);



        return rootView;
    }
}
