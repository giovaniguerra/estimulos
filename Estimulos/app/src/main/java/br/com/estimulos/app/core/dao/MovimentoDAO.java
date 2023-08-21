package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.Tentativa;

/**
 * Created by Caio Gustavo on 09/04/2016.
 */
public class MovimentoDAO extends DAOImpl<Integer, Movimento> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_movimento";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public MovimentoDAO(BancoDadosHelper bdHelper) {
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
                Tabela.X.getName() + " real not null, "  +
                Tabela.Y.getName() + " real not null," +
                Tabela.TENTATIVA.getName() + " integer, " +
                "foreign key(" + Tabela.TENTATIVA.getName() + ") references " + TentativaDAO.TABLE_NAME + "(" + ID + ")" +
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
        X("x"),
        Y("y"),
        TENTATIVA("tentativa_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param movimento - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Movimento movimento) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.X.getName(), movimento.getX());
        retorno.put(Tabela.Y.getName(), movimento.getY());
        retorno.put(Tabela.TENTATIVA.getName(), movimento.getTentativa().getID());

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
    public Movimento fill(Cursor cursor) throws Exception {
        Movimento movimento = new Movimento();
        movimento.setTentativa(new Tentativa());
        movimento.getTentativa().setID(cursor.getInt(cursor.getColumnIndex(Tabela.TENTATIVA.getName())));
        movimento.setX(cursor.getInt(cursor.getColumnIndex(Tabela.X.getName())));
        movimento.setY(cursor.getInt(cursor.getColumnIndex(Tabela.Y.getName())));

        movimento.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        movimento.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        movimento.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return movimento;
    }
}
