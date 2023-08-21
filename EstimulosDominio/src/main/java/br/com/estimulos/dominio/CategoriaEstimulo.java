package br.com.estimulos.dominio;

import java.util.List;

/** 
 *
 * @author Caio Gustavo R. Cruz - Primeiva versão da classe. 
 */
public class CategoriaEstimulo extends EntidadeDominio{
    
    private String nome;
    private List<Estimulo> estimulos;

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

}
