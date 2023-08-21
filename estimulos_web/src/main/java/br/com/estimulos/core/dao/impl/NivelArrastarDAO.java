/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.Posicao;
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
@Component("br.com.estimulos.dominio.NivelArrastar")
public class NivelArrastarDAO extends AbstractJdbcDAO{
    
    private static final String TABELA = "tb_nivel_arrastar";
    private static final String ID = "nivel_arrastar_id";
    private static final String CODIGO = "nivel_arrastar_cod";
    private static final String SEQUENCE = "tb_nivel_arrastar_seq";

    public NivelArrastarDAO(){
        super(TABELA, ID);
    }
    
    public NivelArrastarDAO(String table, String idTable) {
        super(TABELA, ID);
    }
    
    public NivelArrastarDAO(Connection cx){
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
        NUM_NIVEL("num_nivel"),
        NUM_TAREFAS("num_tarefas"),
        URI_IMAGEM("uri_imagem"),        
        QTD_ESTIMULO("qtd_estimulos"),        
        LIMITE_ERRO("limite_erro"),
        LIMITE_SOM_ERRO("limite_som_erro"),
        RAND_POSICAO("rand_posicao"),
        FASE("fase_cod"),
        JOGO("jogo_cod");

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
        NivelArrastar nivel = (NivelArrastar) entidade;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NUM_NIVEL.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NUM_TAREFAS.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.URI_IMAGEM.getName());            
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.QTD_ESTIMULO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.LIMITE_ERRO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.LIMITE_SOM_ERRO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.RAND_POSICAO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.FASE.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.JOGO.getName());   
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            System.out.println(sql.toString());         
            
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(nivel.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    nivel.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }       
            /**********************************************/
            
            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, nivel.getID());
            pst.setInt(2, nivel.getNumero());
            pst.setInt(3, nivel.getNumTarefas());
            pst.setString(4, nivel.getUriImagem());
            
            pst.setInt(5, nivel.getQtdeEstimulos());
            pst.setInt(6, nivel.getLimiteErros());
            pst.setInt(7, nivel.getLimiteSomDeErro());
            
            pst.setBoolean(8, nivel.isRandPosicao());
            pst.setInt(9, nivel.getFase().getID());
            pst.setInt(10, nivel.getJogo().getID());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(nivel.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(11, time );
            
            // seta data atualizacao
            data.setTime(nivel.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(12, time );            
            pst.setBoolean(13, nivel.getSincronizado());
            /***************************************************************/
            
            
            pst.executeUpdate();
            connection.commit();
            
            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            nivel.setCodigo(PREFIXO+nivel.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, nivel.getCodigo());
//            pst.setInt(2, nivel.getID());
//            pst.executeUpdate();
//                   
//            connection.commit();
            /********************************************************/
            
            
            StringBuilder sql1; 
            PosicaoDAO posicaoDAO = new PosicaoDAO(connection);
            posicaoDAO.ctrlTransaction = false;
            for(Posicao p: nivel.getPosicoes()){
                posicaoDAO.salvar(p);
                
                sql1 = new StringBuilder();
                sql1.append("INSERT INTO tb_posicao_nivel (nivel_id, posicao_id, fase_id)");
                sql1.append(" VALUES (?,?,?)");

                pst = connection.prepareStatement(sql1.toString());
                pst.setInt(1, nivel.getID());
                pst.setInt(2, p.getID());
                pst.setInt(3, nivel.getFase().getID());
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
