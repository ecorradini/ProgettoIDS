package it.getout.gestioneconnessioni;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.PosizioneUtente;
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
    private final String COL_XF="XF"; // x del beacon di fine del tronco
    private final String COL_YF="YF"; // y del beacon di fine del tronco
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


    //passatogli l'id del beacon mi restituisce il piano  COL_CAZZOOOOOOOOOO
    public Piano initPianoAttuale(Edificio edificio, String idBeacon){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT "+TABLE_PIANO+"."+COL_NOME+" AS NOME_PIANO"+
                " FROM "+TABLE_BEACON+","+TABLE_TRONCO+","+TABLE_PIANO+","+TABLE_EDIFICIO+
                " WHERE "+TABLE_BEACON+"."+COL_ID+"="+idBeacon+" AND "+
                TABLE_BEACON+"."+COL_TRONCO+"="+TABLE_TRONCO+"."+COL_ID+" AND "+
                TABLE_PIANO+"."+COL_NOME+"="+TABLE_TRONCO+"."+COL_PIANO+" AND "+TABLE_PIANO+
                "."+COL_EDIFICIO+" = "+TABLE_EDIFICIO+"."+COL_NOME;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nPiano = res.getString(res.getColumnIndex("NOME_PIANO"));
        res.close();
        db.close();

        Piano attuale = null;
        int index = 0;
        do {
            if(PosizioneUtente.getEdificioAttuale().getPiani().get(index).toString().equals(nPiano)) {
                attuale = PosizioneUtente.getEdificioAttuale().getPiani().get(index);
            }
            index++;
        } while(attuale==null && index < PosizioneUtente.getEdificioAttuale().getPiani().size());


        return attuale;
    }

    //passandogli l'id del beacon mi restituisce le coordinate x e y
    public PointF getPosizione(String idBeacon) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT "+COL_X+","+COL_Y+
                " FROM "+TABLE_BEACON+" WHERE "+TABLE_BEACON+"."+COL_ID+"="+idBeacon;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        PointF pos = new PointF(res.getFloat(res.getColumnIndex(COL_X)),res.getFloat(res.getColumnIndex(COL_Y)));
        res.close();
        db.close();

        return pos;
    }


    // dato un nome dell'edificio restituisce la lista dei suoi piani
    public ArrayList<Piano> initPiani(String nomeEdificio) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT "+COL_NOME+ " FROM "+TABLE_PIANO+ " WHERE "+COL_EDIFICIO+"="+nomeEdificio;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Piano> listaPiani = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaPiani.add(new Piano(res.getString(res.getColumnIndex(COL_NOME))));
        }
        res.close();
        db.close();

        return listaPiani;

    }

    // dato un nome del piano restituisce tutte le sue aule
    public ArrayList<Aula> initAule(String nomePiano) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT "+COL_NOME+","+COL_X+","+COL_Y+ " FROM "+TABLE_AULA+ " WHERE "+COL_PIANO+"="+nomePiano;

        Piano attuale = null;
        int index = 0;
        do {
            if(PosizioneUtente.getEdificioAttuale().getPiani().get(index).toString().equals(nomePiano)) {
                attuale = PosizioneUtente.getEdificioAttuale().getPiani().get(index);
            }
            index++;
        } while(attuale==null && index < PosizioneUtente.getEdificioAttuale().getPiani().size());

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Aula> listaAule = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaAule.add(new Aula(res.getString(res.getColumnIndex(COL_NOME)),
                    new PointF(res.getFloat(res.getColumnIndex(COL_X)),res.getFloat(res.getColumnIndex(COL_Y))),attuale));
        }
        res.close();
        db.close();

        return listaAule;
    }


    public ArrayList<Tronco> initTronchi(String nomePiano) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT "+COL_LARGHEZZA+","+COL_X+","+COL_Y+","+COL_XF+","+COL_YF+
                     " FROM "+TABLE_TRONCO+ " WHERE "+COL_PIANO+"="+nomePiano;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Tronco> listaTronchi = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaTronchi.add(new Tronco(
                    new PointF(res.getFloat(res.getColumnIndex(COL_X)), res.getFloat(res.getColumnIndex(COL_Y))),
                    new PointF(res.getFloat(res.getColumnIndex(COL_XF)), res.getFloat(res.getColumnIndex(COL_YF))),
                    res.getDouble(res.getColumnIndex(COL_LARGHEZZA))));
        }
        res.close();
        db.close();

        return listaTronchi;


    }

    //id_tronco string string dato un tronco voglio tutti i beacon del tronco
    //non capisco Strng dentro hasmap visto che sarebbe la stessa stringa contenuta nell'oggetto Beacon

    public HashMap<String,Beacon> initBeacons(Tronco troncoAttuale) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT "+TABLE_BEACON+"."+COL_ID+" AS ID_BEACON"+
                ","+TABLE_BEACON+"."+COL_X+" AS X_BEACON"+
                ","+TABLE_BEACON+"."+COL_Y+" AS Y_BEACON"+
                " FROM "+TABLE_TRONCO+","+TABLE_BEACON+","+TABLE_PIANO+","+TABLE_EDIFICIO+
                " WHERE "+TABLE_BEACON+"."+COL_TRONCO+"="+troncoAttuale.toString();

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        HashMap<String, Beacon> listaBeacon = new HashMap<String, Beacon>();

        //Beacon(String id, PointF posizione)
        while(res.moveToNext()) {
            listaBeacon.put(        //metodo put per riempire un'hashmap
                    new String(res.getString(res.getColumnIndex(COL_ID))),
                    new Beacon(
                        new String(res.getString(res.getColumnIndex("ID_BEACON"))),
                        new PointF(res.getFloat(res.getColumnIndex("X_BEACON")), res.getFloat(res.getColumnIndex("Y_BEACON")))));
        }
        res.close();
        db.close();

        return listaBeacon;

    }

    public String queryMappa(Edificio edificio, Piano piano) {
        return new String();
    }



}

