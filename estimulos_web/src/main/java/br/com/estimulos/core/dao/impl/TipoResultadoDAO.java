/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import static br.com.estimulos.core.dao.impl.AbstractJdbcDAO.COLUNAS_ENTIDADE_DOMINIO;
import static br.com.estimulos.core.dao.impl.AbstractJdbcDAO.PARAMETRO_SEQ;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.TipoResultado;
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
@Component("br.com.estimulos.dominio.TipoResultado")
public class TipoResultadoDAO extends AbstractJdbcDAO{
    private static final String TABELA = "tb_tipo_resultado";
    private static final String ID = "tipo_resultado_id";
    private static final String SEQUENCE = "tb_tipo_resultado_seq";

    public TipoResultadoDAO() {
        super(TABELA, ID);
    }

    public TipoResultadoDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public TipoResultadoDAO(Connection cx) {
        super(cx, TABELA, ID);
    }

    @Override
    protected String getNomeTabela() {
        return TABELA;
    }

    @Override
    protected String getNomeCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getNomeSequence() {
        return SEQUENCE;
    }

    
    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum Tabela {
        NOME("nome"),
        DESCRICAO("descricao");

        private final String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }
    
    
    @Override
    public void salvar(IEntidade entidade) throws SQLException {
        if(connection == null){
            openConnection();
        }
        connection.setAutoCommit(false);
        PreparedStatement pst = null;
        TipoResultado tipoResultado = (TipoResultado) entidade;
      
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.DESCRICAO.getName());
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?)");
            
            System.out.println(sql.toString());
            
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(tipoResultado.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    tipoResultado.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }       
            /**********************************************/
           
            
            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, tipoResultado.getID());
            pst.setString(2, tipoResultado.getNome());
            pst.setString(3, tipoResultado.getDescricao());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(tipoResultado.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(4, time );
            
            // seta data atualizacao
            data.setTime(tipoResultado.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(5, time );            
            pst.setBoolean(6, tipoResultado.getSincronizado());
            /***************************************************************/
            
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
        } finally{
            
            try {
                
                if(ctrlTransaction)
                    connection.close();
                
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        }              
    }

    @Override
    public void alterar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {
        List<IEntidade> entidades = new ArrayList<IEntidade>();
        if(connection == null){
            openConnection();
        }
        try {
            
            PreparedStatement ps;
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.SELECT.getName());
            sql.append(TABELA);              
            
            ps = connection.prepareStatement(sql.toString());           
            ResultSet rs = ps.executeQuery();
            TipoResultado resultado;
            while(rs.next()){
                resultado = new TipoResultado();
                resultado.setID(rs.getInt(ID));
                resultado.setNome(rs.getString(Tabela.NOME.getName()));
                resultado.setDescricao(rs.getString(Tabela.DESCRICAO.getName()));
                
                entidades.add(resultado);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        
        return entidades;
    }

    @Override
    public IEntidade visualizar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
