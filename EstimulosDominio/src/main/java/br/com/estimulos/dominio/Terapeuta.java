package br.com.estimulos.dominio;

import java.util.List;

/** 
 * @author Caio Gustavo R. Cruz
 */
public class Terapeuta extends Pessoa{

    private Usuario usuario;

    /**
     * @return the grupos
     */
//    public List<Grupo> getGrupos() {
//        return grupos;
//    }
//
//    /**
//     * @param grupos the grupos to set
//     */
//    public void setGrupos(List<Grupo> grupos) {
//        this.grupos = grupos;
//    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
