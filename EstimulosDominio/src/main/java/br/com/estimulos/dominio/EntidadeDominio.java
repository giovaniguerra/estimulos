/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Giovani
 */
public class EntidadeDominio implements IEntidade , Serializable{
    /**
     * Este id eh o identificar gerado pelo banco de dados 
     */
    private Integer ID;
    /**
     * Codigo e o identificador da entidade em ambos os bancos de dados
     */
    private String codigo;
    
    private boolean sincronizado;
    
    private Date dataCriacao;
    
    private Date ultimaAtualizacao;

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public Date getDataCriacao() {
        return dataCriacao;
    }

    @Override
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    @Override
    public void setUltimaAtualizacao(Date ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the sincronizado
     */
    public boolean getSincronizado() {
        return sincronizado;
    }

    /**
     * @param sincronizado the sincronizado to set
     */
    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }


}
