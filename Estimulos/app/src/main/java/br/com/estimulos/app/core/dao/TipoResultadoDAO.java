package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.TipoResultado;

/**
 * Created by Caio Gustavo on 09/04/2016.
 */
public class TipoResultadoDAO extends DAOImpl<Integer, TipoResultado> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_tipo_resultado";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public TipoResultadoDAO(BancoDadosHelper bdHelper) {
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
                Tabela.NOME.getName() + " text not null, "  +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.DESCRICAO.getName() + " text not null);";
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
        NOME("nome"),
        SINCRONIZADO("sincronizado"),
        DESCRICAO("descricao");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param tipoResultado - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(TipoResultado tipoResultado) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NOME.getName(), tipoResultado.getNome());
        retorno.put(Tabela.DESCRICAO.getName(), tipoResultado.getDescricao());
        retorno.put(Tabela.SINCRONIZADO.getName(), tipoResultado.getSincronizado() ? 1 : 0);
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
    public TipoResultado fill(Cursor cursor) throws Exception {
        TipoResultado tipoResultado = new TipoResultado();
        tipoResultado.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));
        tipoResultado.setDescricao(cursor.getString(cursor.getColumnIndex(Tabela.DESCRICAO.getName())));
        tipoResultado.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        tipoResultado.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        tipoResultado.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        tipoResultado.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return tipoResultado;
    }
}
