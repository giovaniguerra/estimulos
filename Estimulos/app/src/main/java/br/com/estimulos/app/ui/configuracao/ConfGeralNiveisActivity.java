package br.com.estimulos.app.ui.configuracao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.FaseDAO;
import br.com.estimulos.app.core.dao.NivelArrastarDAO;
import br.com.estimulos.app.core.dao.NivelTocarDAO;
import br.com.estimulos.app.core.factory.DAOFactory;
import br.com.estimulos.app.core.factory.FaseFactory;
import br.com.estimulos.app.interfaces.InterfaceDAO;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;

public class ConfGeralNiveisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CheckBox.OnCheckedChangeListener{

    public static final String TAG = "NivelGeralAct";

    private static final int QTDE_NIVEIS = 5;
    private static final int QTDE_MAX_ESTIMULO = 5;
    private static final int QTDE_MAX_TAREFA = 10;
    private static final int QTDE_MAX_ERROS = 10;
    private static final int QTDE_MAX_SOM_ERROS = 10;
    private static final int QTDE_MAX_INSTRUCOES = 10;


    /**
     * Recupera as views na tela
     */
    private Spinner spnFase;
    private Spinner spnNivel;
    private Spinner spnEstimulo;
    private Spinner spnTarefa;
    private Spinner spnLimiteErro;
    private Spinner spnLimiteSomErro;
    private Spinner spnLimiteInstrucaoTTS;
    private Button btConfigurarPosicao;

    private CheckBox checkBoxPosicaoRand;

//    private CheckBox checkBoxEstimuloRand;
//    private EditText etInicioEstimuloRand;


    /**
     * Arrays para as spinners
     */
    private String[] fases;
    private String[] niveis;
    private String[] numEstimulos;
    private String[] numTarefas;
    private String[] numErros;
    private String[] numSomErros;
    private String[] numIntrucaoTTS;

    private Integer idJogo;
    private int operacaoTela;

    /**
     * Instancia os daos necessarios
     */
    private NivelArrastarDAO nivelArrastarDAO;
    private NivelTocarDAO nivelTocarDAO;
    private FaseDAO faseDAO;
    private List<NivelArrastar> niveisArrastar;
    private List<NivelTocar> niveisTocar;
    private List<Fase> listFases;

