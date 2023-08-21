package br.com.estimulos.web.helper;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.TipoResultado;
import java.util.Date;
import java.util.List;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
public class EstatisticaHelper {
    
    private List<CategoriaEstimulo> categorias;
    private List<Estimulo> estimulos;
    private List<Fase> fases;
    private List<Nivel> niveis;
    private List<TipoResultado> motivos;
    private Date inicio;
    private Date fim;

    /**
     * @return the categorias
     */
    public List<CategoriaEstimulo> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias the categorias to set
     */
    public void setCategorias(List<CategoriaEstimulo> categorias) {
        this.categorias = categorias;
    }

    /**
     * @return the estimulos
     */
    public List<Estimulo> getEstimulos() {
        return estimulos;
    }

    /**
     * @param estimulos the estimulos to set
     */
    public void setEstimulos(List<Estimulo> estimulos) {
        this.estimulos = estimulos;
    }

    /**
     * @return the fases
     */
    public List<Fase> getFases() {
        return fases;
    }

    /**
     * @param fases the fases to set
     */
    public void setFases(List<Fase> fases) {
        this.fases = fases;
    }

    /**
     * @return the niveis
     */
    public List<Nivel> getNiveis() {
        return niveis;
    }

    /**
     * @param niveis the niveis to set
     */
    public void setNiveis(List<Nivel> niveis) {
        this.niveis = niveis;
    }

    /**
     * @return the motivos
     */
    public List<TipoResultado> getMotivos() {
        return motivos;
    }

    /**
     * @param motivos the motivos to set
     */
    public void setMotivos(List<TipoResultado> motivos) {
        this.motivos = motivos;
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
}
