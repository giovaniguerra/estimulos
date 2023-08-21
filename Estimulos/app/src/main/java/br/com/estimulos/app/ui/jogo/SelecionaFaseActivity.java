package br.com.estimulos.app.ui.jogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.DAOImpl;
import br.com.estimulos.app.core.dao.FaseDAO;
import br.com.estimulos.app.ui.adapter.FaseAdapter;
import br.com.estimulos.dominio.Fase;

public class SelecionaFaseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String TAG = "SEL_FASE_ACT";

    private ImageButton btProximo;
    private ImageButton btAnterior;

    private GameBuilder builder;
    private GridView gridFases;
    private int gridSize;
    private FaseDAO faseDAO;
    private String faseSelecionada;
    private List<Fase> fases;

    private Map<Integer, DAOImpl> mapNivelDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_fase);

        btProximo = (ImageButton) findViewById(R.id.btProximo);
        btProximo.setOnClickListener(this);

        btAnterior = (ImageButton) findViewById(R.id.btAnterior);
        btAnterior.setOnClickListener(this);


        //Instanciar e popular o mapa de daos
        mapNivelDAO = new HashMap<>();

        // Instancia o DAO da fase
        faseDAO = new FaseDAO(BancoDadosHelper.getInstance(this));

        try {
            fases = faseDAO.consultarTodos();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Recuperar o builder vindo da intent
        builder = (GameBuilder) getIntent().getSerializableExtra(GameBuilder.TAG);

        gridFases = (GridView) findViewById(R.id.gridFases);
        gridFases.setOnItemClickListener(this);

        // guardar o tamanho da lista de jogos e adicionar como numero de colunas ao grid
        gridSize = fases.size();
        gridFases.setNumColumns(gridSize);
        // calcular o tamanho do grid junto com seus itens
        gridViewSetting(gridFases);
        // adicionar o adapter de jogos ao grid
        FaseAdapter adapter = new FaseAdapter(this, fases);
        gridFases.setAdapter(adapter);

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
        getMenuInflater().inflate(R.menu.menu_seleciona_fase, menu);
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

            try {
                if(faseSelecionada != null) {
                    Intent intent = new Intent(this, SelecionaNivelActivity.class);
                    builder.setNomeFase(faseSelecionada);
                    intent.putExtra(GameBuilder.TAG, builder);
                    startActivity(intent);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(v == btAnterior){
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        faseSelecionada = (String) view.getTag();
    }
}
