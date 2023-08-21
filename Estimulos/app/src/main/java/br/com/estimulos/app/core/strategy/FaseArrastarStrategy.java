package br.com.estimulos.app.core.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.aplicacao.GameBuilder;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.app.core.dao.TarefaDAO;
import br.com.estimulos.app.core.dao.TentativaDAO;
import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.app.interfaces.IStrategyFase;
import br.com.estimulos.app.ui.jogo.CriarJogoActivity;
import br.com.estimulos.dominio.EnumObjetoTela;
import br.com.estimulos.dominio.EnumEstatistica;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.ObjetoTela;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Tentativa;
import br.com.estimulos.dominio.TipoResultado;

/**
 * Classe responsavel por criar e tratar eventos de toque da Fase de Arrastar do jogo.
 *
 * Created by Giovani on 03/03/2016.
 */
public class FaseArrastarStrategy implements IStrategyFase, Animation.AnimationListener {

    /** Constantes
     */
    public final static String TAG = "StrategyArrastar";
    public final static Integer ID_ESTIMULO = R.id.id_estimulo;
    public final static Integer TIPO_OBJETO_TELA = R.id.tipo_objeto_tela;

    /** Variaveis
     */

    /**
     * Nivel que contem as informacoes para criar uma nova Tarefa
     */
    private NivelArrastar nivel;

    /**
     * Atributo que contem todos os dados da tarefa que sera criado por essa estrategia
     */
    private Tarefa tarefa;

    /**
     * Variavel para guardar dados da posicao de um estimulo
     */
    private LocalEstimulo localEstimulo;

    /**
     * List com todas as views que serao criadas nesta Tarefa
     */
    private List<View> views;

    /**
     * Array com dois ImageViews contendo as duas mascaras utilizadas para o Drag 'n Drop
     * Lista com os ImageViews das duas mascaras utilizadas
     */
    private ImageView[] listMasks;

    /**
     * View utilizada para simular o modo Arrastar na tela
     */
    private ImageView dragView;

    /**
     * Posicao X onde se Inicia o toque na tela
     */
    private Integer posicaoXInicial;

    /**
     * Posicao Y onde se Inicia o toque na tela
     */
    private Integer posicaoYInicial;

    /**
     * Inteira para controlar as gravações das coordenadas durante a ação de arrastar
     */
    int contador=0;

    /**
     * Lista com Ids dos estimulos insignificantes ja escolhidos
     */
    private List<Integer> listIdInsig;

    /**
     * Flag indicando se a mascara alvo foi criada ou não
     * TRUE - indica que foi criada
     * FALSE - Indica que não foi criada
     */
    private boolean flgCriouAlvo;

    /**
     * Variavel para guardar um valor randomico
     */
    private int random;

    /**
     * Index para armazenar posicao do contador na lista
     */
    private int contadorLocal;


    /**
     * Variavel para guardar dados de uma tentativa
     */
    private Tentativa tentativa;

    /**
     * Variavel para guardar os movimentos
     */
    private Movimento movimento;

    /**
     * Lista para guardar todos os movimentos da tentativa
     */
    private List<Movimento> movimentos;

    /**
     * Variavel que representa o tipo do resultado
     */
    private TipoResultado tipo;

    /**
     * Construtor
     */
    public FaseArrastarStrategy(){
        views = new ArrayList<>();
        listMasks = new ImageView[2];
        listIdInsig = new ArrayList<>();
        flgCriouAlvo = false;
        contadorLocal = 0;
    }

