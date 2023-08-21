/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.strategy;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;

/**
 *
 * @author Caio Gustavo
 */
public interface IStrategy {
    
     public String processar(IEntidade entidade); 
}
