package br.com.estimulos.dominio;

import java.util.Date;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
public abstract class Estatistica extends EntidadeDominio {
    
    private CategoriaEstimulo categoria;
    private Estimulo estimulo;
    private Fase fase;
    private TipoResultado tipo;
    private Date inicio;
    private Date fim;
    private Long data;
    private Paciente paciente;

    /**
     * @return the categoria
     */
    public CategoriaEstimulo getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(CategoriaEstimulo categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the estimulo
     */
    public Estimulo getEstimulo() {
        return estimulo;
    }

    /**
     * @param estimulo the estimulo to set
     */
    public void setEstimulo(Estimulo estimulo) {
        this.estimulo = estimulo;
    }

    /**
     * @return the fase
     */
    public Fase getFase() {
        return fase;
    }

    /**
     * @param fase the fase to set
     */
    public void setFase(Fase fase) {
        this.fase = fase;
    }

    /**
     * @return the tipo
     */
    public TipoResultado getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(TipoResultado tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fim
     */
    public Date getFim() {
        return fim;
    }

    /**
     * @param fim the fim to set
     */
    public void setFim(Date fim) {
        this.fim = fim;
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the data
     */
    public Long getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Long data) {
        this.data = data;
    }
    
}
