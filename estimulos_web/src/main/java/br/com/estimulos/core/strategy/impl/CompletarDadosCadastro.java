/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.strategy.impl;

import br.com.estimulos.core.strategy.IStrategy;
import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import java.util.Date;

/**
 *
 * @author Caio Gustavo
 */
public class CompletarDadosCadastro implements IStrategy{

    @Override
    public String processar(IEntidade entidade) {
        
        EntidadeDominio dominio = (EntidadeDominio) entidade;
        dominio.setDataCriacao(new Date());
        dominio.setUltimaAtualizacao(new Date());
        
        if(dominio.getID() == null)
            dominio.setSincronizado(false);
        else
            dominio.setSincronizado(true);
        
        
        return null;
    }
    
}
