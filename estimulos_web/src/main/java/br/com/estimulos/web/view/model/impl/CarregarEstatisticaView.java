package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estatistica;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.Grafico;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Replay;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.dominio.TipoResultado;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.helper.EstatisticaHelper;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Component("/estimulos/carregarestatisticas")
public class CarregarEstatisticaView extends AbstractViewModel {

    @Override
    public Resultado getObjetoResultado(InputStream stream, ICommand comando) {
        return new Resultado();
    }

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {
        EstatisticaHelper helper = new EstatisticaHelper();
        Gson gson = new Gson();
        String retorno = null;

        String cat;
        String est;
        String fase;
        String motivo;
        String inicio;
        String fim;

        String acao = request.getParameter("acao");
        switch (acao) {
            case "CONSULTAR":

                Resultado resultCategoria = comando.execute(new CategoriaEstimulo());
                Resultado resultTipoResultado = comando.execute(new TipoResultado());

                Fase faseArrastar = new Fase();
                faseArrastar.setID(5);
                faseArrastar.setNome("Arrastar");

                Fase faseTocar = new Fase();
                faseTocar.setID(4);
                faseTocar.setNome("Tocar");

                List<Fase> fases = new ArrayList<>();
                fases.add(faseArrastar);
                fases.add(faseTocar);

                List<CategoriaEstimulo> categorias = new ArrayList<>();
                for (IEntidade e : resultCategoria.getEntidades()) {
                    categorias.add((CategoriaEstimulo) e);
                }

                List<TipoResultado> resultados = new ArrayList<>();
                for (IEntidade e : resultTipoResultado.getEntidades()) {
                    resultados.add((TipoResultado) e);
                }

                helper.setCategorias(categorias);
                helper.setFases(fases);
                helper.setMotivos(resultados);
                retorno = gson.toJson(helper);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "BUSCAR":
                String objeto = request.getParameter("objeto");
                if (objeto.equals("ESTIMULO")) {

                }
                String categoria = request.getParameter("categoria");
                Estimulo estimulo = new Estimulo();
                estimulo.setCategoria(new CategoriaEstimulo());
                estimulo.getCategoria().setID(Integer.valueOf(categoria));

                Resultado r = comando.execute(estimulo);

                List<Estimulo> estimulos = new ArrayList<>();
                for (IEntidade e : r.getEntidades()) {
                    estimulos.add((Estimulo) e);
                }

                helper.setEstimulos(estimulos);
                retorno = gson.toJson(helper);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "GERAR":
                cat = request.getParameter("categoria");
                est = request.getParameter("estimulo");
                fase = request.getParameter("fase");
                motivo = request.getParameter("motivo");
                inicio = request.getParameter("inicio");
                fim = request.getParameter("fim");

                Estatistica estatistica = new Grafico();

                estatistica.setCategoria(new CategoriaEstimulo());
                estatistica.setEstimulo(new Estimulo());
                estatistica.setFase(new Fase());
                estatistica.setPaciente(new Paciente());
                estatistica.setTipo(new TipoResultado());

                if (!"todos".equals(cat)) {
                    estatistica.getCategoria().setID(Integer.valueOf(cat));
                }

                if (!"todos".equals(est)) {
                    estatistica.getEstimulo().setID(Integer.valueOf(est));
                }

                if (!"todos".equals(fase)) {
                    estatistica.getFase().setID(Integer.valueOf(fase));
                }

                if (!"todos".equals(motivo)) {
                    estatistica.getTipo().setID(Integer.valueOf(motivo));
                }

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

                if (!inicio.isEmpty()) {
                    try {
                        Date dataInicio = formato.parse(inicio);
                        estatistica.setInicio(dataInicio);
                    } catch (ParseException ex) {
                        Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (!fim.isEmpty()) {
                    try {
                        Date dataFim = formato.parse(fim);
                        estatistica.setFim(dataFim);
                    } catch (ParseException ex) {
                        Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                Resultado resultado = comando.execute(estatistica);

                retorno = gson.toJson(resultado);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "REPLAY":
                cat = request.getParameter("categoria");
                est = request.getParameter("estimulo");
                fase = request.getParameter("fase");
                String nivel = request.getParameter("nivel");
                motivo = request.getParameter("motivo");
                inicio = request.getParameter("dataInicio");
                fim = request.getParameter("dataFim");
                
                Estatistica replay = new Replay();
                
                replay.setCategoria(new CategoriaEstimulo());
                replay.setEstimulo(new Estimulo());
                replay.setFase(new Fase());
                replay.setPaciente(new Paciente());
                replay.setTipo(new TipoResultado());
                replay.getFase().setNiveis(new ArrayList<Nivel>());
                
                if (!"todos".equals(cat)) {
                    replay.getCategoria().setID(Integer.valueOf(cat));
                }

                if (!"todos".equals(est)) {
                    replay.getEstimulo().setID(Integer.valueOf(est));
                }

                if (!"todos".equals(fase)) {
                    replay.getFase().setID(Integer.valueOf(fase));
                }
                
                if (!"todos".equals(nivel)) {
                    Nivel n = new NivelArrastar();
                    n.setNumero(Integer.valueOf(nivel));
                    replay.getFase().getNiveis().add(n);
                }

                if (!"todos".equals(motivo)) {
                    replay.getTipo().setID(Integer.valueOf(motivo));
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                if (inicio != null && !inicio.isEmpty()) {
                    try {
                        Date dataInicio = format.parse(inicio);
                        replay.setInicio(dataInicio);
                    } catch (ParseException ex) {
                        Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (fim != null && !fim.isEmpty()) {
                    try {
                        Date dataFim = format.parse(fim);
                        replay.setFim(dataFim);
                    } catch (ParseException ex) {
                        Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                Resultado res = comando.execute(replay);

                retorno = gson.toJson(res);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }
}
