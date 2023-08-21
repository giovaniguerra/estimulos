package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.estimulos.dominio.Terapeuta;

/**
 * Esta classe implementa o DAO para a entidade Terapeuta
 *
 * Created by Caio Cruz on 21/02/2016.
 */
public class TerapeutaDAO extends DAOImpl<Integer, Terapeuta> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_terapeuta";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public TerapeutaDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela Terapeuta
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.NOME.getName() + " text not null, " +
                Tabela.SINCRONIZADO.getName() + " integer not null" +
                ");";
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
        SINCRONIZADO("sincronizado"),
        NOME("nome");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }



    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param terapeuta - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Terapeuta terapeuta) throws Exception {
        ContentValues retorno = new ContentValues();
        retorno.put(TerapeutaDAO.Tabela.NOME.getName(), terapeuta.getNome());
        retorno.put(Tabela.SINCRONIZADO.getName(), terapeuta.getSincronizado() ? 1 : 0);

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
    public Terapeuta fill(Cursor cursor) throws Exception {
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNome(cursor.getString(cursor.getColumnIndex(TerapeutaDAO.Tabela.NOME.getName())));

        terapeuta.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        terapeuta.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        terapeuta.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        terapeuta.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));

        return terapeuta;
    }
}
