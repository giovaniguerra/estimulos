/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("br.com.estimulos.dominio.Jogo")
public class JogoDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_jogo";
    private static final String ID = "jogo_id";
    private static final String CODIGO = "jogo_cod";
    private static final String SEQUENCE = "tb_jogo_seq";

    public JogoDAO() {
        super(TABELA, ID);
    }

    public JogoDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public JogoDAO(Connection cx) {
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
        TEMA("tema"),
        PACIENTE("paciente_id"),
        IMAGEM("imagem_id");

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
        if (connection == null || connection.isClosed()) {
            openConnection();
        }
        connection.setAutoCommit(false);

        PreparedStatement pst = null;
        Jogo jogo = (Jogo) entidade;
        // esta gravando somente os dados iniciais do jogo?
        if (jogo.getEstimulos().isEmpty()) {

//        Imagem imagem = jogo.getImagem();       
            Paciente paciente = jogo.getPaciente();

//        ImagemDAO imgDAO = new ImagemDAO(connection);
//        imgDAO.ctrlTransaction = false;
//        imgDAO.salvar(imagem);
            PacienteDAO pacienteDAO = new PacienteDAO(connection);
            pacienteDAO.ctrlTransaction = false;
            pacienteDAO.salvar(paciente);

            try {
                StringBuilder sql = new StringBuilder();

                sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
                sql.append(TABELA);
                sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
                sql.append(ID);
                sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
                sql.append(Tabela.TEMA.getName());
                sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
                sql.append(Tabela.PACIENTE.getName());
//            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
//            sql.append(Tabela.IMAGEM.getName());                

                sql.append(COLUNAS_ENTIDADE_DOMINIO);
                sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
                sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
                sql.append("(?,?,?,?,?,?)");

                System.out.println(sql);

                /**
                 * Codigo que verifica se o ID esta setado caso nao esteja,
                 * busca id da sequence
                 */
                if (jogo.getID() == null) {
                    pst = connection.prepareStatement(getSelectSequence());
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        jogo.setID(rs.getInt(PARAMETRO_SEQ));
                    }
                }
                /**
                 * *******************************************
                 */

                pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, jogo.getID());
                pst.setString(2, jogo.getTema());
                pst.setInt(3, jogo.getPaciente().getID());
//            pst.setInt(4, jogo.getImagem().getID());

                /**
                 * SETA dados da entidade dominio
                 */
                // seta data de criacao
                Calendar data = Calendar.getInstance();
                data.setTime(jogo.getDataCriacao());
                Timestamp time = new Timestamp(data.getTimeInMillis());
                pst.setTimestamp(4, time);

                // seta data atualizacao
                data.setTime(jogo.getUltimaAtualizacao());
                time = new Timestamp(data.getTimeInMillis());
                pst.setTimestamp(5, time);
                pst.setBoolean(6, jogo.getSincronizado());
                /**
                 * ************************************************************
                 */

                pst.executeUpdate();
                connection.commit();
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
                    
                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        } else {
            try {
                for (Estimulo e : jogo.getEstimulos()) {
                    StringBuilder sql1 = new StringBuilder();
                    sql1.append("INSERT INTO tb_jogo_estimulo (jogo_id, estimulo_id)");
                    sql1.append(" VALUES (?,?)");

                    pst = connection.prepareStatement(sql1.toString());
                    pst.setInt(1, jogo.getID());
                    pst.setInt(2, e.getID());
                    pst.executeUpdate();
                }

                connection.commit();

                /**
                 * SALVA O CODIGO PARA ESTA ENTIDADE
                 */
//            jogo.setCodigo(PREFIXO+jogo.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, jogo.getCodigo());
//            pst.setInt(2, jogo.getID());
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

                    pst.close();
                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void alterar(IEntidade entidade) throws SQLException {

        if (connection == null) {
            openConnection();
        }
        PreparedStatement ps = null;

    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IEntidade visualizar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
