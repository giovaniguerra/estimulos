package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.estimulos.dominio.Imagem;

/**
 * Esta classe implementa o DAO para a entidade Imagem
 *
 * Created by Caio Cruz on 20/02/2016.
 */
public class ImagemDAO extends DAOImpl<Integer, Imagem> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_imagem";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public ImagemDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela Imagem
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.URI_IMAGEM.getName() + " text not null, " +
                Tabela.URI_MASCARA.getName() + " text, " +
                Tabela.LARGURA.getName() + " integer, " +
                Tabela.ALTURA.getName() + " integer);";
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
        URI_IMAGEM("uri_imagem"),
        URI_MASCARA("uri_mascara"),
        LARGURA("largura"),
        ALTURA("altura");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected void update(Imagem object, SQLiteDatabase database) throws Exception {
        // Try to update the data.
        ContentValues values = getValuesFor(object);
        object.setUltimaAtualizacao(new Date());
        values.put(LAST_UPDATE, Long.toString(object.getUltimaAtualizacao().getTime()));

        database.update(getTableName(), values, ID + " = " + object.getID(), null);
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param imagem - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Imagem imagem) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.URI_IMAGEM.getName(), imagem.getUri());
        retorno.put(Tabela.URI_MASCARA.getName(), imagem.getUriMascara());
        retorno.put(Tabela.LARGURA.getName(), imagem.getLargura());
        retorno.put(Tabela.ALTURA.getName(), imagem.getAltura());
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
    public Imagem fill(Cursor cursor) throws Exception {
        Imagem imagem = new Imagem();
        imagem.setUri(cursor.getString(cursor.getColumnIndex(Tabela.URI_IMAGEM.getName())));
        imagem.setAltura(cursor.getInt(cursor.getColumnIndex(Tabela.ALTURA.getName())));
        imagem.setLargura(cursor.getInt(cursor.getColumnIndex(Tabela.LARGURA.getName())));
        imagem.setUriMascara(cursor.getString(cursor.getColumnIndex(Tabela.URI_MASCARA.getName())));

        imagem.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        imagem.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        imagem.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return imagem;
    }
}
