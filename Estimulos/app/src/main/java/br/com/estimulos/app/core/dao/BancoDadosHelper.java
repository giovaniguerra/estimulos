package br.com.estimulos.app.core.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Caio Gustavo on 11/02/2016.
 */
public class BancoDadosHelper extends SQLiteOpenHelper{

   
    private final Context context;
  
    private static final String DATABASE_NAME = "estimulos.db";
  
    public static final int DATABASE_VERSION = 1;
  
    private static BancoDadosHelper singleton;
 
    private static SQLiteDatabase sqLiteDatabase;

    private BancoDadosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    public static BancoDadosHelper getInstance(Context context){
        if(singleton == null)
            singleton = new BancoDadosHelper(context);
        return singleton;
    }

    public SQLiteDatabase getMyWritableDatabase() {
        if ((sqLiteDatabase == null) || (!sqLiteDatabase.isOpen())) {
            sqLiteDatabase = this.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    @Override
    public void close() {
        super.close();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.beginTransaction();

        database.execSQL(new EstimuloDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new NivelTocarDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new NivelArrastarDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new JogoEstimuloDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new TerapeutaDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new JogoDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new PacienteDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new AudioDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new ImagemDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new CategoriaEstimuloDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new FaseDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new ReforcadorDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new PosicaoNivelDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new PosicaoDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new EstimuloPosicaoDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new TarefaDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new TentativaDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new MovimentoDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new TipoResultadoDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new ObjetoTelaDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new NivelTarefaDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new SequenceDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));
        database.execSQL(new JogoResforcadorDAO(this).getCreateBancoDadosSQL(DATABASE_VERSION));

        
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        database.beginTransaction();

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public Context getContext() {
        return context;
    }

}
