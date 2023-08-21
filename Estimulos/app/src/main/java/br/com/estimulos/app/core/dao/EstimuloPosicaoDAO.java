package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Tarefa;

/**
 * Created by Caio Gustavo on 07/04/2016.
 */
public class EstimuloPosicaoDAO extends DAOImpl<Integer, Tarefa> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_estimulo_posicao";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public EstimuloPosicaoDAO(BancoDadosHelper bdHelper) {
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
                ID + " integer primary key autoincrement not null, " +
                Tabela.ESTIMULO.getName() + " integer not null, "  +
                Tabela.POSICAO.getName() + " integer not null, " +
                Tabela.TAREFA.getName() + " integer not null, " +
                "foreign key(" + Tabela.ESTIMULO.getName() + ") references " + EstimuloDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.POSICAO.getName() + ") references " + PosicaoDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.TAREFA.getName() + ") references " + TarefaDAO.TABLE_NAME + "(" + ID + ")" +
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
        ESTIMULO("estimulo_id"),
        POSICAO("posicao_id"),
        TAREFA("tarefa_id");


        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(Tarefa tarefa, SQLiteDatabase database) throws Exception {

        Integer idTarefa = tarefa.getID();
        ContentValues retorno =  new ContentValues();

        PosicaoDAO posicaoDAO = new PosicaoDAO(getDatabaseHelper());
        for(LocalEstimulo l: tarefa.getListLocais()) {
            retorno.put(Tabela.TAREFA.getName(), idTarefa);

            if(l.getPosicao().getID() != null){
                retorno.put(Tabela.POSICAO.getName(), l.getPosicao().getID());
            }
            else{
                retorno.put(Tabela.POSICAO.getName(),
                posicaoDAO.salvar(l.getPosicao()) );
            }
            retorno.put(Tabela.ESTIMULO.getName(), l.getEstimulo().getID());
            database.insert(getTableName(), null, retorno);
        }
        return 0;
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
    public Tarefa fill(Cursor cursor) throws Exception {
        return null;
    }

    public List<LocalEstimulo> listLocaisEstimulo(Integer idTarefa) throws Exception {
        List<LocalEstimulo> locais = new ArrayList<>();

        // Instancia o DAO do estimulo para fazer a busca
        EstimuloDAO estimuloDAO = new EstimuloDAO(getDatabaseHelper());
        PosicaoDAO posicaoDAO = new PosicaoDAO(getDatabaseHelper());

        // Para se conectar com o Banco
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        // Cria a query que retorna a lista de registros da tarefa informada
        String query = "select * from " + getTableName() + " where " + Tabela.TAREFA.getName() + " = " + idTarefa;

        // Executa a consulta no banco
        Cursor cursor = database.rawQuery(query, null);

        // Instancia a lista de resultados da consulta
        List<Integer> result = new ArrayList<Integer>(cursor.getCount());

        // Para armazenar o id de consulta do estimulo
        Integer idEstimulo;
        Estimulo estimulo;

        // Para armazenar o id de consulta da posicao
        Integer idPosicao;
        Posicao posicao;

        LocalEstimulo localEstimulo;
        if (cursor.moveToFirst()) {
            // Realiza o loop para percorrer os registros consultados
            while (!cursor.isAfterLast()) {

                // Recupera apenas o id do estimulo do registro da vez
                idEstimulo = cursor.getInt(cursor.getColumnIndex(Tabela.ESTIMULO.getName()));

                // Recupera apenas o id da posicao do registro da vez
                idPosicao = cursor.getInt(cursor.getColumnIndex(Tabela.POSICAO.getName()));

                estimulo = estimuloDAO.visualizar(idEstimulo);

                posicao = posicaoDAO.visualizar(idPosicao);

                localEstimulo = new LocalEstimulo();
                localEstimulo.setEstimulo(estimulo);
                localEstimulo.setPosicao(posicao);

                // Adiciona o estimulo e local
                locais.add(localEstimulo);

                // Move o cursor para o proximo registro
                cursor.moveToNext();
            }
        }
        // Fecha o cursor do banco
        cursor.close();

        return locais;
    }
}





