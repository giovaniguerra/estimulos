package br.com.estimulos.app.ui.jogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.ui.adapter.JogoAdapter;
import br.com.estimulos.app.ui.configuracao.MenuConfiguracaoActivity;
import br.com.estimulos.dominio.Jogo;

public class SelecionaJogoActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "SEL_JOGO_ACT";

    private JogoDAO jogoDao;
    private GameBuilder builder;

    private ImageButton btProximo;
    private ImageButton btAnterior;
    private GridView gridJogos;
    private int gridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_jogo);


        btProximo = (ImageButton) findViewById(R.id.btProximo);
        btProximo.setOnClickListener(this);

        btAnterior = (ImageButton) findViewById(R.id.btAnterior);
        btAnterior.setOnClickListener(this);

        builder = new GameBuilder(0);

        gridJogos = (GridView) findViewById(R.id.gridJogos);
        gridJogos.setOnItemClickListener(this);

        jogoDao = new JogoDAO(BancoDadosHelper.getInstance(this));
        List<Jogo> jogos = null;

        try {
            jogos = jogoDao.consultarTodos();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        // guardar o tamanho da lista de jogos e adicionar como numero de colunas ao grid
        gridSize = jogos.size();
        gridJogos.setNumColumns(gridSize);
        // calcular o tamanho do grid junto com seus itens
        gridViewSetting(gridJogos);
        // adicionar o adapter de jogos ao grid
        JogoAdapter adapter = new JogoAdapter(this, jogos, JogoAdapter.GRID_ADAPTER);
        gridJogos.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.menu_activity_seleciona_jogo, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == btProximo){
            // TODO: testar se algum jogo foi selecionado, se nao, escolher o primeiro?
            Intent intent = new Intent(this, SelecionaEstimuloActivity.class);
            intent.putExtra(GameBuilder.TAG, builder);
            startActivity(intent);

        }
        if(v == btAnterior){
            onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO : Adicionar cor do background da view selecionada e guardar a view em uma variavel
        // como a ultima view selecionada.
//        selectedView = view;
//        Drawable fundo = this.getResources().getDrawable(R.drawable.borda_frame);
//        fundo.set
//        fundo.setAlpha(255*0.3);
        view.setSelected(true);
        builder.setIdJogo((Integer) view.getTag());
    }
}
