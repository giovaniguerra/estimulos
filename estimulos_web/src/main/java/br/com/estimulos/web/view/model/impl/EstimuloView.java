package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
@Component("/estimulos/estimulo")
@MultipartConfig
public class EstimuloView extends AbstractViewModel {

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {
        Estimulo estimulo = new Estimulo();
        CategoriaEstimulo categoria = new CategoriaEstimulo();
        Gson gson = new Gson();
        String retorno = null;
        Resultado resultCategoria = new Resultado();
        String id;

        String acao = request.getParameter("acao");
        switch (acao) {
            case "CONSULTAR":
                resultCategoria = new Resultado();
                resultCategoria = comando.execute(new CategoriaEstimulo());

                List<CategoriaEstimulo> categorias = new ArrayList<>();
                if (resultCategoria.getEntidades() != null) {
                    for (IEntidade e : resultCategoria.getEntidades()) {
                        categorias.add((CategoriaEstimulo) e);
                    }
                }

                retorno = gson.toJson(categorias);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "BUSCAR":
                Resultado resultEstimulo = comando.execute(new Estimulo());

                List<Estimulo> estimulos = new ArrayList<>();
                for (IEntidade e : resultEstimulo.getEntidades()) {
                    estimulos.add((Estimulo) e);
                }

                retorno = gson.toJson(estimulos);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "VISUALIZAR":
                id = request.getParameter("id");
                Estimulo e = new Estimulo();
                e.setID(Integer.valueOf(id));
                Resultado r = comando.execute(e);
                e = new Estimulo();
                if (!r.getEntidades().isEmpty()) {
                    e = (Estimulo) r.getEntidades().get(0);
                }

                retorno = gson.toJson(e);
                try {
                    response.getWriter().write(retorno);

                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "SALVAR":
                // Fazer o cadastro do novo estímulo
                break;

            case "ALTERAR":
                System.out.println("OPA");
                break;

            case "EXCLUIR":
                id = request.getParameter("id");
                estimulo = new Estimulo();
                estimulo.setID(Integer.valueOf(id));
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
