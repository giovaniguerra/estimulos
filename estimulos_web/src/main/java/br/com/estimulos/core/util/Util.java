/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.util;

import br.com.estimulos.dominio.IEntidade;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 *
 * @author Giovani
 */
public class Util {
/* Contantes
 */
    /** Diretorio base para arquivos do projeto */
    public static final String DIR_BASE = System.getProperty("user.home") + File.separatorChar + "Estimulos" + File.separatorChar;    
    
    /** Construtor privado */
    private Util(){}    
        
    public static <T extends IEntidade> String toJson(T entidade){
        Gson gson = new Gson();
        return gson.toJson(entidade);
    }
    
    public static <T extends IEntidade> T fromJson(String json, Class<? extends T> type){
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

        /**
     * TODO javadoc
     * @param entidade
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends IEntidade> byte[] entidadeToBytes(final T entidade)
            throws IOException {
//        ZipFile file = new ZipFile("MeuZIP");
//        ZipEntry entry = new ZipEntry("imagens");
        //entry.
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream gz = new GZIPOutputStream(baos);
        final ObjectOutputStream oos = new ObjectOutputStream(gz);

        try {
            oos.writeObject(entidade);
            // usar o finish para que seja criado o EOF do arquivo.
            gz.finish();
            oos.flush();
            return baos.toByteArray();
        }finally {
            oos.close();
            gz.close();
            baos.close();
        }
    }

    /**
     * TODO javadocs
     * @param bytesEntidade
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T extends IEntidade> T entidadeFromBytes(byte[] bytesEntidade)
            throws IOException, ClassNotFoundException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytesEntidade);
        final GZIPInputStream gs = new GZIPInputStream(bais);
        final ObjectInputStream ois = new ObjectInputStream(gs);
        try {
            @SuppressWarnings("unchecked")
            T expandedObject = (T) ois.readObject();
            return expandedObject;
        } finally {
            ois.close();
            gs.close();
            bais.close();
        }
    }

    /**
     * TODO javadocs
     * @param bytes
     * @return
     */
    public static String bytesToBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * TODO javadocs
     * @param encodedString
     * @return
     */
    public static byte[] base64StringToBytes(String encodedString){
        return Base64.getDecoder().decode(encodedString);
    }
    
            /* Criar Service para enviar os dados de tempos em tempos;
                Varrer todo o banco procurando por dados não sincronizados.
                Ao encontrar os dados:
           Criar zip temporario dentro da pasta da aplicacao;
           Ler o arquivo;
           Criar ZipEntries e ..
           Recuperar e Gravar as imagens destes dados no zip;
           Pegar o nome salvo destas imagens e substituir no objeto;
           Gravar o objeto no zip;
           Ler zip para byte[] e codificar para Base64;
           Enviar string codificada;
           Esperar o retorno e apagar o arquivo criado;
            
           O que acontece se der falha no servidor?
                Remover o zip..
            
            Links: 
            Uploads com Jersey WS:
            http://www.mkyong.com/webservices/jax-rs/file-upload-example-in-jersey/
            ZipEntry:
            http://www.avajava.com/tutorials/lessons/how-can-i-create-a-zip-file-from-a-set-of-files.html
            
            
            */
    
    public static <T> void addEntidadeToZip(T entidade){
        
    }
    
}
