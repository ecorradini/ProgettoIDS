package it.getout.gestioneconnessioni;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.getout.MainActivity;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.PosizioneUtente;
import it.getout.gestioneposizione.Tronco;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class ServerHelper {

    private static final String BASE_URL = "http://DA SOSTITUIRE CON URL SERVER";
    private static final String SERV_PERCORSO = "/percorso";            //URL percorso
    private static final String SERV_PIANIEDI = "/pianiedificio?";      //URL piano da edificio
    private static final String SERV_EDIFICIO = "/edificioattuale?";    //URL edificio da idbeacon
    private static final String SERV_BEACON = "/beacontronco?";         //URL beacon da tronco
    private static final String SERV_PIANIATT = "/pianoattuale?";       //URL pianoattuale da idbeacon
    private static final String SERV_POSIZIONE = "/posizione?";         //URL posizione da idbeacon
    private static final String SERV_AULEPIANO = "/aulepiano?";         //URL aule da piano
    private static final String SERV_TRONCHIPIANO = "/tronchipiano?";   //URL tronchi da piano
    private static final String SERV_MAPPAPIANO = "/mappapiano?";       //URL tronchi da piano


    private Context context;

    public ServerHelper(Context c) {
        context = c;
    }

    public void richiediPercorso(PointF destinazione) {
        new RichiediPercorsoTask().execute(destinazione);
    }

    public void RichiediPianibyEdificio(Edificio edificio) {
        new RichiediPianibyEdificioTask().execute(edificio);
    }

    public void RichiediEdificio(String idbeacon) {
        new RichiediEdificioTask().execute(idbeacon);
    }

    public void RichiediBeaconbyTronco(Tronco...tronco) {
        new RichiediBeaconbyTroncoTask().execute(tronco);
    }

    public void RichiedipianobyBeacon(String...beacon) {
        new RichiedipianobyBeaconTask().execute(beacon);
    }

    //AsyncTask che richiede il percorso al Server
    private class RichiediPercorsoTask extends AsyncTask<PointF,Void,Boolean> {
        private PointF puntoDestinazione;
        private ArrayList<Tronco> percorsoRisultato;
        @Override
        protected Boolean doInBackground(PointF... destinazione) {
            puntoDestinazione = destinazione[0];
            //La variabile da restituire
            percorsoRisultato = new ArrayList<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_PERCORSO;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "percorso"
                        JSONArray tronchi = response.getJSONArray("percorso");
                        for (int j = 0; j < tronchi.length(); j++) {
                            JSONObject current = tronchi.getJSONObject(j);

                            //Di un tronco voglio cordinate di inizio e fine
                            PointF inizio = new PointF(
                                    Float.parseFloat(current.getString("XI")),
                                    Float.parseFloat(current.getString("YI"))
                            );
                            PointF fine = new PointF(
                                    Float.parseFloat(current.getString("XF")),
                                    Float.parseFloat(current.getString("YF"))
                            );

                            for(int i=0; i < PosizioneUtente.getPianoAttuale().getTronchi().size(); i++) {
                                Tronco attuale = PosizioneUtente.getPianoAttuale().getTronco(i);
                                if (attuale.equals(inizio,fine)) {
                                    percorsoRisultato.add(attuale);
                                    break;
                                }
                            }

                            PosizioneUtente.getPercorso().setTronchi(percorsoRisultato);
                            ((MainActivity)context).getMappaFragment().disegnaPercorso();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("XI", String.valueOf(PosizioneUtente.getPosizione().x));
                    params.put("YI", String.valueOf(PosizioneUtente.getPosizione().y));
                    params.put("XF",String.valueOf(puntoDestinazione.x));
                    params.put("YF",String.valueOf(puntoDestinazione.y));
                    params.put("EDIFICIO",PosizioneUtente.getEdificioAttuale().toString());
                    params.put("PIANO",PosizioneUtente.getPianoAttuale().toString());

                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

    //AsyncTask che richiede i piani in base all'edificio al Server
    private class RichiediPianibyEdificioTask extends AsyncTask<Edificio,Void,Boolean> {
        private Edificio building;
        private ArrayList<Piano> piani;

        @Override
        protected Boolean doInBackground(Edificio...edificio) {
            building = edificio[0];
            //La variabile da restituire
            piani = new ArrayList<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_PIANIEDI + building;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "piani"
                        JSONArray array = response.getJSONArray("pianiedificio");
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject current = array.getJSONObject(j);

                            //per ogni elemento di array, ricavo il nome del piano e inserisco il piano nell'arraylist
                            piani.add(new Piano(current.getString("nome")));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    for (int i=0;i < piani.size();i++){
                        params.put("PIANO" + Integer.toString(i), piani.get(i).toString());
                    }
                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

    //AsyncTask che richiede l'edificio in base all'idbeacon connesso dal Server
    private class RichiediEdificioTask extends AsyncTask<String,Void,Boolean> {
        private String idBeacon;
        private Edificio edificio;

        @Override
        protected Boolean doInBackground(String...idbeacon) {
            idBeacon = idbeacon[0];
            //La variabile da restituire
            edificio = null;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_EDIFICIO + idBeacon;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray("edificioattuale");
                            JSONObject current = array.getJSONObject(0);
                            //ricavo l'edificio e lo istanzio
                            edificio = new Edificio(current.getString("nome"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                        params.put("EDIFICIO", edificio.toString());
                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediBeaconbyTroncoTask extends AsyncTask<Tronco,Void,Boolean> {
        private Tronco troncos;
        private ArrayList<Beacon> beacons;

        @Override
        protected Boolean doInBackground(Tronco...tronco) {
            troncos = tronco[0];
            //La variabile da restituire
            beacons = new ArrayList<Beacon>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_BEACON + troncos;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray("beacontronco");
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i);

                            PointF posizione = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));
                            beacons.add(new Beacon(current.getString("ID"), posizione));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    for (int i=0;i < beacons.size();i++){
                        params.put("BEACON" + Integer.toString(i), beacons.get(i).getId());
                    }
                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiedipianobyBeaconTask extends AsyncTask<String,Void,Boolean> {
        private String idbeacon;
        private Piano piani;

        @Override
        protected Boolean doInBackground(String...beacon) {
            idbeacon = beacon[0];
            //La variabile da restituire
            piani = null;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_PIANIATT + idbeacon;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "piani"
                        JSONArray array = response.getJSONArray("pianoattuale");

                        JSONObject current = array.getJSONObject(0);
                        piani = new Piano(current.getString("nomePiano"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("PIANO", piani.toString());
                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }
}
