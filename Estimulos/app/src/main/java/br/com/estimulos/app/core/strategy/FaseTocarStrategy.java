package br.com.estimulos.app.core.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.ObjetoTela;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Tentativa;
import br.com.estimulos.dominio.TipoResultado;

/**
 *  Classe responsável por criar e tratar eventos do toque da Fase de Tocar do Jogo.
 *
 * Created by Giovani on 03/03/2016.
 */
public class FaseTocarStrategy implements IStrategyFase {

    /** Constantes */
    public final static String TAG = "StrategyTocar";
    public final static Integer ID_ESTIMULO = R.id.id_estimulo;
    public final static Integer TIPO_OBJETO_TELA = R.id.tipo_objeto_tela;

    /**
     * List com todas as views que serao criadas nesta Tarefa
     */
    private List<View> views;

    /**
     * Nivel que contem as informacoes para criar uma nova Tarefa
     */
    private NivelTocar nivel;

    /**
     * Lista com Ids dos estimulos insignificantes ja escolhidos
     */
    private List<Integer> listIdInsig;

    /**
     * Lista com os ImageViews das duas mascaras utilizadas
     */
    private ImageView[] listMasks;

     /** Flag indicando se a mascara alvo foi criada ou nao
     * TRUE - Indica que foi criada
     * FALSE - Indica que não foi criada
     */
    private boolean flgCriouAlvo;

    /**
     * Variavel para guardar um valor randomico
     */
    private int random;

    /**
     * Tentativa da jogada
     */
    private Tentativa tentativa;

    /**
     * Lista para armazenar os movimentos
     */
    List<Movimento> movimentos;

    /**
     * Variável para registrar o toque
     */
    private Movimento movimento;

    /**
     * Atributo que contem todos os dados da tarefa que sera criado por essa estrategia
     */
    private Tarefa tarefa;


    /**
     * Construtor
     */
    public FaseTocarStrategy(){
        views = new ArrayList<>();
        listIdInsig = new ArrayList<>();
        listMasks = new ImageView[2];
        flgCriouAlvo = false;
    }

