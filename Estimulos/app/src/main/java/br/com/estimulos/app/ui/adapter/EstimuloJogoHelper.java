package br.com.estimulos.app.ui.adapter;

import br.com.estimulos.dominio.Estimulo;

/**
 * Created by caioc_000 on 09/03/2016.
 */
public class EstimuloJogoHelper {


    private Estimulo estimulo;
    private boolean flagVinculado;

    /** Tipo do estimulo que sera criado nesta posicao
    *                    0 = Estimulo Principal
    *                    1 = Mascara do Estimulo
    *                    2 = Estimulo Insignificante;
    */
    private int tipoEstimulo;

    public EstimuloJogoHelper(Estimulo estimulo, boolean flagVinculado){
        this.estimulo = estimulo;
        this.flagVinculado = flagVinculado;
    }

    public enum Tipo{
        ESTIMULO_PRINCIPAL(0), ESTIMULO_MASCARA(1), ESTIMULO_INSIGNIFICANTE(2);

        private Tipo(int tipo){
            this.tipo = tipo;
        }

        private int tipo;

        public int getTipo(){
            return this.tipo;
        }
    }

    public boolean isFlagVinculado() {
        return flagVinculado;
    }

    public void setFlagVinculado(boolean flagVinculado) {
        this.flagVinculado = flagVinculado;
    }

    public Estimulo getEstimulo() {
        return estimulo;
    }

    public void setEstimulo(Estimulo estimulo) {
        this.estimulo = estimulo;
    }
}
