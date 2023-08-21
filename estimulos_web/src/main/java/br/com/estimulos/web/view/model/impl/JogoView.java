package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Component("/estimulos/jogo")
public class JogoView extends AbstractViewModel {

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {
        Gson gson = new Gson();
        String retorno = null;
        Jogo jogo;
        Resultado resultado;

        String acao = request.getParameter("acao");
        switch (acao) {
            case "JOGO":
                resultado = new Resultado();
                jogo = new Jogo();
                String nome = request.getParameter("nome");
                String nome_paciente = request.getParameter("nome_paciente");
                String dtNasc = request.getParameter("dt_nascimento");

                jogo.setTema(nome);
                Paciente p = new Paciente();
                p.setNome(nome_paciente);
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date dataInicio = formato.parse(dtNasc);
                    p.setDtNascimento(dataInicio);
                } catch (ParseException ex) {
                    Logger.getLogger(JogoView.class.getName()).log(Level.SEVERE, null, ex);
                }
                p.setDataCriacao(new Date());
                p.setUltimaAtualizacao(new Date());
                p.setSincronizado(false);
                jogo.setPaciente(p);
                jogo.setDataCriacao(new Date());
                jogo.setSincronizado(false);
                jogo.setUltimaAtualizacao(new Date());

                resultado = comando.execute(jogo);
                retorno = gson.toJson(resultado.getEntidades().get(0));
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "VINCULAR":
                String id = request.getParameter("id");
                String[] ids = request.getParameterValues("estimulos[]");
                Estimulo e;
                jogo = new Jogo();
                List<Estimulo> list = new ArrayList<Estimulo>();
                for (int i = 0; i < ids.length; i++) {
                    e = new Estimulo();
                    e.setID(Integer.valueOf(ids[i]));
                    list.add(e);
                }
                jogo.setEstimulos(list);
                jogo.setID(Integer.valueOf(id));
                System.out.println("Opa");
                resultado = new Resultado();
                resultado = comando.execute(jogo);
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
