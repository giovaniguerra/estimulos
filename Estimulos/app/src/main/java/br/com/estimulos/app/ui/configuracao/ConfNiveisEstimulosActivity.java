package br.com.estimulos.app.ui.configuracao;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.ui.adapter.ListPosicaoAdapter;
import br.com.estimulos.dominio.EnumObjetoTela;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.Posicao;

public class ConfNiveisEstimulosActivity extends AppCompatActivity
        implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener{

    public static final String TAG = "CONF_NIVEIS_ACT";
    public static final int RESULT_NIVEIS = 1;
    public final static Integer TIPO_OBJETO_TELA = R.id.tipo_objeto_tela;
    private static final String FASE_ARRASTAR = "Fase Arrastar";
    private static final String FASE_TOCAR = "Fase Tocar";

    private FrameLayout conteiner;
    private Toolbar toolbar;
    private Button btSalvar;
//    private ListView listEstimulo;
    private List<String> posicoes;
    private ListPosicaoAdapter adapter;
    private List<ImageView> listImageViews;
    private Fase fase;
    private Nivel nivel;

    /** Posicao X onde se Inicia o toque na tela*/
    private Integer posicaoXInicial;
    /** Posicao Y onde se Inicia o toque na tela*/
    private Integer posicaoYInicial;
    /** Sensor de toques para escalar uma View*/
    private ScaleGestureDetector scaleGestureDetector;


    private Map<Integer, String> tiposEstimulo;

    private int larguraAtual;
    private int alturaAtual;

    private int contadorPrincipal;
    private int contadorAlvo;
    private int contadorInsignificante;

    private int qtdeMaxPrincipal;
    private int qtdeMaxAlvo;
    private int qtdeMaxInsignificante;

    private int width;
    private int height;

    private String tipoFase;

    /** Variaveis para controlar o dispatcher */
//
    private boolean eventInProgress;
    private boolean motionEventConsumed;
    private boolean cancelLongPressEvent;
    private boolean cancelMoveEvent;
    private View targetView;
    private Long timeToqueInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_niveis_estimulos);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conteiner = (FrameLayout) findViewById(R.id.containerView);
//        btSalvar.setOnClickListener(this);
//        listEstimulo = (ListView) findViewById(R.id.listViewEstimulos);
        scaleGestureDetector =
                new ScaleGestureDetector(this,
                        new MyOnScaleGestureListener());

        Intent intent = getIntent();
        fase = (Fase) intent.getSerializableExtra("fase");
        nivel = (Nivel) intent.getSerializableExtra("nivel");

        // TODO : Criar um meio melhor para descobrir qual eh a fase..
        if(fase.getNomeClasseHerdade().equals(FaseArrastar.class.getName()))
            tipoFase = FASE_ARRASTAR;
        else
            tipoFase = FASE_TOCAR;

        listImageViews = new ArrayList<>();
        /** Inicializar contadores*/
        contadorPrincipal = 0;
        contadorAlvo = 0;
        contadorInsignificante = 0;
        qtdeMaxPrincipal = 0;
        qtdeMaxAlvo = 0;
        qtdeMaxInsignificante = 0;

        /** Criar mapa de estimulos e criar limites para cada tipo de estimulo */
            /** Estimulo Principal */
        tiposEstimulo = new HashMap<>();
        tiposEstimulo.put(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto(), "Estímulo Principal");
        qtdeMaxPrincipal = 1;
            /** Estimulo Alvo */
        if(!fase.getNomeClasseHerdade().equals(FaseTocar.class.getName())) {
            tiposEstimulo.put(EnumObjetoTela.ESTIMULO_ALVO.getObjeto(), "Estímulo Alvo");
            qtdeMaxAlvo = 1;
        }
            /** Estimulo Insignificante */
        if(nivel.getQtdeEstimulos() > qtdeMaxPrincipal + qtdeMaxAlvo) {
            tiposEstimulo.put(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto(), "Estímulo Aleatório");
            qtdeMaxInsignificante = nivel.getQtdeEstimulos() - qtdeMaxPrincipal - qtdeMaxAlvo;
        }
