package it.getout.gestioneconnessioni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Alessandro on 01/02/2018.
 */

public class ConnessioneDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=3;

    //nome del database
    private static final String DATABASE_NAME= "locale";


    public ConnessioneDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_edificio = "CREATE TABLE "+DBStrings.TABLE_EDIFICIO+"("+
                DBStrings.COL_NOME+" TEXT PRIMARY KEY"+")";
        String create_piano = "CREATE TABLE "+DBStrings.TABLE_PIANO+"("+
                DBStrings.COL_NOME+" TEXT PRIMARY KEY,"+
                DBStrings.COL_EDIFICIO+" TEXT NOT NULL"+")";
        String create_aula="CREATE TABLE "+DBStrings.TABLE_AULA+"("+
                DBStrings.COL_NOME+" TEXT PRIMARY KEY,"+
                DBStrings.COL_X+" REAL NOT NULL,"+
                DBStrings.COL_Y+" REAL) NOT NULL,"+
                DBStrings.COL_PIANO+" TEXT NOT NULL,"+
                DBStrings.COL_ENTRATA+" TEXT"+")";
        String create_tronco="CREATE TABLE "+DBStrings.TABLE_TRONCO+"("+
                DBStrings.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBStrings.COL_X+" REAL NOT NULL,"+
                DBStrings.COL_Y+" REAL NOT NULL,"+
                DBStrings.COL_XF+" REAL NOT NULL,"+
                DBStrings.COL_YF+" REAL NOT NULL,"+
                DBStrings.COL_LARGHEZZA+" REAL NOT NULL,"+
                DBStrings.COL_PIANO+" TEXT NOT NULL,"+
                DBStrings.COL_LUNGHEZZA+" REAL NOT NULL"+")";
        String create_beacon="CREATE TABLE "+DBStrings.TABLE_BEACON+"("+
                DBStrings.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBStrings.COL_X+" REAL NOT NULL,"+
                DBStrings.COL_Y+" REAL NOT NULL,"+
                DBStrings.COL_TRONCO+" TEXT NOT NULL,"+
                DBStrings.COL_UTENTI+"INTEGER"+
                DBStrings.COL_USCITA+"INTEGER"+")";
        String create_mappa="CREATE TABLE "+DBStrings.TABLE_MAPPA+"("+
                DBStrings.COL_PIANO+" TEXT PRIMARY KEY," +
                DBStrings.COL_IMMAGINE+" TEXT NOT NULL"+")";

        sqLiteDatabase.execSQL(create_edificio);
        sqLiteDatabase.execSQL(create_piano);
        sqLiteDatabase.execSQL(create_aula);
        sqLiteDatabase.execSQL(create_tronco);
        sqLiteDatabase.execSQL(create_beacon);
        sqLiteDatabase.execSQL(create_mappa);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_EDIFICIO);
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_MAPPA);
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_BEACON);
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_TRONCO);
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_AULA);
        db.execSQL("DROP TABLE IF EXISTS "+DBStrings.TABLE_PIANO);

        onCreate(db);
    }



}

