/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.view.model.impl;

import br.com.estimulos.core.fachada.impl.Fachada;
import br.com.estimulos.core.util.Util;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import br.com.estimulos.web.command.ICommand;
import br.com.estimulos.web.command.impl.SalvarCommand;
import br.com.estimulos.web.servico.login.LoginUtil;
import br.com.estimulos.web.view.model.AbstractViewModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
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
 * @author Caio Gustavo
 */
@Component("/estimulos/login")
public class UsuarioView extends AbstractViewModel{

    public UsuarioView(){
        fachada = new Fachada();
    }
 
    
    @Override
    public void tratarRequisicaoFrontController( HttpServletRequest request, HttpServletResponse response, ICommand comando) {
      
        Usuario usuario = new Usuario();
        usuario.setLogin(request.getParameter("txtEmail"));
        usuario.setSenha(request.getParameter("txtSenha"));
        try {  
            
            Resultado resultado =  comando.execute(usuario);

            usuario = (Usuario) resultado.getEntidades().get(0);

            if(usuario.getID() != null){
                LoginUtil.setUsuarioLogado(usuario);
                request.setAttribute("msg", "Bem Vindo!!!!");
                request.getRequestDispatcher("/pages/Home.jsp").forward(request, response); 
            } 
            else {
                request.setAttribute("msg", "Usuário não encontrado!");
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
            }           
            
        } catch (ServletException ex) {
            Logger.getLogger(UsuarioView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UsuarioView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Resultado getObjetoResultado(InputStream stream, ICommand comando) {
        Resultado r = new Resultado();        
        Usuario u;        
        try {
            u = readStream(stream);
        } catch (IOException ex) {
            r.setMensagem("Houve um problema interno no servidor!");
            Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
            return r;
        } catch (ClassNotFoundException ex) {
            r.setMensagem("Objeto inválido!");
            Logger.getLogger(TerapeutaView.class.getName()).log(Level.SEVERE, null, ex);
            return r;
        } 
        return comando.execute(u);
        
//        usuario.setLogin("giovani");
//        usuario.setSenha("123");
//        if(usuario != null && usuario.getLogin().equals("giovani") && usuario.getSenha().equals("123")){
//        
//            List<IEntidade> d = new ArrayList<>();
//            d.add(usuario);
//            r.setEntidades(d);
//            r.setMensagem("Enviado login teste com sucesso!");
//        }
//        return r;        
//    }
//        Resultado r = new Resultado();
//        if(usuario.getLogin().equals("giovani") && usuario.getSenha().equals("123")){
//        
//            List<IEntidade> d = new ArrayList<>();
//            d.add(usuario);
//            r.setEntidades(d);
//            r.setMensagem("Enviado login teste com sucesso!");
//        }
//        return r;        
    }
    
    private Usuario readStream(InputStream stream) throws IOException, ClassNotFoundException {
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
        while((entry = zipis.getNextEntry()) != null){
            int lenght;
            byte[] b = new byte[1024];
            // Gravar no arquivo temporario
            while((lenght = zipis.read(b)) != -1){
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
        Usuario u = (Usuario) ois.readObject();
        // Remover arquivo temporario
        file.delete();
        // Fechar as conexoes
        fis.close();
        ois.close();
        // Retornar usuario
        return u;
    }
    
}
