package br.com.estimulos.app.core.servicos.webservice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import br.com.estimulos.app.core.servicos.RestClient;
import br.com.estimulos.app.interfaces.IWebService;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by Giovani on 09/05/2016.
 */
public abstract class AbstractWebService implements IWebService {
    /*
     * Variáveis
     */
    protected Context context;
    /**
     * Construtor do objeto
     */
    protected AbstractWebService(Context context){
        this.context = context;
    }

    @Override
    public <T> void enviar(T entidade, String url, AsyncHttpResponseHandler responseHandler) {
        String nomeArquivo;
        File file;
        try {
            nomeArquivo = criarZip(entidade);
            file = new File(context.getFilesDir(), nomeArquivo);
        } catch (IOException e) {
            Log.d("Usuario REST", e.getMessage());
            Toast.makeText(context, "Erro na criação do zip", Toast.LENGTH_LONG).show();
            return;
        }
        // Criar entidade do tipo "multipart-form/data" com o arquivo
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().addBinaryBody("arquivoTeste", file);

        /** Enviar requisição ao servidor */
        RestClient.post(context, url, multipartEntity.build(), responseHandler);
        // Possui conexão
    }

    @Override
    @NonNull
    public <T> String criarZip(T entidade) throws IOException {
        // Cria um arquivo temporario para salvar o objeto
        File file = File.createTempFile("tmp", null, context.getFilesDir());
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        // Grava o objeto dentro do arquivo
        oos.writeObject(entidade);
        oos.flush();
        // Fecha as conexões
        oos.close();
        fos.close();
        // Cria um arquivo para zipar o arquivo temporario
        File fileZip = File.createTempFile("tmp", ".zip", context.getFilesDir());
        fos = new FileOutputStream(fileZip);
        ZipOutputStream zipos = new ZipOutputStream(fos);
        // Abre o arquivo temporario
        FileInputStream fis = context.openFileInput(file.getName());

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
        context.deleteFile(file.getName());

        return fileZip.getName();
    }
}
