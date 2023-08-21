/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.core.util.Util;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.servico.login.LoginUtil;
import br.com.estimulos.web.view.model.AbstractViewModel;
import com.google.gson.Gson;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author Giovani
 */
@Component("/estimulos/terapeuta")
public class TerapeutaView extends AbstractViewModel {

    @Override
    public Resultado getObjetoResultado(InputStream stream, ICommand comando) {
        Resultado r = new Resultado();
        Terapeuta t;
        try {
            t = readStream(stream);
        } catch (IOException ex) {
            r.setMensagem("Houve um problema interno no servidor!");
            Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
            return r;
        } catch (ClassNotFoundException ex) {
            r.setMensagem("Objeto inválido!");
            Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
            return r;
        }
        return comando.execute(t);
    }

    private Terapeuta readStream(InputStream stream) throws IOException, ClassNotFoundException {
        // Criar o arquivo temporario para guardar os dados vindo do InputStream
        File dir = new File(Util.DIR_BASE);
        dir.mkdirs();
        File file = File.createTempFile("tmp", null, dir);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        // Criar ZipInput para receber os dados que vieram zipados do stream
        ZipInputStream zipis = new ZipInputStream(stream);
        ZipEntry entry;
        // Rodar enquanto houver Entries
        while ((entry = zipis.getNextEntry()) != null) {
            int lenght;
            byte[] b = new byte[1024];
            // Gravar no arquivo temporario
            while ((lenght = zipis.read(b)) != -1) {
                bos.write(b, 0, lenght);
            }
        }
        // Fechar conexões
        bos.close();
        zipis.close();
        // Abrir leitura do arquivo temporario
        FileInputStream fis = new FileInputStream(Util.DIR_BASE + file.getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        // Ler objeto 
        Terapeuta t = (Terapeuta) ois.readObject();
        // Remover arquivo temporario
        file.delete();
        // Fechar as conexoes
        fis.close();
        ois.close();
        // Retornar terapeuta
        return t;
    }

    @Override
    public void tratarRequisicaoFrontController(HttpServletRequest request, HttpServletResponse response, ICommand comando) {

        Terapeuta terapeuta = new Terapeuta();
        String operacao = request.getParameter("operacao");
        Resultado resultado;
        Gson gson = new Gson();
        String retorno = null;
        
        switch (operacao) {
            case "VISUALIZAR":
                terapeuta.setNome(request.getParameter("txtNome"));

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date parsed = null;
                try {
                    parsed = format.parse(request.getParameter("dtNasc"));
                } catch (ParseException ex) {
                    Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                terapeuta.setDtNascimento(parsed);

                Usuario usuario = new Usuario();
                usuario.setLogin(request.getParameter("txtEmail"));
                usuario.setSenha(request.getParameter("txtSenha"));

                terapeuta.setUsuario(usuario);

                resultado = comando.execute(terapeuta);

                if (resultado.getMensagem() == null) {
                    terapeuta = (Terapeuta) resultado.getEntidades().get(0);
                    LoginUtil.setUsuarioLogado(terapeuta.getUsuario());
                    request.setAttribute("msg", "Bem Vindo!!!!");

                    try {
                        request.getRequestDispatcher("/pages/Home.jsp").forward(request, response);
                    } catch (ServletException ex) {
                        Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    request.setAttribute("msg", resultado.getMensagem());

                    try {
                        request.getRequestDispatcher("/Login.jsp").forward(request, response);
                    } catch (ServletException ex) {
                        Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;
                
            case "CONSULTAR":                
                Terapeuta t = new Terapeuta();
                t.setUsuario(LoginUtil.getUsuarioLogado());
                
                Resultado r = comando.execute(t);
                terapeuta = (Terapeuta)r.getEntidades().get(0);
                
                retorno = gson.toJson(terapeuta);
                try {
                    response.getWriter().write(retorno);
                } catch (IOException ex) {
                    Logger.getLogger(CarregarEstatisticaView.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

    }

}
