/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.view.model;

import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.ICommand;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Caio Gustavo
 */
public interface IViewModel {
    
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando);
    
    /** Utilizado para converter uma String JSON em um Resultado
     * 
     * @param json
     * @param comando
     * @return 
     */
    public Resultado getObjetoResultado(InputStream stream, ICommand comando);
    
}
