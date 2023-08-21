package br.com.estimulos.app.core.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.estimulos.app.interfaces.InterfaceDAO;
import br.com.estimulos.dominio.EntidadeDominio;

/**
 * Esta classe implementa o padrao DAO - Date Access Object
 * <p/>
 * Created by Caio Cruz on 11/02/2016.
 */
public abstract class DAOImpl<K, T extends EntidadeDominio> implements InterfaceDAO<K, T> {


    protected BancoDadosHelper bdHelper;

    protected final String ID = "id";

    protected final String CREATION_DATE = "creation_date";

    protected final String LAST_UPDATE = "last_update";

    public DAOImpl(BancoDadosHelper bdHelper) {
        this.bdHelper = bdHelper;
    }

    public abstract String getTableName();

    private SQLiteDatabase getDatabase() {
        return this.bdHelper.getMyWritableDatabase();
    }

    public BancoDadosHelper getDatabaseHelper() {
        return bdHelper;
    }


    @Override
    public long salvar(T object) throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        database.beginTransaction();
        long id = -1;
        try {
            id = save(object, database);
            database.setTransactionSuccessful();
        } catch (Exception error) {
            Log.e("TG", "There was a error while inserting the object of class " + object.getClass().getName() + ":\n" + error.getMessage());

        } finally {
            database.endTransaction();
        }
        return id;
    }

    protected long save(T object, SQLiteDatabase database) throws Exception {
        object.setSincronizado(false);
        ContentValues values = getValuesFor(object);
        if (object.getDataCriacao() == null) {
            object.setDataCriacao(new Date());
        }
        if (object.getUltimaAtualizacao() == null) {
            object.setUltimaAtualizacao(new Date());
        }

        if(object.getID() == null) {
            SequenceDAO dao = new SequenceDAO(getDatabaseHelper());
            object.setID(dao.getSequence(getTableName()));
        }

        values.put(ID, object.getID());
        values.put(CREATION_DATE, Long.toString(object.getDataCriacao().getTime()));
        values.put(LAST_UPDATE, Long.toString(object.getUltimaAtualizacao().getTime()));
        long id = database.insert(getTableName(), null, values);

        // retorna o id da sequence
        return object.getID();
    }

    @Override
    public void alterar(T object) throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        database.beginTransaction();
        try {
            update(object, database);
        } catch (Exception error) {
             Log.e("TG", "There was a error while inserting place styling attributes: " + error.getMessage());
        } finally {
            database.endTransaction();
        }
    }

    protected void update(T object, SQLiteDatabase database) throws Exception {
        // Try to update the data.
        object.setSincronizado(false);
        ContentValues values = getValuesFor(object);
        object.setUltimaAtualizacao(new Date());
        values.put(LAST_UPDATE, Long.toString(object.getUltimaAtualizacao().getTime()));

        database.update(getTableName(), values, ID + " = " + object.getID(), null);
        database.setTransactionSuccessful();
    }

    @Override
    public int remover(T object) throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        database.beginTransaction();
        int returnValue = 0;
        try {
            returnValue = remove(object, database);
            database.setTransactionSuccessful();
        } catch (Exception error) {
            //Log.e(Util.LOG_TAG, "There was a error while removing the object of class " + object.getClass().getName() + ":\n" + error.getMessage());

        } finally {
            database.endTransaction();
        }
        return returnValue;
    }


    protected int remove(T object, SQLiteDatabase database) throws Exception {
        return database.delete(getTableName(), ID + " = " + object.getID(), null);
    }

    @Override
    public T visualizar(K key) throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + getTableName() + " where id = " + key, null);
        T result = null;
        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                T object = fill(cursor);
                result = object;
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public List<T> consultarTodos() throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + getTableName(), null);
        List<T> result = new ArrayList<T>(cursor.getCount());
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                T object = fill(cursor);
                result.add(object);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public List<T> pesquisa(Map<String, String> filters) throws Exception {
        SQLiteDatabase database = getDatabaseHelper().getMyWritableDatabase();
        String query = "select * from " + getTableName();
        if (!filters.isEmpty()) {
            query += " where ";
        }
        Iterator<Map.Entry<String, String>> filterIterator = filters.entrySet().iterator();
        if (filterIterator.hasNext()) {
            Map.Entry<String, String> filter = filterIterator.next();
            query += filter.getKey() + " = " + filter.getValue();
        }
        while (filterIterator.hasNext()) {
            Map.Entry<String, String> filter = filterIterator.next();
            query += " and " + filter.getKey() + " = " + filter.getValue();
        }
        Cursor cursor = database.rawQuery(query, null);
        List<T> result = new ArrayList<T>(cursor.getCount());
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                T object = fill(cursor);
                result.add(object);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    @Override
    public int count() throws Exception {
        Cursor cursor = getDatabaseHelper().getReadableDatabase().rawQuery("select count(*) from " + getTableName(), null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    @Override
    public String getCreateBancoDadosSQL(int versao) {
        return null;
    }

    @Override
    public String getUpdateBancoDadosSQL(int vsAntiga, int vsNova) {
        return null;
    }

    public abstract ContentValues getValuesFor(T object) throws Exception;

    public abstract T fill(Cursor cursor) throws Exception;

}
