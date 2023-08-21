package br.com.estimulos.dominio;

import java.util.List;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
public class Replay extends Estatistica {
    
    private List<Tentativa> tentativas;

    /**
     * @return the tentativas
     */
    public List<Tentativa> getTentativas() {
        return tentativas;
    }

    /**
     * @param tentativas the tentativas to set
     */
    public void setTentativas(List<Tentativa> tentativas) {
        this.tentativas = tentativas;
    }

}
