package it.getout.GestioneConnessioni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.GestionePosizione.Aula;
import it.getout.GestionePosizione.Beacon;
import it.getout.GestionePosizione.Edificio;
import it.getout.GestionePosizione.Piano;
import it.getout.GestionePosizione.Tronco;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private int DATABASE_VERSION;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public Edificio initEdificioAttuale(String string) {   //tipo edificio ancora non è dato sapere
            return new Edificio();
    }

    public Piano initPianoAttuale(Edificio edificio){
            return new Piano();
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
