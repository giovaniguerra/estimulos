package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.Nivel;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.NivelTocar;
import br.com.estimulos.dominio.Paciente;

/**
 * Esta classe implementa o DAO para a entidade Jogo
 *
 * Created by Caio Cruz on 21/02/2016.
 */
public class JogoDAO extends DAOImpl<Integer, Jogo> {

    /**
     * Atributo identificador do nome da tabela
     */
    public static final String TABLE_NAME = "tb_jogo";

    /**
     * Construtor desta classe
     * @param bdHelper - Recebe a instancia unica do banco SQLite
     */
    public JogoDAO(BancoDadosHelper bdHelper) {
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
     * Metodo para obter a string de criacao da tabela Jogo
     * @param versao - versao do banco de dados
     * @return - Query sql de criacao da tabela
     */
    @Override
    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABLE_NAME + " (" +
                ID + " integer primary key  not null, " +
                CREATION_DATE + " integer not null, " +
                LAST_UPDATE + " integer not null, " +
                Tabela.TEMA.getName() + " text not null, "  +
                Tabela.SINCRONIZADO.getName() + " integer not null, " +
                Tabela.IMAGEM.getName() + " text not null, "  +
                Tabela.PACIENTE.getName() + " integer not null, " +
                "foreign key(" + Tabela.PACIENTE.getName() + ") references " + PacienteDAO.TABLE_NAME + "(" + ID + ")" +
                "foreign key(" + Tabela.IMAGEM.getName() + ") references " + ImagemDAO.TABLE_NAME + "(" + ID + ")" +
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
        TEMA("tema"),
        PACIENTE("paciente_id"),
        SINCRONIZADO("sincronizado"),
        IMAGEM("imagem_id");

        private String name;

        private Tabela(String name) { this.name=name; }

        public String getName() { return this.name; }

    }

    /**
     * Reescrita do metodo para persistir uma entidade de Jogo
     * necessaria para que a entidade Paciente seja salva junto com jogo
     * @param jogo - entidade a ser persistida
     * @param database - instancia do banco de dados
     * @return - id retornado do banco
     * @throws Exception - caso falte atributos
     */
    @Override
    protected long save(Jogo jogo, SQLiteDatabase database) throws Exception {
        jogo.setSincronizado(false);
        ContentValues retorno =  new ContentValues();

        if(jogo.getID() == null){
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            jogo.setID(dao.getSequence(TABLE_NAME));
        }
        retorno.put(ID, jogo.getID());

        /**
         * Caso o tema esteja null ou em branco
         */
        if(jogo.getTema() == null || jogo.getTema().trim().isEmpty()){
            throw new Exception("jogo sem nome");
        } else {

            retorno.put(Tabela.TEMA.getName(), jogo.getTema());
        }

        /**
         * Caso o paciente esteja nulo - lancar uma exception
         */
        if(jogo.getPaciente() == null){
            throw new Exception("jogo paciente");
        } else {

            /**
             * Instancia o DAO do paciente para fazer a persistencia
             */
            PacienteDAO pacienteDAO = new PacienteDAO(getDatabaseHelper());
            /**
             * Recebe o id do paciente salvo no banco
             */
            long pacienteId = pacienteDAO.save(jogo.getPaciente(), database);

            /**
             * Insere o id do paciente para o relacionamento com o jogo
             */
            retorno.put(Tabela.PACIENTE.getName(), pacienteId);

        }

        if(jogo.getImagem() == null) {
            throw new Exception("jogo sem imgagem");
        } else {

            ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());

            long imagemId = imagemDAO.save(jogo.getImagem(), database);

            retorno.put(Tabela.IMAGEM.getName(), imagemId);
        }

        /**
         * Caso os atributos de data for nulo instancia uma nova data
         */
        if (jogo.getDataCriacao() == null) {
            jogo.setDataCriacao(new Date());
        }
        if (jogo.getUltimaAtualizacao() == null) {
            jogo.setUltimaAtualizacao(new Date());
        }
        retorno.put(Tabela.SINCRONIZADO.getName(), jogo.getSincronizado() ? 1 : 0);
        retorno.put(CREATION_DATE, Long.toString(jogo.getDataCriacao().getTime()));
        retorno.put(LAST_UPDATE, Long.toString(jogo.getUltimaAtualizacao().getTime()));

        /**
         * Executa o insert do Jogo
         */
        long id = database.insert(getTableName(), null, retorno);
        id = jogo.getID();
        /**
         * Instancia o JogoEstimuloDAO para salvar o relacionamento com estimulos
         */
        JogoEstimuloDAO jogoEstimuloDAO = new JogoEstimuloDAO(getDatabaseHelper());
        jogoEstimuloDAO.salvar(jogo);

        JogoResforcadorDAO jogoResforcadorDAO = new JogoResforcadorDAO(getDatabaseHelper());
        jogoResforcadorDAO.salvar(jogo);


