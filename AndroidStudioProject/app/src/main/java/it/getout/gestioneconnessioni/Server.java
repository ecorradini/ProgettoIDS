package it.getout.gestioneconnessioni;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;

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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.getout.Client;
import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;
import it.getout.gestionevisualizzazionemappa.Mappa;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Server
{

    //private static final String BASE_URL = "http://192.168.0.112:9600";
    //private static final String BASE_URL = "http://192.168.1.184:9600";
    private static final String BASE_URL = "http://172.23.134.169:9600";
    private static final String SERV_PERCORSO = "/percorso";            //URL percorso
    private static final String SERV_PIANIEDI = "/pianiedificio?";      //URL piano da edificio
    private static final String SERV_EDIFICIO = "/edificioattuale?";    //URL edificio da idbeacon
    private static final String SERV_BEACON = "/beacontronco?";         //URL beacon da tronco
    private static final String SERV_PIANIATT = "/pianoattuale?";       //URL pianoattuale da idbeacon
    private static final String SERV_POSIZIONE = "/posizione?";         //URL posizione da idbeacon
    private static final String SERV_AULEPIANO = "/aulepiano?";         //URL aule da piano
    private static final String SERV_TRONCHIPIANO = "/tronchipiano?";   //URL tronchi da piano
    private static final String SERV_MAPPAPIANO = "/mappapiano?";       //URL tronchi da piano
    private static final String SERV_SUMUSER = "/sommautente?";         //UTL aggiunta utente


    private Context context;

    public Server(Context c) {
        context = c;
    }

    public void richiediPercorso(PointF destinazione) { new RichiediPercorsoTask().execute(destinazione); }

    public void richiediPianibyEdificio(Edificio edificio) { new RichiediPianibyEdificioTask().execute(edificio); }

    public void richiediEdificio(String idbeacon) { new RichiediEdificioTask().execute(idbeacon); }

    public void richiediBeaconbyTronco(Tronco tronco) { new RichiediBeaconbyTroncoTask().execute(tronco); }

    public void richiediPianobyBeacon(String beacon) { new RichiediPianobyBeaconTask().execute(beacon); }

    public void richiediPosizione(String beacon) {
        new RichiediPosizioneTask().execute(beacon);
    }

    public void richiediAulebyPiano(Piano piano) {
        new RichiediAulebyPianoTask().execute(piano);
    }

    public void richiediTronchibyPiano(Piano piano) { new RichiediTronchibyPianoTask().execute(piano); }

    public void richiediMappabyPiano(Piano...piano) {
        new RichiediMappabyPianoTask().execute(piano);
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

                            for(int i = 0; i < Posizione.getPianoAttuale().getTronchi().size(); i++) {
                                Tronco attuale = Posizione.getPianoAttuale().getTronco(i);
                                if (attuale.equals(inizio,fine)) {
                                    percorsoRisultato.add(attuale);
                                    break;
                                }
                            }

                            Posizione.getPercorso().setTronchi(percorsoRisultato);
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
                    params.put("XI", String.valueOf(Posizione.getPosizione().x));
                    params.put("YI", String.valueOf(Posizione.getPosizione().y));
                    params.put("XF",String.valueOf(puntoDestinazione.x));
                    params.put("YF",String.valueOf(puntoDestinazione.y));
                    params.put("EDIFICIO", Posizione.getEdificioAttuale().toString());
                    params.put("PIANO", Posizione.getPianoAttuale().toString());

                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

    //AsyncTask che richiede i piani in base all'edificio al Server
    private class RichiediPianibyEdificioTask extends AsyncTask<Edificio,Void,ArrayList<Piano>> {
        private Edificio edificio;
        private boolean downloaded;
        ArrayList<Piano> piani;

        @Override
        protected ArrayList<Piano> doInBackground(Edificio...e) {
            edificio = e[0];
            //La variabile da restituire
            piani = new ArrayList<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_PIANIEDI + edificio.toString();
            downloaded = false;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "piani"
                        JSONArray array = response.getJSONArray(edificio.toString());
                        if(array.length()>0) {
                            for (int j = 0; j < array.length(); j++) {

                                JSONObject current = array.getJSONObject(j);

                                String nomePiano = current.getString("PIANO");
                                //per ogni elemento di array, ricavo il nome del piano e inserisco il piano nell'arraylist
                                //piani.add(new Piano(current.getString("nome")));
                                Log.d("PIANO " + edificio.toString(), nomePiano);
                                piani.add(new Piano(nomePiano));
                            }
                        }
                        downloaded = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto di aver scaricato tutti i piano
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            return piani;
        }

        @Override
        protected void onPostExecute(ArrayList<Piano> p) {
            if(p.size()>0) {
                edificio.setPiani(p);
            }
        }
    }

    //AsyncTask che richiede l'edificio in base all'idbeacon connesso dal Server
    private class RichiediEdificioTask extends AsyncTask<String,Void,Edificio> {
        private String idBeacon;
        private Edificio edificio;
        private boolean downloaded;

        @Override
        protected Edificio doInBackground(String... idbeacon)
        {
            idBeacon = idbeacon[0];
            edificio = null;
            downloaded = false;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_EDIFICIO + idBeacon;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String nomeEdificio = response.getString("EDIFICIO_ATTUALE");
                        edificio = new Edificio(nomeEdificio);
                        downloaded = true;
                        ((Client)context).stopLoading();
                        ((Client)context).inizializzaFragment();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSON ED ATTUALE ERROR", error.toString());
                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto di aver scaricato tutti i piano
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

           return edificio;
        }

        @Override
        protected void onPostExecute(Edificio edificio) {
            Log.d("EDIFICIO",edificio.toString());
            Posizione.setEdificioAttuale(edificio);
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediBeaconbyTroncoTask extends AsyncTask<Tronco,Void,HashMap<String,Beacon>> {
        private Tronco tronco;
        private HashMap<String,Beacon> beacons;
        private boolean downloaded;

        @Override
        protected HashMap<String,Beacon> doInBackground(final Tronco...tronchi) {
            downloaded=false;
            tronco = tronchi[0];
            //La variabile da restituire
            beacons = new HashMap<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_BEACON + tronco;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray(tronco.toString());
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i);

                            String id = current.getString("ID");
                            PointF posizione = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));

                            Log.d("BEACONS "+tronco.toString(),id);

                            beacons.put(id,new Beacon(id, posizione));
                        }

                        downloaded = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto di aver scaricato tutti i piano
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

           return beacons;
        }

        @Override
        protected void onPostExecute(HashMap<String,Beacon> b) {
            tronco.setBeacons(b);
        }
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiediPianobyBeaconTask extends AsyncTask<String,Void,Piano> {
        private String idbeacon;
        private Piano piano;
        private boolean downloaded;

        @Override
        protected Piano doInBackground(String...beacon) {
            downloaded = false;
            idbeacon = beacon[0];
            //La variabile da restituire
            piano = null;
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
                        String nomePiano = response.getString("PIANO_ATTUALE");

                        for(int i = 0; i< Posizione.getEdificioAttuale().getPiani().size(); i++) {
                            if(nomePiano.equals(Posizione.getEdificioAttuale().getPiano(i).toString())) piano = Posizione.getEdificioAttuale().getPiano(i);
                        }

                        downloaded = true;

                    } catch (JSONException e) {
                        Log.d("ECCEZIONE PIANO",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto che l'edificio venga instanziato
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return piano;
        }

        @Override
        protected void onPostExecute(Piano p) {
            Log.d("PIANO_ATTUALE",p.toString());
            Posizione.setPianoAttuale(p);
        }
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiediPosizioneTask extends AsyncTask<String,Void,Boolean> {
        private String idbeacon;
        private PointF posizione;

        @Override
        protected Boolean doInBackground(String...beacon) {
            idbeacon = beacon[0];
            //La variabile da restituire
            posizione = null;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_POSIZIONE + idbeacon;
            String url2 = BASE_URL + SERV_POSIZIONE + idbeacon;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "posizione"
                        JSONArray array = response.getJSONArray("posizione");

                        JSONObject current = array.getJSONObject(0);
                        posizione = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));

                        Posizione.setPosizione(posizione);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Richiamo la procedura di somma utente
            try {
                URL sumUser = new URL(url2);
                HttpURLConnection urlConnection = (HttpURLConnection) sumUser.openConnection();
                urlConnection.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediAulebyPianoTask extends AsyncTask<Piano,Void,ArrayList<Aula>> {
        private Piano piano;
        private ArrayList<Aula> aule;
        private boolean downloaded;

        @Override
        protected ArrayList<Aula> doInBackground(Piano...pianos) {
            piano = pianos[0];
            //La variabile da restituire
            aule = new ArrayList<Aula>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_AULEPIANO + piano;
            downloaded = false;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray(piano.toString());
                        if(array.length()>0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject current = array.getJSONObject(i).getJSONObject("AULA");

                                String nomeAula = current.getString("NOME");

                                String entrata = current.getString("ENTRATA");

                                Log.d("AULA " + piano.toString(), nomeAula);

                                aule.add(new Aula(nomeAula, entrata, piano));
                            }
                        }
                        downloaded = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto di aver scaricato tutti i piano
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            return aule;
        }

        @Override
        protected void onPostExecute(ArrayList<Aula> a) {
            piano.setAule(a);
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediTronchibyPianoTask extends AsyncTask<Piano,Void,ArrayList<Tronco>> {
        private Piano piano;
        private ArrayList<Tronco> tronchi;
        private boolean downloaded;

        @Override
        protected ArrayList<Tronco> doInBackground(Piano...pianos) {
            piano = pianos[0];
            downloaded = false;
            //La variabile da restituire
            tronchi = new ArrayList<Tronco>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_TRONCHIPIANO + piano;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray(piano.toString());
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i).getJSONObject("TRONCO");

                            PointF inizio = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));
                            PointF fine = new PointF(Float.parseFloat(current.getString("XF")),Float.parseFloat(current.getString("YF")));
                            float larghezza = Float.parseFloat(current.getString("LARGHEZZA"));

                            Log.d("TRONCHI "+piano.toString(),"("+inizio.x+","+inizio.y+")/("+fine.x+","+fine.y+")");
                            tronchi.add(new Tronco(inizio, fine, larghezza));
                        }

                        downloaded = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            //Aspetto di aver scaricato tutti i piano
            Thread attesa = new Thread() {
                public void run() {
                    while(!downloaded);
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return tronchi;
        }

        @Override
        protected void onPostExecute(ArrayList<Tronco> t) {
            if(t.size()>0) {
                piano.setTronchi(t);
            }
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediMappabyPianoTask extends AsyncTask<Piano,Void,Boolean> {
        private Piano piano;
        private Bitmap mappa;

        @Override
        protected Boolean doInBackground(Piano...pianos) {
            piano = pianos[0];
            //La variabile da restituire
            mappa = null;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_MAPPAPIANO + piano;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray("mappapiano");

                            JSONObject current = array.getJSONObject(0);
                            Mappa.setMappa(current.getString("MAPPA"));
                            mappa = Mappa.getMappa();

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
                        params.put("MAPPA", Double.toString(Mappa.getWitdh()) + " - " + Double.toString(Mappa.getWitdh()));
                    return params;
                }
            };
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            return true;
        }
    }

}
