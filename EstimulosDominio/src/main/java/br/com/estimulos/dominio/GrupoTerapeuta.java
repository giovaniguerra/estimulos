package br.com.estimulos.dominio;

/** 
 * @author Caio Gustavo R. Cruz - Primeiva versão da classe. 
 */
public class GrupoTerapeuta extends EntidadeDominio{
    
    private Integer id_terapeuta;
    private Integer id_grupo;

    /**
     * @return the id_terapeuta
     */
    public Integer getId_terapeuta() {
        return id_terapeuta;
    }

    /**
     * @param id_terapeuta the id_terapeuta to set
     */
    public void setId_terapeuta(Integer id_terapeuta) {
        this.id_terapeuta = id_terapeuta;
    }

    /**
     * @return the id_grupo
     */
    public Integer getId_grupo() {
        return id_grupo;
    }

    /**
     * @param id_grupo the id_grupo to set
     */
    public void setId_grupo(Integer id_grupo) {
        this.id_grupo = id_grupo;
    }

}
