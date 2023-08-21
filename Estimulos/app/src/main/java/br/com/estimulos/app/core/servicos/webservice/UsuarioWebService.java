package br.com.estimulos.app.core.servicos.webservice;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import br.com.estimulos.app.core.util.Util;
import br.com.estimulos.app.ui.launcher.MainActivity;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;

/**
 * Classe responsável por tratar requisições referentes à um usuário.
 *
 * Created by Giovani on 22/04/2016.
 */
public class UsuarioWebService extends AbstractWebService {
    /*
     * Contantes privadas
     */
    private final static String USUARIO_URL = "/rest/usuarios";
    private final static String LOGIN_ACTION_URL = "/login";
    private final static String REGISTRO_ACTION_URL = "/registrar";

    /**
     * Construtor do objeto
     * @param context - Contexto da aplicação
     */
    public UsuarioWebService(Context context){
        super(context);
    }

    /**
     * Método para registrar um terapeuta no servidor.
     * @param terapeuta
     * @param responseHandler
     */
    public void registrar(Terapeuta terapeuta, AsyncHttpResponseHandler responseHandler){
        // Testar conexão com internet
        if(Util.isOnline(context)){
            enviar(terapeuta, USUARIO_URL + REGISTRO_ACTION_URL, responseHandler);
            // Possui conexão
        } else {
            // Não possui conexão com internet
            Toast.makeText(context, "Deu pau ou não reconheceu a conexão!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método para verificar as credenciais de um usuário.
     * @param usuario
     * @param responseHandler
     */
    public void logar(Usuario usuario, AsyncHttpResponseHandler responseHandler){
        // Testar conexão com internet
        if(Util.isOnline(context)){
            enviar(usuario, USUARIO_URL + LOGIN_ACTION_URL, responseHandler);
        // Possui conexão
        } else {
            // Não possui conexão
            Toast.makeText(context, "Deu pau ou não reconheceu a conexão!", Toast.LENGTH_LONG).show();
            // Possui conta salva no celular?
            if(consultarLoginOffline(usuario)){
                navegarParaHomeActivity();
            } else {
                // conta inválida
                Toast.makeText(context, "Email ou senha incorretos!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Método para consultar um login no banco interno do aplicativo, caso não haja internet.
     * @param u - O usuário contendo suas credenciais.
     * @return  TRUE - Indica que o login e senha informados estão corretos.
     *          FALSE - Indica que o login e senha informados não estão corretos.
     */
    private boolean consultarLoginOffline(Usuario u) {
        // TODO : Cade o UsuarioDAO ??
//        UsuarioDAO dao = new UsuarioDAO(BancoDadosHelper.getInstance(context));
//        dao.consultar(u);
        return false;
    }

    /**
     * Método para chamar a Activity inicial do Jogo.
     */
    public void navegarParaHomeActivity(){
        Intent homeIntent = new Intent(context,MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(homeIntent);
    }
}
