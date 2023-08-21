/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio Gustavo
 */
@Component("br.com.estimulos.dominio.Audio")
public class AudioDAO extends AbstractJdbcDAO{
    private static final String TABELA = "tb_audio";
    private static final String ID = "audio_id";
    private static final String CODIGO = "audio_cod";   
    private static final String SEQUENCE = "tb_audio_seq";

    public AudioDAO(){
        super(TABELA, ID);
    }
    
    public AudioDAO(String table, String idTable) {
        super(TABELA, ID);
    }
    
    public AudioDAO(Connection cx){
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
        NOME("nome"),
        URI_AUDIO("uri_audio"),
        DURACAO_MILIS("duracao_milis");

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
        Audio audio = (Audio) entidade;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.URI_AUDIO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.DURACAO_MILIS.getName());  
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?)");
            
            System.out.println(sql.toString());
            
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(audio.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    audio.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }       
            /**********************************************/
            
            pst = connection.prepareStatement(sql.toString());
            pst.setInt(1, audio.getID());
            pst.setString(2, audio.getNome());
            pst.setString(3, audio.getUri());
            pst.setInt(4, audio.getDuracao());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(audio.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(5, time );
            
            // seta data atualizacao
            data.setTime(audio.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(6, time );            
            pst.setBoolean(7, audio.getSincronizado());
            /***************************************************************/
            
            pst.executeUpdate();           
            connection.commit();
                     
            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            audio.setCodigo(PREFIXO+audio.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, audio.getCodigo());
//            pst.setInt(2, audio.getID());
//            pst.executeUpdate();
//                   
//            connection.commit();
            /********************************************************/
            
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
    public EntidadeDominio visualizar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
