/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("br.com.estimulos.dominio.Terapeuta")
public class TerapeutaDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_terapeuta";
    private static final String ID = "terapeuta_id";
    private static final String CODIGO = "terapeuta_cod";
    private static final String SEQUENCE = "tb_terapeuta_seq";

    public TerapeutaDAO() {
        super(TABELA, ID);
    }

    public TerapeutaDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public TerapeutaDAO(Connection cx) {
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
        NOME("nome"),
        USUARIO("usuario_id");

        private final String name;

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
        PreparedStatement pst = null;
        Terapeuta terapeuta = (Terapeuta) entidade;

        UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
        usuarioDAO.ctrlTransaction = false;
        usuarioDAO.salvar(terapeuta.getUsuario());

        try {
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.USUARIO.getName());

            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?)");

            System.out.println(sql.toString());

            /**
             * Codigo que verifica se o ID esta setado caso nao esteja, busca id
             * da sequence
             */
            if (terapeuta.getID() == null) {
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    terapeuta.setID(rs.getInt(PARAMETRO_SEQ));
                }
            }
            /**
             * *******************************************
             */

            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, terapeuta.getID());
            pst.setString(2, terapeuta.getNome());
            pst.setInt(3, terapeuta.getUsuario().getID());

            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(terapeuta.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(4, time);

            // seta data atualizacao
            data.setTime(terapeuta.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(5, time);
            pst.setBoolean(6, terapeuta.getSincronizado());
            /**
             * ************************************************************
             */

            pst.executeUpdate();
            connection.commit();

            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            terapeuta.setCodigo(PREFIXO+terapeuta.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, terapeuta.getCodigo());
//            pst.setInt(2, terapeuta.getID());
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
       Terapeuta terapeuta = (Terapeuta) entidade;
        openConnection();
        PreparedStatement pst = null;        

        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("update tb_usuario set ");
            if(terapeuta.getUsuario().getLogin()!= null){
                sql.append("login=? ");
            }
            if(terapeuta.getUsuario().getSenha() != null){
                sql.append("senha=? ");
            }
            if(terapeuta.getDtNascimento() != null){
                
            }
            if(terapeuta.getNome() != null){
                
            }
            
            sql.append(";");
            pst = connection.prepareStatement(sql.toString());
            pst.setString(1, terapeuta.getNome());
            pst.setInt(2, terapeuta.getID());            
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
        openConnection();
        PreparedStatement pst = null;
        Terapeuta terapeuta = (Terapeuta) entidade;
        List<IEntidade> resultados = new ArrayList<>();
        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("select t.terapeuta_id,t.nome, t.dt_nascimento, u.login, u.senha, u.usuario_id from tb_terapeuta t\n"
                    + "inner join tb_usuario u on (u.usuario_id = t.usuario_id)\n"
                    + "where u.usuario_id=?;");

            pst = connection.prepareStatement(sql.toString());
            pst.setInt(1, terapeuta.getUsuario().getID());

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

            ResultSet rs = pst.executeQuery();

            Terapeuta t;
            Usuario u;

            while (rs.next()) {
                t = new Terapeuta();
                u = new Usuario();

                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setID(rs.getInt("usuario_id"));

                t.setNome(rs.getString("nome"));
                t.setDtNascimento(new Date(rs.getDate("dt_nascimento").getTime()));
                t.setID(rs.getInt("terapeuta_id"));

                t.setUsuario(u);

                resultados.add(t);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("ERRO: " + ex.getMessage());
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
        return resultados;
    }

    @Override
    public IEntidade visualizar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