    /**
     * Implementacoes do IStrategy
     * */
    //
    @Override
    public List<View> criarViews(GameBuilder builder, int width, int height, Context context) {
        nivel = (NivelTocar) builder.getNivel();

        nivel.setRandPosicao(true);

        List<Estimulo> estimulosInsignificante = new ArrayList<>();
        List<Estimulo> estimulosPrincipais = new ArrayList<>();

        Map<String, String> filtro = new HashMap<>();
        filtro.put(EstimuloDAO.Tabela.CATEGORIA_ESTIMULO.getName(), builder.getIdCategoria().toString());

        EstimuloDAO estmDAO = new EstimuloDAO(BancoDadosHelper.getInstance(context));
        try {
            estimulosInsignificante = estmDAO.consultarEstimuloInsignificante(builder.getIdCategoria());
            estimulosPrincipais = estmDAO.pesquisa(filtro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.setEstimulosInsignificantes(estimulosInsignificante);
        builder.setEstimulosPrincipais(estimulosPrincipais);

        // TODO remover estes hardcodes acima ....

        tarefa = new Tarefa();
        tarefa.setNumTarefa(builder.getContadorTarefa() + 1);
        tarefa.setCompletada(false);
        tarefa.setListLocais(new ArrayList<LocalEstimulo>());

        /** Adicionar cor de fundo para o layout */
        addPlanoFundo(context, width, height);

        /** Adicionar os estimulos ao layout*/

        int qtde = nivel.getPosicoes().size();

        // criar randomico para utilizar dentro do metodo NewView
        random = new Random().nextInt(builder.getEstimulosPrincipais().size());

        // zerar lista de Ids dos estimulos insignificantes
        listIdInsig = new ArrayList<>();

        // criar tela baseado na margem top e left de cada imagem
        for(int i = 0; i < qtde; i++){
            Posicao p = nivel.getPosicoes().get(i);

            // calcular as medidas passadas em % em relacao ao tamanho da tela
            int larguraImagem = (int) (width * p.getLargura() );
            int alturaImagem = (int) (height * p.getAltura() );
            int margemX = (int) (width * p.getMargemX() );
            int margemY = (int) (height * p.getMargemY() );

            Integer tipoEstimulo;
            if(nivel.isRandPosicao())
                tipoEstimulo = criarPosicaoRandomica(qtde, i);
            else {
                tipoEstimulo = p.getTipo();
            }

//            Integer tipoEstimulo = !nivel.isRandPosicao() ||
//                    p.getTipo().equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()) ?
//                    p.getTipo() : criarPosicaoRandomica(qtde, i);

            newView(builder,
                    context,
                    larguraImagem,
                    alturaImagem,
                    margemX,
                    margemY,
                    tipoEstimulo);
        }
        // TODO : tirar o valor settado hard code
        tarefa.setFaseId(3);
        guardarTarefa(context, tarefa);
        return views;
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

    @Override
    public boolean onTouch(View v, MotionEvent event, ViewGroup conteiner, Context context, GameBuilder builder) {
        int action = event.getAction();
        ObjetoTela objeto = new ObjetoTela();

        if(tentativa == null){
            tentativa = new Tentativa();
            movimentos = new ArrayList<>();
            tentativa.setResultado(new TipoResultado());
        }
        System.out.println("TAG DA VIEW: "+v.getTag(TIPO_OBJETO_TELA));
        // salvar posicoes iniciais de toque
        final int posicaoXAtual = (int) event.getRawX();
        final int posicaoYAtual = (int) event.getRawY();

        // Testar se é o estimulo principal sendo tocado.
        if(v.getTag(TIPO_OBJETO_TELA) == null || !v.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())) {
            registrarTentativa(v, event, builder, posicaoXAtual, posicaoYAtual);
            return false;
        }

        // converter view para ImageView
        ImageView imgView = (ImageView) v;

        // Tratar eventos de toque (Apertar, Mover e Soltar)
        switch (action) {
            case MotionEvent.ACTION_DOWN: {

            }
            case MotionEvent.ACTION_UP: {
                // Testar se soltou dentro de um ImageView
                List<ImageView> alvos = buscarView(posicaoXAtual, posicaoYAtual);
//                ImageView imgAlvo = buscarView(posicaoXAtual, posicaoYAtual);
                // nao encontrou nenhum alvo?
                if(alvos == null) {
                    Log.d(TAG, "Nao encontrou nenhuma view onde foi solto, TagView");
                    registrarTentativa(imgView, event, builder, posicaoXAtual, posicaoYAtual);
                    // TODO : buscar o ID do erro no banco a partir da descrição. Settar o ID na entidade, e a descrição no TipoResultado
                    // TODO : salvar esta tentativa (o ultimo movimento, o objetoTelaFinal e o resultado deste erro)
                    return false;
                }
                // Testar se soltou dentro de um ImageView
                ImageView imgAlvo = null;
                for(ImageView iv : alvos) {
                    if (iv.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())) {
                        imgAlvo = iv;
                        break;
                    }
                }
                // nao encontrou nenhum alvo?
                if (imgAlvo == null) {
                    Log.d(TAG, "Nao encontrou nenhuma view onde foi tocado, TagView = ");// + imgAlvo.getTag());
                    registrarTentativa(imgView, event, builder, posicaoXAtual, posicaoYAtual);
                    return false;
                }
                // Comparar Tag para ver se o alvo eh o correto
                if (v.getTag(TIPO_OBJETO_TELA) == null || !imgAlvo.getTag(TIPO_OBJETO_TELA).equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())) {
                    Log.d(TAG, "Alvo errado !! TagView = " + imgAlvo.getTag());
                    registrarTentativa(imgView, event, builder, posicaoXAtual, posicaoYAtual);
                    return false;
                }

                // buscar as margens da view encontrada
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgView.getLayoutParams();
                // Testar o pixel tocado no Estimulo Alvo e ver se a  mascara tambem foi acertada
                // O pixel tocado nao eh preto?
                if (recuperarCorPixel(listMasks[0], event.getX() - imgAlvo.getLeft() + params.leftMargin,
                        event.getY() - imgAlvo.getTop() + params.topMargin) == Color.TRANSPARENT) {
                    Log.d(CriarJogoActivity.TAG, "Errou!!");
                    //objeto.setTipoObjetoTela(EnumObjetoTela.FUNDO.getObjeto());
                    registrarTentativa(imgView, event, builder, posicaoXAtual, posicaoYAtual);
                    return false;
                } else {
                    Log.d(CriarJogoActivity.TAG, "Acertou o pixel do alvo!!");
                    //objeto.setTipoObjetoTela(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
                    registrarTentativa(imgView, event, builder, posicaoXAtual, posicaoYAtual);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param v - View que foi tocada
     * @param event - Evento relacionado ao toque
     * @param builder - GameBuilder deste jogo
     * @param posicaoX - Posicao X atual do toque
     * @param posicaoY - Posicao Y atual do toque
     */
    private void registrarTentativa(View v, MotionEvent event, GameBuilder builder, int posicaoX, int posicaoY) {
        /** Usando essas variaveis, poderia criar uma frase como:
         * Descricao do erro : "Tocou no fundo direito." */
        String acao = null;
        String preposicao = " no "; // TODO : criar preposicao baseado no masc. ou fem. do estimulo
//        String tipoObjeto;
        String nomeObjeto = "Teste";
        Estimulo estimulo = null;

        tentativa.setTarefa(tarefa);
        ObjetoTela objeto = new ObjetoTela();
        switch (event.getAction()) {
            // Errou no toque?
            case MotionEvent.ACTION_DOWN: {

            }
            case MotionEvent.ACTION_UP:
                movimento = new Movimento();
                movimento.setX(posicaoX);
                movimento.setY(posicaoY);
                movimentos.add(movimento);
                System.out.println("[SALVAR]: Posicao Atual: X=" + posicaoX + " Y=" + posicaoY);

                tentativa.setMovimentos(movimentos);
                String msgResultado;
                System.out.println("---- LISTAGEM DE TODOS OS MOVIMENTOS -----");
                for (int i = 0; i < tentativa.getMovimentos().size(); i++) {
                    System.out.println("Posicao X: " + tentativa.getMovimentos().get(i).getX() + " Y:" + tentativa.getMovimentos().get(i).getY());
                }
                // TODO : Tirar os dados inseridos hard code
                acao = "Tocou";
                converterTags(v, builder, nomeObjeto, estimulo);
                objeto.setTipoObjetoTela(0);
                if (estimulo != null)
                    objeto.setCodigo(estimulo.getID().toString());
                else
                    objeto.setCodigo(nomeObjeto);
                tentativa.setObjetoFinal(objeto);
                tentativa.setObjetoInicial(objeto);

                TentativaDAO tentativaDAO = new TentativaDAO(BancoDadosHelper.getInstance(v.getContext()));
                //tentativa.getObjetoInicial().setCodigo("Inicial");
                tentativa.getObjetoFinal().setCodigo("Final");
                System.out.println("---- PRONTO PARA SALVAR ----");
                System.out.println("Tentativa:\nID: "+tentativa.getID());
                System.out.println("\nObjeto: "+objeto.getID() +"  - "+objeto.getCodigo());

                // TODO : Gravar no banco
//                try {
//                    tentativaDAO.salvar(tentativa);
//                    System.out.println("Salvou!");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
        // TODO : decidir se esse método vai estar aqui
//        Integer tipoObjetoTag = (Integer) v.getTag(TIPO_OBJETO_TELA);
//        if(tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())
//                || tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())){
//            nomeObjeto = "estímulo";
//            // fazer loop para procurar estimulo na lista
//            for(Estimulo e : builder.getEstimulosPrincipais()){
//                if(e.getID().equals(v.getTag(ID_ESTIMULO))) {
//                    estimulo = e;
//                    break;
//                }
//            }
//        } else if(tipoObjetoTag.equals(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto())){
//            nomeObjeto = "estímulo";
//            for(Estimulo e : builder.getEstimulosInsignificantes()){
//                if(e.getID().equals(v.getTag(ID_ESTIMULO))){
//                    estimulo = e;
//                    break;
//                }
//            }
//        } else if(tipoObjetoTag.equals(EnumObjetoTela.FUNDO.getObjeto())) {
//            nomeObjeto = "fundo";
//        } else {
//            nomeObjeto = null;
//            estimulo = null;
//        }
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
                || tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_ALVO.getObjeto())){
            // criar bitmap do estimulo principal
            Util.carregarBitmap(context, builder.getEstimulosPrincipais().get(random).getImagem().getUri(), v);
//            v.setTag(tipoEstimulo);
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
            masc.setVisibility(View.INVISIBLE);

            views.add(v);
            views.add(masc);
            // guardar no array de mascaras do estimulo principal[0] e da mascara[1]
            if(tipoEstimulo.equals(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto()))
                listMasks[0] = masc;
            else
                listMasks[1] = masc;
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
            v.setTag(ID_ESTIMULO, es.get(randInsignificante).getID());//tipoEstimulo);
            views.add(v);
        } else {
            // nao eh um tipo de estimulo reconhecido, nao fazer nada
        }
    }

    private void addPlanoFundo(Context context, int largura, int altura) {
        FrameLayout.LayoutParams paramsFundo = new FrameLayout.LayoutParams(largura, altura);
        paramsFundo.leftMargin = 0;
        paramsFundo.topMargin = 0;
        paramsFundo.gravity = Gravity.TOP;

        ImageView fundo = new ImageView(context);
        fundo.setLayoutParams(paramsFundo);
        fundo.setBackgroundResource(R.drawable.gradiente_azul);
        fundo.setScaleType(ImageView.ScaleType.FIT_CENTER);

        views.add(fundo);

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
            // ja criou um principal?
            if(flgCriouAlvo)
                rand = EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto();
            else {
                rand = new Random().nextInt(3);

                // criou um estimulo principal?
                if(rand == EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto())
                    flgCriouAlvo = true;
                    // nao criou o estimulo principal e eh a ultima posicao?
                else if(!flgCriouAlvo && atual == (total - 1))
                    rand = EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto();
            }
        } while(rand == EnumObjetoTela.ESTIMULO_ALVO.getObjeto());

        // testar se eh o ultimo estimulo, se for "zerar" a flg
        if(atual == total - 1)
            flgCriouAlvo = false;
        Log.d(TAG, "******* Criou estimulo TIPO = " + rand);
        return rand;
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

}
