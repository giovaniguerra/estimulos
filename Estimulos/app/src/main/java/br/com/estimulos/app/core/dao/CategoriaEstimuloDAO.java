package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.estimulos.dominio.CategoriaEstimulo;

/**
 * Esta classe implementa o DAO para a entidade
 *
 * Created by Caio Cruz on 18/02/2016.
 */
public class CategoriaEstimuloDAO extends DAOImpl<Integer, CategoriaEstimulo> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_categoria_estimulos";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public CategoriaEstimuloDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela Categoria
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
    public String getUpdateBancoDadosSQL(int vsAntiga, int vsNova) {  return ""; }

    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum Tabela {
        NOME("nome"),
        SINCRONIZADO("sincronizado");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param cateoria - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(CategoriaEstimulo cateoria) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NOME.getName(), cateoria.getNome());
        retorno.put(Tabela.SINCRONIZADO.getName(), cateoria.getSincronizado() ? 1 : 0);
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
    public CategoriaEstimulo fill(Cursor cursor) throws Exception {
        CategoriaEstimulo categoria = new CategoriaEstimulo();

        categoria.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));

        categoria.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        categoria.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        categoria.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        categoria.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);

        return categoria;
    }
}
