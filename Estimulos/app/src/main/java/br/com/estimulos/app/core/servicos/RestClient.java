package br.com.estimulos.app.core.servicos;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Classe Abstrata responsável por tratar Responses e Requests HTTPs de forma Assíncrona.
 *
 * Created by Giovani on 22/04/2016.
 */
public abstract class RestClient {
    /*
     * Constantes
     */
    private static final String BASE_URL = "http://192.168.1.110:8084/";

    /*
     * Variaveis
     */
    private static AsyncHttpClient client = new AsyncHttpClient();

    /*
     * Métodos HTTP
     */
    public static void get(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        // Timeout para Teste apenas
        client.setTimeout(100000);
        client.post(context, getAbsoluteUrl(url), entity, null, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        // Timeout para Teste apenas
        client.setTimeout(100000);
        client.post(context, getAbsoluteUrl(url), entity, null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        StringBuilder sb = new StringBuilder();
        return sb.append(BASE_URL).append(relativeUrl).toString();
    }
}
