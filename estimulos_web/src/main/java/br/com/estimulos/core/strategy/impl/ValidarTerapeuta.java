/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.strategy.impl;

import br.com.estimulos.core.strategy.IStrategy;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
public class ValidarTerapeuta implements IStrategy{

    @Override
    public String processar(IEntidade entidade) {

        String mensagem = "";
        Terapeuta terapeuta = (Terapeuta) entidade;
        
        if(terapeuta.getUsuario().getDataCriacao() == null){
            terapeuta.getUsuario().setDataCriacao(new Date());
        }
        if(terapeuta.getUsuario().getUltimaAtualizacao()== null){
            terapeuta.getUsuario().setUltimaAtualizacao(new Date());
        }        
        if(terapeuta.getID() != null){
            terapeuta.setSincronizado(true);
            terapeuta.getUsuario().setSincronizado(true);
        }
        else{
            terapeuta.setSincronizado(false);
            terapeuta.getUsuario().setSincronizado(false);
        }
        
        if(terapeuta.getNome() == null || terapeuta.getNome().isEmpty())
            mensagem += "Informe o nome do usuario!";
        
        if(terapeuta.getDtNascimento() == null)
            mensagem += "\nInforme a data de nascimento!";
        
        if(terapeuta.getUsuario().getLogin().isEmpty())
            mensagem += "\nInforme o email";
        
        if(terapeuta.getUsuario().getSenha().isEmpty())
            mensagem += "\nInforme a senha";
        
        if(mensagem.equals(""))
            return null;
        else
            return mensagem;
        
    }
    
}
