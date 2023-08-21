/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Tarefa;
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
@Component("br.com.estimulos.dominio.Tarefa")
public class TarefaDAO extends AbstractJdbcDAO{
    
    private static final String TABELA = "tb_tarefa";
    private static final String ID = "tarefa_id";
    private static final String CODIGO = "tarefa_cod";
    private static final String SEQUENCE = "tb_tarefa_seq";

    public TarefaDAO(){
        super(TABELA, ID);
    }
    
    public TarefaDAO(String table, String idTable) {
        super(TABELA, ID);
    }
    
    public TarefaDAO(Connection cx){
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
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum Tabela {
        NUM_TAREFA("num_tarefa"),
        COMPLETADA("completada"),
        FASE("fase_id");

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
        Tarefa tarefa = (Tarefa) entidade;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NUM_TAREFA.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.COMPLETADA.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.FASE.getName());
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?)");
            
            System.out.println(sql.toString());
            
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(tarefa.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    tarefa.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }       
            /**********************************************/
            
            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, tarefa.getID());
            pst.setInt(2, tarefa.getNumTarefa());
            pst.setBoolean(3, tarefa.isCompletada());
            pst.setInt(4, tarefa.getFaseId());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(tarefa.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(5, time );
            
            // seta data atualizacao
            data.setTime(tarefa.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(6, time );            
            pst.setBoolean(7, tarefa.getSincronizado());
            /***************************************************************/
            
            pst.executeUpdate();
            connection.commit();
            
            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            tarefa.setCodigo(PREFIXO+tarefa.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, tarefa.getCodigo());
//            pst.setInt(2, tarefa.getID());
//            pst.executeUpdate();
//                   
//            connection.commit();
            /********************************************************/
            
             for(LocalEstimulo e: tarefa.getListLocais()){
                StringBuilder sql1 = new StringBuilder();
                sql1.append("INSERT INTO tb_estimulo_posicao (estimulo_id, posicao_id, tarefa_id)");
                sql1.append(" VALUES (?,?,?)");

                pst = connection.prepareStatement(sql1.toString());
                pst.setInt(1, e.getEstimulo().getID());
                pst.setInt(2, e.getPosicao().getID());
                pst.setInt(3, tarefa.getID());
                pst.executeUpdate();
            }
            
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
        
        if(connection == null){
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
