package br.com.estimulos.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caio Cruz
 */
public class Grupo extends EntidadeDominio {

    private String nome;
    private String descricao;
    private List<Paciente> pacientes;
    private List<Terapeuta> terapeutas;

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the pacientes
     */
    public List<Paciente> getPacientes() {
        if(pacientes == null)
            pacientes = new ArrayList<>();
        return pacientes;
    }

    /**
     * @param pacientes the pacientes to set
     */
    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    /**
     * @return the terapeutas
     */
    public List<Terapeuta> getTerapeutas() {
        if(terapeutas == null)
            terapeutas = new ArrayList<>();
        return terapeutas;
    }

    /**
     * @param terapeutas the terapeutas to set
     */
    public void setTerapeutas(List<Terapeuta> terapeutas) {
        this.terapeutas = terapeutas;
    }
}
