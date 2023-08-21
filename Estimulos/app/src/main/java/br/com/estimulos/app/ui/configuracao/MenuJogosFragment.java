package br.com.estimulos.app.ui.configuracao;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.ui.adapter.JogoAdapter;
import br.com.estimulos.dominio.Jogo;

/**
 * Tela para listar os jogos do aplicativo
 */
public class MenuJogosFragment extends Fragment {

    public  static final String TAG_JOGO = "JOGO";
    public static final int ATUALIZAR = 10;
    private List<Jogo> jogos;
    private JogoAdapter jogoAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conf_jogos, container, false);

        JogoDAO jogoDAO = new JogoDAO(BancoDadosHelper.getInstance(getActivity().getApplicationContext()));

        try {
            jogos = jogoDAO.consultarTodos();
        }catch (Exception e){
            e.printStackTrace();
        }

        jogoAdapter = new JogoAdapter(getActivity().getApplicationContext() , jogos, JogoAdapter.LIST_ADAPTER);
        ListView listView = (ListView)rootView.findViewById(R.id.listViewJogos);
        listView.setAdapter(jogoAdapter);

        registerForContextMenu(listView);

        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_jogos, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Jogo jogo;
        Intent intent;
        switch (item.getItemId()){
            case R.id.novo_jogo_id:
                intent = new Intent(getActivity().getApplicationContext(), ConfDadosJogoActivity.class);
                intent.putExtra(OPERACAO_JOGO, Operacoes.CADASTRO.getOperacao());
                startActivity(intent);

                return true;
            case R.id.editar_jogo_id:
                jogo = jogos.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfDadosJogoActivity.class);
                intent.putExtra(TAG_JOGO, jogo);
                intent.putExtra(OPERACAO_JOGO, Operacoes.EDITAR.getOperacao());
                startActivityForResult(intent, 1);

                return true;
            case R.id.vincular_estimulo_id:
                jogo = jogos.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfVinculaEstimulos.class);
                intent.putExtra(OPERACAO_JOGO, Operacoes.EDITAR.getOperacao());
                intent.putExtra(TAG_JOGO, jogo.getID());
                startActivity(intent);
                return true;
            case R.id.conf_niveis_id:
                jogo = jogos.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfGeralNiveisActivity.class);
                intent.putExtra(TAG_JOGO, jogo.getID());
                startActivity(intent);
                return true;
            case R.id.vincular_reforcador_id:
                jogo = jogos.get(info.position);

                intent = new Intent(getActivity().getApplicationContext(), ConfVinculaReforcador.class);
                intent.putExtra(TAG_JOGO, jogo.getID());
                startActivity(intent);
                return true;
            default:
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){

            if(resultCode == Activity.RESULT_OK){
                jogoAdapter.notifyDataSetChanged();
            }
        }

    }
}
