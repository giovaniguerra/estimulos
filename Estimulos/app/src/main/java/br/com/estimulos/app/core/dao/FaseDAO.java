package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseArrastar;
import br.com.estimulos.dominio.FaseTocar;

/**
 * Created by caioc_000 on 13/03/2016.
 */
public class FaseDAO extends DAOImpl<Integer, Fase> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_fase";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public FaseDAO(BancoDadosHelper bdHelper) {
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
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.NOME.getName() + " text not null, "  +
                Tabela.URI_IMAGEM.getName() + " text not null," +
                Tabela.NOME_CLASSE_HERDADA.getName() + " text not null);";
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
        URI_IMAGEM("uri_imagem"),
        SINCRONIZADO("sincronizado"),
        NOME_CLASSE_HERDADA("nome_classe");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param fase - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Fase fase) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NOME.getName(), fase.getNome());
        retorno.put(Tabela.URI_IMAGEM.getName(), fase.getUriImagem());
        retorno.put(Tabela.NOME_CLASSE_HERDADA.getName(), fase.getNomeClasseHerdade());
        retorno.put(Tabela.SINCRONIZADO.getName(), fase.getSincronizado() ? 1 : 0);
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
    public Fase fill(Cursor cursor) throws Exception {
        Fase fase = new Fase();
        fase.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        fase.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));
        fase.setUriImagem(cursor.getString(cursor.getColumnIndex(Tabela.URI_IMAGEM.getName())));
        fase.setNomeClasseHerdade(cursor.getString(cursor.getColumnIndex(Tabela.NOME_CLASSE_HERDADA.getName())));
        fase.setID(cursor.getInt(cursor.getColumnIndex(ID)));

        return fase;
    }
}
