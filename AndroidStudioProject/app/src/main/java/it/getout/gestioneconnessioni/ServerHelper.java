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

import it.getout.gestioneposizione.PosizioneUtente;
import it.getout.gestioneposizione.Tronco;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class ServerHelper {

    private static final String BASE_URL = "http://DA SOSTITUIRE CON URL SERVER";
    private static final String SERV_PERCORSO="/percorso";

    private Context context;

    public ServerHelper(Context c) {
        context = c;
    }

    public void richiediPercorso(PointF destinazione) {
        new RichiediPercorsoTask().execute(destinazione);
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
            String url = BASE_URL+SERV_PERCORSO;
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

                            for(int i=0; i< PosizioneUtente.getPianoAttuale().getTronchi().size(); i++) {
                                Tronco attuale = PosizioneUtente.getPianoAttuale().getTronco(i);
                                if (attuale.equals(inizio,fine)) {
                                    percorsoRisultato.add(attuale);
                                    break;
                                }
                            }
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
}
