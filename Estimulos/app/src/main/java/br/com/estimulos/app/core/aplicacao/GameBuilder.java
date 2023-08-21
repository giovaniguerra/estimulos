package br.com.estimulos.app.core.aplicacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Nivel;

/**
 * Created by Giovani on 29/02/2016.
 */

/**
 * A classe GameBuilder eh responsavel por conhecer todos os dados necessarios para a criacao
 * das tarefas a serem realizadas pelo paciente durante o jogo.
 */

public class GameBuilder implements Serializable {

    /**
     * Constantes
     */
// Constantes
    public final static String TAG = "GAME_BUILDER";

    /**
     * Variaveis
     */
// Variaveis
    /**
     * ID do jogo a ser construido
     */
    private int idJogo;

    /**
     * Contador de tarefas avancadas durante o jogo
     */
    private int contadorTarefa;

    /**
     * ID da categoria do estimulo a ser jogado.
     */
    private Integer idCategoria;

    /**
     * Estimulo que está como principal dentro de uma tarefa
     */
    private Estimulo estimuloPrincipalAtual;

    /**
     * Lista com os estimulos de uma categoria escolhida
     */
    private List<Estimulo> estimulosPrincipais;

    /**
     * Lista com todos os estimulosInsignificantes utilizados para criar a tarefa (insignificantes)
     */
    private List<Estimulo> estimulosInsignificantes;

    /**
     * Nivel de dificuldade escolhido para a fase
     */
    private Nivel nivel;

    /**
     * Fase selecionada
     */
    private String nomeFase;

    /**
     * Boolean para guardar se o toque na tela deve ser bloqueado ou nao
     *          TRUE - Indica que o toque nao deve ser realizado
     *          FALSE - Indica que o toque deve ser processado
     */
    private boolean toqueBloqueado;

    /**
     * Construtor
     */
// Construtor
    public GameBuilder(int idJogo) {
        this.idJogo = idJogo;
        this.contadorTarefa = 0;
    }

    /**
     * Outros metodos
     */
// Outros metodos

    /**
     * Metodo para adicionar um estimulo insignificante a lista de estimulosInsignificantes
     */
    public void addEstimuloInsignificante(Estimulo estimulo) {
        if (estimulosInsignificantes == null) {
            estimulosInsignificantes = new ArrayList<>();
        }
        estimulosInsignificantes.add(estimulo);
    }

    /**
     * Metodo para adicionar um estimulo principal a lista de estimulosPrincipais
     */
    public void addEstimuloPrincipal(Estimulo estimulo) {
        if (estimulosPrincipais == null) {
            estimulosPrincipais = new ArrayList<>();
        }
        estimulosPrincipais.add(estimulo);
    }

    /**
     * Getters and Setters
     */
// Getters and Setters
    public int getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public int getContadorTarefa() {
        return contadorTarefa;
    }

    public void setContadorTarefa(int contadorTarefa) {
        this.contadorTarefa = contadorTarefa;
    }

    public List<Estimulo> getEstimulosInsignificantes() {
        return estimulosInsignificantes;
    }

    public void setEstimulosInsignificantes(List<Estimulo> estimulosInsignificantes) {
        this.estimulosInsignificantes = estimulosInsignificantes;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Estimulo getEstimuloPrincipalAtual() {
        return estimuloPrincipalAtual;
    }

    public void setEstimuloPrincipalAtual(Estimulo estimuloPrincipalAtual) {
        this.estimuloPrincipalAtual = estimuloPrincipalAtual;
    }

    public List<Estimulo> getEstimulosPrincipais() {
        return estimulosPrincipais;
    }

    public void setEstimulosPrincipais(List<Estimulo> estimulosPrincipais) {
        this.estimulosPrincipais = estimulosPrincipais;
    }

    public String getNomeFase() {
        return nomeFase;
    }

    public void setNomeFase(String nomeFase) {
        this.nomeFase = nomeFase;
    }

    public boolean isToqueBloqueado() {
        return toqueBloqueado;
    }

    public void setToqueBloqueado(boolean toqueBloqueado) {
        this.toqueBloqueado = toqueBloqueado;
    }
}
