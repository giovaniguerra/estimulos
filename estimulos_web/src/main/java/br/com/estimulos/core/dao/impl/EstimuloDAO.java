/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
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
@Component("br.com.estimulos.dominio.Estimulo")
public class EstimuloDAO extends AbstractJdbcDAO {

    private static final String TABELA = "tb_estimulos";
    private static final String ID = "estimulo_id";
    private static final String CODIGO = "estimulo_cod";
    private static final String SEQUENCE = "tb_estimulos_seq";

    public EstimuloDAO() {
        super(TABELA, ID);
    }

    public EstimuloDAO(String table, String idTable) {
        super(TABELA, ID);
    }

    public EstimuloDAO(Connection cx) {
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
        CATEGORIA_ESTIMULO("categoria_estimulo_id"),
        IMAGEM("imagem_id"),
        AUDIO("audio_id"),
        COD("codigo"),
        GENERO("genero");

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
        ctrlTransaction = false;

        PreparedStatement pst = null;
        Estimulo estimulo = (Estimulo) entidade;
        CategoriaEstimulo categoria = estimulo.getCategoria();
        Imagem imagem = estimulo.getImagem();
        Audio audio = estimulo.getAudio();

        if (categoria.getID() == null) {
            CategoriaEstimuloDAO categoriaDAO = new CategoriaEstimuloDAO(connection);
            categoriaDAO.ctrlTransaction = false;

            categoriaDAO.salvar(categoria);
        }

        ImagemDAO imgDAO = new ImagemDAO(connection);
        imgDAO.ctrlTransaction = false;
        imgDAO.connection.setAutoCommit(false);
        imgDAO.salvar(imagem);

        AudioDAO audioDAO = new AudioDAO(connection);
        audioDAO.ctrlTransaction = false;
        imgDAO.connection.setAutoCommit(false);
        audioDAO.salvar(audio);

        try {
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.INSERT.getName());
            sql.append(TABELA);
            sql.append(AbstractJdbcDAO.SQL.ABRE_COLUNA.getName());
            sql.append(ID);
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.NOME.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.CATEGORIA_ESTIMULO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.AUDIO.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.IMAGEM.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.COD.getName());
            sql.append(AbstractJdbcDAO.SQL.VIRGULA.getName());
            sql.append(Tabela.GENERO.getName());

            sql.append(COLUNAS_ENTIDADE_DOMINIO);
            sql.append(AbstractJdbcDAO.SQL.FECHA_COLUNA.getName());
            sql.append(AbstractJdbcDAO.SQL.VALUES.getName());
            sql.append("(?,?,?,?,?,?,?,?,?,?)");

            System.out.println(sql);

            /**
             * Codigo que verifica se o ID esta setado caso nao esteja, busca id
             * da sequence
             */
            if (estimulo.getID() == null) {
                pst = connection.prepareStatement(getSelectSequence());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    estimulo.setID(rs.getInt(PARAMETRO_SEQ));
                }
            }

            pst = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, estimulo.getID());
            pst.setString(2, estimulo.getNome());
            pst.setInt(3, estimulo.getCategoria().getID());
            pst.setInt(4, estimulo.getAudio().getID());
            pst.setInt(5, estimulo.getImagem().getID());
            pst.setString(6, estimulo.getCodigo());
            pst.setString(7, estimulo.getGenero());

            /**
             * SETA dados da entidade dominio
             */
            // seta data de criacao
            Calendar data = Calendar.getInstance();
            data.setTime(estimulo.getDataCriacao());
            Timestamp time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(8, time);

            // seta data atualizacao
            data.setTime(estimulo.getUltimaAtualizacao());
            time = new Timestamp(data.getTimeInMillis());
            pst.setTimestamp(9, time);
            pst.setBoolean(10, estimulo.getSincronizado());
            /**
             * ************************************************************
             */

            pst.executeUpdate();
            connection.commit();

            /**
             * SALVA O CODIGO PARA ESTA ENTIDADE
             */
