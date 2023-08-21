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
public class Posicao extends EntidadeDominio{
    /** Largura em relacao a tela (largura >= 0 && largura <= 1.0). */
    private float largura;
    
    /** Altura em relacao a tela (altura >= 0 && altura <= 1.0). */
    private float altura;
    
    /** Tamanho da margem esquerda em relacao a tela (margemX >= 0 && margemX <= 1.0). */
    private float margemX;
    
    /** Tamanho da margem do topo em relacao a tela (margemY >= 0 && margemY <= 1.0 ). */
    private float margemY;
    
    /** Tipo do estimulo que esta originalmente nesta posicao. */
    private Integer tipo;

    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getMargemX() {
        return margemX;
    }

    public void setMargemX(float margemX) {
        this.margemX = margemX;
    }

    public float getMargemY() {
        return margemY;
    }

    public void setMargemY(float margemY) {
        this.margemY = margemY;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

}
