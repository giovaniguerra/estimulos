package br.com.estimulos.app.core.servicos.webservice;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.dominio.Tarefa;

/**
 * Classe responsável por tratar requisições sobre as estatísticas de uso do jogo.
 *
 * Created by Giovani on 08/05/2016.
 */
public class EstatisticaWebService extends AbstractWebService{
    /*
     * Constantes privadas
     */
    private final static String ESTATISTICA_URL = "/rest/estatistica";

    /**
     * Construtor do objeto
     */
    public EstatisticaWebService(Context context) {
        super(context);
    }

    /*
     * Métodos da classe
     */

    /**
     * Método para enviar uma lista de tarefas ao servidor.
     * @param tarefas
     * @param responseHandler
     */
    public void enviarEstatisticas(List<Tarefa> tarefas, AsyncHttpResponseHandler responseHandler) {
        // Testar conexão com internet
        if(Util.isOnline(context)){
            enviar(tarefas, ESTATISTICA_URL, responseHandler);
        } else {
            // Não possui conexão com internet
            Toast.makeText(context, "Deu pau ou não reconheceu a conexão!", Toast.LENGTH_LONG).show();
        }
    }

}
