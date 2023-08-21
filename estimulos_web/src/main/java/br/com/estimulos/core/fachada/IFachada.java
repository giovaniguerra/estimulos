/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.fachada;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;

/**
 *
 * @author Caio Gustavo
 */
public interface IFachada {
    
    public Resultado salvar(IEntidade entidade);
    public Resultado alterar(IEntidade entidade);
    public Resultado excluir(IEntidade entidade);
    public Resultado consultar(IEntidade entidade);
    public Resultado visualizar(IEntidade entidade);
    
}
