package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Imagem;

/**
 * Esta classe implementa o DAO para a entidade Estimulo
 *
 * Created by Caio Cruz on 11/02/2016.
 */
public class EstimuloDAO extends DAOImpl<Integer, Estimulo> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_estimulos";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public EstimuloDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela Estimulo
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.NOME.getName() + " text not null, " +
                Tabela.GENERO.getName() + " text not null, " +
                Tabela.CATEGORIA_ESTIMULO.getName() + " integer not null, " +
                Tabela.IMAGEM.getName() + " integer not null, " +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.AUDIO.getName() + " integer, " +
                "foreign key(" + Tabela.CATEGORIA_ESTIMULO.getName() + ") references " + CategoriaEstimuloDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.IMAGEM.getName() + ") references " + ImagemDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.AUDIO.getName() + ") references " + AudioDAO.TABLE_NAME + "(" + ID + ")" +
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
        NOME("nome"),
        CATEGORIA_ESTIMULO("categoria_estimulo_id"),
        IMAGEM("imagem_id"),
        AUDIO("audio_id"),
        SINCRONIZADO("sincronizado"),
        GENERO("genero");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Reescrita do metodo save para a persistencia da entidade Estimulo
     * necessario por conta dos relacionamentos com Categoria e MidiaEstimulo
     * @param estimulo - entidade a ser persistida
     * @param database - instancia do banco dados
     * @return - id da entidade persistida
     * @throws Exception - caso ocorra erro
     */
    @Override
    protected long save(Estimulo estimulo, SQLiteDatabase database) throws Exception {
        /**
         * Prepara o contentvalues para persistir o estimulo
         */
        estimulo.setSincronizado(false);
        ContentValues retorno =  getValuesFor(estimulo);
        retorno.put(Tabela.NOME.getName(), estimulo.getNome());

        if(estimulo.getID() == null){
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            estimulo.setID(dao.getSequence(TABLE_NAME));
        }
        retorno.put(ID, estimulo.getID());


        if(estimulo.getAudio() != null){
            /**
             * DAO para a persistencia da antidade Audio
             */
            AudioDAO audioDAO = new AudioDAO(getDatabaseHelper());

            /**
             * Persiste a entidade audio e obtem o id
             */
            long audioId = audioDAO.save(estimulo.getAudio(), database);

            /**
             * Para o relacionametento entre as entidade
             */
            retorno.put(Tabela.AUDIO.getName(), audioId);
        }



        /**
         * Caso imagem venha nulo
         * lanca uma exception
         */
        if(estimulo.getImagem() == null){
            throw new Exception("Sem imagem");
        }
        else {

            /**
             * DAO para a persistencia da entidade Imagem
             */
            ImagemDAO imgDAO = new ImagemDAO(getDatabaseHelper());
            long imgId = imgDAO.save(estimulo.getImagem(), database);

            /**
             * para o relacionameto entre as entidades
             */
            retorno.put(Tabela.IMAGEM.getName(), imgId);
        }


        /**
         * Lanca exception caso a categoria esteja nula
         */
        if(estimulo.getCategoria() == null){
            throw new Exception("sem categoria");
        }
        else {

            /**
             * Caso o id da categoria esteja nulo criar uma nova categoria
             */
            if (estimulo.getCategoria().getID() == null) {
                CategoriaEstimuloDAO categoriaDAO = new CategoriaEstimuloDAO(getDatabaseHelper());
                long categoriaId = categoriaDAO.save(estimulo.getCategoria(), database);
                retorno.put(Tabela.CATEGORIA_ESTIMULO.getName(), categoriaId);
            }
            /**
             * A categoria ja existe no banco de dados
             */
            else {
                retorno.put(Tabela.CATEGORIA_ESTIMULO.getName(), estimulo.getCategoria().getID());
            }
        }

        if (estimulo.getDataCriacao() == null) {
            estimulo.setDataCriacao(new Date());
        }
        if (estimulo.getUltimaAtualizacao() == null) {
            estimulo.setUltimaAtualizacao(new Date());
        }
        retorno.put(CREATION_DATE, Long.toString(estimulo.getDataCriacao().getTime()));
        retorno.put(LAST_UPDATE, Long.toString(estimulo.getUltimaAtualizacao().getTime()));
        long id = database.insert(getTableName(), null, retorno);

        return estimulo.getID();
    }

    @Override
    protected void update(Estimulo object, SQLiteDatabase database) throws Exception {
        object.setSincronizado(false);
        ContentValues values =  getValuesFor(object);

        if(object.getImagem() == null) {
            throw new Exception("jogo sem imgagem");
        } else {
            ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());

            imagemDAO.update(object.getImagem(), database);
        }

        if(object.getCategoria() == null){
            throw new Exception("Sem categoria");
        } else {
            CategoriaEstimuloDAO categoriaEstimuloDAO = new CategoriaEstimuloDAO(getDatabaseHelper());

            categoriaEstimuloDAO.update(object.getCategoria(), database);
        }

        object.setUltimaAtualizacao(new Date());
        values.put(LAST_UPDATE, Long.toString(object.getUltimaAtualizacao().getTime()));

        database.update(getTableName(), values, ID + " = " + object.getID(), null);
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param estimulo - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Estimulo estimulo) throws Exception {

        ContentValues retorno =  new ContentValues();
        retorno.put(Tabela.NOME.getName(), estimulo.getNome());

        if(estimulo.getCategoria() != null){
            retorno.put(Tabela.CATEGORIA_ESTIMULO.getName(), estimulo.getCategoria().getID());
        }
        if(estimulo.getAudio() != null){
            retorno.put(Tabela.AUDIO.getName(), estimulo.getAudio().getID());
        }
        if(estimulo.getImagem() != null){
            retorno.put(Tabela.IMAGEM.getName(), estimulo.getImagem().getID());
        }

        if(estimulo.getGenero() != null)
            retorno.put(Tabela.GENERO.getName(), estimulo.getGenero());

        retorno.put(Tabela.SINCRONIZADO.getName(), estimulo.getSincronizado() ? 1 : 0);
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
    public Estimulo fill(Cursor cursor) throws Exception {

        Estimulo estimulo = new Estimulo();
        estimulo.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));

        // busca pela categoria pertencente
        CategoriaEstimuloDAO categoriaDAO = new CategoriaEstimuloDAO(getDatabaseHelper());
        CategoriaEstimulo categoria = categoriaDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.CATEGORIA_ESTIMULO.getName())));

        // atribui a categoria
        estimulo.setCategoria(categoria);

        // busca pela imagem correspondente
        ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());
        Imagem imagem = imagemDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.IMAGEM.getName())));

        // atribui a imagem
        estimulo.setImagem(imagem);

        // busca pelo audio
        AudioDAO audioDAO = new AudioDAO(getDatabaseHelper());
        Audio audio = audioDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.AUDIO.getName())));

        // atribui o audio
        estimulo.setAudio(audio);
        estimulo.setGenero(cursor.getString(cursor.getColumnIndex(Tabela.GENERO.getName())));
        estimulo.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        estimulo.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        estimulo.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        estimulo.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);
        return estimulo;

    }

    public List<Estimulo> consultarEstimuloInsignificante(Integer idCategoria) throws Exception{
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        String query = "select * from " +
                getTableName() +
                " where " +
                Tabela.CATEGORIA_ESTIMULO.getName() +
                " <> " +
                idCategoria;

        Cursor cursor = database.rawQuery(query, null);
        Estimulo estimulo;
        List<Estimulo> estimulos = new ArrayList<>(cursor.getCount());
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                estimulo = new Estimulo();
                estimulo.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                estimulo.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
                estimulo.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
                estimulo.setNome(cursor.getString(cursor.getColumnIndex(Tabela.NOME.getName())));
                // busca pela imagem correspondente
                ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());
                Imagem imagem = imagemDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.IMAGEM.getName())));

                // atribui a imagem
                estimulo.setImagem(imagem);

                cursor.moveToNext();
                estimulos.add(estimulo);
            }
        }

        cursor.close();
        return estimulos;
    }
}
