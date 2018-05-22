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

public class DBStrings {



    //nomi delle tabelle
    public static final String TABLE_EDIFICIO="EDIFICIO";
    public static final String TABLE_PIANO="PIANO";
    public static final String TABLE_AULA="AULA";
    public static final String TABLE_TRONCO="TRONCO";
    public static final String TABLE_BEACON="BEACON";
    public static final String TABLE_MAPPA="MAPPA";

    //colonne tabelle
    public static final String COL_NOME="NOME";
    public static final String COL_EDIFICIO="EDIFICIO";
    public static final String COL_PIANO="PIANO";
    public static final String COL_X="X";
    public static final String COL_Y="Y";
    public static final String COL_XF="XF"; // x del beacon di fine del tronco
    public static final String COL_YF="YF"; // y del beacon di fine del tronco
    public static final String COL_ID="ID";
    public static final String COL_LARGHEZZA="LARGHEZZA";
    public static final String COL_IMMAGINE="IMMAGINE";
    public static final String COL_TRONCO="TRONCO";
    public static final String COL_ENTRATA="ENTRATA";
    public static final String COL_LUNGHEZZA="LUNGHEZZA";
    public static final String COL_USCITA="USCITA";

}
