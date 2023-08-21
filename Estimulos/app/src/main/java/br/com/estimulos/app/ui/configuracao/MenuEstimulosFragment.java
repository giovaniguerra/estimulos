package br.com.estimulos.app.ui.configuracao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class MenuEstimulosFragment extends Fragment {

    public  static final String TAG_ESTIMULO = "ESTIMULO";
    private List<Estimulo> estimulos;
    private EstimuloAdapter estimuloAdapter;
    private EstimuloDAO estimuloDAO;
    public static final String OPERACAO_JOGO = "OPERACAO";

    /**
     * Enumeracao para identificar se a operacao Cadastro novo jogo ou Editar jogo
     */
    public enum Operacoes {
        CADASTRO(1),
        EDITAR(2);

        private int operacao;

        private Operacoes(int operacao) { this.operacao=operacao; }

        public int getOperacao() { return this.operacao; }
    }

    public MenuEstimulosFragment(){}

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

        registerForContextMenu(listView);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_estimulos, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Estimulo estimulo;
        Intent intent;
        switch (item.getItemId()){
            case R.id.novo_estimulo_id:
                intent = new Intent(getActivity().getApplicationContext(), ConfDadosEstimuloActivity.class);
                intent.putExtra(OPERACAO_JOGO, Operacoes.CADASTRO.getOperacao());
                startActivity(intent);

                return true;
            case R.id.editar_estimulo_id:
                estimulo = estimulos.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfDadosEstimuloActivity.class);
                intent.putExtra(TAG_ESTIMULO, estimulo);
                intent.putExtra(OPERACAO_JOGO, Operacoes.EDITAR.getOperacao());
                startActivity(intent);

                return true;

            default:
        }
        return super.onContextItemSelected(item);
    }
}
