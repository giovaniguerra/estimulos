package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.Posicao;

/**
 * Esta classe implementa o DAO para a entidade NivelArrastar
 *
 * Created by Caio Cruz on 21/02/2016.
 */
public class NivelArrastarDAO extends DAOImpl<Integer, NivelArrastar> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_nivel_arrastar";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public NivelArrastarDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao desta classe
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.NUM_NIVEL.getName() + " integer not null, "  +
                Tabela.NUM_TAREFAS.getName() + " integer not null, " +
                Tabela.URI_IMAGEM.getName() + " text not null, " +
                Tabela.QTD_ESTIMULO.getName() + " integer not null, " +
                Tabela.LIMITE_ERRO.getName() + " integer not null, " +
                Tabela.LIMITE_SOM_ERRO.getName() + " integer not null, " +
                Tabela.LIMITE_INSTRUCOES_TTS.getName() + " integer not null, " +
                Tabela.RAND_POSICAO.getName() + " integer not null, " +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.JOGO.getName() + " integer not null, " +
                Tabela.FASE.getName() + " integer not null, " +
                "foreign key(" + Tabela.JOGO.getName() + ") references " + JogoDAO.TABLE_NAME + "(" + ID + ")" +
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
        NUM_NIVEL("num_nivel"),
        NUM_TAREFAS("num_tarefas"),
        URI_IMAGEM("uri_imagem"),
        FASE("fase_id"),
        QTD_ESTIMULO("qtd_estimulos"),
        JOGO("jogo_id"),
        LIMITE_ERRO("limite_erro"),
        LIMITE_SOM_ERRO("limite_som_erro"),
        SINCRONIZADO("sincronizado"),
        LIMITE_INSTRUCOES_TTS("limite_tts"),
        RAND_POSICAO("rand_posicao");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    @Override
    protected long save(NivelArrastar nivel, SQLiteDatabase database) throws Exception {
        nivel.setSincronizado(false);
        ContentValues retorno =  getValuesFor(nivel);

        if(nivel.getID() == null){
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            nivel.setID(dao.getSequence(TABLE_NAME));
        }
        retorno.put(ID, nivel.getID());

        nivel.setDataCriacao(new Date());
        nivel.setUltimaAtualizacao(new Date());

        retorno.put(CREATION_DATE, Long.toString(nivel.getDataCriacao().getTime()));
        retorno.put(LAST_UPDATE, Long.toString(nivel.getUltimaAtualizacao().getTime()));

        /**
         * Executa o insert do Jogo
         */
        long id = database.insert(getTableName(), null, retorno);
        id = nivel.getID();

        PosicaoDAO posicaoDAO = new PosicaoDAO(getDatabaseHelper());
        PosicaoNivelDAO posicaoNivelDAO = new PosicaoNivelDAO(getDatabaseHelper());

        if(nivel.getPosicoes() != null){

            for(Posicao p: nivel.getPosicoes()){
                posicaoDAO.salvar(p);

            }
            posicaoNivelDAO.salvar(nivel);

        }

        /**
         * Instancia o JogoEstimuloDAO para salvar o relacionamento com estimulos
         */

        return nivel.getID();
    }


    @Override
    protected void update(NivelArrastar nivel, SQLiteDatabase database) throws Exception {
        nivel.setSincronizado(false);
        ContentValues values = getValuesFor(nivel);

        nivel.setUltimaAtualizacao(new Date());
        values.put(LAST_UPDATE, Long.toString(nivel.getUltimaAtualizacao().getTime()));

        PosicaoDAO posicaoDAO = new PosicaoDAO(getDatabaseHelper());
        PosicaoNivelDAO posicaoNivelDAO = new PosicaoNivelDAO(getDatabaseHelper());
        // TODO: Chamar posicaoDAO para excluir todas posições desse nivel
        // TODO: Idem para nivelPosicaoDAO
        for(Posicao p: nivel.getPosicoes()){
            p.setID(null);
            posicaoDAO.save(p, database);
        }
        posicaoNivelDAO.update(nivel, database);

        database.update(getTableName(), values, ID + " = " + nivel.getID(), null);

    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param nivel - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(NivelArrastar nivel) throws Exception {
        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NUM_NIVEL.getName(), nivel.getNumero());
        retorno.put(Tabela.NUM_TAREFAS.getName(), nivel.getNumTarefas());
        retorno.put(Tabela.URI_IMAGEM.getName(), nivel.getUriImagem());
        retorno.put(Tabela.FASE.getName(), nivel.getFase().getID());
        retorno.put(Tabela.QTD_ESTIMULO.getName(), nivel.getQtdeEstimulos());
        retorno.put(Tabela.JOGO.getName(), nivel.getJogo().getID());
        retorno.put(Tabela.LIMITE_ERRO.getName(), nivel.getLimiteErros());
        retorno.put(Tabela.LIMITE_SOM_ERRO.getName(), nivel.getLimiteSomDeErro());
        retorno.put(Tabela.LIMITE_INSTRUCOES_TTS.getName(), nivel.getLimiteInstrucoesTTS());
        retorno.put(Tabela.RAND_POSICAO.getName(), nivel.isRandPosicao() ? 1 : 0);
        retorno.put(Tabela.SINCRONIZADO.getName(), nivel.getSincronizado() ? 1 : 0);

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
    public NivelArrastar fill(Cursor cursor) throws Exception {
        NivelArrastar arrastar = new NivelArrastar();
        arrastar.setNumero(cursor.getInt(cursor.getColumnIndex(Tabela.NUM_NIVEL.getName())));
        arrastar.setNumTarefas(cursor.getInt(cursor.getColumnIndex(Tabela.NUM_TAREFAS.getName())));
        arrastar.setUriImagem(cursor.getString(cursor.getColumnIndex(Tabela.URI_IMAGEM.getName())));

        arrastar.setJogo(new Jogo());
        arrastar.getJogo().setID(cursor.getInt(cursor.getColumnIndex(Tabela.JOGO.getName())));

        arrastar.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        arrastar.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        arrastar.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        arrastar.setFase(new Fase());
        arrastar.getFase().setID(cursor.getInt(cursor.getColumnIndex(Tabela.FASE.getName())));
        arrastar.setQtdeEstimulos(cursor.getInt(cursor.getColumnIndex(Tabela.QTD_ESTIMULO.getName())));
        arrastar.setLimiteErros(cursor.getInt(cursor.getColumnIndex(Tabela.LIMITE_ERRO.getName())));
        arrastar.setLimiteSomDeErro(cursor.getInt(cursor.getColumnIndex(Tabela.LIMITE_SOM_ERRO.getName())));
        arrastar.setLimiteInstrucoesTTS(cursor.getInt(cursor.getColumnIndex(Tabela.LIMITE_INSTRUCOES_TTS.getName())));
        arrastar.setRandPosicao(cursor.getInt(cursor.getColumnIndex(Tabela.RAND_POSICAO.getName())) == 1);
        arrastar.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        PosicaoNivelDAO posicaoNivelDAO = new PosicaoNivelDAO(getDatabaseHelper());
        arrastar.setPosicoes(posicaoNivelDAO.listPosicao(arrastar));

        return arrastar;
    }

}
