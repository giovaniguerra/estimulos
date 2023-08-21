package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Reforcador;

/**
 * Created by Caio Gustavo on 28/05/2016.
 */
public class JogoResforcadorDAO extends DAOImpl<Integer, Jogo> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_jogo_reforcador";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public JogoResforcadorDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela JogoReforcador
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key autoincrement not null, " +
                Tabela.JOGO.getName() + " text not null, " +
                Tabela.REFORCADOR.getName() + " text not null, " +
                "foreign key(" + Tabela.JOGO.getName() + ") references " + JogoDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.REFORCADOR.getName() + ") references " + ReforcadorDAO.TABLE_NAME + "(" + ID + ")" +
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
        JOGO("jogo_id"),
        REFORCADOR("reforcador_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Jogo jogo, SQLiteDatabase database) throws Exception {
        Integer idJogo = jogo.getID();
        ContentValues retorno =  new ContentValues();
        for (Reforcador r: jogo.getReforcadores()) {
            retorno.put(Tabela.JOGO.getName(), idJogo);
            retorno.put(Tabela.REFORCADOR.getName(), r.getID());
            database.insert(getTableName(), null, retorno);
        }
        return 0;
    }

    @Override
    protected void update(Jogo jogo, SQLiteDatabase database) throws Exception {
        /**
         * Deleta todos os relacionamentos
         * e insere os novos
         */
        if(database.delete(getTableName(), Tabela.JOGO.getName() + " = ?",new String[] {jogo.getID()+""} ) > 0){
            Integer idJogo = jogo.getID();
            ContentValues retorno =  new ContentValues();
            for (Reforcador e: jogo.getReforcadores()) {
                retorno.put(Tabela.JOGO.getName(), idJogo);
                retorno.put(Tabela.REFORCADOR.getName(), e.getID());
                database.insert(getTableName(), null, retorno);
            }
            database.setTransactionSuccessful();
        }
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param je - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Jogo je) throws Exception {
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
    public Jogo fill(Cursor cursor) throws Exception {
        return null;
    }

    /**
     * Este metodo retorna a lista de reforcadore vinculados a um id de Jogo
     * @param idJogo -  parametro de pesquisa
     * @return Estimulos - lista de reforcadore vinculados
     * @throws Exception - caso ocorra erro
     */
    public List<Reforcador> listReforcadores(Integer idJogo) throws Exception{
        // Instancia a lista de retorno
        List<Reforcador> reforcadors = new ArrayList<>();

        // Instancia o DAO do reforcadore para fazer a busca
        ReforcadorDAO reforcadorDAO = new ReforcadorDAO(getDatabaseHelper());

        // Para se conectar com o Banco
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        // Cria a query que retorna a lista de ids dos reforcadors vinculados ao id do Jogo informado
        String query = "select * from " + getTableName() + " where " + Tabela.JOGO.getName() + " = " + idJogo;

        // Executa a consulta no banco
        Cursor cursor = database.rawQuery(query, null);

        // Instancia a lista de resultados da consulta
        List<Integer> result = new ArrayList<Integer>(cursor.getCount());

        // Para armezar o id de consulta do reforcadore
        Integer idReforcador;

        if (cursor.moveToFirst()) {
            // Realiza o loop para percorrer os registros consultados
            while (!cursor.isAfterLast()) {

                // Recupera apenas o id do reforcadore do registro da vez
                idReforcador = cursor.getInt(cursor.getColumnIndex(Tabela.REFORCADOR.getName()));

                // Adiciona o reforcadore consultado
                reforcadors.add(reforcadorDAO.visualizar(idReforcador));

                // Move o cursor para o proximo registro
                cursor.moveToNext();
            }
        }
        // Fecha o cursor do banco
        cursor.close();

        // Retorna a lista de reforcadors
        return reforcadors;
    }
}
