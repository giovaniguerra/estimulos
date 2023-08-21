package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Jogo;


/**
 * Esta classe implementa o DAO para o relacionamento de muitos-para-muitos entre
 * as entidades Jogo e Estimulo
 *
 * Created by caioc_000 on 21/02/2016.
 */
public class JogoEstimuloDAO extends DAOImpl<Integer, Jogo> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_jogo_estimulo";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public JogoEstimuloDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela JogoEstimulo
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key autoincrement not null, " +
                Tabela.JOGO.getName() + " text not null, " +
                Tabela.ESTIMULO.getName() + " text not null, " +
                "foreign key(" + Tabela.JOGO.getName() + ") references " + JogoDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.ESTIMULO.getName() + ") references " + EstimuloDAO.TABLE_NAME + "(" + ID + ")" +
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
        ESTIMULO("estimulo_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Jogo jogo, SQLiteDatabase database) throws Exception {
        Integer idJogo = jogo.getID();
        ContentValues retorno =  new ContentValues();
        for (Estimulo e: jogo.getEstimulos()) {
            retorno.put(Tabela.JOGO.getName(), idJogo);
            retorno.put(Tabela.ESTIMULO.getName(), e.getID());
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
            for (Estimulo e: jogo.getEstimulos()) {
                retorno.put(Tabela.JOGO.getName(), idJogo);
                retorno.put(Tabela.ESTIMULO.getName(), e.getID());
                database.insert(getTableName(), null, retorno);
            }
            database.setTransactionSuccessful();
        }
        Cursor cursor = getDatabaseHelper().getReadableDatabase().rawQuery("select count(*) from " + getTableName() + " where " + Tabela.JOGO.getName() + " = " + jogo.getID(), null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);


        /**
         * Se foi a primeira vez que que entro devera inserir
         */
        if(count == 0){
            Integer idJogo = jogo.getID();
            ContentValues retorno =  new ContentValues();
            for (Estimulo e: jogo.getEstimulos()) {
                retorno.put(Tabela.JOGO.getName(), idJogo);
                retorno.put(Tabela.ESTIMULO.getName(), e.getID());
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
     * Este metodo retorna a lista de estimulos vinculados a um id de Jogo
     * @param idJogo -  parametro de pesquisa
     * @return Estimulos - lista de estimulos vinculados
     * @throws Exception - caso ocorra erro
     */
    public List<Estimulo> listEstimulos(Integer idJogo) throws Exception{
        // Instancia a lista de retorno
        List<Estimulo> estimulos = new ArrayList<>();

        // Instancia o DAO do estimulo para fazer a busca
        EstimuloDAO estimuloDAO = new EstimuloDAO(getDatabaseHelper());

        // Para se conectar com o Banco
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        // Cria a query que retorna a lista de ids dos estimulos vinculados ao id do Jogo informado
        String query = "select * from " + getTableName() + " where " + Tabela.JOGO.getName() + " = " + idJogo;

        // Executa a consulta no banco
        Cursor cursor = database.rawQuery(query, null);

        // Instancia a lista de resultados da consulta
        List<Integer> result = new ArrayList<Integer>(cursor.getCount());

        // Para armezar o id de consulta do estimulo
        Integer idEstimulo;
        if (cursor.moveToFirst()) {
            // Realiza o loop para percorrer os registros consultados
            while (!cursor.isAfterLast()) {

                // Recupera apenas o id do estimulo do registro da vez
                idEstimulo = cursor.getInt(cursor.getColumnIndex(Tabela.ESTIMULO.getName()));

                // Adiciona o estimulo consultado
                estimulos.add(estimuloDAO.visualizar(idEstimulo));

                // Move o cursor para o proximo registro
                cursor.moveToNext();
            }
        }
        // Fecha o cursor do banco
        cursor.close();

        // Retorna a lista de estimulos
        return estimulos;
    }
}
