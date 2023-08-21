package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.Posicao;

/**
 * Esta classe implementa o DAO para a entidade Posicao
 *
 * Created by Caio Gustavo on 06/04/2016.
 */
public class PosicaoDAO extends DAOImpl<Integer, Posicao> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_posicao";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public PosicaoDAO(BancoDadosHelper bdHelper) {
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
                Tabela.ALTURA.getName() + " real, "  +
                Tabela.LARGURA.getName() + " real," +
                Tabela.MARGEM_X.getName() + " real," +
                Tabela.MARGEM_Y.getName() + " real," +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.TIPO_ESTIMULO.getName() + " integer);";
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
        ALTURA("altura"),
        LARGURA("largura"),
        MARGEM_X("margem_x"),
        MARGEM_Y("margem_y"),
        SINCRONIZADO("sincronizado"),
        TIPO_ESTIMULO("tipo_estimulo");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param posicao - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Posicao posicao) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.ALTURA.getName(), posicao.getAltura());
        retorno.put(Tabela.LARGURA.getName(), posicao.getLargura());
        retorno.put(Tabela.MARGEM_X.getName(), posicao.getMargemX());
        retorno.put(Tabela.MARGEM_Y.getName(), posicao.getMargemY());
        retorno.put(Tabela.TIPO_ESTIMULO.getName(), posicao.getTipo());
        retorno.put(Tabela.SINCRONIZADO.getName(), posicao.getSincronizado() ? 1 : 0);
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
    public Posicao fill(Cursor cursor) throws Exception {
        Posicao posicao = new Posicao();
        posicao.setLargura(cursor.getFloat(cursor.getColumnIndex(Tabela.LARGURA.getName())));
        posicao.setAltura(cursor.getFloat(cursor.getColumnIndex(Tabela.ALTURA.getName())));
        posicao.setMargemX(cursor.getFloat(cursor.getColumnIndex(Tabela.MARGEM_X.getName())));
        posicao.setMargemY(cursor.getFloat(cursor.getColumnIndex(Tabela.MARGEM_Y.getName())));
        posicao.setTipo(cursor.getInt(cursor.getColumnIndex(Tabela.TIPO_ESTIMULO.getName())));
        posicao.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        posicao.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        posicao.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        posicao.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return posicao;
    }
}
