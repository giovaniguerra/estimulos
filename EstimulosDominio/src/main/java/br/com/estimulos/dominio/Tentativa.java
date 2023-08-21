/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Giovani
 */
public class Tentativa extends EntidadeDominio{
    private Tarefa tarefa;
    private List<Movimento> movimentos;
    private transient ObjetoTela objetoInicial;
    private transient ObjetoTela objetoFinal;
    private TipoResultado resultado;
    
    public Tentativa(){
        movimentos = new ArrayList<>();
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<Movimento> movimentos) {
        this.movimentos = movimentos;
    }
    
    public void addMovimento(Movimento movimento){
        this.movimentos.add(movimento);
    }

    public ObjetoTela getObjetoInicial() {
        return objetoInicial;
    }

    public void setObjetoInicial(ObjetoTela objetoInicial) {
        this.objetoInicial = objetoInicial;
    }

    public ObjetoTela getObjetoFinal() {
        return objetoFinal;
    }

    public void setObjetoFinal(ObjetoTela objetoFinal) {
        this.objetoFinal = objetoFinal;
    }

    public TipoResultado getResultado() {
        return resultado;
    }

    public void setResultado(TipoResultado resultado) {
        this.resultado = resultado;
    }   
}
