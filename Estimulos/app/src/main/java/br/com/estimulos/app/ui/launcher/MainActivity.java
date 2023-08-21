package br.com.estimulos.app.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import br.com.estimulos.app.R;
import br.com.estimulos.app.teste.CriadorRegistrosSQLite;
import br.com.estimulos.app.ui.configuracao.MenuConfiguracaoActivity;
import br.com.estimulos.app.ui.jogo.SelecionaJogoActivity;
import br.com.estimulos.app.ui.jogo.SobreActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * imagem do botao iniciar atividade
     */
    private ImageView ivIniciar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CriadorRegistrosSQLite.criarMock(this);

        /**
         * encontra o elemento
         */
        ivIniciar = (ImageView) findViewById(R.id.ivInicio);
        /**
         * podem ser clicados nesta activity
         */
        ivIniciar.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_principal) {
            Intent intent = new Intent(this, MenuConfiguracaoActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_sobre){
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_logout){
            //TODO:Fazer o logout do usuario
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // o botao iniciar atividade foi clicado
        if(v == ivIniciar){

            Intent intent = new Intent(this, SelecionaJogoActivity.class);
            startActivity(intent);

        }

    }
}