//            estimulo.setCodigo(PREFIXO+estimulo.getID());            
//            pst = connection.prepareStatement(getUpdateCodigoTabela());
//            pst.setString(1, estimulo.getCodigo());
//            pst.setInt(2, estimulo.getID());
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
        PreparedStatement pst = null;
        Estimulo e = (Estimulo) entidade;

    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {

        Estimulo estimulo = (Estimulo) entidade;
        if (estimulo.getCategoria() == null) {
            openConnection();
            PreparedStatement pst = null;

            List<IEntidade> resultados = new ArrayList<>();

            try {
                connection.setAutoCommit(false);

                StringBuilder sql = new StringBuilder();
                sql.append("select e.estimulo_id, i.id_imagem, c.categoria_id, e.nome as nome_estimulo, i.uri_imagem, c.nome as nome_categoria\n"
                        + "from tb_estimulos e\n"
                        + "inner join tb_imagem i on (e.imagem_id = i.id_imagem)\n"
                        + "inner join tb_categoria_estimulos c on (c.categoria_id = e.categoria_estimulo_id);");
                pst = connection.prepareStatement(sql.toString());
                ResultSet rs = pst.executeQuery();
                Estimulo e;
                Imagem i;
                CategoriaEstimulo c;

                while (rs.next()) {
                    e = new Estimulo();
                    i = new Imagem();
                    c = new CategoriaEstimulo();

                    e.setID(rs.getInt("estimulo_id"));
                    i.setID(rs.getInt("id_imagem"));
                    c.setID(rs.getInt("categoria_id"));
                    e.setNome(rs.getString("nome_estimulo"));
                    i.setUri(rs.getString("uri_imagem"));
                    c.setNome(rs.getString("nome_categoria"));

                    e.setImagem(i);
                    e.setCategoria(c);

                    resultados.add(e);
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

        List<IEntidade> entidades = new ArrayList<IEntidade>();
        if (connection == null) {
            openConnection();
        }

        try {

            PreparedStatement ps;
            StringBuilder sql = new StringBuilder();

            sql.append(AbstractJdbcDAO.SQL.SELECT.getName());
            sql.append(TABELA);

            if (estimulo.getCategoria() != null) {
                sql.append(SQL.WHERE.getName());
                sql.append(Tabela.CATEGORIA_ESTIMULO.getName());
                sql.append(AbstractJdbcDAO.SQL.IGUAL.getName());
                sql.append(AbstractJdbcDAO.SQL.INTERROGACAO.getName());
            }

            ps = connection.prepareStatement(sql.toString());
            if (estimulo.getCategoria() != null) {
                ps.setInt(1, estimulo.getCategoria().getID());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                estimulo = new Estimulo();
                estimulo.setID(rs.getInt(ID));
                estimulo.setNome(rs.getString(Tabela.NOME.getName()));
                estimulo.setImagem(new Imagem());
                estimulo.getImagem().setUri(rs.getString(Tabela.IMAGEM.getName()));
                estimulo.setGenero(rs.getString(Tabela.GENERO.getName()));

                entidades.add(estimulo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entidades;

    }

    @Override
    public IEntidade visualizar(IEntidade entidade) throws SQLException {

        Estimulo estimulo = (Estimulo) entidade;
        openConnection();
        PreparedStatement pst = null;
        Estimulo e = null;
        
        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("select * from tb_estimulos where estimulo_id=?;");
            pst = connection.prepareStatement(sql.toString());
            pst.setInt(1, estimulo.getID());
            ResultSet rs = pst.executeQuery();

            Imagem i;
            CategoriaEstimulo c;
            while (rs.next()) {
                e = new Estimulo();
                i = new Imagem();
                c = new CategoriaEstimulo();

                e.setID(rs.getInt("estimulo_id"));
                i.setID(rs.getInt("imagem_id"));
                c.setID(rs.getInt("categoria_estimulo_id"));
                e.setNome(rs.getString("nome"));                

                e.setImagem(i);
                e.setCategoria(c);
            }
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
        return e;
    }
}
