/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;


/**
 *
 * @author Giovani
 */
public class Reforcador extends EntidadeDominio{
   private String nome;
   
   private Integer tempoDuracao;
   
   private Midia midia;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTempoDuracao() {
        return tempoDuracao;
    }

    public void setTempoDuracao(Integer tempoDuracao) {
        this.tempoDuracao = tempoDuracao;
    }

    public Midia getMidia() {
        return midia;
    }

    public void setMidia(Midia midia) {
        this.midia = midia;
    }
    
}
