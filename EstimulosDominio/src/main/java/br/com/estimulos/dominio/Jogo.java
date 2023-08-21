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
public class Jogo extends EntidadeDominio{
    private String tema;
    private Imagem imagem;
    private Paciente paciente;
    private List<Estimulo> estimulos;
    private List<Reforcador> reforcadores;
    private List<Fase> fases;
    
    public void addEstimulo(Estimulo estimulo){
        this.estimulos.add(estimulo);
    }

    public void addReforcador(Reforcador reforcador){
        this.reforcadores.add(reforcador);
    }
    
    public void addFase(Fase fase){
        this.fases.add(fase);
    }
        
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Imagem getImagem() {
        if(imagem == null)
            imagem = new Imagem();
        return imagem;
    }

    public void setImg(Imagem imagem) {
        this.imagem = imagem;
    }

    public Paciente getPaciente() {
        if(paciente == null)
            paciente = new Paciente();
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Estimulo> getEstimulos() {
        if(estimulos == null)
            estimulos = new ArrayList<>();
        return estimulos;
    }

    public List<Reforcador> getReforcadores() {
        if(reforcadores == null)
            reforcadores = new ArrayList<>();
        return reforcadores;
    }

    public void setReforcadores(List<Reforcador> reforcadores) {
        this.reforcadores = reforcadores;
    }

    public void setEstimulos(List<Estimulo> estimulos) {
        this.estimulos = estimulos;
    }

    public List<Fase> getFases() {
        if(fases == null)
            fases = new ArrayList<>();
        return fases;
    }

    public void setFases(List<Fase> fases) {
        this.fases = fases;
    }

}
