/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Giovani
 */
public class Tarefa extends EntidadeDominio{
    private Integer numTarefa;
    private List<LocalEstimulo> listLocais;
    private boolean completada;
    private Integer faseId;
    
    public Tarefa(){
        listLocais = new ArrayList<>();
    }

    public Integer getNumTarefa() {
        return numTarefa;
    }

    public void setNumTarefa(Integer numTarefa) {
        this.numTarefa = numTarefa;
    }

    public List<LocalEstimulo> getListLocais() {
        return listLocais;
    }

    public void setListLocais(List<LocalEstimulo> listLocais) {
        this.listLocais = listLocais;
    }
    
    public void addLocalEstimulo(LocalEstimulo localEstimulo){
        listLocais.add(localEstimulo);
    }
    
    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    /**
     * @return the faseId
     */
    public Integer getFaseId() {
        return faseId;
    }

    /**
     * @param faseId the faseId to set
     */
    public void setFaseId(Integer faseId) {
        this.faseId = faseId;
    }
}
