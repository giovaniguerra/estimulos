package br.com.estimulos.app.interfaces;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.estimulos.app.core.aplicacao.GameBuilder;

/**
 * Created by Giovani on 03/03/2016.
 */
public interface IStrategyFase {

    public List<View> criarViews(GameBuilder builder, int conteinerWidth, int conteinerHeight, Context context);

    public boolean onTouch(View v, MotionEvent event, ViewGroup conteiner, Context context, GameBuilder builder);
}
