package br.com.estimulos.app.ui.configuracao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.ui.adapter.EstimuloJogoHelper;
import br.com.estimulos.app.ui.adapter.ReforcadorJogoHelper;
import br.com.estimulos.app.ui.adapter.VinculaEstimuloAdapter;
import br.com.estimulos.app.ui.adapter.VinculaReforcadorAdapter;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Reforcador;

public class ConfVinculaReforcador extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private List<Reforcador> reforcadoresJogo;
    private List<Reforcador> listReforcadores;
    private List<ReforcadorJogoHelper> reforcadorJogoHelpers;
    private VinculaReforcadorAdapter adapter;
    private ReforcadorDAO reforcadorDAO;
    private JogoDAO jogoDAO;
    private Jogo jogo;
    private Button btSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_vincula_reforcador);
        try {
            btSalvar = (Button) findViewById(R.id.btSalvar);
            btSalvar.setOnClickListener(this);

            reforcadorJogoHelpers = new ArrayList<>();

            jogoDAO = new JogoDAO(BancoDadosHelper.getInstance(this));
            reforcadorDAO = new ReforcadorDAO(BancoDadosHelper.getInstance(this));

            Integer id = (Integer) getIntent().getSerializableExtra(MenuJogosFragment.TAG_JOGO);

            jogo = jogoDAO.retornarJogoCompleto(id);

            if(jogo.getReforcadores() != null)
                reforcadoresJogo = jogo.getReforcadores();

            listReforcadores = reforcadorDAO.consultarTodos();




            /**
             * Monta o adapter do estimulo para exibir no GRIDVIEW
             */
            adapter = new VinculaReforcadorAdapter(this, montaListaReforcadorHelper());
            ListView listView = (ListView) findViewById(R.id.listReforcador);
            listView.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo responsavel por montar a lista de Estimulo Helper a ser exibida no GRID
     * @return
     */
    public List<ReforcadorJogoHelper> montaListaReforcadorHelper(){
        // Instancia um novo estimulo Helper
        ReforcadorJogoHelper reforcadorJogoHelper;
        // Limpa a lista
        reforcadorJogoHelpers.clear();
        // Loop para varres os estimulosFiltrados selecionados
        for (Reforcador r: listReforcadores) {
            // Instancia um estimulo com o indicador true - para Vinculado ao jogo
            // ou False - para nao vinculado
            reforcadorJogoHelper = new ReforcadorJogoHelper(r,  estaVinculado(r.getID()));
            // adiciona na lista
            reforcadorJogoHelpers.add(reforcadorJogoHelper);
        }
        // retorna a lista
        return reforcadorJogoHelpers;
    }

    /**
     * Metodo para verifica se o id do estimulo informado esta vinculado ou nao
     * com o jogo
     * @param id
     * @return
     */
    private boolean estaVinculado(Integer id){
        // Flg que presenta o vinculo do estimulo com o jogo
        boolean flg =false;
        // Percorre a lista de estimulosFiltrados vinculados ao jogo
        for (Reforcador r: reforcadoresJogo) {
            // se encontrou este estimulo na lista do jogo
            if (r.getID() == id){
                // true - para indicar que o estimulo esta vinculado
                flg = true;
            }
        }
        // retorna a condicao
        return flg;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = (int) buttonView.getTag();

        for (Reforcador r: listReforcadores) {
            if(r.getID() == id){
                if (isChecked){
                    reforcadoresJogo.add(r);
                }else{
                    int cont = 0;
                    for(Reforcador ref: reforcadoresJogo){
                        if(ref.getID()==r.getID()){
                            break;
                        }
                        cont++;
                    }
                    reforcadoresJogo.remove(cont);
                }
                break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(btSalvar == v){
            try {
                jogo.setEstimulos(null);
                jogo.setReforcadores(reforcadoresJogo);
                jogoDAO.alterar(jogo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
            super.onBackPressed();

        }

    }

}