    /**
     * Implementacoes do IStrategy
     * */
//IStrategy
    @Override
    public List<View> criarViews(GameBuilder builder, int width, int height, Context context) {
        // recuperar o nivel vindo do builder
        nivel = (NivelArrastar) builder.getNivel();

        // TODO : remover esta linha, somente para teste
//        nivel.setRandPosicao(true);

        List<Estimulo> estimulosInsignificante = new ArrayList<>();
        List<Estimulo> estimulosPrincipais = new ArrayList<>();

        Map<String, String> filtro = new HashMap<>();
        filtro.put(EstimuloDAO.Tabela.CATEGORIA_ESTIMULO.getName(), builder.getIdCategoria().toString());

        EstimuloDAO estmDAO = new EstimuloDAO(BancoDadosHelper.getInstance(context));
        try {
            estimulosInsignificante = estmDAO.consultarEstimuloInsignificante(builder.getIdCategoria());
            estimulosPrincipais = estmDAO.pesquisa(filtro);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        builder.setEstimulosInsignificantes(estimulosInsignificante);
        builder.setEstimulosPrincipais(estimulosPrincipais);


        // TODO MUDAR ATÉ AQUI

        // instanciar uma nova Tarefa
        tarefa = new Tarefa();
        tarefa.setNumTarefa(builder.getContadorTarefa() + 1);
        tarefa.setCompletada(false);
        tarefa.setListLocais(new ArrayList<LocalEstimulo>());

        /** Adicionar cor de fundo para o layout */
        addPlanoFundo(context, width, height);

        /** Adicionar seta ao layout*/
        addSeta(context, width, height);

        /** Adicionar os estimulos ao layout*/
        int qtde = nivel.getPosicoes().size();

        // criar randomico para utilizar dentro do metodo NewView
        random = new Random().nextInt(builder.getEstimulosPrincipais().size());

        // zerar lista de Ids dos estimulos insignificantes
        listIdInsig = new ArrayList<>();

        // criar tela baseado na margem top e left de cada imagem
        for(int i = 0; i < qtde; i++){
            Posicao p = nivel.getPosicoes().get(i);
            // salvar posicao no LocalEstimulo
            localEstimulo = new LocalEstimulo();
            localEstimulo.setPosicao(p);

            int alturaImagem = (int) (height * p.getAltura());
            int larguraImagem = (int) (width * p.getLargura());
            int margemY = (int) (height * p.getMargemY());
            int margemX = (int) (width * p.getMargemX());

            Integer tipoEstimulo = !nivel.isRandPosicao() ||
                    p.getTipo().equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()) ?
                    p.getTipo() : criarPosicaoRandomica(qtde, i);

            newView(builder,
                    context,
                    larguraImagem,
                    alturaImagem,
                    margemX,
                    margemY,
                    tipoEstimulo);
        }
        // TODO : tirar os dados inseridos no hard code
        tarefa.setFaseId(3);
        guardarTarefa(context, tarefa);
        return views;
    }

    /** ************ TIPOS TENTATIVA ***********
     *
     *
     *  *** ACTION_DOWN (TOQUE)
     *
     *  1 Não tocou no estímulo principal {
     *      1.1 Tocou no lado esquerdo do fundo.
     *      1.2 Tocou no lado direito do fundo.
     *      1.3 Tocou na seta.
     *      1.4 Tocou no Estimulo Alvo
     *      1.5 Tocou no Estimulo Insignificante.
     *      1.6 Tocou no fundo (parte branca do meio, se houver na fase).
     *
     *      Salvar 1 Movimento
     *      1 ObjetoTela inicial e 1 final iguais
     *      Resultado : nome =  (1)
     *                  descricao = Tocou no 1.x + nomeObjeto (se for um estimulo).
     *  }
     *
     *  2 Acertou um pixel invisivel da mascara do estimulo principal {
     *      Neste ponto, ele acertou o 1.1
     *  }
     *
     *  *** ACTION_MOVE (ARRASTANDO)
     *
     *  1 {
     *      Apenas guardar um novo Movimento.
     *  }
     *
     *  *** ACTION_UP (ARRASTOU/SOLTOU)
     *
     *  1 Não acertou o alvo (objeto do pareamento) {
     *      1.1 Arrastou o nomeObjeto para o {
     *          1.1~1.6
     *      }
     *  }
     *
     */
    //

