/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.servico.login;

import br.com.estimulos.dominio.Usuario;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Caio Gustavo
 */
public class LoginUtil {
    
    public static Usuario usuarioLogado;
    public static Long TEMPO_EXPIRAR_LOGIN = 50000000l;//Long.getLong("login.tempo.expiracao");
    public static Calendar ultimaAcao = Calendar.getInstance();

    
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void logout(){
        usuarioLogado = null;
        atualizarUltimaAcao();
    }
    public static void setUsuarioLogado(Usuario usuarioLogado) {
        LoginUtil.usuarioLogado = usuarioLogado;
        atualizarUltimaAcao();
    }
    public static void atualizarUltimaAcao(){    
        ultimaAcao.setTime(new Date());
    }

    
}
