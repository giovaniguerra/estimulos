package br.com.estimulos.app.interfaces;

import android.support.annotation.NonNull;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;

/**
 * Created by Giovani on 09/05/2016.
 */
public interface IWebService {

    <T> void enviar(T entidade, String url, AsyncHttpResponseHandler responseHandler);

    /**
     * Método utilizado para zipar uma entidade T.
     * @param entidade
     * @param <T>
     * @return O Nome do arquivo criado.
     * @throws IOException
     */
    @NonNull
    <T> String criarZip(T entidade) throws IOException;
}