    @Override
    public boolean onTouch(View v, MotionEvent event, ViewGroup conteiner, Context context, GameBuilder builder) {
        // instanciar uma nova tentativa
        if(tentativa == null) {
            tentativa = new Tentativa();
            movimentos = new ArrayList<>();
            tentativa.setResultado(new TipoResultado());
        }
        // Guardar acao realizada pelo usuario
        int action = event.getAction();
        // Salvar posicoes iniciais de toque
        final int posicaoXAtual = (int) event.getRawX();
        final int posicaoYAtual = (int) event.getRawY();
        // Testar se é o estimulo principal sendo arrastado.
        if(v.getTag(TIPO_OBJETO_TELA) == null || !v.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())) {

        //TODO:Registrar Tentativa
//            registrarTentativa(v, event, builder, (int) event.getX(), (int) event.getY());
            return false;
        }
        // Converter View para ImageView
        ImageView imgView = (ImageView) v;
        // Tratar eventos de toque (Apertar, Mover e Soltar)
        switch (action){
            case MotionEvent.ACTION_DOWN : {
                // Salvar posicao (X,Y) do toque para usar no Drag
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                posicaoXInicial = posicaoXAtual - params.leftMargin;
                posicaoYInicial = posicaoYAtual - params.topMargin;

                /** Testar o pixel tocado no Estimulo Principal e ver se a mascara tambem foi acertada
                 * O pixel tocado nao eh preto? */
                if(recuperarCorPixel(listMasks[0], event.getX(), event.getY()) ==  Color.TRANSPARENT) {
                    // TODO : acertou o ImageView mas nao acertou a mascara (na vdd acertou algum fundo)
                    return false;                           // cancelar toque
                } else {
                    // TODO : tirar os dados inseridos no hard code
                    ObjetoTela objTela = new ObjetoTela();
                    objTela.setTipoObjetoTela((int) imgView.getTag(TIPO_OBJETO_TELA));
                    objTela.setCodigo("Inicial");
                    objTela.setID(10);
                    tentativa.setObjetoInicial(objTela);
                    // TODO :  a mascaracertoua, continuar com drag e salvar esse movimento inicial, objetoTela que tocou(estimulo)
                }
                Bitmap novoBitmap = recortarImagem(imgView, listMasks[0]);
                // tornar a view original invisivel
                imgView.setVisibility(View.INVISIBLE);
                // Adicionar a Imagem da Mascara ao Layout e tornar visivel
                if(dragView == null)
                    criarDragView(conteiner, context, v, novoBitmap);
                return true;
            }

            case MotionEvent.ACTION_MOVE : {
                // Apenas atualizar a posicao (X,Y) da Imagem da Mascara
//                registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
                moverDragView(conteiner,
                        dragView,
                        posicaoXAtual,
                        posicaoYAtual,
                        this.posicaoXInicial,
                        this.posicaoYInicial);
                return true;
            }

            case MotionEvent.ACTION_UP : {
                // Testar se soltou dentro de um ImageView
                List<ImageView> alvos = buscarView(posicaoXAtual, posicaoYAtual);
//                ImageView imgAlvo = buscarView(posicaoXAtual, posicaoYAtual);
                // nao encontrou nenhum alvo?
                if(alvos == null) {
                    Log.d(TAG, "Nao encontrou nenhuma view onde foi solto, TagView");
                    voltarDragView(dragView, v);
                    // TODO : buscar o ID do erro no banco a partir da descrição. Settar o ID na entidade, e a descrição no TipoResultado
                    // TODO : salvar esta tentativa (o ultimo movimento, o objetoTelaFinal e o resultado deste erro)

                    return false;
                }
                ImageView imgAlvo = null;
                for(ImageView iv : alvos){
                    if(iv.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())){
                        imgAlvo = iv;
                    } else {
                        if(iv.getTag(TIPO_OBJETO_TELA) != null) {
                            Log.d(TAG, "Alvo errado, TagView = " + iv.getTag(TIPO_OBJETO_TELA));
                            if(iv.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.LADO_ESQUERDO)){
                                tentativa.getResultado().setID(EnumEstatistica.SOLTOU_LADO_ESQUERDO.getObjeto());
                                Log.d(TAG, "AEEEOOO - Lado Esquerdo");
                            }
                            if(iv.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.LADO_DIREITO)){
                                tentativa.getResultado().setID(EnumEstatistica.SOLTOU_LADO_DIREITO.getObjeto());
                                Log.d(TAG, "AEEEOOO - Lado Direito");
                            }
                            if(iv.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.SETA)){
                                tentativa.getResultado().setID(EnumEstatistica.SOLTOU_SETA.getObjeto());
                                Log.d(TAG, "AEEEOOO - Seta");
                            }

                        }
                    }
                }
                // testar se o imageView esta nulo
