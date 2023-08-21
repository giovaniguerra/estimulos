package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.Posicao;

/**
 * Created by Caio Gustavo on 06/04/2016.
 */
public class PosicaoNivelDAO extends DAOImpl<Integer, Nivel> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_posicao_nivel";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public PosicaoNivelDAO(BancoDadosHelper bdHelper) {
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
                Tabela.NIVEL_ID.getName() + " integer not null, "  +
                Tabela.POSICAO_ID.getName() + " integer not null, " +
                Tabela.FASE.getName() + " integer not null, " +
                "foreign key(" + Tabela.POSICAO_ID.getName() + ") references " + PosicaoDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.FASE.getName() + ") references " + FaseDAO.TABLE_NAME + "(" + ID + ")" +
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
        NIVEL_ID("nivel_id"),
        POSICAO_ID("posicao_id"),
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
        for (Posicao p: nivel.getPosicoes()) {
            retorno.put(Tabela.NIVEL_ID.getName(), idNivel);
            retorno.put(Tabela.POSICAO_ID.getName(), p.getID());
            retorno.put(Tabela.FASE.getName(), idFase);
            database.insert(getTableName(), null, retorno);
        }
        return 0;
    }

    @Override
    protected void update(Nivel nivel, SQLiteDatabase database) throws Exception {

        if(database.delete(getTableName(), Tabela.NIVEL_ID.getName() + " = ? AND " +
                Tabela.FASE.getName() + " = ?",
                new String[] {nivel.getID()+"", nivel.getFase().getID()+""} ) > 0) {
            Integer idNivel = nivel.getID();
            Integer idFase = nivel.getFase().getID();
            ContentValues retorno =  new ContentValues();
            for (Posicao p: nivel.getPosicoes()) {
                retorno.put(Tabela.NIVEL_ID.getName(), idNivel);
                retorno.put(Tabela.POSICAO_ID.getName(), p.getID());
                retorno.put(Tabela.FASE.getName(), idFase);
                database.insert(getTableName(), null, retorno);
            }
            database.setTransactionSuccessful();
        }
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param nivel - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Nivel nivel) throws Exception {

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

    /**
     * Este metodo retorna a lista de posicoes vinculados a um id de um Nivel
     * @param nivel -  parametro de pesquisa
     * @return Estimulos - lista de estimulos vinculados
     * @throws Exception - caso ocorra erro
     */
    public List<Posicao> listPosicao(Nivel nivel) throws Exception{
        // Instancia a lista de retorno
        List<Posicao> posicoes = new ArrayList<>();

        // Instancia o DAO de posicoes para fazer a busca
        PosicaoDAO posicaoDAO = new PosicaoDAO(getDatabaseHelper());

        // Para se conectar com o Banco
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        // Cria a query que retorna a lista de ids das posicoes vinculados ao id do nivel informado
        String query = "select * from " + getTableName() + " where " + Tabela.NIVEL_ID.getName() + " = " + nivel.getID() + " and " +
                    Tabela.FASE.getName() + " = " + nivel.getFase().getID();

        // Executa a consulta no banco
        Cursor cursor = database.rawQuery(query, null);

        // Instancia a lista de resultados da consulta
        List<Integer> result = new ArrayList<Integer>(cursor.getCount());

        // Para armezar o id de consulta do estimulo
        Integer idPosicao;
        if (cursor.moveToFirst()) {
            // Realiza o loop para percorrer os registros consultados
            while (!cursor.isAfterLast()) {

                // Recupera apenas o id do estimulo do registro da vez
                idPosicao = cursor.getInt(cursor.getColumnIndex(Tabela.POSICAO_ID.getName()));

                // Adiciona o estimulo consultado
                posicoes.add(posicaoDAO.visualizar(idPosicao));

                // Move o cursor para o proximo registro
                cursor.moveToNext();
            }
        }
        // Fecha o cursor do banco
        cursor.close();

        // Retorna a lista de estimulos
        return posicoes;
    }
}
