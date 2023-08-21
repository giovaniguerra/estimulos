/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.command;

import br.com.estimulos.core.fachada.impl.Fachada;
import br.com.estimulos.core.fachada.IFachada;

/**
 *
 * @author Caio Gustavo
 */
public abstract class AbstractCommand implements ICommand{

    protected IFachada fachada = new Fachada();
    
}
