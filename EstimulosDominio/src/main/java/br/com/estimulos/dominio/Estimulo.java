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
public class Estimulo extends EntidadeDominio{
    private String nome;
    private Imagem imagem;
    private Audio audio;
    private CategoriaEstimulo categoria;
    private String genero;
    
//    public enum Tipo{
//        ESTIMULO_PRINCIPAL(0), ESTIMULO_ALVO(1), ESTIMULO_INSIGNIFICANTE(2);
//        
//        private Integer codigo;
//        
//        private Tipo(Integer codigo){
//            this.codigo = codigo;
//        }
//        
//        public Integer getCodigo(){
//            return this.codigo;
//        }
//    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }   

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
     * @return the imagem
     */
    public Imagem getImagem() {
        return imagem;
    }

    /**
     * @param imagem the imagem to set
     */
    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    /**
     * @return the audio
     */
    public Audio getAudio() {
        return audio;
    }

    /**
     * @param audio the audio to set
     */
    public void setAudio(Audio audio) {
        this.audio = audio;
    }
  
    /**
     * @return the genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }
  

}
