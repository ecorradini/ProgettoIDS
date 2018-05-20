package it.getout.gestioneconnessioni;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;

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
                DBStrings.COL_NOME+" VARCHAR(20) PRIMARY KEY"+")";
        String create_piano = "CREATE TABLE "+DBStrings.TABLE_PIANO+"("+
                DBStrings.COL_NOME+" VARCHAR(20) PRIMARY KEY,"+
                DBStrings.COL_EDIFICIO+" VARCHAR(20) NOT NULL"+")";
        String create_aula="CREATE TABLE "+DBStrings.TABLE_AULA+"("+
                DBStrings.COL_NOME+" VARCHAR(20) PRIMARY KEY,"+
                DBStrings.COL_X+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_Y+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_PIANO+" VARCHAR(20) NOT NULL"+")";
        String create_tronco="CREATE TABLE "+DBStrings.TABLE_TRONCO+"("+
                DBStrings.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBStrings.COL_X+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_Y+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_XF+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_YF+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_LARGHEZZA+" FLOAT(8,4) NOT NULL,"+
                DBStrings.COL_PIANO+" VARCHAR(20) NOT NULL"+")";
        String create_beacon="CREATE TABLE "+DBStrings.TABLE_BEACON+"("+
                DBStrings.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBStrings.COL_X+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_Y+" FLOAT(6,2) NOT NULL,"+
                DBStrings.COL_TRONCO+" VARCHAR(20) NOT NULL"+")";
        String create_mappa="CREATE TABLE "+DBStrings.TABLE_MAPPA+"("+
                DBStrings.COL_IMMAGINE+" LONGTEXT NOT NULL,"+
                DBStrings.COL_PIANO+" VARCHAR(20) PRIMARY KEY"+")";

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

