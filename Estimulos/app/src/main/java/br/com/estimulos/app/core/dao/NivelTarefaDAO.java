package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.Tarefa;

/**
 * Created by Caio Gustavo on 07/04/2016.
 */
public class NivelTarefaDAO extends DAOImpl<Integer, Nivel> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_nivel_tarefa";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public NivelTarefaDAO(BancoDadosHelper bdHelper) {
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
                ID + " integer primary key autoincrement not null, " +
                Tabela.NIVEL.getName() + " integer not null, "  +
                Tabela.TAREFA.getName() + " integer not null," +
                Tabela.FASE.getName() + " integer not null);";
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
        NIVEL("nivel_id"),
        TAREFA("tarefa_id"),
        FASE("fase_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Nivel nivel, SQLiteDatabase database) throws Exception {

        Integer idNivel = nivel.getID();
        Integer idFase = nivel.getFase().getID();
        ContentValues retorno =  new ContentValues();


        for(Tarefa t: nivel.getListTarefas()) {
            retorno.put(Tabela.NIVEL.getName(), idNivel);
            retorno.put(Tabela.TAREFA.getName(), t.getID());
            retorno.put(Tabela.FASE.getName(), idFase);
            database.insert(getTableName(), null, retorno);
        }
        return 0;
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param fase - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Nivel fase) throws Exception {
        return null;
    }



    /**
     * Reescrita do metodo que faz a atribuicao dos valores vindo do banco
     * em uma nova instancia da entidade
     * @param cursor - objeto do banco de dados para recuperacao dos dados
     * @return - nova instancia da entidade
     * @throws Exception - caso ocorra erro
     */
    @Override
    public Nivel fill(Cursor cursor) throws Exception {
       return null;
    }
}
