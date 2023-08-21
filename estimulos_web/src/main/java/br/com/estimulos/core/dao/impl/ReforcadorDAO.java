/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Midia;
import br.com.estimulos.dominio.Reforcador;
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
@Component("br.com.estimulos.dominio.Reforcador")
public class ReforcadorDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_reforcador";
    private static final String ID = "reforcador_id";
    private static final String CODIGO = "reforcador_cod";
    private static final String SEQUENCE = "tb_reforcador_seq";

    public ReforcadorDAO() {
        super(TABELA, ID);
    }

    public ReforcadorDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public ReforcadorDAO(Connection cx) {
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
        URI_MIDIA("uri_midia"),
        DURACAO_MILIS("duracao_milis"),
        TIPO_MIDIA("tipo");

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
        PreparedStatement pst = null;
        Reforcador reforcador = (Reforcador) entidade;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.URI_MIDIA.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.DURACAO_MILIS.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.TIPO_MIDIA.getName());

            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?,?)");

            System.out.println(sql.toString());

            /**
             * Codigo que verifica se o ID esta setado caso nao esteja, busca id
             * da sequence
             */
            if (reforcador.getID() == null) {
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    reforcador.setID(rs.getInt(PARAMETRO_SEQ));
                }
            }
            /**
             * *******************************************
             */

            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, reforcador.getID());
            pst.setString(2, reforcador.getNome());
            pst.setString(3, reforcador.getMidia().getUri());
            pst.setInt(4, reforcador.getTempoDuracao());
            pst.setString(5, reforcador.getMidia().getClass().getName());

            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(reforcador.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(6, time);

            // seta data atualizacao
            data.setTime(reforcador.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(7, time);
            pst.setBoolean(8, reforcador.getSincronizado());
            /**
             * ************************************************************
             */

            pst.executeUpdate();
            connection.commit();

            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            reforcador.setCodigo(PREFIXO+reforcador.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, reforcador.getCodigo());
//            pst.setInt(2, reforcador.getID());           
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

        if (connection == null) {
            openConnection();
        }
        PreparedStatement ps = null;

    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {
        openConnection();
        PreparedStatement pst = null;

        List<IEntidade> resultados = new ArrayList<>();

        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("select * from tb_reforcador;");
            pst = connection.prepareStatement(sql.toString());
            ResultSet rs = pst.executeQuery();
            Reforcador r;
            Audio a = new Audio();
            Imagem i = new Imagem();
            System.out.println(Audio.class.getName());
            while(rs.next()){
                r = new Reforcador();
                if((Audio.class.getName()).equals(rs.getString("tipo"))){
                    a = new Audio();
                    a.setUri(rs.getString("uri_midia"));
                    r.setMidia(a);
                }
                else if((Imagem.class.getName()).equals(rs.getString("tipo"))){
                    i = new Imagem();
                    i.setUri(rs.getString("uri_midia"));
                    r.setMidia(i);
                }
                r.setNome(rs.getString("nome"));
                r.setID(rs.getInt("reforcador_id"));
                
                resultados.add(r);                
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
