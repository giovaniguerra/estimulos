/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

/**
 *
 * @author Giovani
 */
public class ObjetoTela extends EntidadeDominio{
    private String codigo;
    private Integer tipoObjetoTela;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getTipoObjetoTela() {
        return tipoObjetoTela;
    }

    public void setTipoObjetoTela(Integer tipoObjetoTela) {
        this.tipoObjetoTela = tipoObjetoTela;
    }
    
    
}