//        posicoes = new ArrayList<>(tiposEstimulo.values());
//        adapter = new ListPosicaoAdapter(this, posicoes);
//        listEstimulo.setAdapter(adapter);

        /** Gerar tamanho das Views TODO: Hardcode aqui */
        larguraAtual = 140;
        alturaAtual = 140;

        /** Runnable pra recuperar largura e altura para criar as views */
        conteiner.post(new Runnable() {
            @Override
            public void run() {
//                FrameLayout c = redimensionarConteiner(getApplicationContext(), conteiner);
                width = conteiner.getWidth();
                height = conteiner.getHeight();
                adicionarPlanoDeFundo(getApplicationContext(), width, height, tipoFase);
                for(Posicao p : nivel.getPosicoes()){
                    View v = adicionarPosicao(conteiner, p);
                    if(v != null){
                        listImageViews.add((ImageView) v);
                        conteiner.addView(v);
                    }

                }
//                conteiner.addView(c);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_conf_niveis_estimulo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_salvar:
                List<Posicao> posicoes = new ArrayList<>();
                Log.d(TAG, "******* LISTAGEM DE POSICOES *******");
                for(ImageView iv : listImageViews){
                    Posicao posicao = new Posicao();

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv.getLayoutParams();
                    posicao.setLargura((float) iv.getWidth() / width);
                    posicao.setAltura((float) iv.getHeight() / height);
                    /** Usar conteiner/2 pois o gravity eh center */
                    posicao.setMargemX((float) iv.getLeft() / width);
                    posicao.setMargemY((float) iv.getTop() / height);
                    posicao.setTipo((int) iv.getTag(R.id.tipo_objeto_tela));
                    posicoes.add(posicao);

                    Log.d(TAG, "*** TIPO DA POSICAO *** : " + posicao.getTipo());
                    Log.d(TAG, "Largura da Posicao: " + posicao.getLargura());
                    Log.d(TAG, "Altura da Posicao: " + posicao.getAltura());
                    Log.d(TAG, "MargemX da posicao: " + posicao.getMargemX());
                    Log.d(TAG, "MargemY da posicao: " + posicao.getMargemY());
                }
                /** TODO : Discutir com o Caio, Guardar o ID das posicoes */
                for(int i = 0; i < nivel.getQtdeEstimulos(); i++){
                    posicoes.get(i).setID(nivel.getPosicoes().get(i).getID());
                }
                nivel.setPosicoes(posicoes);
                // retornar lista de posicoes
                Intent returnIntent = new Intent();
                returnIntent.putExtra("nivel", nivel);
                setResult(RESULT_OK, returnIntent);
                finish();
                super.onBackPressed();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
//    private FrameLayout redimensionarConteiner(Context context, FrameLayout conteiner) {
//        conteiner.setBackgroundColor(Color.TRANSPARENT);
//        FrameLayout newConteiner = new FrameLayout(context);
//
//        if(conteiner.getHeight() <= 0 || conteiner.getWidth() <= 0)
//            return null;
//
//        float newAltura = conteiner.getHeight();
//        float newLargura = newAltura * Util.PROPORCAO_LARGURA;
//        float margemX = conteiner.getWidth() - newLargura;// - listEstimulo.getWidth();
//
//        /** Atualizar altura e largura do conteiner */
//        height = (int) newAltura;
//        width = (int) newLargura;
//
//        LinearLayout.LayoutParams paramsNewConteiner;
//        paramsNewConteiner = new LinearLayout.LayoutParams((int) newLargura, (int) newAltura);
//        paramsNewConteiner.leftMargin = (int) Math.abs(margemX / 2);
//        newConteiner.setLayoutParams(paramsNewConteiner);
//
//        return newConteiner;
//    }


    private View adicionarPosicao(ViewGroup conteiner, Posicao p) {
        /** Testar se há posições para ser adicionadas */
        if(p == null){
            Log.e(TAG, "Lista de posições inválidas");
            return null;
        }
        /** Guardar largura e altura do conteiner */
//        int width = conteiner.getWidth();
//        int height = conteiner.getHeight();

        /** calcular as medidas passadas em % em relacao ao tamanho da tela */

        int alturaImagem = (int) (height * p.getAltura());
        int larguraImagem = (int) (width * p.getLargura());
        int margemX = (int) (width * p.getMargemX());
        int margemY = (int) (height * p.getMargemY());
        int tipoEstimulo = p.getTipo();

        // calcular o menor lado
        if(larguraImagem < alturaImagem) {
            alturaImagem = larguraImagem;
        } else {
            larguraImagem = alturaImagem;
        }

        /** Atualizar largura e altura atual */
        larguraAtual = larguraImagem;
        alturaAtual = alturaImagem;

        // Instanciar os params do Layout para nova View
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(larguraImagem, alturaImagem);
        params.leftMargin = margemX;
        params.topMargin = margemY;

        /** Criar e testar qual drawable usar */
        Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.retangulo);
        if (tipoEstimulo == EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()) {
            // Excedeu o limite? Avisar o usuario
            if(contadorPrincipal >= qtdeMaxPrincipal) {
                Toast.makeText(this, "Atingiu o limite de estímulos principais = " +
                        qtdeMaxPrincipal, Toast.LENGTH_SHORT).show();
                return null;
            }
            drawable.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
            contadorPrincipal++;
        } else if (tipoEstimulo == EnumObjetoTela.ESTIMULO_ALVO.getObjeto()) {
            if(contadorAlvo >= qtdeMaxAlvo) {
                Toast.makeText(this, "Atingiu o limite de estímulos alvo = " +
                        qtdeMaxAlvo, Toast.LENGTH_SHORT).show();
                return null;
            }
            drawable.setColorFilter(getResources().getColor(R.color.dodgeBlue), PorterDuff.Mode.SRC_ATOP);
            contadorAlvo++;
        } else {
            if(contadorInsignificante >= qtdeMaxInsignificante) {
                Toast.makeText(this, "Atingiu o limite de estímulos aleatórios = " +
                        qtdeMaxInsignificante, Toast.LENGTH_SHORT).show();
                return null;
            }
            drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            contadorInsignificante++;
        }

        /** Criar view de retorno e setar atributos */
        ImageView v = new ImageView(this);
        v.setLayoutParams(params);
        v.setImageDrawable(drawable);
        v.setTag(R.id.tipo_objeto_tela, tipoEstimulo);
        v.setOnTouchListener(this);
        v.setOnLongClickListener(this);

        return v;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        Log.i(TAG, "Entrou no dispatchTouchEvent ... Action = " + e.getAction());
        ViewConfiguration.get(this);

        if (eventInProgress) {
            // View shall only receive scale gesture event if visible
            if(e.getPointerCount() > 1) {
                scaleGestureDetector.onTouchEvent(e);
                cancelMoveEvent = true;
            } else {
                cancelMoveEvent = false;
            }
            if (scaleGestureDetector.isInProgress()) {
                motionEventConsumed = true;
                Log.d(TAG, "MotionEventConsumed = " + motionEventConsumed );
            }
        }

        if (motionEventConsumed) {
            if (e.getAction() == MotionEvent.ACTION_UP)
                motionEventConsumed = false;
            if (cancelLongPressEvent) {
                cancelLongPressEvent = false;
                targetView.cancelLongPress();
            }
            return (true);
        }

        // Get the action that was done on this touch event
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // store the X value when the user's finger was pressed down
                posicaoXInicial = (int) e.getX();
                posicaoYInicial = (int) e.getY();
                cancelLongPressEvent = true;
                eventInProgress = true;
                Date date = new Date();
                timeToqueInicial = date.getTime();
                break;
            }

            case MotionEvent.ACTION_MOVE:
                // When having moved by too many x or y pixels, then
                // cancel any ongoing long klick events
                Log.d(TAG, "Entrou no Action Move Dispatcher **** Qtde pixels movido = " + (Math.abs(e.getX() - posicaoXInicial)
                        + Math.abs(e.getY() - posicaoYInicial)));
                if(cancelMoveEvent){
                    Log.d(TAG, "onMove Dispatcher *** Cancelar move");
                    return true;
                }

                if (cancelLongPressEvent
                        && Math.abs(e.getX() - posicaoXInicial)
                        + Math.abs(e.getY() - posicaoYInicial) > 40) {
                    Log.d(TAG, "Deve cancelar o onLongClick");
                    targetView.cancelLongPress();
                    cancelLongPressEvent = false;
                }
                break;

            case MotionEvent.ACTION_UP: {
                if (eventInProgress) {
                    eventInProgress = false;
                    Log.d(TAG, "Entrou no ActionUP Dispatcher ... cancelLongPress = " + cancelLongPressEvent);
                    Date now = new Date();
                    if (cancelLongPressEvent
                            && Math.abs(e.getX() - posicaoXInicial)
                            + Math.abs(e.getY() - posicaoYInicial) > 40
                            && Math.abs(now.getTime() - timeToqueInicial) < ViewConfiguration.getLongPressTimeout()) {
                        Log.d(TAG, "Deve cancelar o onLongClick");
                        targetView.cancelLongPress();
                        cancelLongPressEvent = false;
                    }
                }
                break;
            }
        }

        // If event was not handled here, then forward it to parent,
        // i. e. to view hierarchy
        return (super.dispatchTouchEvent(e));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "Entrou no onTouch .. Action = " + event.getAction());

        targetView = v;
        // Tratar os eventos de Drag 'n Drop
        int action = event.getAction();
        /** Limitar quantidade de estimulos criados
         * ao clicar no botão de salvar, criar list de posicoes e retorna-las */

        // salvar posicoes iniciais de toque
        final int posicaoXAtual = (int) event.getRawX();
        final int posicaoYAtual = (int) event.getRawY();

        // Tratar eventos de toque (Apertar, Mover e Soltar)
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "Entrou no Action_Down");
                Log.d(TAG, "Touched view = " + v);

                // Salvar posicao (X,Y) do toque para usar no Drag
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                posicaoXInicial = posicaoXAtual - params.leftMargin;
                posicaoYInicial = posicaoYAtual - params.topMargin;
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "Entrou no Action_Move");
//                if(isScaling) {
//                    Log.d(TAG, "Cancelou onMove");
//                    return true;
//                }
                Log.d(TAG, "Tentou mover a view");
                moverView(v,
                        posicaoXAtual,
                        posicaoYAtual,
                        this.posicaoXInicial,
                        this.posicaoYInicial);

                return true;
            }
            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "********* Action_UP ********");

                return true;
            }

        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "Entrou no onLongClick");
        Log.d(TAG, "Long Cick Touched View = " + v);

        // Declara o objeto de Dialog
        final Dialog dialog = new Dialog(this);

        // Seta o titulo da dialog
        dialog.setTitle("Opções");

        // Adiciona o layout
        dialog.setContentView(R.layout.dialog_tipo_posicao);

        dialog.setCanceledOnTouchOutside(true);


        dialog.getWindow().setBackgroundDrawableResource(R.color.cinzaTransparente);

        // Exibe o dialog
        dialog.show();

        // Recupera as view do layout do dialog
        final Button btEstimuloPrincipal = (Button) dialog.findViewById(R.id.estimulo_principal);
        final Button btEstimuloAlvo = (Button) dialog.findViewById(R.id.estimulo_alvo);
        final Button btEstimuloInsignificante = (Button) dialog.findViewById(R.id.estimulo_insignificante);
        final Button btExcluir = (Button) dialog.findViewById(R.id.btExcluir);
        // adiciona os listeners
        btEstimuloPrincipal.setOnClickListener(this);
        btEstimuloAlvo.setOnClickListener(this);
        btEstimuloInsignificante.setOnClickListener(this);
        btExcluir.setOnClickListener(this);
        // adiciona as Tags
        btEstimuloPrincipal.setTag(R.id.tipo_objeto_tela, EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        btEstimuloAlvo.setTag(R.id.tipo_objeto_tela, EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        btEstimuloInsignificante.setTag(R.id.tipo_objeto_tela, EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());

        // Seta o clique no botao de cancelar
//        btCancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO: Limitar estimulos aos seus respectivos fundos.
        /** Caso o usuario tenha clicado no botão salvar*/
        if (v.getId() == R.id.btExcluir) {
            // Tocou no botão excluir
            Log.d(TAG, "Entrou no botão excluir");
            // TODO : Criar novo Dialog pedindo confirmação?
            targetView.setVisibility(View.GONE);
            listImageViews.remove(targetView);
            conteiner.removeView(targetView);

            int tipoEstimulo = (int) targetView.getTag(R.id.tipo_objeto_tela);
            if (tipoEstimulo == EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()) {
                contadorPrincipal--;
            } else if (tipoEstimulo == EnumObjetoTela.ESTIMULO_ALVO.getObjeto()) {
                contadorAlvo--;
            } else {
                contadorInsignificante--;
            }
            targetView = null;

        // Tocou no botão excluir
        } else {
            /** Tocou em uma View da lista */
            ImageView imageView = new ImageView(this);
            int tipoEstimulo = (int) v.getTag(R.id.tipo_objeto_tela);

            Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.retangulo);
            if (tipoEstimulo == EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()) {
                // Excedeu o limite? Avisar o usuario
                if(contadorPrincipal >= qtdeMaxPrincipal) {
                    Toast.makeText(this, "Atingiu o limite de estímulos principais = " +
                            qtdeMaxPrincipal, Toast.LENGTH_SHORT).show();
                    return;
                }
                drawable.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
                contadorPrincipal++;
            } else if (tipoEstimulo == EnumObjetoTela.ESTIMULO_ALVO.getObjeto()) {
                if(contadorAlvo >= qtdeMaxAlvo) {
                    Toast.makeText(this, "Atingiu o limite de estímulos alvo = " +
                            qtdeMaxAlvo, Toast.LENGTH_SHORT).show();
                    return;
                }
                drawable.setColorFilter(getResources().getColor(R.color.dodgeBlue), PorterDuff.Mode.SRC_ATOP);
                contadorAlvo++;
            } else {
                if(contadorInsignificante >= qtdeMaxInsignificante) {
                    Toast.makeText(this, "Atingiu o limite de estímulos aleatórios = " +
                            qtdeMaxInsignificante, Toast.LENGTH_SHORT).show();
                    return;
                }
                drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                contadorInsignificante++;
            }

            FrameLayout.LayoutParams paramsBotao = new FrameLayout.LayoutParams(larguraAtual, alturaAtual);
            // TODO : Corrigir Gravity
//            paramsBotao.gravity = Gravity.CENTER;
            paramsBotao.leftMargin = conteiner.getWidth() /2;
            paramsBotao.topMargin = conteiner.getHeight() / 2;
            imageView.setImageDrawable(drawable);
            imageView.setTag(R.id.tipo_objeto_tela, tipoEstimulo);
            imageView.setLayoutParams(paramsBotao);
            imageView.setOnTouchListener(this);
            imageView.setOnLongClickListener(this);
            listImageViews.add(imageView);
            conteiner.addView(imageView);
        }
    }

    private void moverView(View v, int atualX, int atualY, int inicialX, int inicialY){

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();

        /** Delta = atual - inicial */
        int posicaoX = v.getLeft() - lp.leftMargin + atualX - inicialX + v.getWidth();
        int posicaoY = v.getTop() - lp.topMargin + atualY - inicialY + v.getHeight();

        /** Testar limites do conteiner */
        if(!(posicaoX > conteiner.getWidth()) && !(posicaoX < v.getWidth())){
            lp.leftMargin = atualX - inicialX;
        }
        if(!(posicaoY > conteiner.getHeight()) && !(posicaoY < v.getHeight())){
            lp.topMargin = atualY - inicialY;
        }

        v.setLayoutParams(lp);
    }

    private void adicionarPlanoDeFundo(Context context, int width, int height, String tipoFase){
        if(tipoFase.equals(FASE_ARRASTAR)){
            addPlanoFundoArrastar(context, conteiner, width, height);
        } else {
            addPlanoFundoTocar(context, conteiner, width, height);
        }
    }

    /**  TODO : Criar Strategy para preencher plano de fundo */
    private void addPlanoFundoArrastar(Context context, ViewGroup conteiner, int largura, int altura) {
        int margemX = (int) (largura * 0.005);

        FrameLayout.LayoutParams paramsFundo = new FrameLayout.LayoutParams(largura / 2 - margemX, altura);
        paramsFundo.leftMargin = 0;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        FrameLayout fundoEsquerdo = new FrameLayout(context);
        fundoEsquerdo.setLayoutParams(paramsFundo);
        fundoEsquerdo.setBackgroundResource(R.drawable.gradiente_azul);
        fundoEsquerdo.setTag(TIPO_OBJETO_TELA, EnumObjetoTela.LADO_ESQUERDO);
        fundoEsquerdo.setOnLongClickListener(this);

        paramsFundo = new FrameLayout.LayoutParams(largura / 2, altura);
        paramsFundo.leftMargin = largura / 2 + margemX;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        FrameLayout fundoDireito = new FrameLayout(context);
        fundoDireito.setBackgroundResource(R.drawable.gradiente_verde);
        fundoDireito.setLayoutParams(paramsFundo);
        fundoDireito.setTag(TIPO_OBJETO_TELA, EnumObjetoTela.LADO_DIREITO);
        fundoDireito.setOnLongClickListener(this);

        conteiner.addView(fundoEsquerdo);
        conteiner.addView(fundoDireito);

    }

    private void addPlanoFundoTocar(Context context, ViewGroup conteiner, int largura, int altura) {
        FrameLayout.LayoutParams paramsFundo = new FrameLayout.LayoutParams(largura, altura);
        paramsFundo.leftMargin = 0;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        FrameLayout fundo = new FrameLayout(context);
        fundo.setLayoutParams(paramsFundo);
        fundo.setBackgroundResource(R.drawable.gradiente_azul);
//        fundo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fundo.setOnLongClickListener(this);

        conteiner.addView(fundo);
    }

    private class MyOnScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d(TAG, "Entrou no onScale");
            // Recuperar o valor que será utilizado para ampliar ou reduzir a imagem
            float scaleFactor = detector.getScaleFactor();

            int newSize;
            /** Ampliar ou reduzir imagem de acordo com o menor lado da imagem */
            if(alturaAtual < larguraAtual)
                newSize = (int) (alturaAtual * scaleFactor - alturaAtual);
            else
                newSize = (int) (larguraAtual * scaleFactor - larguraAtual);

            // TODO : Criar valor minimo e maximo para o tamanho de um estimulo
            larguraAtual += newSize;
            alturaAtual += newSize;

            for(ImageView v : listImageViews){
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                params.width = larguraAtual;
                params.height = alturaAtual;

                v.setLayoutParams(params);
            }
            return true;
        }
    }
}
