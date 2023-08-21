package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
@Component("/estimulos/reforcador")
public class ReforcadorView extends AbstractViewModel {

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {
        Gson gson = new Gson();
        String retorno = null;
        
        String acao = request.getParameter("acao");
        switch (acao) {
            case "BUSCAR":
                Resultado resultado = comando.execute(new Reforcador());
                List<Reforcador> reforcadores = new ArrayList<>();
                for (IEntidade r : resultado.getEntidades()) {
                    reforcadores.add((Reforcador) r);
                }

                retorno = gson.toJson(reforcadores);
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
