package br.com.estimulos.dominio;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
public enum EnumEstatistica {
    TOCOU_VAZIO(0), TOCOU_INSIGNIFICANTE(1), TOCOU_PRINCIPAL(2),
    SOLTOU_LADO_ESQUERDO(3), SOLTOU_LADO_DIREITO(4), SOLTOU_SETA(5),
    ERROU(6), ACERTOU(7);
    

    private EnumEstatistica(Integer objeto) {
        this.objeto = objeto;
    }

    private final Integer objeto;

    public Integer getObjeto() {
        return this.objeto;
    }
}
