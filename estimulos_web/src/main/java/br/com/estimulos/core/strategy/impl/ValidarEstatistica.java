package br.com.estimulos.core.strategy.impl;

import br.com.estimulos.core.strategy.IStrategy;
import br.com.estimulos.dominio.Estatistica;
import br.com.estimulos.dominio.Grafico;
import br.com.estimulos.dominio.IEntidade;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
public class ValidarEstatistica implements IStrategy {

    @Override
    public String processar(IEntidade entidade) {
        String msg="Não é possível gerar este gráfico!";
        Estatistica grafico = (Grafico) entidade;        
//        if(grafico.getFase().getID() != null && grafico.getTipo().getID() != null &&
//                grafico.getInicio() != null && grafico.getFim() != null){
//            return null;
//        }        
        //return msg;
        return null;
    }

}
