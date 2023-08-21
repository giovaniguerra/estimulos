/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.core.dao.IDAO;
import br.com.estimulos.core.util.ConexaoJDBC;
import br.com.estimulos.dominio.IEntidade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Caio Gustavo
 */
public abstract class AbstractJdbcDAO implements IDAO{
    
    public Connection connection;
    protected String table;
    protected String idTable;
    public boolean ctrlTransaction = true;
    public static final String PREFIXO = "WEB";
    
    public static final String COLUNAS_ENTIDADE_DOMINIO = ", dt_cadastro, dt_atualizacao, sincronizado";
    public static final String PARAMETRO_SEQ = "nextval";
    public static final String SINCRONIZADO = "sincronizado";
    
    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum SQL {
        INSERT("insert into "),
        UPDATE("update "),
        SELECT("select * from "),
        VIRGULA(", "),
        SET(" set "),
        IGUAL(" = "),
        WHERE(" where "),
        INTERROGACAO("?"),
        ABRE_COLUNA(" ("),
        FECHA_COLUNA(") "),
        VALUES("values "),
        AND(" and "),
        SO_SELECT("select "),
        APOSTROFE("'");

        private String name;

        private SQL(String name) { this.name=name; }

        public String getName() { return this.name; }

    }
    
    public AbstractJdbcDAO(Connection connection, String table, String idTable){
        
        this.table = table;
        this.idTable = idTable;
        this.connection = connection;
    }
    
    protected AbstractJdbcDAO(String table, String idTable){
        
        this.table = table;
        this.idTable = idTable;
    }
    
    @Override
    public void excluir(IEntidade entidade){
        openConnection();
        PreparedStatement pst = null;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(table);
        sb.append(" WHERE ");
        sb.append(idTable);
        sb.append("=");
        sb.append("?");
        try {
            
            connection.setAutoCommit(false);
            pst = connection.prepareStatement(sb.toString());
            pst.setInt(1,entidade.getID());
            
            pst.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                pst.close();
                if(ctrlTransaction)
                    connection.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    protected void openConnection() {
        
        try {
            if(connection == null || connection.isClosed())
                connection = ConexaoJDBC.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected String getUpdateCodigoTabela(){
        StringBuilder sql = new StringBuilder();
        sql.append(AbstractJdbcDAO.SQL.UPDATE.getName());
        sql.append(table);
        sql.append(AbstractJdbcDAO.SQL.SET.getName());
        sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
        sql.append(getNomeCodigo());
        sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
        sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
        sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
        sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());
        sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
        sql.append(AbstractJdbcDAO.SQL.WHERE.getName());
        sql.append(idTable);
        sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
        sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());

        return sql.toString();
    }
    
    protected String getSelectSequence(){
        
        StringBuilder sql = new StringBuilder();
        sql.append(AbstractJdbcDAO.SQL.SO_SELECT.getName());
        sql.append(PARAMETRO_SEQ);
        sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
        sql.append(AbstractJdbcDAO.SQL.APOSTROFE.getName());
        sql.append(getNomeSequence());
        sql.append(AbstractJdbcDAO.SQL.APOSTROFE.getName());
        sql.append("::regclass");
        sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
        
        return sql.toString();
    }
    
    protected abstract String getNomeTabela();
    protected abstract String getNomeCodigo();
    protected abstract String getNomeSequence();
    
    
}