//                registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
                // Comparar Tag para ver se o alvo eh o correto

                if(imgAlvo == null || !imgAlvo.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())) {
                    Log.d(TAG, "Alvo errado !!");
                    // voltar Imagem ao local e torná-la visivel novamente.
                    voltarDragView(dragView, v);
//                    registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
                    return false;
                }
                // buscar as margens da view encontrada
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgView.getLayoutParams();
                // Testar o pixel tocado no Estimulo Alvo e ver se a  mascara tambem foi acertada
                // O pixel tocado nao eh preto?
                if(recuperarCorPixel(listMasks[1], event.getX() - imgAlvo.getLeft() + params.leftMargin,
                        event.getY() - imgAlvo.getTop() + params.topMargin) == Color.TRANSPARENT) {
                    Log.d(CriarJogoActivity.TAG, "Errou!!");
                    voltarDragView(dragView, v);
//                    registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
                    return false;
                } else {
                    tentativa.getResultado().setID(EnumEstatistica.ACERTOU.getObjeto());
                    Log.d(CriarJogoActivity.TAG, "Acertou o pixel do alvo!!");
                    imgAlvo.setAlpha(255);
                    removerDragView(conteiner);
                    tarefa.setCompletada(true);
                    atualizarTarefa(context, tarefa);
//                    registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
                    return true;
                }
            }
        }
        return true;
    }
// Fim IStrategy

    /**
     * Implementacoes do AnimationListener
     */
// AnimationListener

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG, "Animacao foi startada!");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        for(View v : views){
            if(v.getTag(TIPO_OBJETO_TELA) != null && v.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()))
                v.setVisibility(View.VISIBLE);
        }
        FrameLayout conteiner = (FrameLayout) dragView.getParent();
        removerDragView(conteiner);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * Metodos privados
     * */
