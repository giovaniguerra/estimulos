/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.util.List;

/**
 *
 * @author Caio Gustavo
 */
public class Resultado {
    
    private String mensagem;
    private List<IEntidade> entidades;

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the entidades
     */
    public List<IEntidade> getEntidades() {
        return entidades;
    }

    /**
     * @param entidades the entidades to set
     */
    public void setEntidades(List<IEntidade> entidades) {
        this.entidades = entidades;
    }

    
}
