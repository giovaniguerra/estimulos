package br.com.estimulos.app.teste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.R;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.CategoriaEstimuloDAO;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.app.core.dao.FaseDAO;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.core.dao.JogoResforcadorDAO;
import br.com.estimulos.app.core.dao.NivelArrastarDAO;
import br.com.estimulos.app.core.dao.NivelTocarDAO;
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.core.dao.TarefaDAO;
import br.com.estimulos.app.core.dao.TentativaDAO;
import br.com.estimulos.app.core.dao.TipoResultadoDAO;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.EnumEstatistica;
import br.com.estimulos.dominio.EnumObjetoTela;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Grupo;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.ObjetoTela;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Tentativa;
import br.com.estimulos.dominio.TipoResultado;
import br.com.estimulos.dominio.Video;

/**
 * Created by caioc_000 on 07/03/2016.
 */
public class CriadorRegistrosSQLite {

    public static void criarMock(Context context){

        EstimuloDAO estimuloDAO = new EstimuloDAO(BancoDadosHelper.getInstance(context));
        try {
            if (estimuloDAO.count() == 0) {
                CategoriaEstimuloDAO categoriaEstimuloDAO = new CategoriaEstimuloDAO(BancoDadosHelper.getInstance(context));

                CategoriaEstimulo categoriaEstimulo = new CategoriaEstimulo();
                categoriaEstimulo.setNome("Bola");
                categoriaEstimuloDAO.salvar(categoriaEstimulo);

                CategoriaEstimulo categoriaEstimulo2 = new CategoriaEstimulo();
                categoriaEstimulo2.setNome("Fruta");
                categoriaEstimuloDAO.salvar(categoriaEstimulo2);

                criarEstimulos("a",1, retornaUriDrawable(context, R.drawable.bola),
                        retornaUriDrawable(context, R.drawable.bola), "Bola de Futebol", estimuloDAO, context);// de Futebol"

                criarEstimulos("a",1, retornaUriDrawable(context, R.drawable.bola_basquete),
                        retornaUriDrawable(context, R.drawable.bola_basquete), "Bola de Basquete", estimuloDAO, context);

                criarEstimulos("a",1, retornaUriDrawable(context, R.drawable.bola_tenis),
                        retornaUriDrawable(context, R.drawable.bola_tenis), "Bola de Tênis", estimuloDAO, context);

                criarEstimulos("a",1, retornaUriDrawable(context, R.drawable.bola_voley),
                        retornaUriDrawable(context, R.drawable.bola_voley), "Bola de Vôley", estimuloDAO, context);

                criarEstimulos("a",3, retornaUriDrawable(context, R.drawable.fruta_banana1),
                        retornaUriDrawable(context, R.drawable.fruta_banana1), "Banana", estimuloDAO, context);

                criarEstimulos("a",3, retornaUriDrawable(context, R.drawable.fruta_pera),
                        retornaUriDrawable(context, R.drawable.fruta_pera), "Pera", estimuloDAO, context);

                criarEstimulos("a",3, retornaUriDrawable(context, R.drawable.fruta_maca),
                        retornaUriDrawable(context, R.drawable.fruta_maca), "Maçã", estimuloDAO, context);

                criarEstimulos("o",3, retornaUriDrawable(context, R.drawable.fruta_mamao),
                        retornaUriDrawable(context, R.drawable.fruta_mamao), "Mamão", estimuloDAO, context);



                JogoDAO jogoDAO = new JogoDAO(BancoDadosHelper.getInstance(context));


                Map<String, String> filtroPrimCategoria = new HashMap<>();
                filtroPrimCategoria.put(CategoriaEstimuloDAO.Tabela.NOME.getName(), "'Bola'");

                List<CategoriaEstimulo> categorias = categoriaEstimuloDAO.pesquisa(filtroPrimCategoria);

                Map<String, String> filtroEstimulo = new HashMap<>();
                filtroEstimulo.put(EstimuloDAO.Tabela.CATEGORIA_ESTIMULO.getName(), "" + categorias.get(0).getID());

                List<Estimulo> estimulosPrimCategoria = estimuloDAO.pesquisa(filtroEstimulo);

                Map<String, String> filtroSegCategoria = new HashMap<>();
                filtroSegCategoria.put(CategoriaEstimuloDAO.Tabela.NOME.getName(), "'Fruta'");

                List<CategoriaEstimulo> segsCategorias = categoriaEstimuloDAO.pesquisa(filtroSegCategoria);

                Map<String, String> filtroEstimuloSeg = new HashMap<>();
                filtroEstimulo.put(EstimuloDAO.Tabela.CATEGORIA_ESTIMULO.getName(), "" + segsCategorias.get(0).getID());

                List<Estimulo> estimulosSegCategoria = estimuloDAO.pesquisa(filtroEstimuloSeg);

                NivelArrastarDAO nivelArrastarDAO = new NivelArrastarDAO(BancoDadosHelper.getInstance(context));
                NivelTocarDAO nivelTocarDAO = new NivelTocarDAO(BancoDadosHelper.getInstance(context));


                long jogo1 = criarJogos(jogoDAO, estimulosPrimCategoria, retornaUriDrawable(context, R.drawable.bola), "Paciente 1", "Jogo 1");
                criarNiveisArrastarPara(context, nivelArrastarDAO, jogo1);
                criarNiveisTocarPara(context, nivelTocarDAO, jogo1);

                estimulosPrimCategoria.remove(1);

                long jogo2 = criarJogos(jogoDAO, estimulosPrimCategoria, retornaUriDrawable(context, R.drawable.bola_oito), "Paciente 2", "Jogo 2");
                criarNiveisArrastarPara(context, nivelArrastarDAO, jogo2);
                criarNiveisTocarPara(context, nivelTocarDAO, jogo2);

                long jogo3 = criarJogos(jogoDAO, estimulosSegCategoria,retornaUriDrawable(context, R.drawable.ic_casa), "Paciente 3", "Jogo 3");
                criarNiveisArrastarPara(context, nivelArrastarDAO, jogo3);
                criarNiveisTocarPara(context, nivelTocarDAO, jogo3);

                estimulosSegCategoria.remove(1);
                estimulosSegCategoria.remove(2);

//                estimulosSegCategoria.remove(3);
//                estimulosSegCategoria.remove(4);
//
//                estimulosSegCategoria.remove(1);
//                estimulosSegCategoria.remove(2);


                long jogo4 = criarJogos(jogoDAO, estimulosSegCategoria, retornaUriDrawable(context, R.drawable.ic_gato), "Paciente 4", "Jogo 4");
                criarNiveisArrastarPara(context, nivelArrastarDAO, jogo4);
                criarNiveisTocarPara(context, nivelTocarDAO, jogo4);


                FaseDAO faseDAO = new FaseDAO(BancoDadosHelper.getInstance(context));
                Fase faseTocar = new Fase();
                faseTocar.setNome("Fase Tocar");
                faseTocar.setNomeClasseHerdade(FaseTocar.class.getName());
                faseTocar.setUriImagem(retornaUriDrawable(context, R.drawable.ic_tocar1));


                Fase faseArrastar = new Fase();
                faseArrastar.setNome("Fase Arrastar");
                faseArrastar.setNomeClasseHerdade(FaseArrastar.class.getName());
                faseArrastar.setUriImagem(retornaUriDrawable(context, R.drawable.ic_arrastar1));

                faseDAO.salvar(faseArrastar);
                faseDAO.salvar(faseTocar);

                Reforcador r = new Reforcador();
                r.setMidia(new Audio());
                r.getMidia().setUri(retornaUriAudio(context, R.raw.aplausos_003, ".mp3"));
                r.setNome("Palmas");
                r.setTempoDuracao(3000);


                Reforcador r1 = new Reforcador();
                r1.setMidia(new Video());
                r1.getMidia().setUri(retornaUriAudio(context, R.raw.video, ".mp4"));
                r1.setTempoDuracao(60000);
                r1.setNome("Vídeo Reforçador");

                ReforcadorDAO reforcadorDAO = new ReforcadorDAO(BancoDadosHelper.getInstance(context));

                long idRef1 = reforcadorDAO.salvar(r);
                long idRef2 = reforcadorDAO.salvar(r1);


                List<Reforcador> reforcadors = new ArrayList<>();
                Reforcador ref1 = new Reforcador();
                ref1.setID((int) idRef1);

                Reforcador ref2 = new Reforcador();
                ref2.setID((int) idRef2);

                reforcadors.add(ref1);
                reforcadors.add(ref2);

                vincularReforcador(jogo1, reforcadors, context);
                vincularReforcador(jogo2, reforcadors, context);
                vincularReforcador(jogo3, reforcadors, context);
                vincularReforcador(jogo4, reforcadors, context);

                criarTarefas(context);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void vincularReforcador(long idJogo, List<Reforcador> reforcadors, Context context){
        Jogo j = new Jogo();
        j.setID((int) idJogo);
        j.setReforcadores(reforcadors);

        JogoResforcadorDAO dao = new JogoResforcadorDAO(BancoDadosHelper.getInstance(context));

        try {
            dao.salvar(j);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void criarTarefas(Context context) throws Exception {

        TarefaDAO tarefaDAO = new TarefaDAO(BancoDadosHelper.getInstance(context));

        Tarefa t = new Tarefa();

        t.setDataCriacao(new Date());
        t.setUltimaAtualizacao(new Date());

        t.setCompletada(false);
        t.setFaseId(3);
        t.setNumTarefa(1);

        LocalEstimulo le = new LocalEstimulo();
        le.setPosicao(new Posicao());
        le.getPosicao().setID(1);
        le.setEstimulo(new Estimulo());
        le.getEstimulo().setID(1);

        List<LocalEstimulo> locais = new ArrayList<>();
        locais.add(le);

        t.setListLocais(locais);

        tarefaDAO.salvar(t);


        TipoResultadoDAO tipoResultadoDAO = new TipoResultadoDAO(BancoDadosHelper.getInstance(context));

        TipoResultado tipoResultado = new TipoResultado();
        tipoResultado.setNome("Acertou a tarefa");
        tipoResultado.setDescricao("O alvo foi o estímulo principal");
        tipoResultado.setID(EnumEstatistica.ACERTOU.getObjeto());

//        TipoResultado tipoResultado1 = new TipoResultado();
//        tipoResultado1.setNome("Errou para um estímulo insignificante");
//        tipoResultado1.setDescricao("O alvo foi um estímulo insignificante");
//
//
//        TipoResultado tipoResultado2 = new TipoResultado();
//        tipoResultado2.setNome("Errou não acertou nenhum estímulo");
//        tipoResultado2.setDescricao("O alvo foi o fundo");
//
//
//        TipoResultado tipoResultado3 = new TipoResultado();
//        tipoResultado3.setNome("Errou não acertou nenhum estímulo");
//        tipoResultado3.setDescricao("O alvo foi a seta");
//
//        TipoResultado tipoResultado4 = new TipoResultado();
//        tipoResultado4.setNome("Errou não acertou nenhum estímulo");
//        tipoResultado4.setDescricao("Tocou no lado direito da tela");

        tipoResultadoDAO.salvar(tipoResultado);
//        tipoResultadoDAO.salvar(tipoResultado1);
//        tipoResultadoDAO.salvar(tipoResultado2);
//        tipoResultadoDAO.salvar(tipoResultado3);
//        tipoResultadoDAO.salvar(tipoResultado4);


        ObjetoTela objInicial = new ObjetoTela();
        objInicial.setTipoObjetoTela(0);
        objInicial.setCodigo("1");

        ObjetoTela objFinal = new ObjetoTela();
        objFinal.setTipoObjetoTela(2);
        objFinal.setCodigo("1");

        List<Movimento> movimentos = new ArrayList<>();

        Movimento m1 = new Movimento();
        m1.setX(15);
        m1.setY(20);


        Movimento m2 = new Movimento();
        m2.setX(15);
        m2.setY(20);

        Movimento m3 = new Movimento();
        m3.setX(15);
        m3.setY(20);

        Movimento m4 = new Movimento();
        m4.setX(15);
        m4.setY(20);

        movimentos.add(m1);
        movimentos.add(m2);
        movimentos.add(m3);
        movimentos.add(m4);

        TentativaDAO tentativaDAO = new TentativaDAO(BancoDadosHelper.getInstance(context));
        Tentativa tentativa = new Tentativa();
        tentativa.setResultado(tipoResultado);
        tentativa.setTarefa(t);
        tentativa.setObjetoFinal(objFinal);
        tentativa.setObjetoInicial(objInicial);
        tentativa.setMovimentos(movimentos);




    }

    private static String retornaUriDrawable(Context context, int idDrawable){


        Bitmap bitMap = BitmapFactory.decodeResource(context.getResources(), idDrawable);

        File mFile1 = Environment.getExternalStorageDirectory();

        String fileName ="img" + idDrawable+ ".jpg";

        File mFile2 = new File(mFile1,fileName);
        try {
            FileOutputStream outStream;

            outStream = new FileOutputStream(mFile2);

            bitMap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            outStream.flush();

            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String retorno = mFile1.getAbsolutePath().toString()+"/"+fileName;

        Log.i("URI DRAWABLE", retorno);
        return retorno;
    }

    private static String retornaUriAudio(Context context, int idRaw, String ext){

        byte[] buffer = null;
        InputStream inputStream  = context.getResources().openRawResource(idRaw);
        int size=0;
        try {
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = Environment.getExternalStorageDirectory().getPath();
        String filename = "/filename"+ext;

        boolean exists = (new File(path)).exists();
        if (!exists){new File(path).mkdirs();}

        FileOutputStream save;
        try {
            save = new FileOutputStream(path+filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path+filename;
    }


    public static void criarNiveisArrastarPara(Context context, NivelArrastarDAO nivelArrastarDAO, long l) {

        /** NIVEL 1 */
        NivelArrastar nivelArrastar = new NivelArrastar();
        nivelArrastar.setJogo(new Jogo());
        nivelArrastar.getJogo().setID((int) l);
        nivelArrastar.setNumero(1);
        nivelArrastar.setNumTarefas(4);
        nivelArrastar.setFase(new Fase());
        nivelArrastar.getFase().setID(3);
        nivelArrastar.setQtdeEstimulos(2);
        nivelArrastar.setRandPosicao(true);
        nivelArrastar.setLimiteErros(5);
        nivelArrastar.setLimiteSomDeErro(3);
        nivelArrastar.setLimiteInstrucoesTTS(2);

        List<Posicao> posicoes = new ArrayList<>();

        Posicao posicao = new Posicao();

        posicao.setAltura(0.5f);
        posicao.setLargura(0.22421876f);
        posicao.setMargemX(0.1265625f);
        posicao.setMargemY(0.228223f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5f);
        posicao.setLargura(0.22421876f);
        posicao.setMargemX(0.6460937f);
        posicao.setMargemY(0.23344947f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);

        nivelArrastar.setPosicoes(posicoes);
        nivelArrastar.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_1));


        /** NIVEL 2 */
        NivelArrastar nivelArrastar1 = new NivelArrastar();
        nivelArrastar1.setJogo(new Jogo());
        nivelArrastar1.getJogo().setID((int) l);
        nivelArrastar1.setNumero(2);
        nivelArrastar1.setNumTarefas(4);
        nivelArrastar1.setFase(new Fase());
        nivelArrastar1.getFase().setID(3);
        nivelArrastar1.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_2));
        nivelArrastar1.setQtdeEstimulos(3);
        nivelArrastar1.setRandPosicao(false);
        nivelArrastar1.setLimiteErros(3);
        nivelArrastar1.setLimiteSomDeErro(3);
        nivelArrastar1.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.4599303f);
        posicao.setLargura(0.20625f);
        posicao.setMargemX(0.1421875f);
        posicao.setMargemY(0.2752613f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.4599303f);
        posicao.setLargura(0.20625f);
        posicao.setMargemX(0.684375f);
        posicao.setMargemY(0.5243902f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.4599303f);
        posicao.setLargura(0.20625f);
        posicao.setMargemX(0.6851562f);
        posicao.setMargemY(0.022648083f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);


        nivelArrastar1.setPosicoes(posicoes);

        /** NIVEL 3 */
        NivelArrastar nivelArrastar2 = new NivelArrastar();
        nivelArrastar2.setJogo(new Jogo());
        nivelArrastar2.getJogo().setID((int) l);
        nivelArrastar2.setNumTarefas(4);
        nivelArrastar2.setNumero(3);
        nivelArrastar2.setFase(new Fase());
        nivelArrastar2.getFase().setID(3);
        nivelArrastar2.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_3));
        nivelArrastar2.setQtdeEstimulos(4);
        nivelArrastar2.setRandPosicao(true);
        nivelArrastar2.setLimiteErros(10);
        nivelArrastar2.setLimiteSomDeErro(5);
        nivelArrastar2.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.159375f);
        posicao.setMargemY(0.35191637f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.71015626f);
        posicao.setMargemY(0.017421603f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.33972126f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.66550523f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelArrastar2.setPosicoes(posicoes);


        /** NIVEL 4 */
        NivelArrastar nivelArrastar3 = new NivelArrastar();
        nivelArrastar3.setJogo(new Jogo());
        nivelArrastar3.getJogo().setID((int) l);
        nivelArrastar3.setNumTarefas(4);
        nivelArrastar3.setNumero(4);
        nivelArrastar3.setFase(new Fase());
        nivelArrastar3.getFase().setID(3);
        nivelArrastar3.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_4));
        nivelArrastar3.setQtdeEstimulos(4);
        nivelArrastar3.setRandPosicao(true);
        nivelArrastar3.setLimiteErros(10);
        nivelArrastar3.setLimiteSomDeErro(5);
        nivelArrastar3.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.159375f);
        posicao.setMargemY(0.35191637f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.71015626f);
        posicao.setMargemY(0.017421603f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.33972126f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.66550523f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelArrastar3.setPosicoes(posicoes);


        /** NIVEL 5 */
        NivelArrastar nivelArrastar4 = new NivelArrastar();
        nivelArrastar4.setJogo(new Jogo());
        nivelArrastar4.getJogo().setID((int) l);
        nivelArrastar4.setNumTarefas(4);
        nivelArrastar4.setNumero(5);
        nivelArrastar4.setFase(new Fase());
        nivelArrastar4.getFase().setID(3);
        nivelArrastar4.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_5));
        nivelArrastar4.setQtdeEstimulos(4);
        nivelArrastar4.setRandPosicao(true);
        nivelArrastar4.setLimiteErros(10);
        nivelArrastar4.setLimiteSomDeErro(5);
        nivelArrastar4.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.159375f);
        posicao.setMargemY(0.35191637f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.71015626f);
        posicao.setMargemY(0.017421603f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.33972126f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.29965156f);
        posicao.setLargura(0.134375f);
        posicao.setMargemX(0.709375f);
        posicao.setMargemY(0.66550523f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelArrastar4.setPosicoes(posicoes);


        try{
            nivelArrastarDAO.salvar(nivelArrastar);
            nivelArrastarDAO.salvar(nivelArrastar1);
            nivelArrastarDAO.salvar(nivelArrastar2);
            nivelArrastarDAO.salvar(nivelArrastar3);
            nivelArrastarDAO.salvar(nivelArrastar4);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void criarNiveisTocarPara(Context context, NivelTocarDAO nivelTocarDAO, long l) {

        /** Nivel 1*/
        NivelTocar nivelTocar = new NivelTocar();
        nivelTocar.setJogo(new Jogo());
        nivelTocar.getJogo().setID((int) l);
        nivelTocar.setNumero(1);
        nivelTocar.setNumTarefas(4);
        nivelTocar.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_1));
        nivelTocar.setQtdeEstimulos(1);
        nivelTocar.setFase(new Fase());
        nivelTocar.getFase().setID(1);
        nivelTocar.setLimiteSomDeErro(3);
        nivelTocar.setLimiteErros(5);
        nivelTocar.setLimiteInstrucoesTTS(2);

        List<Posicao> posicoes = new ArrayList<>();

        Posicao posicao = new Posicao();

        posicao.setAltura(0.70905924f);
        posicao.setLargura(0.31796876f);
        posicao.setMargemX(0.3375f);
        posicao.setMargemY(0.15156795f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        nivelTocar.setPosicoes(posicoes);

        /** Nivel 2*/
        NivelTocar nivelTocar1 = new NivelTocar();
        nivelTocar1.setJogo(new Jogo());
        nivelTocar1.getJogo().setID((int) l);
        nivelTocar1.setNumero(2);
        nivelTocar1.setNumTarefas(4);
        nivelTocar1.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_2));
        nivelTocar1.setQtdeEstimulos(2);
        nivelTocar1.setFase(new Fase());
        nivelTocar1.getFase().setID(1);
        nivelTocar1.setLimiteSomDeErro(3);
        nivelTocar1.setLimiteErros(5);
        nivelTocar1.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.68118465f);
        posicao.setLargura(0.30546874f);
        posicao.setMargemX(0.1109375f);
        posicao.setMargemY(0.16550523f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.68118465f);
        posicao.setLargura(0.30546874f);
        posicao.setMargemX(0.59296876f);
        posicao.setMargemY(0.16027875f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelTocar1.setPosicoes(posicoes);

        /** Nivel 3*/
        NivelTocar nivelTocar2 = new NivelTocar();
        nivelTocar2.setJogo(new Jogo());
        nivelTocar2.getJogo().setID((int) l);
        nivelTocar2.setNumTarefas(4);
        nivelTocar2.setNumero(3);
        nivelTocar2.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_3));
        nivelTocar2.setQtdeEstimulos(3);
        nivelTocar2.setFase(new Fase());
        nivelTocar2.getFase().setID(1);
        nivelTocar2.setLimiteSomDeErro(3);
        nivelTocar2.setLimiteErros(5);
        nivelTocar2.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.04375f);
        posicao.setMargemY(0.18989547f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.3734375f);
        posicao.setMargemY(0.18641116f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.69921875f);
        posicao.setMargemY(0.19512194f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelTocar2.setPosicoes(posicoes);


        /** Nivel 4*/
        NivelTocar nivelTocar3 = new NivelTocar();
        nivelTocar3.setJogo(new Jogo());
        nivelTocar3.getJogo().setID((int) l);
        nivelTocar3.setNumTarefas(4);
        nivelTocar3.setNumero(4);
        nivelTocar3.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_4));
        nivelTocar3.setQtdeEstimulos(3);
        nivelTocar3.setFase(new Fase());
        nivelTocar3.getFase().setID(1);
        nivelTocar3.setLimiteSomDeErro(3);
        nivelTocar3.setLimiteErros(5);
        nivelTocar3.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.04375f);
        posicao.setMargemY(0.18989547f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.3734375f);
        posicao.setMargemY(0.18641116f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.69921875f);
        posicao.setMargemY(0.19512194f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelTocar3.setPosicoes(posicoes);


        /** Nivel 5*/
        NivelTocar nivelTocar4 = new NivelTocar();
        nivelTocar4.setJogo(new Jogo());
        nivelTocar4.getJogo().setID((int) l);
        nivelTocar4.setNumTarefas(4);
        nivelTocar4.setNumero(5);
        nivelTocar4.setUriImagem(retornaUriDrawable(context, R.drawable.nivel_5));
        nivelTocar4.setQtdeEstimulos(3);
        nivelTocar4.setFase(new Fase());
        nivelTocar4.getFase().setID(1);
        nivelTocar4.setLimiteSomDeErro(3);
        nivelTocar4.setLimiteErros(5);
        nivelTocar4.setLimiteInstrucoesTTS(2);

        posicoes = new ArrayList<>();

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.04375f);
        posicao.setMargemY(0.18989547f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.3734375f);
        posicao.setMargemY(0.18641116f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5609756f);
        posicao.setLargura(0.2515625f);
        posicao.setMargemX(0.69921875f);
        posicao.setMargemY(0.19512194f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_INSIGNIFICANTE.getObjeto());
        posicoes.add(posicao);

        nivelTocar4.setPosicoes(posicoes);


        try{
            nivelTocarDAO.salvar(nivelTocar);
            nivelTocarDAO.salvar(nivelTocar1);
            nivelTocarDAO.salvar(nivelTocar2);
            nivelTocarDAO.salvar(nivelTocar3);
            nivelTocarDAO.salvar(nivelTocar4);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static long criarJogos(JogoDAO jogoDAO, List<Estimulo> estimulos, String uriImagem, String nomePaciente, String tema){
        Jogo jogo = new Jogo();
        jogo.setDataCriacao(new Date());
        jogo.setEstimulos(estimulos);
        jogo.setFases(null);
        jogo.setImg(new Imagem());
        jogo.getImagem().setUri(uriImagem);
        jogo.getImagem().setNome("coxinha");
        jogo.setPaciente(new Paciente());

        jogo.getPaciente().setNome(nomePaciente);
        jogo.getPaciente().setDtNascimento(new Date());
        jogo.setReforcadores(null);
        jogo.setTema(tema);
        jogo.setUltimaAtualizacao(new Date());


        try{
            return jogoDAO.salvar(jogo);

        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }



    public static void criarEstimulos(String genero, int idCate, String uriImagem, String uriMascara, String nomeEstimulo, EstimuloDAO estimuloDAO, Context context){
        Estimulo estimulo;
        CategoriaEstimulo primeiraCategoria = new CategoriaEstimulo();
        primeiraCategoria.setID(idCate);
        Imagem imagem = new Imagem();
        imagem.setLargura(10);
        imagem.setAltura(10);
        imagem.setNome("ic_casa.png");
        imagem.setUri(uriImagem);
        imagem.setUriMascara(uriMascara);
        imagem.setDataCriacao(new Date());
        imagem.setUltimaAtualizacao(new Date());

        estimulo = new Estimulo();
        estimulo.setCategoria(primeiraCategoria);

        estimulo.setNome(nomeEstimulo);
        estimulo.setDataCriacao(new Date());
        estimulo.setUltimaAtualizacao(new Date());
        estimulo.setAudio(null);
        estimulo.setImagem(imagem);
        estimulo.setGenero(genero);

        try{
            estimuloDAO.salvar(estimulo);
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_LONG);
        }
    }
}
