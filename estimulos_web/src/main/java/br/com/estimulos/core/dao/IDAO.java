/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Caio Gustavo
 */
public interface IDAO {
    
    public void salvar(IEntidade entidade) throws SQLException;
    public void alterar(IEntidade entidade)throws SQLException;
    public void excluir(IEntidade entidade)throws SQLException;
    public List<IEntidade> consultar(IEntidade entidade)throws SQLException; 
    public IEntidade visualizar (IEntidade entidade) throws SQLException;
}