        /**
        * Salvar a configuracao dos niveis
         * loop para percorrer as fases
        */
//        for (Fase f: jogo.getFases()) {
//            if(f instanceof FaseTocar){
//                persistirNiveisTocar(f.getNiveis(), id);
//            }
//            else if(f instanceof FaseArrastar){
//                persistirNiveisArrastar(f.getNiveis(), id);
//            }
//        }

        return jogo.getID();
    }

    @Override
    protected void update(Jogo jogo, SQLiteDatabase database) throws Exception {
        jogo.setSincronizado(false);
        /**
         * Se lista de estimulos nao estiver nula
         */
        if(jogo.getEstimulos() != null && !jogo.getEstimulos().isEmpty()){
            // Instancia o DAO auxiliar do relacionamento entre Jogo e Estimulo
            JogoEstimuloDAO jogoEstimuloDAO = new JogoEstimuloDAO(getDatabaseHelper());

            jogoEstimuloDAO.update(jogo, database);
        }
        else if(jogo.getReforcadores() != null && !jogo.getReforcadores().isEmpty()){
            // Instancia o DAO auxiliar do relacionamento entre Jogo e reforçador
            JogoResforcadorDAO jogoResforcadorDAO = new JogoResforcadorDAO(getDatabaseHelper());

            jogoResforcadorDAO.update(jogo, database);
        }
        else {
            ContentValues values =  getValuesFor(jogo);
            /**
             * Caso o paciente esteja nulo - lancar uma exception
             */
            if(jogo.getPaciente() == null){
                throw new Exception("jogo sem paciente");
            } else{

                /**
                 * Instancia o DAO do paciente para fazer a persistencia
                 */
                PacienteDAO pacienteDAO = new PacienteDAO(getDatabaseHelper());
                /**
                 * Recebe o id do paciente salvo no banco
                 */
                pacienteDAO.update(jogo.getPaciente(), database);
            }

            if(jogo.getImagem() == null) {
                throw new Exception("jogo sem imgagem");
            } else {
                ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());

                imagemDAO.update(jogo.getImagem(), database);


            }
            //TODO: continuar atualizando o restante dos objetos

            jogo.setUltimaAtualizacao(new Date());
            values.put(LAST_UPDATE, Long.toString(jogo.getUltimaAtualizacao().getTime()));

            database.update(getTableName(), values, ID + " = " + jogo.getID(), null);
        }
    }

    /**
     * Metodo para salvar todos os niveis arrastar do jogo
     * @param niveis
     */
    private void persistirNiveisArrastar(List<Nivel> niveis, long id) throws Exception{
        /**
         * Instacia o DAO correspondente ao nivel de arrastar
         */
        NivelArrastarDAO nivelArrastarDAO = new NivelArrastarDAO(getDatabaseHelper());

        NivelArrastar nA;
        /**
         * Percorre o array e salva os niveis
         */
        for (Nivel n: niveis) {
            // converte o nivel em nivelarrastar
            nA = (NivelArrastar) n;
//            nA.setIdJogo((int) id);
            nivelArrastarDAO.salvar(nA);
        }
    }

    /**
     * Metodo para salvar todos os niveis tocar do jogo
     * @param niveis
     */
    private void persistirNiveisTocar(List<Nivel> niveis, long id) throws Exception{
        /**
         * Instacia o DAO correspondente ao nivel de tocar
         */
        NivelTocarDAO nivelTocarDAO = new NivelTocarDAO(getDatabaseHelper());

        NivelTocar nT;
        /**
         * Percorre o array e salva os niveis
         */
        for (Nivel n: niveis) {
            // converte o nivel em nivelarrastar
            nT = (NivelTocar) n;
//            nT.setIdJogo((int) id);
            nivelTocarDAO.salvar(nT);
        }
    }

    /**
     * Metodo para mapear quais atributos serao persistidos em quais colunas
     * dentro da tabela
     * @param jogo - entidade a ser persistida
     * @return - contentvalues - mapeamento das colunas da tabela
     * @throws Exception - caso ocorra erro sera lancada
     */
    @Override
    public ContentValues getValuesFor(Jogo jogo) throws Exception {
        ContentValues retorno = new ContentValues();
        retorno.put(Tabela.TEMA.getName(), jogo.getTema());
        retorno.put(Tabela.PACIENTE.getName(), jogo.getPaciente().getID());
        retorno.put(Tabela.IMAGEM.getName(), jogo.getImagem().getID());
        retorno.put(Tabela.SINCRONIZADO.getName(), jogo.getSincronizado() ? 1 : 0);
        return retorno;
    }

    /**
     * Este metodo tem por objeto retornar um Jogo com todos atributos e relacionamentos
     * preenchidos no objeto
     * @param idJogo - Identificar de um Jogo
     * @return Jogo - objeto
     * @throws Exception - caso ocorra algum erro
     */
    public Jogo retornarJogoCompleto(Integer idJogo) throws Exception{
        // Conecta com o banco
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();

        // Executa a consulta de todos os campos para o id do jogo informado
        Cursor cursor = database.rawQuery("select * from " + getTableName() + " where id = " + idJogo, null);

        // Instancia o objeto de retorno
        Jogo jogo = null;

        // Se retornou algo do banco
        if (cursor.moveToFirst()) {
            // Posiciona o cursor no registro
            if (!cursor.isAfterLast()) {

                // Recebe a instancia do objeto Preenchido
                // com os parametros definidos no metodo fill desta classe
                jogo = fill(cursor);

                // Instancia o DAO auxiliar do relacionamento entre Jogo e Estimulo
                JogoEstimuloDAO jogoEstimuloDAO = new JogoEstimuloDAO(getDatabaseHelper());

                // Preenche o objeto jogo com os estimulos vinculados a ele
                jogo.setEstimulos(jogoEstimuloDAO.listEstimulos(idJogo));

                // Instancia o DAO auxiliar do relacionamento entre Jogo e Reforcador
                JogoResforcadorDAO jogoResforcadorDAO = new JogoResforcadorDAO(getDatabaseHelper());

                // Preenche o objeto jogo com os reforcadores vinculados a ele
                jogo.setReforcadores(jogoResforcadorDAO.listReforcadores(idJogo));

                // Move o cursor para o proximo registro
                cursor.moveToNext();
            }
        }

/*
        // Instancia a lista de fases para preencher o Jogo
        List<Fase> fases = new ArrayList<>();

        // Instancia as fases especificas para jogo
        FaseTocar faseTocar = new FaseTocar();
        FaseArrastar faseArrastar = new FaseArrastar();

        // Instancia o DAO responsavel pela configuracao da fase Arrastar
        NivelArrastarDAO nivelArrastarDAO = new NivelArrastarDAO(getDatabaseHelper());

        // Realiza o filtro para buscar os niveis da fase de arrastar
        Map<String, String> filtroArrastar = new HashMap<>();
        filtroArrastar.put(NivelArrastarDAO.Tabela.JOGO.getName(), "" + cursor.getInt(cursor.getColumnIndex(ID)));

        // Recebe a lista de niveis retornada
        List<NivelArrastar> niveisArrastar = nivelArrastarDAO.pesquisa(filtroArrastar);

        // Loop para converter os niveis em Superclasse
        List<Nivel> nAs = new ArrayList<>();
        for (NivelArrastar nA: niveisArrastar) {
            nAs.add(nA);
        }

        // Adiciona os niveis a fase arrastar
        faseArrastar.setNiveis(nAs);

        // Instancia o DAO responsavel pelos niveis da fase de Tocar
        NivelTocarDAO nivelTocarDAO = new NivelTocarDAO(getDatabaseHelper());

        // Cria o filtro para buscar somente os niveis que pertencem a este jogo
        Map<String, String> filtroTocar = new HashMap<>();
        filtroTocar.put(NivelTocarDAO.Tabela.JOGO.getName(), "" + cursor.getInt(cursor.getColumnIndex(ID)));

        // Recebe o resultado da consulta
        List<NivelTocar> niveisTocar = nivelTocarDAO.pesquisa(filtroTocar);

        // Converte os niveis em Superclasse
        List<Nivel> nTs = new ArrayList<>();
        for (NivelTocar nT: niveisTocar) {
            nTs.add(nT);
        }

        // Adiciona os niveis a fase de Tocar
        faseTocar.setNiveis(nTs);

        // Adiciona a fase a lista
        fases.add(faseTocar);
        fases.add(faseArrastar);

        // Adiciona a lista ao jogo
        jogo.setFases(fases);
*/

        // Fecha o cursor
        cursor.close();

        // retorna o objeto
        return jogo;
    }

    /**
     * Reescrita do metodo que faz a atribuicao dos valores vindo do banco
     * em uma nova instancia da entidade
     * @param cursor - objeto do banco de dados para recuperacao dos dados
     * @return - nova instancia da entidade
     * @throws Exception - caso ocorra erro
     */
    @Override
    public Jogo fill(Cursor cursor) throws Exception {
        Jogo jogo = new Jogo();
        jogo.setTema(cursor.getString(cursor.getColumnIndex(Tabela.TEMA.getName())));

        // recupera o paciente
        PacienteDAO pacienteDAO = new PacienteDAO(getDatabaseHelper());
        Paciente paciente = pacienteDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.PACIENTE.getName())));

        jogo.setPaciente(paciente);

        // recupera a imagem
        ImagemDAO imagemDAO = new ImagemDAO(getDatabaseHelper());
        Imagem imagem = imagemDAO.visualizar(cursor.getInt(cursor.getColumnIndex(Tabela.IMAGEM.getName())));

        jogo.setImg(imagem);
        jogo.setSincronizado(cursor.getInt(cursor.getColumnIndex(Tabela.SINCRONIZADO.getName())) == 1);

        jogo.setID(cursor.getInt(cursor.getColumnIndex(ID)));
        jogo.setDataCriacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATION_DATE)))));
        jogo.setUltimaAtualizacao(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)))));
        return jogo;
    }
}
