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

public class Database extends GestoreDati {

    private ConnessioneDatabase connessione;

    public Database(Context context) {
        connessione = new ConnessioneDatabase(context);
    }

    @Override
    public Edificio richiediEdificioAttuale(String idBeacon) {   //tipo edificio ancora non è dato sapere
        SQLiteDatabase db = connessione.getReadableDatabase();
        String sql = "SELECT "+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME+" AS NOME_EDIFICIO"+
                " FROM "+DBStrings.TABLE_EDIFICIO+","+DBStrings.TABLE_PIANO+","+DBStrings.TABLE_TRONCO+","+DBStrings.TABLE_BEACON+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+" AND "+
                DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+"="+DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+" AND "+
                DBStrings.TABLE_PIANO+"."+DBStrings.COL_EDIFICIO+"="+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME+
                DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+"="+idBeacon;
        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nEdificio = res.getString(res.getColumnIndex("NOME_EDIFICIO"));
        res.close();
        db.close();
        return new Edificio(nEdificio);
    }

    //passatogli l'id del beacon mi restituisce il piano
    @Override
    public Piano richiediPianoAttuale(String idBeacon){
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+" AS NOME_PIANO"+
                " FROM "+DBStrings.TABLE_BEACON+","+DBStrings.TABLE_TRONCO+","+DBStrings.TABLE_PIANO+","+DBStrings.TABLE_EDIFICIO+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+"="+idBeacon+" AND "+
                DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+" AND "+
                DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+" AND "+DBStrings.TABLE_PIANO+
                "."+DBStrings.COL_EDIFICIO+" = "+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nPiano = res.getString(res.getColumnIndex("NOME_PIANO"));
        res.close();
        db.close();

        Piano attuale = null;
        int index = 0;
        do {
            if(Posizione.getEdificioAttuale().getPiani().get(index).toString().equals(nPiano)) {
                attuale = Posizione.getEdificioAttuale().getPiani().get(index);
            }
            index++;
        } while(attuale==null && index < Posizione.getEdificioAttuale().getPiani().size());


        return attuale;
    }

    //passandogli un edificio ed un piano mi deve restituire la stringa in base64 dell'immagine del piano
    @Override
    public String richiediMappaPiano(String pianoAttuale) {

        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.COL_IMMAGINE+
                " FROM "+DBStrings.TABLE_MAPPA+
                " WHERE "+DBStrings.COL_PIANO+"="+pianoAttuale;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nImage = res.getString(res.getColumnIndex(DBStrings.COL_IMMAGINE));
        res.close();
        db.close();

        return nImage ;
    }

    //id_tronco string string dato un tronco voglio tutti i beacon del tronco
    //non capisco Strng dentro hasmap visto che sarebbe la stessa stringa contenuta nell'oggetto Beacon
    @Override
    public HashMap<String,Beacon> richiediBeaconTronco(int troncoAttuale) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+" AS ID_BEACON"+
                ","+DBStrings.TABLE_BEACON+"."+DBStrings.COL_X+" AS X_BEACON"+
                ","+DBStrings.TABLE_BEACON+"."+DBStrings.COL_Y+" AS Y_BEACON"+
                " FROM "+DBStrings.TABLE_BEACON+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+troncoAttuale;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        HashMap<String, Beacon> listaBeacon = new HashMap<String, Beacon>();

        //Beacon(String id, PointF posizione)
        while(res.moveToNext()) {
            listaBeacon.put(        //metodo put per riempire un'hashmap
                    res.getString(res.getColumnIndex(DBStrings.COL_ID)),
                    new Beacon(
                            res.getString(res.getColumnIndex("ID_BEACON")),
                            new PointF(res.getFloat(res.getColumnIndex("X_BEACON")), res.getFloat(res.getColumnIndex("Y_BEACON")))));
        }
        res.close();
        db.close();

        return listaBeacon;

    }

    @Override
    public ArrayList<Tronco> richiediTronchiPiano(String nomePiano) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_ID+","+DBStrings.COL_LARGHEZZA+","+DBStrings.COL_X+","+DBStrings.COL_Y+","+DBStrings.COL_XF+","+DBStrings.COL_YF+
                " FROM "+DBStrings.TABLE_TRONCO+ " WHERE "+DBStrings.COL_PIANO+"="+nomePiano;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Tronco> listaTronchi = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaTronchi.add(new Tronco(
                    res.getInt(res.getColumnIndex(DBStrings.COL_ID)),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_X)), res.getFloat(res.getColumnIndex(DBStrings.COL_Y))),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_XF)), res.getFloat(res.getColumnIndex(DBStrings.COL_YF))),
                    res.getFloat(res.getColumnIndex(DBStrings.COL_LARGHEZZA))));
        }
        res.close();
        db.close();

        return listaTronchi;

    }


    // dato un nome del piano restituisce tutte le sue aule
    @Override
    public ArrayList<Aula> richiediAulePiano(String nomePiano) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_NOME+","+DBStrings.COL_X+","+DBStrings.COL_Y+
                " FROM "+DBStrings.TABLE_AULA+ " WHERE "+DBStrings.COL_PIANO+"="+nomePiano;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Aula> listaAule = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaAule.add(new Aula(res.getString(res.getColumnIndex(DBStrings.COL_NOME)),
                    res.getString(res.getColumnIndex(DBStrings.COL_ENTRATA))));
        }
        res.close();
        db.close();

        return listaAule;
    }

    // dato un nome dell'edificio restituisce la lista dei suoi piani
    @Override
    public ArrayList<Piano> richiediPianiEdificio(String nomeEdificio) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_NOME+ " FROM "+DBStrings.TABLE_PIANO+ " WHERE "+DBStrings.COL_EDIFICIO+"="+nomeEdificio;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Piano> listaPiani = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaPiani.add(new Piano(res.getString(res.getColumnIndex(DBStrings.COL_NOME))));
        }
        res.close();
        db.close();

        return listaPiani;

    }

}

