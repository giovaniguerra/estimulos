/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.web.services;

import br.com.estimulos.core.util.Util;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.web.command.impl.SalvarCommand;
import br.com.estimulos.web.command.impl.VisualizarCommand;
import br.com.estimulos.web.view.model.IViewModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Giovani
 */
@Path("/usuarios")
public class ServiceUsuario extends AbstractService {
    
    private final AnnotationConfigApplicationContext context;
    
    public ServiceUsuario(){
        // recupera as anotations
        context = new AnnotationConfigApplicationContext("br.com.estimulos.web");
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response logar(            
            @FormDataParam("arquivoTeste") InputStream uploadedInputStream,
            @FormDataParam("arquivoTeste") FormDataContentDisposition fileDetail){
        ResponseBuilder builder;
//        int status;
        IViewModel model = (IViewModel) context.getBean("/estimulos/login");
        
        Resultado resultado = model.getObjetoResultado(uploadedInputStream, new VisualizarCommand());
        // Testar resultado
        // houve algum problema para retornar o resultado?
        if(resultado == null) 
            builder = Response.serverError();
        // há algum status devolvido na mensagem?
        else if(resultado.getMensagem() != null && !resultado.getMensagem().isEmpty())
            builder = Response.status(Response.Status.BAD_REQUEST)
                    .header(MSG_STATUS, resultado.getMensagem());
        // Sucesso, criar arquivo temp, criar zip e retornar zip a quem requisitou?
        else {
            File arquivoZip;
            FileInputStream fis = null;
            try {
                arquivoZip = criarZip(resultado.getEntidades().get(0));
                fis = new FileInputStream(arquivoZip);
            } catch (IOException ex) {
                Logger.getLogger(ServiceUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            // usar o deleteOnExit para remover o arquivoZip ?
            //
            builder = Response.status(Response.Status.CREATED)
                    .entity(fis)
                    .type(MediaType.MULTIPART_FORM_DATA_TYPE);
        }
        return builder.build();
    }
    
    @POST
    @Path("/registrar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response registrar(
            @FormDataParam("arquivoTeste") InputStream uploadedInputStream,
            @FormDataParam("arquivoTeste") FormDataContentDisposition fileDetail) {
        ResponseBuilder builder;
//        int status;
        IViewModel model = (IViewModel) context.getBean("/estimulos/terapeuta");
        
        Resultado resultado = model.getObjetoResultado(uploadedInputStream, new SalvarCommand());
        // Testar resultado
        // houve algum problema para retornar o resultado?
        if(resultado == null) 
            builder = Response.serverError();
        // há algum status devolvido na mensagem?
        else if(resultado.getMensagem() != null && !resultado.getMensagem().isEmpty())
            builder = Response.status(Response.Status.BAD_REQUEST)
                    .header(MSG_STATUS, resultado.getMensagem());
        // Sucesso, criar arquivo temp, criar zip e retornar zip a quem requisitou?
        else {
            File arquivoZip;
            FileInputStream fis = null;
            try {
                arquivoZip = criarZip(resultado.getEntidades().get(0));
                fis = new FileInputStream(arquivoZip);
            } catch (IOException ex) {
                Logger.getLogger(ServiceUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            // usar o deleteOnExit para remover o arquivoZip ?
            //
            builder = Response.status(Response.Status.CREATED)
                    .entity(fis)
                    .type(MediaType.MULTIPART_FORM_DATA_TYPE);
        }
        return builder.build();
       
        /* Pegar Resultado através do JSON */
//        Resultado resultado = model.getObjetoResultado(encodedString, new SalvarCommand());
//        // Testar Resultado
//        if(resultado == null || resultado.getMensagem() != null && resultado.getEntidades() == null &&
//                resultado.getEntidades().isEmpty()) {
//            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .header(MSG_STATUS, resultado.getMensagem());
//            return builder.build();
//        }
//        StringBuilder json = new StringBuilder();
//        for(IEntidade e : resultado.getEntidades()){
//            if(e != null && e instanceof Terapeuta){
//                Terapeuta t = (Terapeuta) e;
//                json.append(Util.toJson(t));
//            }
//        }
//
//        builder = Response.ok(json.toString(),
//                MediaType.APPLICATION_JSON_TYPE)
//                .status(Response.Status.CREATED)
//                .header(MSG_STATUS, resultado.getMensagem());
//        return builder.build();
       
    }
    
    private<T> File criarZip(T entidade) throws IOException {
        // Cria um arquivo temporario para salvar o objeto
//        String nomeArquivoTemp = "arquivoTemp2.temp";
        File dirBase = new File(Util.DIR_BASE);
        dirBase.mkdirs();        
        File tempFile = File.createTempFile("tmp", ".temp", dirBase);
        
        FileOutputStream fos = new FileOutputStream(tempFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // Grava o objeto dentro do arquivo
        oos.writeObject(entidade);
        oos.flush();
        // Fecha as conexões
        oos.close();
        fos.close();
        // Cria um arquivo para zipar o arquivo temporario
//        String nomeArquivoZip = "objeto.zip";
        File tempFileZip = File.createTempFile("tmp", ".zip", dirBase);
        fos = new FileOutputStream(tempFileZip);
        ZipOutputStream zipos = new ZipOutputStream(fos);
        // Abre o arquivo temporario
        FileInputStream fis = new FileInputStream(tempFile);

        // TODO : Pensar nesses nomes dos zipEntries
        ZipEntry zipEntry = new ZipEntry("entidade");
        zipos.putNextEntry(zipEntry);
        // Cria o buffer para ler o arquivo temporario e grava os bytes lidos no arquivo zip
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipos.write(bytes, 0, length);
        }
        // Fecha as conexões
        zipos.closeEntry();
        zipos.finish();
        fos.close();
        fis.close();
        // Remove o arquivo temporario
        tempFile.delete();
        // retorna o nome do arquivo gerado
       return tempFileZip;
    }
}
