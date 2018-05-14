package it.getout.gestioneconnessioni;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Base64;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.GrafoTronchi;
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

        return new Piano(nPiano);
    }

    //passandogli un edificio ed un piano mi deve restituire la stringa in base64 dell'immagine del piano
    @Override
    public Bitmap richiediMappaPiano(String pianoAttuale) {

        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.COL_IMMAGINE+
                " FROM "+DBStrings.TABLE_MAPPA+
                " WHERE "+DBStrings.COL_PIANO+"="+pianoAttuale;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nImage = res.getString(res.getColumnIndex(DBStrings.COL_IMMAGINE));
        res.close();
        db.close();

        byte[] decodedString = Base64.decode(nImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

        String sql = " SELECT "+DBStrings.COL_ID+","+DBStrings.COL_LARGHEZZA+","+DBStrings.COL_LUNGHEZZA+","+DBStrings.COL_X+","+DBStrings.COL_Y+","+DBStrings.COL_XF+","+DBStrings.COL_YF+
                " FROM "+DBStrings.TABLE_TRONCO+ " WHERE "+DBStrings.COL_PIANO+"="+nomePiano;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Tronco> listaTronchi = new ArrayList<>();
        res.moveToFirst();
        while(res.moveToNext()) {
            listaTronchi.add(new Tronco(
                    res.getInt(res.getColumnIndex(DBStrings.COL_ID)),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_X)), res.getFloat(res.getColumnIndex(DBStrings.COL_Y))),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_XF)), res.getFloat(res.getColumnIndex(DBStrings.COL_YF))),
                    res.getFloat(res.getColumnIndex(DBStrings.COL_LARGHEZZA)),
                    res.getFloat(res.getColumnIndex(DBStrings.COL_LUNGHEZZA))));
        }
        res.close();
        db.close();

        return listaTronchi;

    }

    public ArrayList<Integer> richiediTronchiAdiacenti(Tronco t) {
        SQLiteDatabase db = connessione.getReadableDatabase();
        ArrayList<Integer> risultato = new ArrayList<>();

        String query = "SELECT TRONCO.ID" +
                " FROM TRONCO," +
                "( SELECT TRONCO.ID as IDINIZIO," +
                " TRONCO.X  as XINIZIO," +
                " TRONCO.Y  as YINIZIO," +
                " TRONCO.XF as XFINIZIO," +
                " TRONCO.YF as YFINIZIO," +
                " TRONCO.PIANO as PIANOINIZIO "+
                " FROM TRONCO" +
                " WHERE TRONCO.ID = "+ t.getId() +
                ") AS TRONCOINIZIO" +
                " WHERE ((TRONCO.X=TRONCOINIZIO.XINIZIO" +
                " AND TRONCO.Y=TRONCOINIZIO.YINIZIO)" +
                " OR (TRONCO.XF=TRONCOINIZIO.XINIZIO" +
                " AND TRONCO.YF=TRONCOINIZIO.YINIZIO)" +
                " OR (TRONCO.X=TRONCOINIZIO.XFINIZIO" +
                " AND TRONCO.Y=TRONCOINIZIO.YFINIZIO)" +
                " OR (TRONCO.XF=TRONCOINIZIO.XFINIZIO" +
                " AND TRONCO.YF=TRONCOINIZIO.YFINIZIO))" +
                " AND TRONCO.ID <> TRONCOINIZIO.IDINIZIO" +
                " AND TRONCO.PIANO = TRONCOINIZIO.PIANOINIZIO";

        Cursor res = db.rawQuery(query,null);
        res.moveToFirst();
        while(res.moveToNext()) {
            risultato.add(res.getInt(res.getColumnIndex("ID")));
        }

        if(risultato.size()>0) {
            return risultato;
        }
        else return null;
    }

    public ArrayList<Float> richiediParametri(int tronco){
        SQLiteDatabase db = connessione.getReadableDatabase();

        ArrayList<Float> tronchi=new ArrayList<>();

        String query =  "SELECT VULN,RV,PF"+
                " FROM PARAMETRI WHERE TRONCO="+tronco;

        Cursor res = db.rawQuery(query,null);
        res.moveToFirst();
        while(res.moveToNext()) {
            tronchi.add(res.getFloat(res.getColumnIndex("VULN")));
            tronchi.add(res.getFloat(res.getColumnIndex("RV")));
            tronchi.add(res.getFloat(res.getColumnIndex("PF")));
        }

        return tronchi;
    }

    public int richiediNumeroPersoneInTronco(int tronco){
        int persone=0;

        SQLiteDatabase db = connessione.getReadableDatabase();
        String query = "SELECT SUM(UTENTI) AS UTENTI FROM BEACON WHERE TRONCO = "+tronco;

        Cursor res = db.rawQuery(query,null);
        res.moveToFirst();

        persone = res.getInt(res.getColumnIndex("UTENTI"));

        return persone;

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

    @Override
    public Beacon richiediPosizione(String idBeacon) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String query =  "SELECT "+DBStrings.COL_ID+","+DBStrings.COL_X+","+DBStrings.COL_Y+" FROM "+DBStrings.TABLE_BEACON+" WHERE "+DBStrings.COL_ID+"=\'"+idBeacon+"\'";

        Cursor res = db.rawQuery(query,null);
        res.moveToFirst();
        String x = res.getString(res.getColumnIndex(DBStrings.COL_X));
        String y = res.getString(res.getColumnIndex(DBStrings.COL_Y));
        res.close();
        db.close();

        return new Beacon(idBeacon,new PointF(Float.parseFloat(x),Float.parseFloat(y)));
    }

    @Override
    public ArrayList<Tronco> richiediPercorsoFuga(String idBeacon) {
        //TODO: BISOGNA CALCOLARE IL PERCORSO OFFLINE
        return new ArrayList<>();
    }

    public GrafoTronchi.Nodo selectNodoByBeacon(String beacon, String edificio, String piano, GrafoTronchi grafo) {
        SQLiteDatabase db = connessione.getReadableDatabase();
        String query = "SELECT TRONCO" +
                " FROM BEACON" +
                " WHERE BEACON.ID = \'"+beacon+"\'";

        int tronco=0;

        Cursor res = db.rawQuery(query,null);
        res.moveToFirst();
        tronco = res.getInt(res.getColumnIndex("TRONCO"));
        res.close();
        db.close();

        ArrayList<GrafoTronchi.Nodo> visitati = new ArrayList<>();
        ArrayList<GrafoTronchi.Nodo> daVisitare = new ArrayList<>();
        daVisitare.add(grafo.getRadice());

        GrafoTronchi.Nodo risultato = null;

        while(daVisitare.size()>0 && risultato==null) {
            if(daVisitare.get(0).getTronco().getID()==tronco) {
                risultato = daVisitare.get(0);
            }
            else {
                if(!visitati.contains(daVisitare.get(0))) {
                    daVisitare.addAll(daVisitare.get(0).getAdiacenti());
                    visitati.add(daVisitare.get(0));
                }
                daVisitare.remove(0);
            }
        }

        return risultato;
    }
}

