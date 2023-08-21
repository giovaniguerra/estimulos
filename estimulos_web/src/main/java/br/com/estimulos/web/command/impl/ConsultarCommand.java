/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.command.impl;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("CONSULTAR")
public class ConsultarCommand extends AbstractCommand{

    @Override
    public Resultado execute(IEntidade entidade) {
        return fachada.consultar(entidade);
    }
    
}
