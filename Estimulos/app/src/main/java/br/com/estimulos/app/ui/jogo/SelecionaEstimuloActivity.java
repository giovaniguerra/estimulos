package br.com.estimulos.app.ui.jogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.ui.adapter.EstimuloAdapter;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Jogo;

public class SelecionaEstimuloActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "SEL_ESTM_ACT";

    private JogoDAO jogoDAO;
    private GameBuilder builder;

    private ImageButton btProximo;
    private ImageButton btAnterior;
    private GridView gridEstimulos;
    private List<Estimulo> categorias;
    private int gridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_estimulo);

        btProximo = (ImageButton) findViewById(R.id.btProximo);
        btProximo.setOnClickListener(this);

        btAnterior = (ImageButton) findViewById(R.id.btAnterior);
        btAnterior.setOnClickListener(this);

        builder = (GameBuilder) getIntent().getSerializableExtra(GameBuilder.TAG);

        gridEstimulos = (GridView) findViewById(R.id.gridEstimulos);
        gridEstimulos.setOnItemClickListener(this);

        jogoDAO = new JogoDAO(BancoDadosHelper.getInstance(this));
        Jogo jogo = null;
        try {
            jogo = jogoDAO.retornarJogoCompleto(builder.getIdJogo());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        geraCategorias(jogo.getEstimulos());

        // guardar o tamanho da lista de jogos e adicionar como numero de colunas ao grid
        gridSize = jogo.getEstimulos().size();
        gridEstimulos.setNumColumns(gridSize);
        // calcular o tamanho do grid junto com seus itens
        gridViewSetting(gridEstimulos);
        // adicionar o adapter de jogos ao grid
        EstimuloAdapter adapter = new EstimuloAdapter(this, categorias, EstimuloAdapter.GRID_ADAPTER);
        gridEstimulos.setAdapter(adapter);

    }

    private void geraCategorias(List<Estimulo> estimulos){
        categorias = new ArrayList<>();
        boolean flag = false;
        for(Estimulo e: estimulos){
            flag = false;
            for (Estimulo c : categorias) {
                if (e.getCategoria().getNome().equals(c.getCategoria().getNome()))
                    flag = true;
            }
            if(!flag)
                categorias.add(e);
        }
    }

    private void gridViewSetting(GridView gridview) {
        // recuperar tamanho da lista do gridView e um tamanho para cada item do grid
        int size= gridSize;
        int width = 100;

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        // Calcular o tamanho total e tamanho de cada item do gridView
        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(singleItemWidth);
        gridview.setHorizontalSpacing(10);
        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridview.setNumColumns(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seleciona_estimulo, menu);
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
        if(v == btProximo){
            // TODO: testar se algum jogo foi selecionado, se nao, escolher o primeiro?
            Intent intent = new Intent(this, SelecionaFaseActivity.class);
            intent.putExtra(GameBuilder.TAG, builder);
            startActivity(intent);

        }
        if(v == btAnterior){
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO : Adicionar cor do background da view selecionada e guardar a view em uma variavel
        // como a ultima view selecionada.#1E90FF
        view.setSelected(true);
        builder.setIdCategoria( ( (Estimulo) view.getTag() ).getCategoria().getID());
    }
}
