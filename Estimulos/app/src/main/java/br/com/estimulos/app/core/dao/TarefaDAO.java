package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.estimulos.dominio.Tarefa;

/**
 * Created by Caio Gustavo on 06/04/2016.
 */
public class TarefaDAO extends DAOImpl<Integer, Tarefa> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_tarefa";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public TarefaDAO(BancoDadosHelper bdHelper) {
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
                Tabela.NUM_TAREFA.getName() + " integer, "  +
                Tabela.FASE.getName() + " integer not null, "  +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.COMPLETADA.getName() + " integer, " +
                "foreign key(" + Tabela.FASE.getName() + ") references " + FaseDAO.TABLE_NAME + "(" + ID + ")" +
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
        NUM_TAREFA("num_tarefa"),
        COMPLETADA("completa"),
        SINCRONIZADO("sincronizado"),
        FASE("fase_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Tarefa tarefa, SQLiteDatabase database) throws Exception {
        /**
         * Prepara o contentvalues para persistir o estimulo
         */
        tarefa.setSincronizado(false);
        if(tarefa.getDataCriacao() == null){
            tarefa.setDataCriacao(new Date());
            tarefa.setUltimaAtualizacao(new Date());
        }

        if(tarefa.getUltimaAtualizacao() == null){
            tarefa.setDataCriacao(new Date());
            tarefa.setUltimaAtualizacao(new Date());
        }

        ContentValues retorno =  getValuesFor(tarefa);

        if(tarefa.getID() == null){
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            tarefa.setID(dao.getSequence(TABLE_NAME));
        }
        retorno.put(ID, tarefa.getID());


        retorno.put(CREATION_DATE, Long.toString(tarefa.getDataCriacao().getTime()));
        retorno.put(LAST_UPDATE, Long.toString(tarefa.getUltimaAtualizacao().getTime()));
        long id = database.insert(getTableName(), null, retorno);
        id = tarefa.getID();

        /**
         * Vai persistir a lista de posicoes?
         */
        if(tarefa.getListLocais() != null){
            EstimuloPosicaoDAO estimuloPosicaoDAO = new EstimuloPosicaoDAO(getDatabaseHelper());
            estimuloPosicaoDAO.salvar(tarefa);
        }


        return tarefa.getID();
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param tarefa - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Tarefa tarefa) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NUM_TAREFA.getName(), tarefa.getNumTarefa());
        if(tarefa.isCompletada())
            retorno.put(Tabela.COMPLETADA.getName(), 1);
        else
            retorno.put(Tabela.COMPLETADA.getName(), 0);

        retorno.put(Tabela.FASE.getName(), tarefa.getFaseId());
        retorno.put(Tabela.SINCRONIZADO.getName(), tarefa.getSincronizado() ? 1 : 0);

        return retorno;
    }



    /**
     * Reescrita do metodo que faz a atribuicao dos valores vindo do banco
     * em uma nova instancia da entidade
     * @param cursor - objeto do banco de dados para recuperacao dos dados
     * @return - nova instancia da entidade
     * @throws Exception - caso ocorra erro
     */
    @Override
    public Tarefa fill(Cursor cursor) throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setNumTarefa(cursor.getInt(cursor.getColumnIndex(Tabela.NUM_TAREFA.getName())));
        if(cursor.getInt(cursor.getColumnIndex(Tabela.COMPLETADA.getName())) == 1)
            tarefa.setCompletada(true);
        else
            tarefa.setCompletada(false);

        tarefa.setFaseId(cursor.getInt(cursor.getColumnIndex(Tabela.FASE.getName())));

        tarefa.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        tarefa.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        tarefa.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));

        EstimuloPosicaoDAO estimuloPosicaoDAO = new EstimuloPosicaoDAO(getDatabaseHelper());
        tarefa.setListLocais(estimuloPosicaoDAO.listLocaisEstimulo(tarefa.getID()));
        tarefa.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);

        return tarefa;
    }
}


