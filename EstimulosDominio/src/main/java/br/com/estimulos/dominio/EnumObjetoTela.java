/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.dominio;

/** Enum de Tipos de Objeto da Tela.
 *
 * @author Giovani
 */
public enum EnumObjetoTela {
      ESTIMULO_PRINCIPAL(0), ESTIMULO_INSIGNIFICANTE(1), ESTIMULO_ALVO(2), 
      FUNDO(3),SETA(4), LADO_ESQUERDO(5), LADO_DIREITO(6);

        private EnumObjetoTela(Integer objeto){
            this.objeto = objeto;
        }

        private final Integer objeto;

        public Integer getObjeto(){
            return this.objeto;
        };
}
