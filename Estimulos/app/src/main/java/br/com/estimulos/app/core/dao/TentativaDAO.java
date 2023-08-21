package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.estimulos.dominio.Movimento;
import br.com.estimulos.dominio.Tentativa;

/**
 * Created by Caio Gustavo on 09/04/2016.
 */
public class TentativaDAO  extends DAOImpl<Integer, Tentativa> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_tentativa";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public TentativaDAO(BancoDadosHelper bdHelper) {
        super(bdHelper);
    }

    /**
     * Metodo para obter o nome da tabela
     * @return - String nome da tabela
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Metodo para obter a string de criacao da tabela Audio
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.TAREFA.getName() + " integer not null, "  +
                Tabela.OBJ_INICIO.getName() + " integer, " +
                Tabela.OBJ_FIM.getName() + " integer, " +
                Tabela.TIPO_RESULTADO.getName() + " integer not null, " +
                "foreign key(" + Tabela.TAREFA.getName() + ") references " + TarefaDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.TIPO_RESULTADO.getName() + ") references " + TipoResultadoDAO.TABLE_NAME + "(" + ID + ")" +
                ");";


        return sql;
    }

    /**
     * Metodo para obter a string de atualizacao desta tabela
     * @param vsAntiga - versao antiga da tabela
     * @param vsNova - nova versao da tabela
     * @return - Query de atualizao
     */
    @Override
    public String getUpdateBancoDadosSQL(int vsAntiga, int vsNova) {
        return "";
    }

    /**
     * Enumeracao responsavel por retornar o respectivo nome das colunas da tabela
     * tornando a criacao das querys mais facil
     */
    public enum Tabela {
        TAREFA("tarefa_id"),
        OBJ_INICIO("obj_inicio_id"),
        OBJ_FIM("obj_fim_id"),
        TIPO_RESULTADO("tipo_resultado_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Tentativa tentativa, SQLiteDatabase database) throws Exception {
        /**
         * Prepara o contentvalues para persistir o estimulo
         */

        if(tentativa.getDataCriacao() == null){
            tentativa.setDataCriacao(new Date());
            tentativa.setUltimaAtualizacao(new Date());
        }

        ContentValues retorno =  new ContentValues();

        if(tentativa.getID() == null){
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            tentativa.setID(dao.getSequence(TABLE_NAME));
        }

//        if(tentativa.getResultado() != null){
//            TipoResultadoDAO tipoResultadoDAO = new TipoResultadoDAO(getDatabaseHelper());
//            retorno.put(Tabela.TIPO_RESULTADO.getName(),tipoResultadoDAO.salvar(tentativa.getResultado()));
//        }

        if(tentativa.getObjetoInicial() != null){
            ObjetoTelaDAO objetoTelaDAO = new ObjetoTelaDAO(getDatabaseHelper());
            retorno.put(Tabela.OBJ_INICIO.getName(), objetoTelaDAO.salvar(tentativa.getObjetoInicial()));
        }

        if(tentativa.getObjetoFinal() != null){
            ObjetoTelaDAO objetoTelaDAO = new ObjetoTelaDAO(getDatabaseHelper());
            retorno.put(Tabela.OBJ_FIM.getName(), objetoTelaDAO.salvar(tentativa.getObjetoFinal()));
        }

        retorno.put(ID, tentativa.getID());

        retorno.put(Tabela.TAREFA.getName(), tentativa.getTarefa().getID());

        retorno.put(Tabela.TIPO_RESULTADO.getName(), tentativa.getResultado().getID());
        retorno.put(Tabela.OBJ_INICIO.getName(), tentativa.getObjetoInicial().getID());
        retorno.put(Tabela.OBJ_FIM.getName(), tentativa.getObjetoFinal().getID());

        retorno.put(CREATION_DATE, Long.toString(tentativa.getDataCriacao().getTime()));
        retorno.put(LAST_UPDATE, Long.toString(tentativa.getUltimaAtualizacao().getTime()));

        long id = database.insert(getTableName(), null, retorno);
        id = tentativa.getID();

        /**
         * Vai persistir a lista de posicoes?
         */
        if(tentativa.getMovimentos() != null){
            MovimentoDAO movimentoDAO = new MovimentoDAO(getDatabaseHelper());

            for(Movimento m: tentativa.getMovimentos()){
                m.setTentativa(tentativa);
                movimentoDAO.salvar(m);
            }
        }

        return tentativa.getID();
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param tentativa - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Tentativa tentativa) throws Exception {
        return null;
    }



    /**
     * Reescrita do metodo que faz a atribuicao dos valores vindo do banco
     * em uma nova instancia da entidade
     * @param cursor - objeto do banco de dados para recuperacao dos dados
     * @return - nova instancia da entidade
     * @throws Exception - caso ocorra erro
     */
    @Override
    public Tentativa fill(Cursor cursor) throws Exception {
        Tentativa tentativa = new Tentativa();
        tentativa.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        tentativa.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        tentativa.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return tentativa;
    }
}
