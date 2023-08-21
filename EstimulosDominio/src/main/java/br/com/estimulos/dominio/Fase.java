/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.util.List;

/**
 *
 * @author Giovani
 */
public class Fase extends EntidadeDominio{
    private String nome;
    private String uriImagem;
    private String nomeClasseHerdade;
    private List<Nivel> niveis;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUriImagem() {
        return uriImagem;
    }

    public void setUriImagem(String uriImagem) {
        this.uriImagem = uriImagem;
    }

    /**
     * @return the nomeClasseHerdade
     */
    public String getNomeClasseHerdade() {
        return nomeClasseHerdade;
    }

    /**
     * @param nomeClasseHerdade the nomeClasseHerdade to set
     */
    public void setNomeClasseHerdade(String nomeClasseHerdade) {
        this.nomeClasseHerdade = nomeClasseHerdade;
    }

    /**
     * @return the niveis
     */
    public List<Nivel> getNiveis() {
        return niveis;
    }

    /**
     * @param niveis the niveis to set
     */
    public void setNiveis(List<Nivel> niveis) {
        this.niveis = niveis;
    }
    
    
    
}
