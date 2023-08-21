package br.com.estimulos.core.dao.impl;

import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estatistica;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.IEntidade;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.ObjetoTela;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Replay;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Tentativa;
import br.com.estimulos.dominio.TipoResultado;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo de Souza Bezerra <gustavo.bezerra@hotmail.com>
 */
@Component("br.com.estimulos.dominio.Replay")
public class ReplayDAO extends AbstractJdbcDAO {

    public ReplayDAO() {
        super(null, null);
    }

    public ReplayDAO(Connection connection, String table, String idTable) {
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
        PreparedStatement pst_tentativa = null;
        boolean primeiroWhere = false;
        List<Object> valores = new ArrayList<>();
        
        Estatistica replay = (Replay) entidade;
        List<IEntidade> resultados = new ArrayList<>();
        List<LocalEstimulo> locais = new ArrayList<>();
        try {
            connection.setAutoCommit(false);

            StringBuilder sql = new StringBuilder();
            StringBuilder sql_tentativa = new StringBuilder();
            sql.append("select f.num_tarefa, f.fase_id, e.estimulo_id, e.nome as nome_estimulo, c.categoria_id, c.nome as nome_categoria, p.altura, p.largura, p.margem_x, p.margem_y, p.tipo_estimulo, i.uri_imagem, i.uri_mascara, t.dt_cadastro\n"
                    + "from tb_tarefa f\n"
                    + "inner join tb_estimulo_posicao ep on (ep.tarefa_id = f.tarefa_id)\n"
                    + "inner join tb_posicao p on (ep.posicao_id = p.posicao_id)\n"
                    + "inner join tb_estimulos e on (ep.estimulo_id = e.estimulo_id)\n"
                    + "inner join tb_imagem i on (e.imagem_id = i.id_imagem)\n"
                    + "inner join tb_tentativa t on (t.id_tarefa = f.tarefa_id)\n"
                    + "inner join tb_tipo_resultado tr on (tr.tipo_resultado_id = t.tipo_resultado)\n"
                    + "inner join tb_movimento m on (m.id_tentativa = t.id_tentativa)\n"
                    + "inner join tb_categoria_estimulos c on (c.categoria_id = e.categoria_estimulo_id)\n");

            sql_tentativa.append("select t.id_tentativa, p.tipo_estimulo, tr.nome, e.estimulo_id, m.id_movimento, m.x, m.y\n"
                    + "from tb_tarefa f\n"
                    + "inner join tb_estimulo_posicao ep on (ep.tarefa_id = f.tarefa_id)\n"
                    + "inner join tb_posicao p on (ep.posicao_id = p.posicao_id)\n"
                    + "inner join tb_estimulos e on (ep.estimulo_id = e.estimulo_id)\n"
                    + "inner join tb_imagem i on (e.imagem_id = i.id_imagem)\n"
                    + "inner join tb_tentativa t on (t.id_tarefa = f.tarefa_id)\n"
                    + "inner join tb_movimento m on (m.id_tentativa = t.id_tentativa)\n"
                    + "inner join tb_tipo_resultado tr on (tr.tipo_resultado_id = t.tipo_resultado)\n"
                    + "where f.fase_id=? and f.num_tarefa=? and not p.tipo_estimulo = 2\n"
                    + "order by t.id_tentativa, id_movimento;");

            if (replay.getInicio() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.dt_cadastro >= ?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.dt_cadastro >= ?");
                }
                Date dataI = new Date(replay.getInicio().getTime());
                valores.add(dataI);
            }
            if (replay.getFim() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.dt_cadastro <= ?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.dt_cadastro <= ?");
                }
                Date dataF = new Date(replay.getFim().getTime());
                valores.add(dataF);
            }
            if (replay.getFase().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where f.fase_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and f.fase_id=?");
                }
                valores.add(replay.getFase().getID());
            }
