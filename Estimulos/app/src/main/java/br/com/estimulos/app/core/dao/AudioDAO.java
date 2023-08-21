package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.estimulos.dominio.Audio;

/**
 * Esta classe implementa o DAO para a entidade Audio
 *
 * Created by Caio Cruz on 20/02/2016.
 */
public class AudioDAO extends DAOImpl<Integer, Audio> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_audio";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public AudioDAO(BancoDadosHelper bdHelper) {
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
                ID + " integer primary key not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.NOME.getName() + " text not null, "  +
                Tabela.URI_AUDIO.getName() + " text not null," +
                Tabela.DURACAO_MILIS.getName() + " integer);";
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
        URI_AUDIO("uri_audio"),
        DURACAO_MILIS("duracao_milis");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param audio - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Audio audio) throws Exception {
        ContentValues retorno =  new ContentValues();

        retorno.put(Tabela.NOME.getName(), audio.getNome());
        retorno.put(Tabela.URI_AUDIO.getName(), audio.getUri());

        retorno.put(Tabela.DURACAO_MILIS.getName(), audio.getDuracao());
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
    public Audio fill(Cursor cursor) throws Exception {
        Audio audio = new Audio();
        audio.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));
        audio.setUri(cursor.getString(cursor.getColumnIndex(Tabela.URI_AUDIO.getName())));
        audio.setDuracao(cursor.getInt(cursor.getColumnIndex(Tabela.DURACAO_MILIS.getName())));

        audio.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        audio.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        audio.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return audio;
    }
}
