package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.ObjetoTela;

/**
 * Created by Caio Gustavo on 09/04/2016.
 */
public class ObjetoTelaDAO extends DAOImpl<Integer, ObjetoTela> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_objeto_tela";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public ObjetoTelaDAO(BancoDadosHelper bdHelper) {
        super(bdHelper);
    }

    /**
     * Metodo para obter o nome da tabela
     * @return - String nome da tabela
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Metodo para obter a string de criacao da tabela Audio
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.CODIGO.getName() + " text not null, "  +
                Tabela.TIPO.getName() + " integer);";
        return sql;
    }

    /**
     * Metodo para obter a string de atualizacao desta tabela
     * @param vsAntiga - versao antiga da tabela
     * @param vsNova - nova versao da tabela
     * @return - Query de atualizao
     */
    @Override
    public String getUpdateBancoDadosSQL(int vsAntiga, int vsNova) {
        return "";
    }

    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum Tabela {
        CODIGO("codigo"),
        TIPO("tipo");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param objetoTela - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(ObjetoTela objetoTela) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.CODIGO.getName(), objetoTela.getCodigo());
        retorno.put(Tabela.TIPO.getName(), objetoTela.getTipoObjetoTela());
        return retorno;
    }



    /**
     * Reescrita do metodo que faz a atribuicao dos valores vindo do banco
     * em uma nova instancia da entidade
     * @param cursor - objeto do banco de dados para recuperacao dos dados
     * @return - nova instancia da entidade
     * @throws Exception - caso ocorra erro
     */
    @Override
    public ObjetoTela fill(Cursor cursor) throws Exception {
        ObjetoTela objetoTela = new ObjetoTela();
        objetoTela.setCodigo(cursor.getString(cursor.getColumnIndex(Tabela.CODIGO.getName())));
        objetoTela.setTipoObjetoTela(cursor.getInt(cursor.getColumnIndex(Tabela.TIPO.getName())));

        objetoTela.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        objetoTela.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        objetoTela.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return objetoTela;
    }
}
