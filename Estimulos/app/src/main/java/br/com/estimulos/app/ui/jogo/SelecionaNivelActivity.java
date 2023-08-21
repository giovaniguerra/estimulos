package br.com.estimulos.app.ui.jogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.NivelArrastarDAO;
import br.com.estimulos.app.core.factory.DAOFactory;
import br.com.estimulos.app.core.factory.FaseFactory;
import br.com.estimulos.app.interfaces.InterfaceDAO;
import br.com.estimulos.dominio.Nivel;

public class SelecionaNivelActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btProximo;
    private ImageButton btAnterior;
    private ImageButton btNivel1;
    private ImageButton btNivel2;
    private ImageButton btNivel3;
    private ImageButton btNivel4;
    private ImageButton btNivel5;
    private ImageButton btSelecionado;
    private GameBuilder builder;
    private int gridSize;

    private GridView gridNiveis;
    private List<Nivel> niveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_nivel);

        btProximo = (ImageButton) findViewById(R.id.btProximo);
        btProximo.setOnClickListener(this);


        btAnterior = (ImageButton) findViewById(R.id.btAnterior);
        btAnterior.setOnClickListener(this);

        btNivel1 = (ImageButton) findViewById(R.id.btNivel1);
        btNivel1.setOnClickListener(this);

        btNivel2 = (ImageButton) findViewById(R.id.btNivel2);
        btNivel2.setOnClickListener(this);

        btNivel3 = (ImageButton) findViewById(R.id.btNivel3);
        btNivel3.setOnClickListener(this);

        btNivel4 = (ImageButton) findViewById(R.id.btNivel4);
        btNivel4.setOnClickListener(this);

        btNivel5 = (ImageButton) findViewById(R.id.btNivel5);
        btNivel5.setOnClickListener(this);

        // Recupera os valores do primeiro Layout
        Intent intent = getIntent();

        // recupera os parametros
        if(savedInstanceState == null) {
            builder = (GameBuilder) intent.getSerializableExtra(GameBuilder.TAG);
        } else {
            builder = (GameBuilder) savedInstanceState.getSerializable(GameBuilder.TAG);
        }
//        String className =  intent.getStringExtra(SelecionaFaseActivity.TAG);

        InterfaceDAO daoNivel = DAOFactory.getDAO(this, FaseFactory.getFase(this, builder.getNomeFase()));

        Map<String, String> filtro = new HashMap<>();
        filtro.put(NivelArrastarDAO.Tabela.JOGO.getName(), builder.getIdJogo() + "");

        try {

            niveis = daoNivel.pesquisa(filtro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seleciona_nivel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // testar se havia um botao selecionado
        if(btSelecionado != null) {
            btSelecionado.setSelected(false);
        }
        // testar qual botao foi selecionado
        if(v == btProximo){
            if(builder.getNivel() != null){
                Intent intent = new Intent(this, CriarJogoActivity.class);
                intent.putExtra(GameBuilder.TAG, builder);
                startActivity(intent);
            }
        }else if(v == btNivel1){
            builder.setNivel(niveis.get(0));
            btSelecionado = btNivel1;
            btNivel1.setSelected(true);
        }else if(v == btNivel2){
            btSelecionado = btNivel2;
            btNivel2.setSelected(true);
            builder.setNivel(niveis.get(1));
        }else if(v == btNivel3){
            btSelecionado = btNivel3;
            btNivel3.setSelected(true);
            builder.setNivel(niveis.get(2));
        }else if(v == btNivel4){
            btSelecionado = btNivel4;
            btNivel4.setSelected(true);
            builder.setNivel(niveis.get(3));
        }else if(v == btNivel5){
            btSelecionado = btNivel5;
            btNivel5.setSelected(true);
            builder.setNivel(niveis.get(4));
        }
        if(v == btAnterior){
            onBackPressed();
        }

    }

}
