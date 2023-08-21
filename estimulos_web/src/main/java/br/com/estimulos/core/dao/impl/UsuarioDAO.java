/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.IEntidade;
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
@Component("br.com.estimulos.dominio.Usuario")
public class UsuarioDAO extends AbstractJdbcDAO{

    private static final String TABELA = "tb_usuario";
    private static final String ID = "usuario_id";
    private static final String CODIGO = "usuario_cod";
    private static final String SEQUENCE = "tb_usuario_seq";

    public UsuarioDAO(){
        super(TABELA, ID);
    }
    
    public UsuarioDAO(String table, String idTable) {
        super(TABELA, ID);
    }
    
    public UsuarioDAO(Connection cx){
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
        LOGIN("login"),
        SENHA("senha");

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
        Usuario usuario = (Usuario) entidade;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.LOGIN.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.SENHA.getName());
            
            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?)");
            
            System.out.println(sql.toString());
            
            /**
             * Codigo que verifica se o ID esta setado
             * caso nao esteja, busca id da sequence
             */
            if(usuario.getID() == null){
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();
            
                if(rs.next()){
                    usuario.setID(rs.getInt(PARAMETRO_SEQ));
                }                
            }       
            /**********************************************/
           
            
            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, usuario.getID());
            pst.setString(2, usuario.getLogin());
            pst.setString(3, usuario.getSenha());
            
            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(usuario.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());            
            pst.setTimestamp(4, time );
            
            // seta data atualizacao
            data.setTime(usuario.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(5, time );            
            pst.setBoolean(6, usuario.getSincronizado());
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
        
        if(connection == null){
            openConnection();
        }
        PreparedStatement ps = null;
        
        
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
            Usuario user;
            while(rs.next()){
                user = new Usuario();
                user.setID(rs.getInt(ID));
                user.setLogin(rs.getString(Tabela.LOGIN.getName()));
                user.setSenha(rs.getString(Tabela.SENHA.getName()));
                
                entidades.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        
        return entidades;
    }   
    
    @Override
    public IEntidade visualizar(IEntidade entidade) throws SQLException {
        int contador = 1;
        if(connection == null){
            openConnection();
        }
        Usuario user = (Usuario) entidade;
        
        try {
            
            PreparedStatement ps;
            
            StringBuilder sql = new StringBuilder();
            
            sql.append(AbstractJdbcDAO.SQL.SELECT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.WHERE.getName());
            
            if(user.getID() != null){
                sql.append(ID);
                sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
                sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());                
            }
            if(user.getLogin() != null){
                sql.append(Tabela.LOGIN.getName());
                sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
                sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());  
            }
            if(user.getSenha() != null){
                sql.append(AbstractJdbcDAO.SQL.AND.getName());
                sql.append(Tabela.SENHA.getName());
                sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
                sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());  
            }
            
            ps = connection.prepareStatement(sql.toString());
            
            if(user.getID() != null){
                ps.setInt(contador++, user.getID());   
            }
            if(user.getLogin() != null){
                ps.setString(contador++, user.getLogin());  
            }
            if(user.getSenha() != null){
                ps.setString(contador++, user.getSenha());
            }
            
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                user.setID(rs.getInt(ID));
                user.setLogin(rs.getString(Tabela.LOGIN.getName()));
                user.setSenha(rs.getString(Tabela.SENHA.getName()));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        
        return user;
    }
}
