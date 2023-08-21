package br.com.estimulos.app.ui.configuracao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.app.ui.adapter.EstimuloAdapter;
import br.com.estimulos.dominio.Estimulo;

/**
 * Fragmento para exibir a lista de jogos
 * A simple {@link Fragment} subclass.
 * @author Caio Cruz
 *
 */
public class ConfEstimulosFragment extends Fragment {

    private List<Estimulo> estimulos;
    private EstimuloAdapter estimuloAdapter;
    private EstimuloDAO estimuloDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conf_estimulos, container, false);

        estimuloDAO = new EstimuloDAO(BancoDadosHelper.getInstance(getActivity().getApplicationContext()));

        try {
            estimulos = estimuloDAO.consultarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        estimuloAdapter = new EstimuloAdapter(getActivity().getApplicationContext() , estimulos, EstimuloAdapter.LIST_ADAPTER);
        ListView listView = (ListView)rootView.findViewById(R.id.listViewEstimulos);

        listView.setAdapter(estimuloAdapter);

        return rootView;
    }

}
