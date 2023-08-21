/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.filtro;

import br.com.estimulos.web.servico.login.LoginUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Caio Gustavo
 */
public class FiltroAcesso implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    // Metodo que autoriza o acesso a uma pagina requisitada
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long ultimoAcesso = LoginUtil.ultimaAcao.getTimeInMillis();
        long novoAcesso = java.util.Calendar.getInstance().getTimeInMillis();
        long inatividadeMaxima = LoginUtil.TEMPO_EXPIRAR_LOGIN;
        long tempoInativo = novoAcesso - ultimoAcesso;
        String paginaDestino = ((HttpServletRequest) request).getRequestURI();

        // eh uma requisicao para o rest?
        int contem = paginaDestino.indexOf("rest");
        if (contem < 1) {
            // o usuário está logado?
            if (LoginUtil.usuarioLogado != null) {
                // ultrapassou o tempo de inatividade?
                if (tempoInativo > inatividadeMaxima) {
                    LoginUtil.logout();
                    request.setAttribute("msg", "Sua sessão expirou");
                    request.getRequestDispatcher("/Login.jsp").forward(request, response);
                } else {
                    LoginUtil.atualizarUltimaAcao();
                    chain.doFilter(request, response);
                }
            } // Usuário não está logado e deseja logar?
            else if (paginaDestino.equals("/estimulos/Login")) {
                chain.doFilter(request, response);
            } // Usuário não está logado e quer acessar uma pagina qualquer?
            else {
                request.setAttribute("msg", "Você não está logado");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } else{
            chain.doFilter(request, response);
        }
            
    }

    @Override
    public void destroy() {
    }

}
