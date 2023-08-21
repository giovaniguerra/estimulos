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
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.ui.adapter.ReforcadorAdapter;
import br.com.estimulos.app.ui.jogo.ReforcadorStartActivity;
import br.com.estimulos.dominio.Reforcador;

/**
 * Fragmento para exibir a lista de reforçadores
 * A simple {@link Fragment} subclass.
 * @author Caio Cruz
 */
public class MenuReforcadoresFragment extends Fragment{


    public  static final String TAG_REFORCADOR = "ESTIMULO";
    private List<Reforcador> reforcadores;
    private ReforcadorAdapter reforcadorAdapter;
    public static final String OPERACAO_JOGO = "OPERACAO";
    private ReforcadorDAO reforcadorDAO;


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


    public MenuReforcadoresFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reforcadores, container, false);

        reforcadorDAO = new ReforcadorDAO(BancoDadosHelper.getInstance(getActivity()));

        try {
            reforcadores = reforcadorDAO.consultarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        reforcadorAdapter = new ReforcadorAdapter(getActivity().getApplicationContext(), reforcadores);
        ListView listView = (ListView)rootView.findViewById(R.id.listViewReforcadores);
        listView.setAdapter(reforcadorAdapter);

        registerForContextMenu(listView);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_reforcadores, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Reforcador reforcador;
        Intent intent;
        switch (item.getItemId()){
            case R.id.novo_refocador_id:
                intent = new Intent(getActivity().getApplicationContext(), ConfReforcadorActivity.class);
                intent.putExtra(OPERACAO_JOGO, Operacoes.CADASTRO.getOperacao());
                startActivity(intent);

                return true;
            case R.id.editar_reforcador_id:
                reforcador = reforcadores.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfReforcadorActivity.class);
                intent.putExtra(TAG_REFORCADOR, reforcador);
                intent.putExtra(OPERACAO_JOGO, Operacoes.EDITAR.getOperacao());
                startActivity(intent);

                return true;
            case R.id.exibir_reforcador_id:
                reforcador = reforcadores.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ReforcadorStartActivity.class);
                intent.putExtra(ReforcadorStartActivity.REFORCADOR_TAG, reforcador);
                startActivity(intent);

                return true;
            default:
        }
        return super.onContextItemSelected(item);
    }
}
