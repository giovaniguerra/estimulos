package br.com.estimulos.dominio;

import java.util.Date;

/** 
 * 
 * @author Caio Gustavo R. Cruz 
 */
public class Pessoa extends EntidadeDominio{
    
    private String nome;
    private Date dtNascimento;

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

    /**
     * @return the dtNascimento
     */
    public Date getDtNascimento() {
        return dtNascimento;
    }

    /**
     * @param dtNascimento the dtNascimento to set
     */
    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }
    

}