//            if (replay.getFase().getNiveis().size() != 0) {
//                if (!primeiroWhere) {
//                    sql.append(" where f.fase_id=?");
//                    primeiroWhere = true;
//                } else {
//                    sql.append(" and f.fase_id=?");
//                }
//                valores.add(replay.getFase().getID());
//            }
            if (replay.getTipo().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where t.tipo_resultado=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and t.tipo_resultado=?");
                }
                valores.add(replay.getTipo().getID());
            }
            if (replay.getCategoria().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where c.categoria_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and c.categoria_id=?");
                }
                valores.add(replay.getCategoria().getID());
            }
            if (replay.getEstimulo().getID() != null) {
                if (!primeiroWhere) {
                    sql.append(" where e.estimulo_id=?");
                    primeiroWhere = true;
                } else {
                    sql.append(" and e.estimulo_id=?");
                }
                valores.add(replay.getEstimulo().getID());
            }
            
            sql.append("group by f.num_tarefa, f.fase_id, e.estimulo_id, e.nome, c.categoria_id, c.nome, p.altura, p.largura, p.margem_x, p.margem_y, p.tipo_estimulo, i.uri_imagem, i.uri_mascara, t.dt_cadastro\n"+
                     "order by p.tipo_estimulo;");
            
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
            
            pst_tentativa = connection.prepareStatement(sql_tentativa.toString());

            ResultSet rs = pst.executeQuery();

            Replay r = null;
            Tentativa t = new Tentativa();
            Tarefa tarefa = new Tarefa();
            LocalEstimulo local;
            Posicao posicao;
            Estimulo estimulo;
            Imagem imagem;
            Movimento movimento;
            ObjetoTela obj;
            CategoriaEstimulo categoria;
            TipoResultado resultado = new TipoResultado();
            List<Movimento> movimentos = new ArrayList<>();
            int aux = 0;

            while (rs.next()) {
                estimulo = new Estimulo();
                imagem = new Imagem();
                categoria = new CategoriaEstimulo();
                local = new LocalEstimulo();
                posicao = new Posicao();

                imagem.setUriMascara(rs.getString("uri_mascara"));
                imagem.setUri(rs.getString("uri_imagem"));
                imagem.setAltura(rs.getInt("altura"));
                imagem.setLargura(rs.getInt("largura"));

                categoria.setID(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("nome_categoria"));

                estimulo.setImagem(imagem);
                estimulo.setCategoria(categoria);
                estimulo.setNome(rs.getString("nome_estimulo"));
                estimulo.setID(rs.getInt("estimulo_id"));

                posicao.setMargemX(rs.getFloat("margem_x"));
                posicao.setMargemY(rs.getFloat("margem_y"));
                posicao.setTipo(rs.getInt("tipo_estimulo"));

                local.setEstimulo(estimulo);
                local.setPosicao(posicao);

                locais.add(local);

                if (tarefa.getNumTarefa() == null) {
                    tarefa.setNumTarefa(rs.getInt("num_tarefa"));
                    tarefa.setFaseId(rs.getInt("fase_id"));

                    tarefa.setListLocais(locais);
                    pst_tentativa.setInt(1, tarefa.getFaseId());
                    pst_tentativa.setInt(2, tarefa.getNumTarefa());

                    ResultSet rs_tentativa = pst_tentativa.executeQuery();

                    while (rs_tentativa.next()) {
                        if (t.getID() == null) {
                            t = new Tentativa();
                            resultado = new TipoResultado();
                            obj = new ObjetoTela();
                            movimento = new Movimento();
                            movimentos = new ArrayList<>();

                            resultado.setNome(rs_tentativa.getString("nome"));
                            obj.setCodigo(String.valueOf(rs_tentativa.getInt("estimulo_id")));
                            obj.setTipoObjetoTela(rs.getInt("tipo_estimulo"));

                            t.setID(rs_tentativa.getInt("id_tentativa"));
                            t.setTarefa(tarefa);
                            t.setResultado(resultado);
                            t.setObjetoInicial(obj);

                            movimento.setX(rs_tentativa.getFloat("x"));
                            movimento.setY(rs_tentativa.getFloat("y"));
                            movimento.setCodigo(String.valueOf(rs_tentativa.getInt("tipo_estimulo")));

                            movimentos.add(movimento);
                        } else if (t.getID() == rs_tentativa.getInt("id_tentativa")) {
                            movimento = new Movimento();
                            movimento.setX(rs_tentativa.getFloat("x"));
                            movimento.setY(rs_tentativa.getFloat("y"));
                            movimento.setCodigo(String.valueOf(rs_tentativa.getInt("tipo_estimulo")));

                            movimentos.add(movimento);
                        } else {
                            t.setMovimentos(movimentos);
                            resultados.add(t);
                            t = new Tentativa();
                            resultado = new TipoResultado();
                            obj = new ObjetoTela();
                            movimento = new Movimento();
                            movimentos = new ArrayList<>();

                            resultado.setNome(rs_tentativa.getString("nome"));
                            obj.setCodigo(String.valueOf(rs_tentativa.getInt("estimulo_id")));
                            obj.setTipoObjetoTela(rs.getInt("tipo_estimulo"));

                            t.setID(rs_tentativa.getInt("id_tentativa"));
                            t.setTarefa(tarefa);
                            t.setResultado(resultado);
                            t.setObjetoInicial(obj);

                            movimento.setX(rs_tentativa.getFloat("x"));
                            movimento.setY(rs_tentativa.getFloat("y"));
                            movimento.setCodigo(String.valueOf(rs_tentativa.getInt("tipo_estimulo")));

                            movimentos.add(movimento);
                        }
                    }
                    t.setMovimentos(movimentos);
                    resultados.add(t);
                } else if (tarefa.getNumTarefa() != rs.getInt("num_tarefa") || tarefa.getFaseId() != rs.getInt("fase_id")) {
                    tarefa.setListLocais(locais);
                    pst_tentativa.setInt(1, tarefa.getFaseId());
                    pst_tentativa.setInt(2, tarefa.getNumTarefa());

                    ResultSet rs_tentativa = pst_tentativa.executeQuery();
                    t.setTarefa(tarefa);

                    while (rs_tentativa.next()) {
                        if (t.getID() == null) {
                            t = new Tentativa();
                            resultado = new TipoResultado();
                            obj = new ObjetoTela();
                            movimento = new Movimento();

                            resultado.setNome(rs_tentativa.getString("nome"));
                            obj.setCodigo(String.valueOf(rs_tentativa.getInt("estimulo_id")));
                            obj.setTipoObjetoTela(rs_tentativa.getInt("tipo_estimulo"));

                            t.setID(rs_tentativa.getInt("id_tentativa"));
                            for (int i = 0; i < resultados.size(); i++) {
                                if (resultados.get(i).getID() == t.getID()) {
                                    break;
                                }
                            }
                            t.setResultado(resultado);
                            t.setObjetoInicial(obj);

                            movimento.setX(rs_tentativa.getFloat("x"));
                            movimento.setY(rs_tentativa.getFloat("y"));
                            movimento.setCodigo(String.valueOf(rs_tentativa.getInt("tipo_estimulo")));

                            movimentos.add(movimento);
                        } else if (t.getID() == rs_tentativa.getInt("id_tentativa")) {
                            movimento = new Movimento();
                            movimento.setX(rs_tentativa.getFloat("x"));
                            movimento.setY(rs_tentativa.getFloat("y"));
                            movimento.setCodigo(String.valueOf(rs_tentativa.getInt("tipo_estimulo")));

                            movimentos.add(movimento);
                        } else {
                            t.setMovimentos(movimentos);
                            resultados.add(t);
                        }
                    }
                    resultados.add(t);
                } else {
                    tarefa.setListLocais(locais);
                    t.setTarefa(tarefa);
                }
            }

//            resultados.add(t);
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
