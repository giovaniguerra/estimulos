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
public class Imagem extends Midia {

    private int largura;
    private int altura;
    private String uriMascara;


    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    /**
     * @return the uriMascara
     */
    public String getUriMascara() {
        return uriMascara;
    }

    /**
     * @param uriMascara the uriMascara to set
     */
    public void setUriMascara(String uriMascara) {
        this.uriMascara = uriMascara;
    }
 
}
