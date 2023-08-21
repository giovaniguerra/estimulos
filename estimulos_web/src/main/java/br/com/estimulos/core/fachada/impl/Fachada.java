/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.fachada.impl;

import br.com.estimulos.core.dao.IDAO;
import br.com.estimulos.core.fachada.IFachada;
import br.com.estimulos.core.strategy.IStrategy;
import br.com.estimulos.core.strategy.impl.CompletarDadosCadastro;
import br.com.estimulos.core.strategy.impl.ValidarEstatistica;
import br.com.estimulos.core.strategy.impl.ValidarTerapeuta;
import br.com.estimulos.dominio.Grafico;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Caio Gustavo
 */
public class Fachada implements IFachada {

    private final AnnotationConfigApplicationContext context;
    private Map<String, Map<String, List<IStrategy>>> rns;
    private Resultado resultado;

    public Fachada() {
        // recupera as anotations
        context = new AnnotationConfigApplicationContext("br.com.estimulos.core");

        rns = new HashMap<String, Map<String, List<IStrategy>>>();
        
        CompletarDadosCadastro completarCadastro = new CompletarDadosCadastro();
        ValidarTerapeuta validarTerapeuta = new ValidarTerapeuta();
        ValidarEstatistica validarEstatistica = new ValidarEstatistica();
        
        List<IStrategy> rnsSalvarTerapeuta = new ArrayList<IStrategy>();
        List<IStrategy> rnsSalvarUsuario = new ArrayList<IStrategy>();
        List<IStrategy> rnsFiltrarEstatistica = new ArrayList<IStrategy>();
         
        rnsSalvarTerapeuta.add(completarCadastro);
        rnsSalvarTerapeuta.add(validarTerapeuta);
        rnsSalvarUsuario.add(completarCadastro);
        rnsFiltrarEstatistica.add(validarEstatistica);
        
        Map<String, List<IStrategy>> rnsTerapeuta = new HashMap<String, List<IStrategy>>();
        Map<String, List<IStrategy>> rnsUsuario = new HashMap<String, List<IStrategy>>();
        Map<String, List<IStrategy>> rnsEstatistica = new HashMap<String, List<IStrategy>>();
        
        rnsUsuario.put("SALVAR", rnsSalvarUsuario);
        rnsTerapeuta.put("SALVAR", rnsSalvarTerapeuta);
        rnsEstatistica.put("CONSULTAR", rnsFiltrarEstatistica);
        
        rns.put(Terapeuta.class.getName(), rnsTerapeuta);
        rns.put(Usuario.class.getName(), rnsUsuario);
        rns.put(Grafico.class.getName(), rnsEstatistica);
        
        

    }

    @Override
    public Resultado salvar(IEntidade entidade) {
        resultado = new Resultado();
        String nmClasse = entidade.getClass().getName();

        String msg = executarRegras(entidade, "SALVAR");

        if (msg == null) {
            IDAO dao = (IDAO) context.getBean(nmClasse);
            try {
                dao.salvar(entidade);
                List<IEntidade> entidades = new ArrayList<IEntidade>();
                entidades.add(entidade);
                resultado.setEntidades(entidades);

            } catch (SQLException e) {

                e.printStackTrace();
                resultado.setMensagem("Não foi possível realizar o registro!");

            }
        } else {
            resultado.setMensagem(msg);
        }
        return resultado;
    }

    @Override
    public Resultado alterar(IEntidade entidade) {
        resultado = new Resultado();

        String nmClasse = entidade.getClass().getName();

        String msg = null; //= executarRegras(entidade, "ALTERAR");

        if (msg == null) {

            IDAO dao = (IDAO) context.getBean(nmClasse);
            try {
                dao.alterar(entidade);
                List<IEntidade> entidades = new ArrayList<IEntidade>();
                entidades.add(entidade);
                resultado.setEntidades(entidades);
            } catch (SQLException e) {

                e.printStackTrace();
                resultado.setMensagem("Não foi possível alterar o registro");
            }
        } else {
            resultado.setMensagem(msg);
        }

        return resultado;
    }

    @Override
    public Resultado excluir(IEntidade entidade) {
        resultado = new Resultado();

        String nmClasse = entidade.getClass().getName();

        String msg = executarRegras(entidade, "EXCLUIR");

        if (msg == null) {

            IDAO dao = (IDAO) context.getBean(nmClasse);
            try {
                dao.excluir(entidade);
                List<IEntidade> entidades = new ArrayList<IEntidade>();
                entidades.add(entidade);
                resultado.setEntidades(entidades);
            } catch (SQLException e) {
                e.printStackTrace();
                resultado.setMensagem("Não foi possível efetuar a exclusão!");

            }
        } else {
            resultado.setMensagem(msg);
        }

        return resultado;
    }

    @Override
    public Resultado consultar(IEntidade entidade) {
        resultado = new Resultado();

        String nmClasse = entidade.getClass().getName();

        String msg = executarRegras(entidade, "CONSULTAR");

        if (msg == null) {

            IDAO dao = (IDAO) context.getBean(nmClasse);

            try {
                resultado.setEntidades(dao.consultar(entidade));

            } catch (SQLException e) {
                e.printStackTrace();
                resultado.setMensagem("Não foi possível realizar a consulta!");

            }
        } else {
            resultado.setMensagem(msg);
        }

        return resultado;
    }

    @Override
    public Resultado visualizar(IEntidade entidade) {
        resultado = new Resultado();
        String nmClasse = entidade.getClass().getName();

        String msg = executarRegras(entidade, "VISUALIZAR");

        if (msg == null) {
            IDAO dao = (IDAO) context.getBean(nmClasse);

            try {
                List<IEntidade> es = new ArrayList<>();
                es.add(dao.visualizar(entidade));
                resultado.setEntidades(es);
            } catch (SQLException e) {
                e.printStackTrace();
                resultado.setMensagem("Não foi possível realizar a consulta!");

            }
        } else {
            resultado.setMensagem(msg);
        }
        return resultado;
    }

    private String executarRegras(IEntidade entidade, String operacao) {
        String nmClasse = entidade.getClass().getName();
        StringBuilder msg = new StringBuilder();

        Map<String, List<IStrategy>> regrasOperacao = rns.get(nmClasse);

        if (regrasOperacao != null) {
            List<IStrategy> regras = regrasOperacao.get(operacao);

            if (regras != null) {
                for (IStrategy s : regras) {
                    String m = s.processar(entidade);

                    if (m != null) {
                        msg.append(m);
                        msg.append("\n");
                    }
                }
            }

        }

        if (msg.length() > 0) {
            return msg.toString();
        } else {
            return null;
        }

    }

}
