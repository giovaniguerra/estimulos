/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.command;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;

/**
 *
 * @author Caio Gustavo
 */
public interface ICommand {
    
    public Resultado execute(IEntidade entidade);
    
}
