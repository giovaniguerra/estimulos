package br.com.estimulos.app.ui.adapter;
import br.com.estimulos.dominio.Reforcador;

/**
 * Created by Caio Gustavo on 28/05/2016.
 */
public class ReforcadorJogoHelper {


    private Reforcador reforcador;
    private boolean flagVinculado;

    public ReforcadorJogoHelper(Reforcador reforcador, boolean flagVinculado){
        this.reforcador = reforcador;
        this.flagVinculado = flagVinculado;
    }

    public boolean isFlagVinculado() {
        return flagVinculado;
    }

    public void setFlagVinculado(boolean flagVinculado) {
        this.flagVinculado = flagVinculado;
    }

    public Reforcador getReforcador() {
        return reforcador;
    }

    public void setReforcador(Reforcador reforcador) {
        this.reforcador = reforcador;
    }

}
