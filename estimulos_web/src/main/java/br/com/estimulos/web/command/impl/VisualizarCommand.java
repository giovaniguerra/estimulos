/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.command.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.AbstractCommand;
import br.com.estimulos.web.command.ICommand;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("VISUALIZAR")
public class VisualizarCommand extends AbstractCommand{

    @Override
    public Resultado execute(IEntidade entidade) {
        return fachada.visualizar(entidade);
    }
    
}
