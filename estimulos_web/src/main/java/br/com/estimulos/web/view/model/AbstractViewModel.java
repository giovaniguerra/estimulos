/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.view.model;

import br.com.estimulos.core.fachada.IFachada;
import br.com.estimulos.core.strategy.IStrategy;

/**
 *
 * @author Caio Gustavo
 */
public abstract class AbstractViewModel implements IViewModel{    
    protected IFachada fachada;
    protected IStrategy strategy;
}
