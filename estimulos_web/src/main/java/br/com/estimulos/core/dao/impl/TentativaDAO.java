/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import static br.com.estimulos.core.dao.impl.AbstractJdbcDAO.COLUNAS_ENTIDADE_DOMINIO;
import static br.com.estimulos.core.dao.impl.AbstractJdbcDAO.PARAMETRO_SEQ;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Tentativa;
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
@Component("br.com.estimulos.dominio.Tentativa")
public class TentativaDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_tentativa";
    private static final String ID = "id_tentativa";
    private static final String CODIGO = "estimulo_cod";
    private static final String SEQUENCE = "tb_estimulos_seq";

    public TentativaDAO() {
        super(TABELA, ID);
    }

    public TentativaDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public TentativaDAO(Connection cx) {
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

    @Override
    public void salvar(IEntidade entidade) throws SQLException {
        openConnection();
        PreparedStatement pst = null;
        Tentativa tentativa = (Tentativa) entidade;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO tb_cliente(nome, cpf, email, telefone, senha, dt_nasc, dt_cadastro");
        sql.append(") VALUES (?,?,?,?,?,?,?);");
    }

    @Override
    public void alterar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
