package br.com.estimulos.app.core.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.estimulos.app.core.dao.AudioDAO;
import br.com.estimulos.app.core.dao.BancoDadosHelper;
import br.com.estimulos.app.core.dao.CategoriaEstimuloDAO;
import br.com.estimulos.app.core.dao.EstimuloDAO;
import br.com.estimulos.app.core.dao.EstimuloPosicaoDAO;
import br.com.estimulos.app.core.dao.FaseDAO;
import br.com.estimulos.app.core.dao.ImagemDAO;
import br.com.estimulos.app.core.dao.JogoDAO;
import br.com.estimulos.app.core.dao.MovimentoDAO;
import br.com.estimulos.app.core.dao.NivelArrastarDAO;
import br.com.estimulos.app.core.dao.NivelTocarDAO;
import br.com.estimulos.app.core.dao.ObjetoTelaDAO;
import br.com.estimulos.app.core.dao.PacienteDAO;
import br.com.estimulos.app.core.dao.PosicaoDAO;
import br.com.estimulos.app.core.dao.ReforcadorDAO;
import br.com.estimulos.app.core.dao.TarefaDAO;
import br.com.estimulos.app.core.dao.TentativaDAO;
import br.com.estimulos.app.core.dao.TerapeutaDAO;
import br.com.estimulos.app.core.dao.TipoResultadoDAO;
import br.com.estimulos.app.interfaces.InterfaceDAO;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.ObjetoTela;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Tentativa;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.TipoResultado;

/**
 * Created by caioc_000 on 13/03/2016.
 */
public class DAOFactory {

    /**O mapa dos DAOs*/
    private static Map<Class<? extends EntidadeDominio>, InterfaceDAO> mapDAO;

    /** Constroi o mapa de DAOs*/
    private static void construirMapa(Context context){
        mapDAO = new HashMap<>();
        mapDAO.put(FaseArrastar.class, new NivelArrastarDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(FaseTocar.class, new NivelTocarDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Audio.class, new AudioDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(CategoriaEstimulo.class, new CategoriaEstimuloDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Estimulo.class, new EstimuloDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Fase.class, new FaseDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Imagem.class, new ImagemDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Jogo.class, new JogoDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Movimento.class, new MovimentoDAO(BancoDadosHelper.getInstance(context)));
//        mapDAO.put(NivelArrastar.class, new NivelArrastarDAO(BancoDadosHelper.getInstance(context)));
//        mapDAO.put(NivelTocar.class, new NivelTocarDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(ObjetoTela.class, new ObjetoTelaDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Paciente.class, new PacienteDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Posicao.class, new PosicaoDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Reforcador.class, new ReforcadorDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Tarefa.class, new TarefaDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Tentativa.class, new TentativaDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(Terapeuta.class, new TerapeutaDAO(BancoDadosHelper.getInstance(context)));
        mapDAO.put(TipoResultado.class, new TipoResultadoDAO(BancoDadosHelper.getInstance(context)));
    }

    public static InterfaceDAO getDAO(Context context, Class<? extends EntidadeDominio> entidade){

        if(mapDAO == null)
            construirMapa(context);
        return mapDAO.get(entidade);
    }
}
