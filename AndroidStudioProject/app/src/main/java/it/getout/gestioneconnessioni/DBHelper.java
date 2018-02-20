package it.getout.gestioneconnessioni;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Tronco;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=0;

    //nome del database
    private static final String DATABASE_NAME= "locale";

    //nomi delle tabelle
    private final String TABLE_EDIFICIO="EDIFICIO";
    private final String TABLE_PIANO="PIANO";
    private final String TABLE_AULA="AULA";
    private final String TABLE_TRONCO="TRONCO";
    private final String TABLE_BEACON="BEACON";
    private final String TABLE_MAPPA="MAPPA";

    //colonne tabelle
    private final String COL_NOME="NOME";
    private final String COL_EDIFICIO="EDIFICIO";
    private final String COL_PIANO="PIANO";
    private final String COL_X="X";
    private final String COL_Y="Y";
    private final String COL_ID="ID";
    private final String COL_LARGHEZZA="LARGHEZZA";
    private final String COL_IMMAGINE="IMMAGINE";
    private final String COL_TRONCO="TRONCO";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_edificio = "CREATE TABLE "+TABLE_EDIFICIO+"("+
                COL_NOME+" VARCHAR(20) PRIMARY KEY"+")";
        String create_piano = "CREATE TABLE "+TABLE_PIANO+"("+
                COL_NOME+" VARCHAR(20) PRIMARY KEY,"+
                COL_EDIFICIO+" VARCHAR(20) NOT NULL"+")";
        String create_aula="CREATE TABLE "+TABLE_AULA+"("+
                COL_NOME+" VARCHAR(20) PRIMARY KEY,"+
                COL_X+" NUMBER(4,2) NOT NULL,"+
                COL_Y+" NUMBER(4,2) NOT NULL,"+
                COL_PIANO+" VARCHAR(20) NOT NULL"+")";
        String create_tronco="CREATE TABLE "+TABLE_TRONCO+"("+
                COL_ID+" NUMBER(10) PRIMARY KEY AUTOINCREMENT,"+
                COL_X+" NUMBER(4,2) NOT NULL,"+
                COL_Y+" NUMBER(4,2) NOT NULL,"+
                COL_LARGHEZZA+" NUMBER(4,4) NOT NULL,"+
                COL_PIANO+" VARCHAR(20) NOT NULL"+")";
        String create_beacon="CREATE TABLE "+TABLE_BEACON+"("+
                COL_ID+" NUMBER(10) PRIMARY KEY AUTOINCREMENT,"+
                COL_X+" NUMBER(4,2) NOT NULL,"+
                COL_Y+" NUMBER(4,2) NOT NULL,"+
                COL_TRONCO+" VARCHAR(20) NOT NULL"+")";
        String create_mappa="CREATE TABLE "+TABLE_MAPPA+"("+
                COL_IMMAGINE+" VARCHAR(MAX) NOT NULL,"+
                COL_PIANO+" VARCHAR(20) PRIMARY KEY"+")";

        sqLiteDatabase.execSQL(create_edificio);
        sqLiteDatabase.execSQL(create_piano);
        sqLiteDatabase.execSQL(create_aula);
        sqLiteDatabase.execSQL(create_tronco);
        sqLiteDatabase.execSQL(create_beacon);
        sqLiteDatabase.execSQL(create_mappa);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EDIFICIO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MAPPA);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BEACON);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TRONCO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_AULA);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PIANO);

        onCreate(db);
    }

    public Edificio initEdificioAttuale(String idBeacon) {   //tipo edificio ancora non è dato sapere
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT "+TABLE_EDIFICIO+"."+COL_NOME+" AS NOME_EDIFICIO"+
                " FROM "+TABLE_EDIFICIO+","+TABLE_PIANO+","+TABLE_TRONCO+","+TABLE_BEACON+
                " WHERE "+TABLE_BEACON+"."+COL_TRONCO+"="+TABLE_TRONCO+"."+COL_ID+" AND "+
                TABLE_TRONCO+"."+COL_PIANO+"="+TABLE_PIANO+"."+COL_NOME+" AND "+
                TABLE_PIANO+"."+COL_EDIFICIO+"="+TABLE_EDIFICIO+"."+COL_NOME+
                TABLE_BEACON+"."+COL_ID+"="+idBeacon;
        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nEdificio = res.getString(res.getColumnIndex("NOME_EDIFICIO"));
        res.close();
        db.close();
        return new Edificio(nEdificio);
    }

    public Piano initPianoAttuale(Edificio edificio, String idBeacon){
        String sql = "SELECT "+COL_NOME+" FROM "+TABLE_PIANO+" WHERE "+COL_NOME+"="+idBeacon+" AND "+COL_EDIFICIO+"="+edificio.toString();

        return new Piano(nome);
    }

    public Point getPosizione(String string) {
        return new Point();                     //fatto a caso
    }

    public ArrayList<Piano> initPiani(String string) {
        return new ArrayList();                 //fatto a caso
    }

    public ArrayList<Aula> initAule(String string) {
        return new ArrayList<>();
    }

    public ArrayList<Tronco> initTronchi(String string) {
        return new ArrayList<>();
    }

    public HashMap<String,Beacon> initBeacons(Tronco tronco) {
        return new HashMap<>();
    }

    public String queryMappa(Edificio edificio, Piano piano) {
        return new String();
    }



}

