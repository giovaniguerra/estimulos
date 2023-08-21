/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.IEntidade;
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
@Component("br.com.estimulos.dominio.Fase")
public class FaseDAO extends AbstractJdbcDAO{
    
    private static final String TABELA = "tb_fase";
    private static final String ID = "fase_id";
    private static final String CODIGO = "fase_cod";
    private static final String SEQUENCE = "tb_fase_seq";

    public FaseDAO(){
        super(TABELA, ID);
    }
    
    public FaseDAO(String table, String idTable) {
        super(TABELA, ID);
    }
    
    public FaseDAO(Connection cx){
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
        URI_IMAGEM("uri_imagem"),
        NOME_CLASSE_HERDADA("nome_classe");

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
        Fase fase = (Fase) entidade;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.URI_IMAGEM.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME_CLASSE_HERDADA.getName());
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?)");
            
            System.out.println(sql.toString());
           
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(fase.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    fase.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }        
            /**********************************************/
            
            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, fase.getID());
            pst.setString(2, fase.getNome());
            pst.setString(3, fase.getUriImagem());
            pst.setString(4, fase.getNomeClasseHerdade());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(fase.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(5, time );
            
            // seta data atualizacao
            data.setTime(fase.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(6, time );            
            pst.setBoolean(7, fase.getSincronizado());
            /***************************************************************/
            
            pst.executeUpdate();
            connection.commit();
            
            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            fase.setCodigo(PREFIXO+fase.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, fase.getCodigo());
//            pst.setInt(2, fase.getID());
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
    public IEntidade visualizar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
