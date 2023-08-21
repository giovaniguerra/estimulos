package br.com.estimulos.app.core.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;

/**
 * Created by caioc_000 on 13/03/2016.
 */
public class FaseFactory {

    /**
     * O mapa das Fases
     */
    private static Map<String, Class<? extends EntidadeDominio>> mapFase;

    /**
     * Constroi o mapa de DAOs
     */
    private static void construirMapa(Context context){
        mapFase = new HashMap<>();
        mapFase.put(FaseArrastar.class.getName(), FaseArrastar.class);
        mapFase.put(FaseTocar.class.getName(), FaseTocar.class);


    }

    public static Class<? extends EntidadeDominio> getFase(Context context, String className){

        if(mapFase == null)
            construirMapa(context);

        return mapFase.get(className);
    }
}
