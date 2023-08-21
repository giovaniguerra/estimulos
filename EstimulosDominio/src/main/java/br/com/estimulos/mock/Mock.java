/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.mock;

import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Jogo;


/**
 *
 * @author Giovani
 */
public class Mock {
    public static Jogo criaJogo(){
        Jogo jogo = new Jogo();
        jogo.getEstimulos().add(new Estimulo());
        jogo.getImagem().setNome("Bola");
        Fase faseTocar = new FaseTocar();
//        faseTocar.getImagem().setAltura(120);
//        faseTocar.getImagem().setLargura(120);
//        faseTocar.get;;;;
//        Fase faseArrastar = new FaseArrastar();
//        jogo.getFases().add(faseTocar);
//        jogo.getFases().add(faseArrastar);
//        Teste!!
        
        
        jogo.getPaciente().setNome("Caio");
//        jogo.getPaciente().setDataNascimento(new Date(1994, 6, 24));
        jogo.getReforcadores();
        jogo.getTema();
        return new Jogo();
    }
    
}
