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
public abstract class Nivel extends EntidadeDominio {
    private Jogo jogo;
    private Integer numero;
    private Integer numTarefas;
    private String uriImagem;
    private Integer qtdeEstimulos;
    private List<Tarefa> listTarefas;
    private Fase fase;
    /** Lista de posicoes de cada ImageView com o tipo do Estimulo a ser inserido. */
    private List<Posicao> posicoes;
    private Integer limiteErros;
    private Integer limiteSomDeErro;
    private Integer limiteInstrucoesTTS;
    /** Este valor booleano indica se a posicao entre os estimulos alvo e 
     insignificantes poderao alternar em suas posicoes a cada tarefa.
            TRUE - indica que a posicao sera randomizada entre eles.
            FALSE - indica que a posicao nao mudara entre eles.*/
    private boolean randPosicao;    
    
    
    /** ATRIBUTOS QUE TALVEZ USAREMOS */
    /** Este valor booleano indica se o estimulo principal deve ser gerado 
     randomicamente a cada tarefa.
            TRUE - indica que o estimulo principal sera criado randomicamente.
            FALSE - indica que sempre sera o mesmo estimulo escolhido.*/
//    private boolean randPrincipal;
    
    /** Este valor booleano indica se devem ser criados novas tarefas ou se
     * as tarefas devem ser carregadas do banco.
     *      TRUE - indica que deve ser criado novas tarefas.
     *      FALSE - indica que deve ser carregados do banco (quando houver).*/
//    private boolean randTarefa;
    
    /** Variavel responsavel por saber a partir de qual tarefa o estimulo 
     principal sera randomizado.
     */
//    private Integer inicioRandPrincipal;
    

    protected Nivel(){
        listTarefas = new ArrayList<>();
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumTarefas() {
        return numTarefas;
    }

    public void setNumTarefas(Integer numTarefas) {
        this.numTarefas = numTarefas;
    }

    public List<Tarefa> getListTarefas() {
        return listTarefas;
    }

    public void setListTarefas(List<Tarefa> listTarefas) {
        this.listTarefas = listTarefas;
    }

    public void addTarefa(Tarefa tarefa){
        listTarefas.add(tarefa);
    }
    
    /**
     * @return the uriImagem
     */
    public String getUriImagem() {
        return uriImagem;
    }

    /**
     * @param uriImagem the uriImagem to set
     */
    public void setUriImagem(String uriImagem) {
        this.uriImagem = uriImagem;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Integer getQtdeEstimulos() {
        return qtdeEstimulos;
    }

    public void setQtdeEstimulos(Integer qtdeEstimulos) {
        this.qtdeEstimulos = qtdeEstimulos;
    }
    
//    public boolean isRandPrincipal() {
//        return randPrincipal;
//    }
//
//    public void setRandPrincipal(boolean randPrincipal) {
//        this.randPrincipal = randPrincipal;
//    }

    public boolean isRandPosicao() {
        return randPosicao;
    }

    public void setRandPosicao(boolean randPosicao) {
        this.randPosicao = randPosicao;
    }

//    public boolean isRandTarefa() {
//        return randTarefa;
//    }
//
//    public void setRandTarefa(boolean randTarefa) {
//        this.randTarefa = randTarefa;
//    }
//
//    public Integer getInicioRandPrincipal() {
//        return inicioRandPrincipal;
//    }
//
//    public void setInicioRandPrincipal(Integer inicioRandPrincipal) {
//        this.inicioRandPrincipal = inicioRandPrincipal;
//    }

    public List<Posicao> getPosicoes() {
        return posicoes;
    }

    public void setPosicoes(List<Posicao> posicoes) {
        this.posicoes = posicoes;
    }

    /**
     * @return the fase
     */
    public Fase getFase() {
        return fase;
    }

    /**
     * @param fase the fase to set
     */
    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Integer getLimiteErros() {
        return limiteErros;
    }

    public void setLimiteErros(Integer limiteErros) {
        this.limiteErros = limiteErros;
    }

    public Integer getLimiteSomDeErro() {
        return limiteSomDeErro;
    }

    public void setLimiteSomDeErro(Integer limiteSomDeErro) {
        this.limiteSomDeErro = limiteSomDeErro;
    }

    public Integer getLimiteInstrucoesTTS() {
        return limiteInstrucoesTTS;
    }

    public void setLimiteInstrucoesTTS(Integer limiteInstrucoesTTS) {
        this.limiteInstrucoesTTS = limiteInstrucoesTTS;
    }

}
