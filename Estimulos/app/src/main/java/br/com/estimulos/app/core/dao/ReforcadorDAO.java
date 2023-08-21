package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Video;

/**
 * Created by Caio Gustavo on 25/03/2016.
 */
public class ReforcadorDAO extends DAOImpl<Integer, Reforcador> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_reforcador";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public ReforcadorDAO(BancoDadosHelper bdHelper) {
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
                Tabela.TIPO_MIDIA.getName() + " text not null, "  +
                Tabela.URI_MIDIA.getName() + " text not null," +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.DURACAO_MILIS.getName() + " integer not null);";
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
        URI_MIDIA("uri_audio"),
        DURACAO_MILIS("duracao_milis"),
        SINCRONIZADO("sincronizado"),
        TIPO_MIDIA("tipo");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param reforcador - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Reforcador reforcador) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NOME.getName(), reforcador.getNome());
        retorno.put(Tabela.URI_MIDIA.getName(), reforcador.getMidia().getUri());
        retorno.put(Tabela.TIPO_MIDIA.getName(), reforcador.getMidia().getClass().getName());
        retorno.put(Tabela.DURACAO_MILIS.getName(), reforcador.getTempoDuracao());
        retorno.put(Tabela.SINCRONIZADO.getName(), reforcador.getSincronizado() ? 1 : 0);
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
    public Reforcador fill(Cursor cursor) throws Exception {
        Reforcador reforcador = new Reforcador();
        reforcador.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));
        reforcador.setTempoDuracao(cursor.getInt(cursor.getColumnIndex(Tabela.DURACAO_MILIS.getName())));

        if(cursor.getString(cursor.getColumnIndex(Tabela.TIPO_MIDIA.getName())).equals(Audio.class.getName()))
            reforcador.setMidia(new Audio());
        if(cursor.getString(cursor.getColumnIndex(Tabela.TIPO_MIDIA.getName())).equals(Video.class.getName()))
            reforcador.setMidia(new Video());

        reforcador.getMidia().setUri(cursor.getString(cursor.getColumnIndex(Tabela.URI_MIDIA.getName())));
        reforcador.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        reforcador.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        reforcador.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        reforcador.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return reforcador;
    }
}