    /**
     *  Para guardar o que esta selecionado
     */
    private Fase faseSelecionada;
    private Nivel nivelSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_niveis);

        spnFase = (Spinner) findViewById(R.id.spFase);
        spnNivel = (Spinner) findViewById(R.id.spNivel);
        spnEstimulo = (Spinner) findViewById(R.id.spNumEstimulos);
        spnTarefa = (Spinner) findViewById(R.id.spNumTarefas);
        spnLimiteErro = (Spinner) findViewById(R.id.spLimiteErros);
        spnLimiteSomErro = (Spinner) findViewById(R.id.spLimiteSomErros);
        spnLimiteInstrucaoTTS = (Spinner) findViewById(R.id.spLimiteInstrucaoTTS);
        checkBoxPosicaoRand = (CheckBox) findViewById(R.id.ckPosicaoAleatoria);
        checkBoxPosicaoRand.setOnCheckedChangeListener(this);
        btConfigurarPosicao = (Button) findViewById(R.id.btConfigurarPosicao);
        btConfigurarPosicao.setOnClickListener(this);

        /**
         * Recupera a intent que contem o id do jogo selecionado
         */
        idJogo = (Integer) getIntent().getSerializableExtra(MenuJogosFragment.TAG_JOGO);
        operacaoTela = getIntent().getIntExtra(MenuJogosFragment.OPERACAO_JOGO, 0);


        try {
            // Carregar as fases e os niveis do jogo
            carregarDados();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


        /**
         * Codigo responsavel por montar a spinner para listar as fases cadastradas
         */
        fases = new String[listFases.size()];
        for(int i = 0; i < listFases.size(); i++){
            fases[i] = listFases.get(i).getNome();
        }
        ArrayAdapter adpterArrayFase = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fases);
        adpterArrayFase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnFase.setAdapter(adpterArrayFase);
        spnFase.setOnItemSelectedListener(this);
        spnFase.setSelection(0);

        /**
         * Codigo responsavel por montar a spinner para listar os níveis cadastrados
         */
        niveis = new String[QTDE_NIVEIS];
        for(int i = 0; i < QTDE_NIVEIS; i++){
            niveis[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adpterArrayNivel = new ArrayAdapter(this, android.R.layout.simple_spinner_item, niveis);
        adpterArrayNivel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNivel.setAdapter(adpterArrayNivel);
        spnNivel.setOnItemSelectedListener(this);

        /**
         * Codigo responsavel por montar a spinner para listar a quantidade de estimulos na tela
         */
        numEstimulos = new String[QTDE_MAX_ESTIMULO];
        for(int i = 0; i < QTDE_MAX_ESTIMULO; i++){
            numEstimulos[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adpterArrayEstimulos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numEstimulos);
        adpterArrayEstimulos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstimulo.setAdapter(adpterArrayEstimulos);
        spnEstimulo.setOnItemSelectedListener(this);

        /**
         * Codigo responsavel por montar a spinner para listar a quantidade de tarefas na tela
         */
        numTarefas = new String[QTDE_MAX_TAREFA];
        for(int i = 0; i < QTDE_MAX_TAREFA; i++){
            numTarefas[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adpterArrayTarefas = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numTarefas);
        adpterArrayTarefas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTarefa.setAdapter(adpterArrayTarefas);
        spnTarefa.setOnItemSelectedListener(this);

        /**
         * Codigo responsavel por montar a spinner para listar a quantidade maxima de erros
         */
        numErros = new String[QTDE_MAX_ERROS];
        for(int i = 0; i < QTDE_MAX_ERROS; i++){
            numErros[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adapterArrayErros = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numErros);
        adapterArrayErros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLimiteErro.setAdapter(adapterArrayErros);
        spnLimiteErro.setOnItemSelectedListener(this);

        /**
         * Codigo responsavel por montar a spinner para listar a quantidade maxima de erros que silenciará o som
         */
        numSomErros = new String[QTDE_MAX_SOM_ERROS];
        for(int i = 0; i < QTDE_MAX_SOM_ERROS; i++){
            numSomErros[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adapterArraySomErros = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numSomErros);
        adapterArraySomErros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLimiteSomErro.setAdapter(adapterArraySomErros);
        spnLimiteSomErro.setOnItemSelectedListener(this);

        /*
         * Codigo responsavel por montar a spinner para listar a quantidade maxima de instrucoes de
         * voz que silenciará o TTS
         */
        // TODO : Melhorar o texto de exibição
        numIntrucaoTTS = new String[QTDE_MAX_INSTRUCOES];
        for(int i = 0; i < QTDE_MAX_INSTRUCOES; i++){
            numIntrucaoTTS[i] = String.valueOf(i + 1);
        }
        ArrayAdapter adapterArrayInstrucaoTTS = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numIntrucaoTTS);
        adapterArrayInstrucaoTTS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLimiteInstrucaoTTS.setAdapter(adapterArrayInstrucaoTTS);
        spnLimiteInstrucaoTTS.setOnItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ConfNiveisEstimulosActivity.RESULT_NIVEIS) {
            if (resultCode == RESULT_OK) {
                nivelSelecionado = (Nivel) data.getSerializableExtra("nivel");
            } else {
                // TODO : Algo deu errado na tela de nivel, o que fazer?
            }
        }
    }

    private void carregarDados() throws Exception {
        Map<String, String> filtro;

        faseDAO = new FaseDAO(BancoDadosHelper.getInstance(getApplicationContext()));
        listFases = faseDAO.consultarTodos();

        filtro = new HashMap<>();
        filtro.put(NivelArrastarDAO.Tabela.JOGO.getName(), ""+ idJogo);
        nivelArrastarDAO = new NivelArrastarDAO(BancoDadosHelper.getInstance(getApplicationContext()));
        niveisArrastar = nivelArrastarDAO.pesquisa(filtro);

        filtro = new HashMap<>();
        filtro.put(NivelTocarDAO.Tabela.JOGO.getName(), "" + idJogo);
        nivelTocarDAO = new NivelTocarDAO(BancoDadosHelper.getInstance(getApplicationContext()));
        niveisTocar = nivelTocarDAO.pesquisa(filtro);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idSpinner = parent.getId();
        switch(idSpinner){
            case R.id.spFase:
                faseSelecionada = listFases.get(position);
                if(faseSelecionada.getNomeClasseHerdade().equals(FaseArrastar.class.getName())){
                    nivelSelecionado = niveisArrastar.get(0);
                }else if(faseSelecionada.getNomeClasseHerdade().equals(FaseTocar.class.getName())){
                    nivelSelecionado = niveisTocar.get(0);
                } else {
                    // TODO : nao encontrou nenhum nivel, isso pode acontecer? O que fazer?
                }
                spnNivel.setSelection(0);
                spnEstimulo.setSelection(nivelSelecionado.getQtdeEstimulos() != null ?
                        nivelSelecionado.getQtdeEstimulos() - 1 : 0);
                spnTarefa.setSelection(nivelSelecionado.getNumTarefas() != null ?
                        nivelSelecionado.getNumTarefas()- 1 : 0);
                spnLimiteErro.setSelection(nivelSelecionado.getLimiteErros() != null ?
                        nivelSelecionado.getLimiteErros() - 1 : 0);
                spnLimiteSomErro.setSelection(nivelSelecionado.getLimiteSomDeErro() != null ?
                        nivelSelecionado.getLimiteSomDeErro() - 1 : 0);
                spnLimiteInstrucaoTTS.setSelection(nivelSelecionado.getLimiteInstrucoesTTS() != null ?
                        nivelSelecionado.getLimiteInstrucoesTTS() - 1 : 0);
                checkBoxPosicaoRand.setChecked(nivelSelecionado.isRandPosicao());

                break;
            case R.id.spNivel:
                if(faseSelecionada.getNomeClasseHerdade().equals(FaseArrastar.class.getName())){
                    // TODO: ArrayOutOfBoundsException prevenir de acontecer ...
                    nivelSelecionado = niveisArrastar.get(position);
                }else if(faseSelecionada.getNomeClasseHerdade().equals(FaseTocar.class.getName())){
                    nivelSelecionado = niveisTocar.get(position);
                } else {
                    // TODO : nao encontrou nenhum nivel, isso pode acontecer? O que fazer?
                }
                spnEstimulo.setSelection(nivelSelecionado.getQtdeEstimulos() != null ?
                        nivelSelecionado.getQtdeEstimulos() - 1 : 0);
                spnTarefa.setSelection(nivelSelecionado.getNumTarefas() != null ?
                        nivelSelecionado.getNumTarefas() - 1 : 0);
                spnLimiteErro.setSelection(nivelSelecionado.getLimiteErros() != null ?
                        nivelSelecionado.getLimiteErros() - 1 : 0);
                spnLimiteSomErro.setSelection(nivelSelecionado.getLimiteSomDeErro() != null ?
                        nivelSelecionado.getLimiteSomDeErro() - 1 : 0);
                spnLimiteInstrucaoTTS.setSelection(nivelSelecionado.getLimiteInstrucoesTTS() != null ?
                        nivelSelecionado.getLimiteInstrucoesTTS() - 1 : 0);
                checkBoxPosicaoRand.setChecked(nivelSelecionado.isRandPosicao());

                break;
            case R.id.spNumEstimulos:
                nivelSelecionado.setQtdeEstimulos(Integer.valueOf(String.valueOf(spnEstimulo.getSelectedItem())));
                break;
            case R.id.spNumTarefas:
                nivelSelecionado.setNumTarefas(Integer.valueOf(String.valueOf(spnTarefa.getSelectedItem())));
                break;
            case R.id.spLimiteErros:
                // TODO : Arrumar o spinner de baixo, baseado nessa seleção
                nivelSelecionado.setLimiteErros(Integer.valueOf(String.valueOf(spnLimiteErro.getSelectedItem())));
                break;
            case R.id.spLimiteSomErros:
                nivelSelecionado.setLimiteSomDeErro(Integer.valueOf(String.valueOf(spnLimiteSomErro.getSelectedItem())));
                break;
            case R.id.spLimiteInstrucaoTTS:
                nivelSelecionado.setLimiteInstrucoesTTS(Integer.valueOf(String.valueOf(spnLimiteInstrucaoTTS.getSelectedItem())));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected parent = " + parent);
    }

    @Override
    public void onClick(View v) {
        if(btConfigurarPosicao == v){
            Intent intent = new Intent(this, ConfNiveisEstimulosActivity.class);
            intent.putExtra("fase", faseSelecionada);
            intent.putExtra("nivel", nivelSelecionado);
            startActivityForResult(intent, ConfNiveisEstimulosActivity.RESULT_NIVEIS);

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "ButtonView = " + buttonView);
        if(checkBoxPosicaoRand == buttonView){
            nivelSelecionado.setRandPosicao(isChecked);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conf_geral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_salvar){
            // Testar nulos
            if(nivelSelecionado.getQtdeEstimulos() == null)
                nivelSelecionado.setQtdeEstimulos(Integer.valueOf(String.valueOf(spnEstimulo.getSelectedItem())));
            if(nivelSelecionado.getNumTarefas() == null)
                nivelSelecionado.setNumTarefas(Integer.valueOf(String.valueOf(spnTarefa.getSelectedItem())));
            if(nivelSelecionado.getLimiteErros() == null)
                nivelSelecionado.setLimiteErros(Integer.valueOf(String.valueOf(spnLimiteErro.getSelectedItem())));
            if(nivelSelecionado.getLimiteSomDeErro() == null)
                nivelSelecionado.setLimiteSomDeErro(Integer.valueOf(String.valueOf(spnLimiteSomErro.getSelectedItem())));
            if(nivelSelecionado.getLimiteInstrucoesTTS() == null)
                nivelSelecionado.setLimiteInstrucoesTTS(Integer.valueOf(String.valueOf(spnLimiteInstrucaoTTS.getSelectedItem())));

            InterfaceDAO dao;
            try {
                dao = DAOFactory.getDAO(this, FaseFactory.getFase(this, faseSelecionada.getNomeClasseHerdade()));
                if(nivelSelecionado.getID() == null) {
                    dao.salvar(nivelSelecionado);
                } else {
                    dao.alterar(nivelSelecionado);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_principal) {
            Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.action_logout){
            //TODO:Fazer o logout do usuario
        }

        return super.onOptionsItemSelected(item);
    }

}
