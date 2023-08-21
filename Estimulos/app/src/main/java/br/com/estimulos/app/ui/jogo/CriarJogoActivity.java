package br.com.estimulos.app.ui.jogo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.core.strategy.FaseArrastarStrategy;
import br.com.estimulos.app.core.strategy.FaseTocarStrategy;
import br.com.estimulos.app.interfaces.IStrategyFase;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.Reforcador;


public class CriarJogoActivity extends AppCompatActivity
        implements  View.OnTouchListener, TextToSpeech.OnInitListener,
            Animation.AnimationListener, Runnable {
    /**
     */
    // Constantes
    public final static String TAG = "CRIAR_JOGO_ACTIVITY";

    /**
     */
    // Variaveis
    private GameBuilder builder;
    private FrameLayout conteiner;
    private ProgressDialog prgDialog;

    private Map<String, IStrategyFase> mapFases;
    private IStrategyFase strategy;

    /* Guardar o tamanho do layout aqui*/
    private Integer width;
    private Integer height;

    private int contadorErros;
    private Reforcador reforcadorTeste;

    /** Variaveis do TTS */
    //
    private TextToSpeech textToSpeech;
    private boolean TTSisReady;
//    private Sintetizador sintTTS;
    private boolean running;
    private static final int INTERVAL = 8000;
    private Thread minhaThread;
    private boolean primeiraVez = true;
    private String acao;
//    private int limiteVoz = 5;   SUBSTITUIDO PELO ATRIBUTO DO NIVEL
    private int contadorLimiteVoz = 0;
    private  MediaPlayer somErro;

    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogar_fase_arrastar);

        somErro = MediaPlayer.create(this, R.raw.som_erro);

        // TODO : Apagar esse teste do reforcador
        ReforcadorDAO dao = new ReforcadorDAO(BancoDadosHelper.getInstance(this));
        reforcadorTeste = new Reforcador();
        contadorErros = 0;

        try {
            reforcadorTeste = dao.visualizar(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO : apagar até aqui

        mapFases = new HashMap<>();
        mapFases.put(NivelArrastar.class.getName(), new FaseArrastarStrategy());
        mapFases.put(NivelTocar.class.getName(), new FaseTocarStrategy());

        // recuperar o builder vindo da escolha de nivel
        builder = (GameBuilder) getIntent().getSerializableExtra(GameBuilder.TAG);
        // Liberar toques na tela
        builder.setToqueBloqueado(false);

        strategy = mapFases.get(builder.getNivel().getClass().getName());

        conteiner = (FrameLayout) findViewById(R.id.conteiner);

        /** Instanciar TTS e seus parametros */
        // TODO : Mudar isso aqui
        if(builder.getNivel().getClass().getName().equals(NivelArrastar.class.getName())) {
            acao = "Arraste ";
        } else {
            acao = "Toque ";
        }

        /** Instanciar o Dialog de Carregamento */
//        prgDialog = new ProgressDialog(this);
//        prgDialog.setMessage("Carregando...");
//        prgDialog.setCancelable(false);
//        prgDialog.show();

        /** Inicializar o Text-To-Speech */
        if(!TTSisReady) {
            startTTS();
        }

        running = true;
        primeiraVez = true;

        minhaThread = new Thread(this);
        minhaThread.setPriority(Thread.MIN_PRIORITY);

        /** Runnable pra recuperar largura e altura para criar as views */
        conteiner.post(new Runnable() {
            @Override
            public void run() {

                width = conteiner.getWidth();
                height = conteiner.getHeight();

                // TODO : Alterar aqui para tentar fazer a ideia de carregar as imagens no final.
                views = strategy.criarViews(builder, width, height, getApplicationContext());


                for (View v : views) {
                    v.setOnTouchListener(CriarJogoActivity.this);
                    conteiner.addView(v);
                }
//                prgDialog.dismiss();

            }
        });

    }

    @Override
    protected void onPause() {
        /** Parar o sintetizador de voz se não estiver nulo */
        if (this.textToSpeech != null) {
            this.textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        if (this.textToSpeech != null) {
            this.textToSpeech.shutdown();
            TTSisReady = false;
        }
        if(!minhaThread.isInterrupted())
            minhaThread.interrupt();
        somErro = null;
        super.onDestroy();
    }
    /**
     * Implementações do OnInitListener
     */
//
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(new Locale("pt", "pt"));
            if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                this.prgDialog.dismiss();
                Toast.makeText(this, "Linguagem não suportada!", Toast.LENGTH_LONG).show();
            } else if (result == TextToSpeech.LANG_MISSING_DATA) {
                this.prgDialog.dismiss();
                Toast.makeText(this, "Linguagem não disponível", Toast.LENGTH_SHORT).show();
                // Request missing data.
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            } else {
                TTSisReady = true;
                minhaThread.start();
                this.prgDialog.dismiss();
            }
        } else {
            Toast.makeText(this, "Erro no Text to Speech", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método responsável por iniciar o Sintetizador
     */
    private void startTTS() {
        // Show a progress dialog.
        this.prgDialog = ProgressDialog.show(this, "Sintetizador de voz", "Carregando", true);
        // Starts an intent that will check if the required data for the synthesizer is available.
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, getResources().getInteger(R.integer.prep_tts));
    }

//    private FrameLayout redimensionarConteiner(FrameLayout conteiner) {
//        if(conteiner.getHeight() <= 0 || conteiner.getWidth() <= 0)
//            return null;
//
//        float newAltura = conteiner.getHeight();
//        float newLargura = newAltura * Util.PROPORCAO_LARGURA;
//        float margemX = conteiner.getWidth() - newLargura;
//
//        /** Atualizar altura e largura do conteiner */
//        height = (int) newAltura;
//        width = (int) newLargura;
//
//        RelativeLayout.LayoutParams paramsNewConteiner;
//        paramsNewConteiner = new RelativeLayout.LayoutParams((int) newLargura, (int) newAltura);
//        paramsNewConteiner.leftMargin = (int) (margemX / 2);
//        conteiner.setLayoutParams(paramsNewConteiner);
//
//        return conteiner;
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == getResources().getInteger(R.integer.request_acerto)){
            if(resultCode == RESULT_OK){

                // chamar proxima tarefa
                Intent intent = new Intent(this, CriarJogoActivity.class);
                intent.putExtra(GameBuilder.TAG, builder);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if(!minhaThread.isInterrupted())
                    minhaThread.interrupt();
//                    onDestroy();

            }
        }
        else if(requestCode == getResources().getInteger(R.integer.request_reforcador)) {
            if (resultCode == RESULT_OK) {
                // zerar o nivel e voltar para tela de escolha do nivel
                builder.setNivel(null);
                // zerar contadores;
                builder.setContadorTarefa(0);
                Intent intent = new Intent(this, SelecionaNivelActivity.class);
                intent.putExtra(GameBuilder.TAG, builder);
                startActivity(intent);
            }
        } else if (requestCode == getResources().getInteger(R.integer.prep_tts)) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // Success, create the synthesizer.
                if (this.textToSpeech == null) {
                    this.textToSpeech = new TextToSpeech(this, this);
                }
            } else {
                this.prgDialog.dismiss();
                Log.e(TAG, "Um erro ocorreu enquanto o sintetizador iniciava");
                // Error, request missing data.
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Testar se por algum motivo o toque nao deve ser processado
        if(builder.isToqueBloqueado())
            return false;

        boolean resultado = strategy.onTouch(v, event, conteiner, getApplicationContext(), builder);

        /** Acertou a tarefa */
        if(resultado){
            if(event.getAction() == MotionEvent.ACTION_UP) {

                ReforcadorDAO dao = new ReforcadorDAO(BancoDadosHelper.getInstance(this));
                Reforcador r = null;
                Log.d(TAG, "Passou a tarefa!!!");
                // parar de rodar a fala
                running = false;

                Intent intent;

                // incrementar mais um no contador de tarefas
                builder.setContadorTarefa(builder.getContadorTarefa() + 1);
                // eh a ultima tarefa?
                if(builder.getContadorTarefa() >= builder.getNivel().getNumTarefas()){
                    Log.d(TAG, "Eh a ultima tarefa, mostrar reforcador!!!");

                    try {
                        // TODO : USAR FILTRO E METODO PESQUISA
                        r = dao.visualizar(3);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                    intent = new Intent(this, ReforcadorStartActivity.class);
                    intent.putExtra(ReforcadorStartActivity.REFORCADOR_TAG, r);
                    startActivityForResult(intent, getResources().getInteger(R.integer.request_reforcador));
                    if(!minhaThread.isInterrupted())
                        minhaThread.interrupt();
//                    onDestroy();
                } else {
                    try {
                        // TODO : USAR FILTRO E METODO PESQUISA
                        r = dao.visualizar(1);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                    intent = new Intent(this, ReforcadorStartActivity.class);
                    intent.putExtra(ReforcadorStartActivity.REFORCADOR_TAG, r);
                    startActivityForResult(intent, getResources().getInteger(R.integer.request_acerto));
                }
            }
        // Errou a Tarefa
        } else {
            Log.d(TAG, "Errou na tarefa!");
            if(contadorErros < builder.getNivel().getLimiteSomDeErro()){

                somErro.start();
            }

//                ReforcadorStartActivity.playAudio("///storage/emulated/0/Pictures/Estimulos/audios/a4.mp3", 1000);
            // Incrementar contador de erros e testar limite
            // TODO: pegar quantidade de erros salvos no builder
            if(++contadorErros == builder.getNivel().getLimiteErros()){
                // bloquear proximos toques e carregar animacao da tela
                builder.setToqueBloqueado(true);
                // interromper o TTS
                if(!minhaThread.isInterrupted()) {
                    minhaThread.interrupt();
                }
                Log.d(TAG, "Entrou no FadeOut!");
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                anim.setAnimationListener(this);
                conteiner.startAnimation(anim);
            }
        }


        return resultado;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "Entrou no Animation End!");
        // manter o layout escuro
        conteiner.setVisibility(View.INVISIBLE);
        // chamar proxima tarefa
        Intent intent = new Intent(this, CriarJogoActivity.class);
        intent.putExtra(GameBuilder.TAG, builder);
        startActivity(intent);
//        onDestroy();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if(primeiraVez){
            falar();
            primeiraVez = false;
        }

        while (running){
            try{
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                if(e.getMessage() != null)
                    Log.e(TAG, e.getMessage());
            }
            falar();
        }
    }

    private void falar() {
        if(contadorLimiteVoz >= builder.getNivel().getLimiteInstrucoesTTS()){
            running = false;
        }
        if(running){
            contadorLimiteVoz++;
            textToSpeech.speak(acao + " " + builder.getEstimuloPrincipalAtual().getGenero() +" " + builder.getEstimuloPrincipalAtual().getNome(),
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