// Metodos privados

    /**
     *
     * @param v - View que foi tocada
     * @param event - Evento relacionado ao toque
     * @param builder - GameBuilder deste jogo
     * @param posicaoX - Posicao X atual do toque
     * @param posicaoY - Posicao Y atual do toque
     */
    private void registrarTentativa(View v, MotionEvent event, GameBuilder builder, int posicaoX, int posicaoY){
        /** Usando essas variaveis, poderia criar uma frase como:
         * Descricao do erro : "Tocou no fundo direito." */
        String acao = null;
        String preposicao = " no "; // TODO : criar preposicao baseado no masc. ou fem. do estimulo
//        String tipoObjeto;
        String nomeObjeto = "Teste";
        Estimulo estimulo = null;

        tentativa.setTarefa(tarefa);
        ObjetoTela objeto = new ObjetoTela();
        switch(event.getAction()){
            // Errou no toque?
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Errou no começo");
                acao = "Tocou";
                converterTags(v, builder, nomeObjeto, estimulo);
                objeto.setTipoObjetoTela((int) v.getTag(TIPO_OBJETO_TELA));
                // TODO : remover dados inseridos no hard code
                objeto.setCodigo("Codigo");
                if(estimulo != null)
                    objeto.setCodigo(estimulo.getID().toString());
                else
                    objeto.setCodigo(nomeObjeto);
                tentativa.setObjetoFinal(objeto);
                break;
            // Errou ao soltar o objeto?
            case MotionEvent.ACTION_MOVE:
                contador++;
                if(contador%10 == 0){
                    movimento = new Movimento();
                    movimento.setX(posicaoX);
                    movimento.setY(posicaoY);
                    movimentos.add(movimento);
                    System.out.println("[SALVAR]: Contador:"+contador+" Posicao Inicial: X=" + posicaoXInicial + " Y=" + posicaoYInicial + "   Posicao Atual: X=" + posicaoX + " Y=" + posicaoY);
                }
                break;
            case MotionEvent.ACTION_UP:
                tentativa.setMovimentos(movimentos);
                String msgResultado;
                // TODO : verificar se devemos retirar esta listagem ou não
                System.out.println("---- LISTAGEM DE TODOS OS MOVIMENTOS -----");
                for(int i = 0; i < tentativa.getMovimentos().size(); i++){
                    System.out.println("Posicao X: "+tentativa.getMovimentos().get(i).getX()+" Y:"+tentativa.getMovimentos().get(i).getY());
                }
                System.out.println("Resultado: " + tentativa.getResultado().getID());
                acao = "Arrastou";
                converterTags(v, builder, nomeObjeto, estimulo);
                objeto.setTipoObjetoTela((int) v.getTag(TIPO_OBJETO_TELA));
                if(estimulo != null)
                    objeto.setCodigo(estimulo.getID().toString());
                else
                    objeto.setCodigo(nomeObjeto);
                tentativa.setObjetoFinal(objeto);

                System.out.println(tentativa.getTarefa().getID());
                TentativaDAO tentativaDAO = new TentativaDAO(BancoDadosHelper.getInstance(v.getContext()));
                // TODO : remover dados inseridos no hard code
                tentativa.getObjetoFinal().setCodigo("Final");
                try {
                    tentativaDAO.salvar(tentativa);
                    System.out.println("Salvou!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                // evento nao utilizado, nao fazer nada
        }
    }

    /**
     *
     * @param v - View que foi tocada
     * @param builder - GameBuilder deste jogo
     * @param nomeObjeto - Variavel que recebera o tipo de objeto ou nulo se nao for conhecido
     * @param estimulo - Variavel que recebera um estimulo se o tipo for de um estimulo ou nulo
     *                 se nao encontrado.
     */
    private void converterTags(View v, GameBuilder builder, String nomeObjeto, Estimulo estimulo){
        Integer tipoObjetoTag = (int) v.getTag(TIPO_OBJETO_TELA);
        if(tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())
            || tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())){
            nomeObjeto = "estímulo";
            // fazer loop para procurar estimulo na lista
            for(Estimulo e : builder.getEstimulosPrincipais()){
                if(e.getID().equals(v.getTag(ID_ESTIMULO))) {
                    estimulo = e;
                    break;
                }
            }
        } else if(tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto())){
            nomeObjeto = "estímulo";
            for(Estimulo e : builder.getEstimulosInsignificantes()){
                if(e.getID().equals(v.getTag(ID_ESTIMULO))){
                    estimulo = e;
                    break;
                }
            }
        } else if(tipoObjetoTag.equals(EnumObjetoTela.LADO_ESQUERDO.getObjeto())){
            nomeObjeto = "fundo esquerdo";
        } else if(tipoObjetoTag.equals(EnumObjetoTela.LADO_DIREITO.getObjeto())) {
            nomeObjeto = "fundo direito";
        } else if(tipoObjetoTag.equals(EnumObjetoTela.SETA.getObjeto())) {
            nomeObjeto = "seta";
        } else if(tipoObjetoTag.equals(EnumObjetoTela.FUNDO.getObjeto())) {
            nomeObjeto = "fundo";
        } else {
            nomeObjeto = null;
            estimulo = null;
        }
    }

    /**
     * Metodo responsavel por criar um novo Estimulo na tela e adicionar a lista de Views
     * @param builder O GameBuider que contem as informacoes dos estimulos
     * @param context Contexto da Aplicacao
     * @param larguraImagem Largura em pixels do ImageView
     * @param alturaImagem Altura em pixels do ImageView
     * @param margemX Margem esquerda em pixels do ImageView em relacao a tela
     * @param margemY Margem do topo em pixels do ImageView em relacao a tela
     * @param tipoEstimulo Tipo do estimulo que sera criado
     */
    private void newView(GameBuilder builder, Context context, int larguraImagem, int alturaImagem,
                        int margemX, int margemY, Integer tipoEstimulo){

        FrameLayout.LayoutParams paramsBotao = new FrameLayout.LayoutParams(larguraImagem, alturaImagem);
        paramsBotao.leftMargin = margemX;
        paramsBotao.topMargin = margemY;
        paramsBotao.gravity = Gravity.TOP;

        ImageView v = new ImageView(context);
        v.setLayoutParams(paramsBotao);
        v.setTag(TIPO_OBJETO_TELA, tipoEstimulo);
        // Adicionar estimulo principal com mascara
        if(tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())
                || tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())) {
            Util.carregarBitmap(context, builder.getEstimulosPrincipais().get(random).getImagem().getUri(), v);
//            v.setImageBitmap(Util.decodeSampledBitmapFromUri(
//                    Uri.parse(builder.getEstimulosPrincipais().get(random).getImagem().getUri()), larguraImagem, alturaImagem));
            v.setTag(ID_ESTIMULO, builder.getEstimulosPrincipais().get(random).getID());
            // setar estimulo no builder
            builder.setEstimuloPrincipalAtual(builder.getEstimulosPrincipais().get(random));

            ImageView masc = new ImageView(context);
            FrameLayout.LayoutParams paramsMask = new FrameLayout.LayoutParams(larguraImagem, alturaImagem);
            paramsMask.leftMargin = margemX;
            paramsMask.topMargin = margemY;
            paramsMask.gravity = Gravity.TOP;

            masc.setLayoutParams(paramsMask);
            Util.carregarBitmap(context, builder.getEstimulosPrincipais().get(random).getImagem().getUriMascara(), masc);
//            masc.setImageBitmap(Util.decodeSampledBitmapFromUri(
//                    Uri.parse(builder.getEstimulosPrincipais().get(random).getImagem().getUriMascara()), larguraImagem, alturaImagem));
            masc.setVisibility(View.INVISIBLE);

            views.add(v);
            views.add(masc);
            // Guardar estimulo no LocalEstimulo
            localEstimulo.setEstimulo(builder.getEstimulosPrincipais().get(random));
        // guardar no array de mascaras do estimulo principal[0] e da mascara[1]
            if(tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()))
                listMasks[0] = masc;
            else {
                v.setAlpha(255 / 2);
                listMasks[1] = masc;
            }
        } else if(tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto())){
            // recuperar um estimulo randomico na lista e adicionar a lista de Ids usados
            List<Estimulo> es = builder.getEstimulosInsignificantes();
            int randInsignificante;
            do {
                // testar limite maximo de estimulos
                if(listIdInsig.size() == es.size()){
                    listIdInsig = new ArrayList<>();
                }
                randInsignificante = new Random().nextInt(es.size());
            } while(listIdInsig.contains(es.get(randInsignificante).getID()));

            listIdInsig.add(es.get(randInsignificante).getID());
            Util.carregarBitmap(context, es.get(randInsignificante).getImagem().getUri(), v);
//            v.setImageBitmap(Util.decodeSampledBitmapFromUri(Uri.parse(es.get(randInsignificante).getImagem().getUri()), larguraImagem, alturaImagem));
            v.setTag(ID_ESTIMULO, es.get(randInsignificante).getID());
            v.setAlpha(255/2);
            views.add(v);
            // Guardar Estimulo no LocalEstimulo
            localEstimulo.setEstimulo(es.get(randInsignificante));
        } else {
            // nao eh um tipo de estimulo reconhecido, nao fazer nada
        }
        // add LocalEstimulo ao List da tarefa
        tarefa.addLocalEstimulo(localEstimulo);
    }


    private void addPlanoFundo(Context context, int largura, int altura) {
        int margemX = (int) (largura * 0.005);

        FrameLayout.LayoutParams paramsFundo = new FrameLayout.LayoutParams(largura / 2 - margemX, altura);
        paramsFundo.leftMargin = 0;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        ImageView fundoEsquerdo = new ImageView(context);
        fundoEsquerdo.setLayoutParams(paramsFundo);
        fundoEsquerdo.setBackgroundResource(R.drawable.gradiente_azul);
        fundoEsquerdo.setTag(TIPO_OBJETO_TELA, EnumObjetoTela.LADO_ESQUERDO);

        paramsFundo = new FrameLayout.LayoutParams(largura / 2, altura);
        paramsFundo.leftMargin = largura / 2 + margemX;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        View fundoDireito = new ImageView(context);
        fundoDireito.setBackgroundResource(R.drawable.gradiente_verde);
        fundoDireito.setLayoutParams(paramsFundo);
        fundoDireito.setTag(TIPO_OBJETO_TELA, EnumObjetoTela.LADO_DIREITO);

        views.add(fundoEsquerdo);
        views.add(fundoDireito);

    }

    private void addSeta(Context context, int largura, int altura) {
        int larguraSeta = (int) (largura * 0.3);
        int alturaSeta = (int) (altura * 0.12);
        FrameLayout.LayoutParams paramsSeta = new FrameLayout.LayoutParams(larguraSeta, alturaSeta);
        paramsSeta.leftMargin = (int) (largura * 0.35);
        paramsSeta.topMargin = (int) (altura * 0.2);
        paramsSeta.gravity = Gravity.TOP;

        ImageView seta = new ImageView(context);
        seta.setLayoutParams(paramsSeta);
        seta.setBackgroundResource(R.drawable.img_flecha);
        seta.setScaleType(ImageView.ScaleType.FIT_CENTER);
        seta.setTag(TIPO_OBJETO_TELA, EnumObjetoTela.SETA);

        views.add(seta);
    }

    /**
     * Este metodo eh utilizado para encontrar uma View dado uma posicao X e Y na tela
     * @param touchX Posicao X do toque
     * @param touchY Posicao Y do toque
     * @return ImageView encontrado ou null se nao encontrou
     */
    private List<ImageView> buscarView(int touchX, int touchY){
        int[] loc = new int[2];
        Rect r = new Rect();
        List<ImageView> imageViews = new ArrayList<>();
        // loop para procurar a view onde foi solto o objeto
        for(int i = 0; i < views.size() - 1; i++){
            ImageView v = (ImageView) views.get(i);
            if(v.getTag(TIPO_OBJETO_TELA) == null)
                continue;
            // pegar o Rect onde da view
            v.getHitRect(r);
            // pegar localizacao da view
            v.getLocationOnScreen(loc);
            // calcular tamanho do Rect
            r.offset(loc[0] - v.getLeft(), loc[1] - v.getTop());
            // acertou dentro do Rect?
            if(r.contains(touchX, touchY)){
                imageViews.add(v);
            }
        }
        return imageViews.isEmpty() ? null : imageViews;
    }

    /**
     * Metodo utilizado para recortar a imagem dado uma mascara
     * @param viewOriginal View com a imagem original
     * @param mascara View com a imagem de mascara (Sao reconhecidos pixels pretos como mascara
     *                para o recorte)
     * @return Um novo Bitmap com o recorte da imagem original.
     */
    private Bitmap recortarImagem(ImageView viewOriginal, ImageView mascara){
        // Criar a copia com a area da mascara << Loop linha x colunas (X,Y)
        Bitmap bitmapOriginal = ((BitmapDrawable)viewOriginal.getDrawable()).getBitmap();
        Bitmap bitmapMask = ((BitmapDrawable)mascara.getDrawable()).getBitmap();
        // instancia de um novo bitmap com as mesmas dimensoes do bitmap original
        Bitmap novoBitmap = Bitmap.createBitmap(
                bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        // canvas para desenhar dentro do novo bitmap
        Canvas canvas = new Canvas(novoBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        // paint para escolher as cores do pincel
        Paint paint = new Paint();
        // guardar largura e altura para loop (linhas e colunas)
        int l = bitmapOriginal.getWidth();
        int c = bitmapOriginal.getHeight();
        // for para percorrer as linhas do bitmap
        for(int i = 0; i < l - 1; i++){
            // for para percorrer as colunas do bitmap
            for(int j = 0; j < c - 1; j++){
                // encontrou ponto preto na mascara?
                if(bitmapMask.getPixel(i, j) != Color.TRANSPARENT){
                    // recuperar cor no bitmapOriginal e colocar no pincel
                    paint.setColor(bitmapOriginal.getPixel(i, j));
                    // pintar o pixel com a cor original com o canvas
                    canvas.drawPoint(i, j, paint);
                }
            } // fim J
        } // fim I

        return novoBitmap;
    }

    /**
     * Metodo utilizado para encontrar a cor do pixel dado uma posicao em relacao a uma View ( no
     * caso de tocar a view e nao acertar a imagem, o pixel retornado eh Branco @link(Color.WHITE)
     * @param v A view onde foi realizado o toque
     * @param touchX Posicao X do toque
     * @param touchY Posicao Y do toque
     * @return retorna a cor do pixel tocado
     */
    private int recuperarCorPixel(View v, float touchX, float touchY) {
        float[] eventXY = new float[] {touchX, touchY};

        Matrix invertMatrix = new Matrix();
        ((ImageView)v).getImageMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(eventXY);
        int x = (int)eventXY[0];
        int y = (int)eventXY[1];

        Drawable imgDrawable = ((ImageView)v).getDrawable();
        Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

        //Limit x, y range within bitmap
        if(x < 0){
            x = 0;
        }else if(x > bitmap.getWidth()-1){
            x = bitmap.getWidth()-1;
        }

        if(y < 0){
            y = 0;
        }else if(y > bitmap.getHeight()-1){
            y = bitmap.getHeight()-1;
        }
//        Log.d(TAG, "Cor do ponto = " + bitmap.getPixel(x, y));
        return bitmap.getPixel(x, y);
    }

    // esse metodo tras cor preta caso clique dentro da ImageView mas fora da imagem
    /**
     * Metodo utilizado para encontrar a cor do pixel dado uma posicao em relacao a uma View ( no
     * caso de tocar a view e nao acertar a imagem, o pixel retornado eh Preto @link(Color.BLACK)
     * @param v A view onde foi realizado o toque
     * @param x Posicao X do toque
     * @param y Posicao Y do toque
     * @return retorna a cor do pixel tocado
     */
    public int getHotspotColor (View v, int x, int y) {
        ImageView img = (ImageView) v;
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    /**
     * Metodo responsavel por criar a copia da view que sera utilizada durante o modo Arrastar na
     * tela
     * @param conteiner Layout onde sera adicionado a dragView
     * @param context Conntexto da Aplicacao
     * @param v View original
     * @param imgRecortada Imagem que sera exibida na dragView
     */
    private void criarDragView(ViewGroup conteiner, Context context, View v, Bitmap imgRecortada) {
        dragView = new ImageView(context);
        dragView.setImageBitmap(imgRecortada);
        dragView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(v.getWidth(), v.getHeight());
        params.topMargin = ((FrameLayout.LayoutParams) v.getLayoutParams()).topMargin;
        params.leftMargin = ((FrameLayout.LayoutParams) v.getLayoutParams()).leftMargin;
        params.gravity = Gravity.TOP;

        dragView.setLayoutParams(params);

        conteiner.addView(dragView);
    }

    /**
     * Metodo chamado toda vez que a dragView for movida de lugar
     * @param conteiner Layout onde esta a dragView
     * @param v A dragView que esta sendo movida de lugar
     * @param atualX Posicao X atual do toque
     * @param atualY Posicao Y atual do toque
     * @param inicialX Posicao X inicial do toque
     * @param inicialY Posicao Y inicial do toque
     */
    private void moverDragView(ViewGroup conteiner, View v, int atualX, int atualY, int inicialX, int inicialY){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
        lp.leftMargin = atualX - inicialX;
        lp.topMargin = atualY - inicialY;
        conteiner.updateViewLayout(v, lp);
    }

    /**
     * Metodo chamado toda vez que eh necessario voltar a dragView para posicao original
     * @param dragView A dragView que esta sendo movida de lugar
     * @param viewOriginal A view que foi originalmente tocada

     */
    private void voltarDragView(View dragView, View viewOriginal){
        FrameLayout.LayoutParams paramsDragView = (FrameLayout.LayoutParams) dragView.getLayoutParams();
        FrameLayout.LayoutParams paramsView = (FrameLayout.LayoutParams) viewOriginal.getLayoutParams();

        Animation anim = new TranslateAnimation(Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, paramsView.leftMargin - paramsDragView.leftMargin,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, paramsView.topMargin - paramsDragView.topMargin);
        anim.setDuration(1000l);
        anim.setAnimationListener(this);
        dragView.startAnimation(anim);
    }

    /**
     * Metodo utilizado para remover a dragView do conteiner
     * @param conteiner Layout onde esta a dragView
     */
    private void removerDragView(ViewGroup conteiner) {
        conteiner.removeView(dragView);
        dragView = null;
    }


    /**
     * Metodo utilizado para gerar randomicamente o tipo de estimulo que preencherá a view.
     * @param total - quantidade total de estimulos
     * @param atual - index do estimulo atual
     * @return 1 - Indica que é um ESTIMULO_ALVO
     *         2 - Indica que é um ESTIMULO_INSIGNIFICANTE
     */
    private Integer criarPosicaoRandomica(int total, int atual) {
        int rand;
        do {
            // ja criou um alvo?
            if(flgCriouAlvo)
                rand = EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto();
            else {
                rand = new Random().nextInt(3);

                // criou um estimulo alvo?
                if(rand ==EnumObjetoTela.ESTIMULO_ALVO.getObjeto())
                    flgCriouAlvo = true;
                    // nao criou o estimulo alvo e eh a ultima posicao?
                else if(!flgCriouAlvo && atual == (total - 1))
                    rand = EnumObjetoTela.ESTIMULO_ALVO.getObjeto();
            }
        } while(rand == EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());

        // testar se eh o ultimo estimulo, se for "zerar" a flg
        if(atual == total - 1)
            flgCriouAlvo = false;

        return rand;
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // Testar as escalas para garantir que o objeto nao seja distorcido
        float finalScale;
        if(scaleWidth < scaleHeight)
            finalScale = scaleWidth;
        else
            finalScale = scaleHeight;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(finalScale, finalScale);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void guardarTarefa(Context context, Tarefa tarefa){
        tarefa.setDataCriacao(new Date());
        TarefaDAO dao = new TarefaDAO(BancoDadosHelper.getInstance(context));
        try {
            tarefa.setID((int) dao.salvar(tarefa));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void atualizarTarefa(Context context, Tarefa tarefa){
        tarefa.setUltimaAtualizacao(new Date());
        TarefaDAO dao = new TarefaDAO(BancoDadosHelper.getInstance(context));
        try {
            dao.alterar(tarefa);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
// Fim metodos privados

}
