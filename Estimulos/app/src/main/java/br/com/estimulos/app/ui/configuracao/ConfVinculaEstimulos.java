package br.com.estimulos.app.ui.configuracao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.CategoriaEstimuloDAO;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.ui.adapter.EstimuloJogoHelper;
import br.com.estimulos.app.ui.adapter.VinculaEstimuloAdapter;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Reforcador;

public class ConfVinculaEstimulos extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private List<Estimulo> estimulosFiltrados;
    private List<Estimulo> estimulosJogo;
    private List<EstimuloJogoHelper> estimulosJogoHelpers;
    private VinculaEstimuloAdapter estimuloAdapter;
    private EstimuloDAO estimuloDAO;
    private CategoriaEstimuloDAO categoriaDAO;
    private JogoDAO jogoDAO;
    private List<String> categorias;
    private Map<String, Integer> mapCategorias;
    private static final String TODOS = "Todos";
    private Jogo jogo;
    private int operacaoTela;
    private Button btProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_vincula_estimulos);

        /**
         * Recupera o botao do layout
         */
        btProximo = (Button) findViewById(R.id.btProximo);
        btProximo.setOnClickListener(this);
        try{
            /**
             * Faz instancia dos DAOs necessarios para as consultas desta activity
             */
            categoriaDAO = new CategoriaEstimuloDAO(BancoDadosHelper.getInstance(this));
            estimuloDAO = new EstimuloDAO(BancoDadosHelper.getInstance(this));
            jogoDAO = new JogoDAO(BancoDadosHelper.getInstance(this));

            /**
             * Recupera a intent que contem o id do jogo selecionado
             */
            Integer id = (Integer) getIntent().getSerializableExtra(MenuJogosFragment.TAG_JOGO);
            operacaoTela = getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);

            /**
             * Verifica a operacao a ser realizada
             */
            if(operacaoTela == MenuJogosFragment.Operacoes.CADASTRO.getOperacao()){
                // Instancia um novo objeto de jogo para o cadastro
                jogo = jogoDAO.visualizar(id);

                estimulosJogo = new ArrayList<>();
                btProximo.setBackgroundResource(R.drawable.bt_salvar);
            }
            else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
                /**
                 * Recupera os todos os dados do Jogo
                 */
                if(id != null) {
                    jogo = jogoDAO.retornarJogoCompleto(id);
                    if(jogo.getEstimulos() != null)
                        estimulosJogo = jogo.getEstimulos();
                }

                btProximo.setBackgroundResource(R.drawable.bt_editar);
            }

            /**
             * Instancia o array de Jogos Helper - Para uso do relacionamento Jogo com Estimulos vinculados
             * Ele consiste em ajudar a marcar quais estimulosFiltrados estão vinculados ao jogo
             */
            estimulosJogoHelpers = new ArrayList<>();

            /**
             * Array para exibir a lista de Categorias
             */
            categorias = new ArrayList<>();

            /**
             * Para para auxiliar a escolha da Categoria
             * na hora de exibir os estimulosFiltrados
             */
            mapCategorias = new HashMap<>();

            /**
             * Adiciona a opcao que exibe todos os estimulosFiltrados
             * sem filtrar por nenhuma categoria
             */
            categorias.add(TODOS);

            /**
             * Loop para armazenar a categoria e
             * mapear o nome como chave e seu id como valor
             */
            for (CategoriaEstimulo c : categoriaDAO.consultarTodos()) {
                categorias.add(c.getNome());
                mapCategorias.put(c.getNome(), c.getID());
            }

            estimulosFiltrados = estimuloDAO.consultarTodos();
            montaListaEstimuloHelper();

            /**
             * Monta o adapter do estimulo para exibir no GRIDVIEW
             */
            estimuloAdapter = new VinculaEstimuloAdapter(this , estimulosJogoHelpers);
            GridView gridView = (GridView) findViewById(R.id.gridEstimulos);
            gridView.setAdapter(estimuloAdapter);

            /**
             * Codigo responsavel por montar a spinner para listar as categorias
             */
            Spinner spinner = (Spinner) findViewById(R.id.spCategoria);
            ArrayAdapter adpterArray = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
            adpterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adpterArray);
            spinner.setOnItemSelectedListener(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo responsavel por montar a lista de Estimulo Helper a ser exibida no GRID
     * @return
     */
    public List<EstimuloJogoHelper> montaListaEstimuloHelper(){
        // Instancia um novo estimulo Helper
        EstimuloJogoHelper estimuloJogoHelper;
        // Limpa a lista
        estimulosJogoHelpers.clear();
        // Loop para varres os estimulosFiltrados selecionados
        for (Estimulo e: estimulosFiltrados) {
            // Instancia um estimulo com o indicador true - para Vinculado ao jogo
            // ou False - para nao vinculado
            estimuloJogoHelper = new EstimuloJogoHelper(e,  estaVinculado(e.getID()));
            // adiciona na lista
            estimulosJogoHelpers.add(estimuloJogoHelper);
        }
        // retorna a lista
        return estimulosJogoHelpers;
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
        for (Estimulo e: estimulosJogo) {
            // se encontrou este estimulo na lista do jogo
            if (e.getID() == id){
                // true - para indicar que o estimulo esta vinculado
                flg = true;
            }
        }
        // retorna a condicao
        return flg;
    }

    /**
     * Metodo executado ao selecionar um item do spinner de Categorias
     * Responsavel por atualizar o grid com os estimulosFiltrados que pertencem
     * a categoria escolhida no spinner.
     * @param parent
     * @param view
     * @param position - posicao a categoria encontra-se no array
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Recupera o nome da categoria selecionada
        String nmCategoria = categorias.get(position);

        // Caso seja Todos estimulosFiltrados
        if(nmCategoria.equals(TODOS)){
            // realiza a consulta de todos os estimulosFiltrados
            try {
                estimulosFiltrados = estimuloDAO.consultarTodos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Selecionou uma categoria especifica
        else{
            // Recupera o id da categoria selecionada
            int idCat = mapCategorias.get(categorias.get(position));
            // Monta o filtro para pesquisar todos estimulosFiltrados que pertencem a categoria
            // com o id selecionado
            Map<String, String> filtro = new HashMap<>();
            filtro.put(EstimuloDAO.Tabela.CATEGORIA_ESTIMULO.getName(), "" + idCat);

            // realiza a consulta dos estimulosFiltrados
            try {
                estimulosFiltrados = estimuloDAO.pesquisa(filtro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Atualiza a lista de estimulosFiltrados no GRID
        estimuloAdapter.updateResults(montaListaEstimuloHelper());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = (int) buttonView.getTag();

        for (Estimulo e: estimulosFiltrados) {
            if(e.getID() == id){
                if (isChecked){
                    estimulosJogo.add(e);
                }else{
                    int cont = 0;
                    for(Estimulo est: estimulosJogo){
                        if(est.getID()==e.getID()){
                            break;
                        }
                        cont++;
                    }
                    estimulosJogo.remove(cont);

                }
                break;
            }
        }
    }

    public void configurarNiveis(){
        Intent intent = new Intent(this, ConfGeralNiveisActivity.class);
        intent.putExtra(MenuJogosFragment.TAG_JOGO, jogo.getID());
        intent.putExtra(MenuJogosFragment.OPERACAO_JOGO, operacaoTela);
        startActivity(intent);
    }
    public void voltar(){
        Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if(v == btProximo){
            /**
             * Verifica a operacao a ser realizada
             */
            if(operacaoTela == MenuJogosFragment.Operacoes.CADASTRO.getOperacao()){

                try {
                    jogo.setEstimulos(estimulosJogo);
                    jogoDAO.alterar(jogo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Escolha")
                        .setPositiveButton("Configurar Níveis", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                configurarNiveis();
                            }
                        })
                        .setNegativeButton("Configuração padrão", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                voltar();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();



            }
            else if(operacaoTela == MenuJogosFragment.Operacoes.EDITAR.getOperacao()) {
                try {
                    jogo.setEstimulos(estimulosJogo);
                    jogoDAO.alterar(jogo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
                startActivity(intent);
            }
        }
    }
}

