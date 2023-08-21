package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Caio Gustavo on 26/04/2016.
 */
public class SequenceDAO {

    protected BancoDadosHelper bdHelper;

    protected final String ID = "id";

    protected  final String TABELA = "tb_sequence";

    protected final String NOME_TABELA = "nome_tabela";

    protected  final String SEQUENCE = "sequence";

    protected final int START = 1;

    protected  final int INCREMENT = 2;

    public SequenceDAO(BancoDadosHelper bdHelper) {
        this.bdHelper = bdHelper;
    }

    public String getCreateBancoDadosSQL(int versao) {
        String sql = "create table " + TABELA + " (" +
                ID + " integer primary key autoincrement not null, " +
                NOME_TABELA + " text not null, " +
                SEQUENCE + " integer not null);";

        return sql;
    }

    public Integer getSequence(String nomeTabela){
        Integer id = null;

        // Faz a busca da tabela
        // Para se conectar com o Banco
        SQLiteDatabase database = bdHelper.getMyWritableDatabase();

        // Cria a query que retorna a lista de registros da tarefa informada
        String query = "select * from " + TABELA + " where " + NOME_TABELA + " = '" + nomeTabela+ "'";

        // Executa a consulta no banco
        Cursor cursor = database.rawQuery(query, null);

        // Se o nome da tabela existir pega o valor, soma o incremento, atualiza e retorna
        if(cursor.getCount() > 0){

            if (cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex(SEQUENCE));

                // Incrementa o id
                id += INCREMENT;

                ContentValues valor = new ContentValues();
                valor.put(SEQUENCE, id);

                // Atualiza o valor da sequence
                database.update(TABELA, valor, NOME_TABELA + " = '" + nomeTabela+ "'", null);
//                database.setTransactionSuccessful();
            }

        }
        // Se não existir a tabela cria uma nova e insere o valor do start, retorna o valor
        else {
            ContentValues valor = new ContentValues();
            valor.put(SEQUENCE, START);
            valor.put(NOME_TABELA, nomeTabela);

            // Insere a primeira sequence para a tabela
            database.insert(TABELA, null, valor);
            id = START;
        }

        return id;
    }


}
