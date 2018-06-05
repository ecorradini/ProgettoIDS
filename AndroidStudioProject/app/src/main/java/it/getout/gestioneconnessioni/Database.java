package it.getout.gestioneconnessioni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.GrafoTronchi;
import it.getout.gestioneposizione.Percorso;
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

    public void inserisciValori(HashMap<String,ContentValues> hashMap){
        SQLiteDatabase db = connessione.getWritableDatabase();
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String chiave = (String)entry.getKey();
            String nomeTabella = chiave.split(",")[0];
            db.insert(nomeTabella, null, (ContentValues) entry.getValue());
        }
    }


    @Override
    public Edificio richiediEdificioAttuale(String idBeacon) {   //tipo edificio ancora non è dato sapere
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME+" AS NOME_EDIFICIO"+
                " FROM "+DBStrings.TABLE_EDIFICIO+","+DBStrings.TABLE_PIANO+","+DBStrings.TABLE_TRONCO+","+DBStrings.TABLE_BEACON+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+" AND "+
                DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+"="+DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+" AND "+
                DBStrings.TABLE_PIANO+"."+DBStrings.COL_EDIFICIO+"="+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME+" AND "+
                DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+"='"+idBeacon+"'";
        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nEdificio = res.getString(res.getColumnIndex("NOME_EDIFICIO"));
        res.close();
        return new Edificio(nEdificio);
    }

    //passatogli l'id del beacon mi restituisce il piano
    @Override
    public Piano richiediPianoAttuale(String idBeacon){
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT "+DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+" AS NOME_PIANO"+
                " FROM "+DBStrings.TABLE_BEACON+","+DBStrings.TABLE_TRONCO+","+DBStrings.TABLE_PIANO+","+DBStrings.TABLE_EDIFICIO+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+"='"+idBeacon+"' AND "+
                DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+" AND "+
                DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+" AND "+DBStrings.TABLE_PIANO+
                "."+DBStrings.COL_EDIFICIO+"="+DBStrings.TABLE_EDIFICIO+"."+DBStrings.COL_NOME;

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        String nPiano = res.getString(res.getColumnIndex("NOME_PIANO"));
        res.close();

        return new Piano(nPiano);
    }

    //passandogli un piano mi deve restituire la stringa in base64 dell'immagine del piano
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
        //res.moveToFirst();
        HashMap<String, Beacon> listaBeacon = new HashMap<String, Beacon>();

        //Beacon(String id, PointF posizione)
        while(res.moveToNext()) {
            listaBeacon.put(        //metodo put per riempire un'hashmap
                    res.getString(res.getColumnIndex("ID_BEACON")),
                    new Beacon(
                            res.getString(res.getColumnIndex("ID_BEACON")),
                            new PointF(res.getFloat(res.getColumnIndex("X_BEACON")), res.getFloat(res.getColumnIndex("Y_BEACON")))));
        }
        res.close();

        return listaBeacon;

    }

    @Override
    public ArrayList<Tronco> richiediTronchiPiano(String nomePiano) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_ID+","+DBStrings.COL_LARGHEZZA+","+DBStrings.COL_LUNGHEZZA+","+DBStrings.COL_X+","+DBStrings.COL_Y+","+DBStrings.COL_XF+","+DBStrings.COL_YF+
                " FROM "+DBStrings.TABLE_TRONCO+ " WHERE "+DBStrings.COL_PIANO+"="+nomePiano;

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Tronco> listaTronchi = new ArrayList<>();
        //res.moveToFirst();
        while(res.moveToNext()) {
            listaTronchi.add(new Tronco(
                    res.getInt(res.getColumnIndex(DBStrings.COL_ID)),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_X)), res.getFloat(res.getColumnIndex(DBStrings.COL_Y))),
                    new PointF(res.getFloat(res.getColumnIndex(DBStrings.COL_XF)), res.getFloat(res.getColumnIndex(DBStrings.COL_YF))),
                    res.getFloat(res.getColumnIndex(DBStrings.COL_LARGHEZZA)),
                    res.getFloat(res.getColumnIndex(DBStrings.COL_LUNGHEZZA))));
        }
        res.close();
        return listaTronchi;

    }

    public ArrayList<Integer> richiediTronchiAdiacenti(Tronco t) {
        SQLiteDatabase db = connessione.getReadableDatabase();
        ArrayList<Integer> risultato = new ArrayList<>();

        String query = "SELECT TRONCO.ID AS ID" +
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
        //res.moveToFirst();
        while(res.moveToNext()) {
            risultato.add(res.getInt(res.getColumnIndex("ID")));
        }

        res.close();

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
        //res.moveToFirst();
        while(res.moveToNext()) {
            tronchi.add(res.getFloat(res.getColumnIndex("VULN")));
            tronchi.add(res.getFloat(res.getColumnIndex("RV")));
            tronchi.add(res.getFloat(res.getColumnIndex("PF")));
        }

        res.close();

        return tronchi;
    }

    public int richiediNumeroPersoneInTronco(int tronco){
        int persone=0;

        SQLiteDatabase db = connessione.getReadableDatabase();
        String query = "SELECT SUM(UTENTI) AS UTENTI FROM BEACON WHERE TRONCO = "+tronco;

        Cursor res = db.rawQuery(query,null);
        //res.moveToFirst();

        persone = res.getInt(res.getColumnIndex("UTENTI"));

        res.close();

        return persone;

    }

    // dato un nome del piano restituisce tutte le sue aule
    @Override
    public ArrayList<Aula> richiediAulePiano(String nomePiano) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_NOME+","+DBStrings.COL_ENTRATA+
                " FROM "+DBStrings.TABLE_AULA+ " WHERE "+DBStrings.COL_PIANO+"='"+nomePiano+"'";

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Aula> listaAule = new ArrayList<>();
        //res.moveToFirst();
        while(res.moveToNext()) {
            listaAule.add(new Aula(res.getString(res.getColumnIndex(DBStrings.COL_NOME)),res.getString(res.getColumnIndex(DBStrings.COL_ENTRATA))));
        }
        res.close();

        return listaAule;
    }

    // dato un nome dell'edificio restituisce la lista dei suoi piani
    @Override
    public ArrayList<Piano> richiediPianiEdificio(String nomeEdificio) {
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = " SELECT "+DBStrings.COL_NOME+ " FROM "+DBStrings.TABLE_PIANO+ " WHERE "+DBStrings.COL_EDIFICIO+"='"+nomeEdificio+"'";

        Cursor res = db.rawQuery(sql,null);
        ArrayList<Piano> listaPiani = new ArrayList<>();
        //res.moveToFirst();
        while(res.moveToNext()) {
            listaPiani.add(new Piano(res.getString(res.getColumnIndex(DBStrings.COL_NOME))));
        }
        res.close();

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

        return new Beacon(idBeacon,new PointF(Float.parseFloat(x),Float.parseFloat(y)));
    }

    @Override
    public ArrayList<Tronco> richiediPercorsoFuga(String idBeacon) {
        return new Percorso(Posizione.getIDBeaconAttuale(),this).getResult();
    }

    public ArrayList<Tronco> richiediTronchiUscita(String beacon) {
        SQLiteDatabase db = connessione.getReadableDatabase();
        ArrayList<Tronco> tronchiUscita = new ArrayList<>();

        String query = "SELECT BEACONPIANO."+DBStrings.COL_TRONCO+" AS "+DBStrings.COL_TRONCO+
                " FROM "+
                "( SELECT "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+" AS "+DBStrings.COL_TRONCO+","+
                DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+" AS "+DBStrings.COL_PIANO+","+DBStrings.TABLE_BEACON+"."+DBStrings.COL_USCITA+" AS "+DBStrings.COL_USCITA+
                " FROM "+DBStrings.TABLE_BEACON+
                " JOIN "+DBStrings.TABLE_TRONCO+
                " ON "+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+"="+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+
                " WHERE "+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+"= "+
                " (SELECT "+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+
                " FROM "+DBStrings.TABLE_BEACON+
                " JOIN "+DBStrings.TABLE_TRONCO+
                " ON "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+"=\'"+beacon+"\'"+
                ")"+
                ") AS BEACONPIANO "+
                " WHERE "+DBStrings.COL_USCITA+"=1";


        Cursor res = db.rawQuery(query,null);

        HashMap<Integer,Tronco> tronchiPiano = new HashMap<>();
        for(int i=0; i<Posizione.getPianoAttuale().getTronchi().size(); i++) {
            tronchiPiano.put(Posizione.getPianoAttuale().getTronchi().get(i).getId(),Posizione.getPianoAttuale().getTronco(i));
        }

        while(res.moveToNext()){
            tronchiUscita.add(tronchiPiano.get(res.getInt(res.getColumnIndex(DBStrings.COL_TRONCO))));
        }

        res.close();

        return tronchiUscita;
    }

    public int richiediTroncoByBeacon(String idBeacon){
        SQLiteDatabase db = connessione.getReadableDatabase();

        String sql = "SELECT TRONCO" +
                " FROM BEACON" +
                " WHERE BEACON.ID = \'"+idBeacon+"\'";

        Cursor res = db.rawQuery(sql,null);
        res.moveToFirst();
        int tronco = res.getInt(res.getColumnIndex(DBStrings.COL_TRONCO));
        res.close();

        return tronco;
    }

    @Override
    public ArrayList<String> richiediUsciteEdificio(String edificio) {
        SQLiteDatabase db = connessione.getReadableDatabase();
        String query = "SELECT "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_ID+" AS ID"+
                " FROM "+DBStrings.TABLE_BEACON+","+DBStrings.TABLE_TRONCO+","+DBStrings.TABLE_PIANO+
                " WHERE "+DBStrings.TABLE_BEACON+"."+DBStrings.COL_TRONCO+"="+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_ID+
                " AND "+DBStrings.TABLE_TRONCO+"."+DBStrings.COL_PIANO+"="+DBStrings.TABLE_PIANO+"."+DBStrings.COL_NOME+
                " AND "+DBStrings.TABLE_PIANO+"."+DBStrings.COL_EDIFICIO+"='"+edificio+"'"+
                " AND "+DBStrings.COL_USCITA+"=1";


        Cursor res = db.rawQuery(query,null);
        ArrayList<String> usciteEdificio = new ArrayList<>();
        while(res.moveToNext()) {
            usciteEdificio.add(res.getString(res.getColumnIndex(DBStrings.COL_ID)));
        }
        res.close();

        return usciteEdificio;
    }

}

