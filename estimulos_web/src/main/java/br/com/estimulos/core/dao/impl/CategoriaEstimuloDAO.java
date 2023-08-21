/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("br.com.estimulos.dominio.CategoriaEstimulo")
public class CategoriaEstimuloDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_categoria_estimulos";
    private static final String ID = "categoria_id";
    private static final String CODIGO = "categoria_cod";
    private static final String SEQUENCE = "tb_categoria_estimulos_seq";

    public CategoriaEstimuloDAO() {
        super(TABELA, ID);
    }

    public CategoriaEstimuloDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public CategoriaEstimuloDAO(Connection cx) {
        super(cx, TABELA, ID);
    }

    @Override
    protected String getNomeTabela() {
        return TABELA;
    }

    @Override
    protected String getNomeCodigo() {
        return CODIGO;
    }

    @Override
    protected String getNomeSequence() {
        return SEQUENCE;
    }

    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da
     * tabela tornando a criacao das querys mais facil
     */
    public enum Tabela {
        NOME("nome");

        private String name;

        private Tabela(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    @Override
    public void salvar(IEntidade entidade) throws SQLException {
        if (connection == null) {
            openConnection();
        }
        connection.setAutoCommit(false);
        ctrlTransaction = false;
        PreparedStatement pst = null;
        CategoriaEstimulo categoria = (CategoriaEstimulo) entidade;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());

            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?)");

            System.out.println(sql.toString());

            /**
             * Codigo que verifica se o ID esta setado caso nao esteja, busca id
             * da sequence
             */
            if (categoria.getID() == null) {
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    categoria.setID(rs.getInt(PARAMETRO_SEQ));
                }
            }
            /**
             * *******************************************
             */

            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, categoria.getID());
            pst.setString(2, categoria.getNome());

            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(categoria.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(3, time);

            // seta data atualizacao
            data.setTime(categoria.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(4, time);
            pst.setBoolean(5, categoria.getSincronizado());
            /**
             * ************************************************************
             */

            pst.executeUpdate();
            connection.commit();

            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            categoria.setCodigo(PREFIXO+categoria.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, categoria.getCodigo());
//            pst.setInt(2, categoria.getID());
//            pst.executeUpdate();
//                   
//            connection.commit();
            /**
             * *****************************************************
             */
            pst.close();

        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();

            }
            e.printStackTrace();
        } finally {

            try {

                if (ctrlTransaction) {
                    connection.close();
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void alterar(IEntidade entidade) throws SQLException {
        CategoriaEstimulo categoria = (CategoriaEstimulo) entidade;
        openConnection();
        PreparedStatement pst = null;        

        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("update tb_categoria_estimulos set nome=? where categoria_id=?");
            sql.append(";");
            pst = connection.prepareStatement(sql.toString());
            pst.setString(1, categoria.getNome());
            pst.setInt(2, categoria.getID());            
            pst.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException exx) {
                System.out.println("ERRO: " + exx.getMessage());
            }
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                connection.close();
            } catch (SQLException ex) {
                System.out.println("ERRO: " + ex.getMessage());
            }
        }        
    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {

        List<IEntidade> entidades = new ArrayList<IEntidade>();
        if (connection == null) {
            openConnection();
        }
        try {

            PreparedStatement ps;
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.SELECT.getName());
            sql.append(TABELA);

            ps = connection.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            CategoriaEstimulo categoria;
            while (rs.next()) {

                categoria = new CategoriaEstimulo();
                categoria.setID(rs.getInt(ID));
                categoria.setNome(rs.getString(Tabela.NOME.getName()));
                categoria.setSincronizado(rs.getBoolean(SINCRONIZADO));
                entidades.add(categoria);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entidades;

    }

    @Override
    public EntidadeDominio visualizar(IEntidade entidade) throws SQLException {
        CategoriaEstimulo categoria = (CategoriaEstimulo) entidade;
        openConnection();
        PreparedStatement pst = null;
        CategoriaEstimulo c = null;

        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("select * from tb_categoria_estimulos where categoria_id=?;");
            pst = connection.prepareStatement(sql.toString());
            pst.setInt(1, categoria.getID());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                c = new CategoriaEstimulo();

                c.setID(rs.getInt("categoria_id"));
                c.setNome(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException exx) {
                System.out.println("ERRO: " + exx.getMessage());
            }
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                connection.close();
            } catch (SQLException ex) {
                System.out.println("ERRO: " + ex.getMessage());
            }
        }
        return c;
    }
}
