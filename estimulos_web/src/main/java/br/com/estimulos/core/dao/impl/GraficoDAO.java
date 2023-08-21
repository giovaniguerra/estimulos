package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estatistica;
import br.com.estimulos.dominio.Grafico;
import br.com.estimulos.dominio.IEntidade;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
@Component("br.com.estimulos.dominio.Grafico")
public class GraficoDAO extends AbstractJdbcDAO {

    public GraficoDAO() {
        super(null, null);
    }

    public GraficoDAO(Connection connection, String table, String idTable) {
        super(connection, table, idTable);
    }

    @Override
    protected String getNomeTabela() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getNomeCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getNomeSequence() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void salvar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar(IEntidade entidade) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IEntidade> consultar(IEntidade entidade) throws SQLException {
        openConnection();
        PreparedStatement pst = null;

        boolean primeiroWhere = false;

        Estatistica grafico = (Grafico) entidade;
        List<IEntidade> resultados = new ArrayList<>();
        /**
         * Lista para adicionar os valores na query
         */
        List<Object> valores = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            sql.append("select t.dt_cadastro, c.nome as \"categoria\", count (1)\n"
                    + "from tb_tentativa t\n"
                    + "inner join tb_tipo_resultado r on(t.tipo_resultado = r.tipo_resultado_id)\n"
                    + "inner join tb_tarefa f on (t.id_tarefa = f.tarefa_id)\n"
                    + "inner join tb_fase tf on (f.fase_id = tf.fase_id)\n"
                    + "inner join tb_objeto_tela o on (t.obj_inicial = o.objeto_tela_id)\n"
                    + "inner join tb_estimulos e on ((select cast(o.codigo as int) from tb_objeto_tela o where t.obj_inicial = o.objeto_tela_id) = e.estimulo_id)\n"
                    + "inner join tb_categoria_estimulos c on (e.categoria_estimulo_id = c.categoria_id)\n"
                    + "inner join tb_movimento m on (m.id_tentativa = t.id_tentativa)\n");

            if (grafico.getInicio() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.dt_cadastro >= ?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.dt_cadastro >= ?");
                }
                Date dataI = new Date(grafico.getInicio().getTime());
                valores.add(dataI);
            }
            if (grafico.getFim() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.dt_cadastro <= ?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.dt_cadastro <= ?");
                }
                Date dataF = new Date(grafico.getFim().getTime());
                valores.add(dataF);
            }
            if (grafico.getFase().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where f.fase_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and f.fase_id=?");
                }
                valores.add(grafico.getFase().getID());
            }
            if (grafico.getTipo().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.tipo_resultado=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.tipo_resultado=?");
                }
                valores.add(grafico.getTipo().getID());
            }
            if (grafico.getCategoria().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where c.categoria_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and c.categoria_id=?");
                }
                valores.add(grafico.getCategoria().getID());
            }
            if (grafico.getEstimulo().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where e.estimulo_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and e.estimulo_id=?");
                }
                valores.add(grafico.getEstimulo().getID());
            }
            sql.append("group by t.dt_cadastro, c.nome\n"
                    + "order by t.dt_cadastro;");
            
            pst = connection.prepareStatement(sql.toString());
            if (!valores.isEmpty()) {
                for (int i = 0; i < valores.size(); i++) {
                    if (valores.get(i) instanceof Date) {
                        pst.setDate(i + 1, (Date) valores.get(i));
                    } else {
                        pst.setInt(i + 1, (int) valores.get(i));
                    }
                }
            }

            ResultSet rs = pst.executeQuery();

            Grafico gr = null;

            while (rs.next()) {
                gr = new Grafico();
                gr.setDataCriacao(rs.getDate("dt_cadastro"));
                gr.setData(gr.getDataCriacao().getTime());
                gr.setCategoria(new CategoriaEstimulo());
                gr.getCategoria().setNome(rs.getString("categoria"));

                gr.setAcertos(rs.getInt("count"));

                resultados.add(gr);
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
