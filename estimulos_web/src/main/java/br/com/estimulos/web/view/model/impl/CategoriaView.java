package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
@Component("/estimulos/categoria")
public class CategoriaView extends AbstractViewModel {

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {
        Gson gson = new Gson();
        String retorno = null;
        Resultado resultado = new Resultado();
        CategoriaEstimulo categoria;

        String acao = request.getParameter("acao");
        switch (acao) {
            case "BUSCAR":
                resultado = comando.execute(new CategoriaEstimulo());
                List<CategoriaEstimulo> categorias = new ArrayList<>();
                for (IEntidade c : resultado.getEntidades()) {
                    categorias.add((CategoriaEstimulo) c);
                }

                retorno = gson.toJson(categorias);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "SALVAR":
                String nome = request.getParameter("nome");
                categoria = new CategoriaEstimulo();
                categoria.setNome(nome);
                categoria.setDataCriacao(new Date());
                categoria.setSincronizado(false);
                categoria.setUltimaAtualizacao(new Date());
                resultado = comando.execute(categoria);
                retorno = gson.toJson(resultado);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "VISUALIZAR":
                String id = request.getParameter("id");
                CategoriaEstimulo c = new CategoriaEstimulo();
                c.setID(Integer.valueOf(id));
                Resultado r = comando.execute(c);
                c = new CategoriaEstimulo();
                if (!r.getEntidades().isEmpty()) {
                    c = (CategoriaEstimulo) r.getEntidades().get(0);
                }

                retorno = gson.toJson(c);
                try {
                    response.getWriter().write(retorno);

                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "ALTERAR":
                String n = request.getParameter("nome");
                String id_categoria = request.getParameter("id");
                CategoriaEstimulo cat = new CategoriaEstimulo();
                cat.setID(Integer.valueOf(id_categoria));
                cat.setNome(n);
                cat.setSincronizado(false);
                cat.setUltimaAtualizacao(new Date());
                resultado = comando.execute(cat);
                retorno = gson.toJson(resultado);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "EXCLUIR":
                id = request.getParameter("id");
                c = new CategoriaEstimulo();
                c.setID(Integer.valueOf(id));
                Resultado excluir = new Resultado();
//                excluir = comando.execute(estimulo);

                retorno = gson.toJson(null);
                try {
                    response.getWriter().write(retorno);

                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    @Override
    public Resultado getObjetoResultado(InputStream stream, ICommand comando) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
