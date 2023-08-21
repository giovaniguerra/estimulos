/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.util.Date;

/**
 *
 * @author Giovani
 */
public interface IEntidade {
    
    public Integer getID();
    
    public void setID(Integer ID);
    
    public Date getDataCriacao();

    public void setDataCriacao(Date dataCriacao);

    public Date getUltimaAtualizacao();

    public void setUltimaAtualizacao(Date ultimaAtualizacao);
}
