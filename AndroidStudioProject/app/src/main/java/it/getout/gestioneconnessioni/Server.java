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
import java.util.concurrent.ExecutionException;

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

public class Server extends GestoreDati
{

    //private static final String BASE_URL = "http://192.168.0.112:9600";
    //private static final String BASE_URL = "http://192.168.1.184:9600";
    private static final String BASE_URL = "http://172.23.134.169:9600";
    private static final String SERV_PERCORSO = "/percorso";            //URL percorso
    private static final String SERV_PIANIEDI = "/pianiedificio?";      //URL piano da edificio
    private static final String SERV_EDIFICIO = "/edificioattuale?";    //URL edificio da idbeacon
    private static final String SERV_BEACON = "/beacontronco?";         //URL beacon da tronco
    private static final String SERV_PIANIATT = "/pianoattuale?";       //URL pianoattuale da idbeacon
    private static final String SERV_AULEPIANO = "/aulepiano?";         //URL aule da piano
    private static final String SERV_TRONCHIPIANO = "/tronchipiano?";   //URL tronchi da piano
    private static final String SERV_MAPPAPIANO = "/mappapiano?";       //URL tronchi da piano
    private static final String SERV_SUMUSER = "/sommautente?";         //UTL aggiunta utente

    private Context context;

    public Server(Context c) {
        context = c;
    }

    @Override
    public Edificio richiediEdificioAttuale(String beacon) {

        class ThreadAttesaEdificio extends Thread {

            private Edificio edificio;
            private String idBeacon;

            public ThreadAttesaEdificio(String id) {
                super();
                this.idBeacon = id;
            }

            @Override
            public void run() {
                try {
                    edificio = new RichiediEdificioTask().execute(idBeacon).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public Edificio getResult() { return edificio; }
        }

        ThreadAttesaEdificio attesaEdifico = new ThreadAttesaEdificio(beacon);
        attesaEdifico.start();
        try {
            attesaEdifico.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaEdifico.getResult();
    }

    @Override
    public Piano richiediPianoAttuale(String beacon) {

        class ThreadAttesaPiano extends Thread {

            private Piano piano;
            private String idBeacon;

            public ThreadAttesaPiano(String id) {
                super();
                this.idBeacon = id;
            }

            @Override
            public void run() {
                try {
                    piano = new RichiediPianoTask().execute(idBeacon).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public Piano getResult() { return piano; }
        }

        ThreadAttesaPiano attesaPiano = new ThreadAttesaPiano(beacon);
        attesaPiano.start();
        try {
            attesaPiano.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaPiano.getResult();    }

    @Override
    public String richiediMappaPiano(String piano) {

        class ThreadAttesaMappa extends Thread {

            private String piano;
            private String mappa;

            public ThreadAttesaMappa(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                try {
                    mappa = new RichiediMappaPianoTask().execute(piano).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public String getResult() { return mappa; }
        }

        ThreadAttesaMappa attesaMappa = new ThreadAttesaMappa(piano);
        attesaMappa.start();
        try {
            attesaMappa.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaMappa.getResult();
    }

    @Override
    public HashMap<String, Beacon> richiediBeaconTronco(int idTronco) {

        class ThreadAttesaBeacon extends Thread {

            private HashMap<String,Beacon> beacons;
            private int idTronco;

            public ThreadAttesaBeacon(int idTronco) {
                super();
                this.idTronco = idTronco;
            }

            @Override
            public void run() {
                try {
                    beacons = new RichiediBeaconTroncoTask().execute(idTronco).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public HashMap<String,Beacon> getResult() { return beacons; }
        }

        ThreadAttesaBeacon attesaBeacon = new ThreadAttesaBeacon(idTronco);
        attesaBeacon.start();
        try {
            attesaBeacon.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaBeacon.getResult();
    }

    @Override
    public ArrayList<Tronco> richiediTronchiPiano(String piano) {

        class ThreadAttesaTronco extends Thread {

            private ArrayList<Tronco> tronchi;
            private String piano;

            public ThreadAttesaTronco(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                try {
                    tronchi = new RichiediTronchiPianoTask().execute(piano).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public ArrayList<Tronco> getResult() { return tronchi; }
        }

        ThreadAttesaTronco attesaTronco = new ThreadAttesaTronco(piano);
        attesaTronco.start();
        try {
            attesaTronco.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaTronco.getResult();
    }

    @Override
    public ArrayList<Aula> richiediAulePiano(String piano) {

        class ThreadAttesaAule extends Thread {

            private ArrayList<Aula> aule;
            private String piano;

            public ThreadAttesaAule(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                try {
                    aule = new RichiediAulePianoTask().execute(piano).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public ArrayList<Aula> getResult() { return aule; }
        }

        ThreadAttesaAule attesaAule = new ThreadAttesaAule(piano);
        attesaAule.start();
        try {
            attesaAule.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaAule.getResult();
    }

    @Override
    public ArrayList<Piano> richiediPianiEdificio(String edificio) {

        class ThreadAttesaPiani extends Thread {

            private ArrayList<Piano> piani;
            private String edificio;

            public ThreadAttesaPiani(String edificio) {
                super();
                this.edificio = edificio;
            }

            @Override
            public void run() {
                try {
                    piani = new RichiediPianiEdificioTask().execute(edificio).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            public ArrayList<Piano> getResult() { return piani; }
        }

        ThreadAttesaPiani attesaPiani = new ThreadAttesaPiani(edificio);
        attesaPiani.start();
        try {
            attesaPiani.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaPiani.getResult();
    }

    //AsyncTask che richiede l'edificio in base all'idbeacon connesso dal Server
    private class RichiediEdificioTask extends AsyncTask<String,Void,Edificio> {

        private String idBeacon;
        private Edificio edificio;

        @Override
        protected Edificio doInBackground(String... idbeacon)
        {
            idBeacon = idbeacon[0];
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String nomeEdificio = response.getString("EDIFICIO_ATTUALE");
                        edificio = new Edificio(nomeEdificio);
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

            Thread attesa = new Thread() {
                public void run() {
                    while(edificio==null);
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
    }

    //AsyncTask che richiede i piani in base all'edificio al Server
    private class RichiediPianiEdificioTask extends AsyncTask<String,Void,ArrayList<Piano>> {

        private String edificio;
        private boolean downloaded;
        ArrayList<Piano> piani;

        @Override
        protected ArrayList<Piano> doInBackground(String...e) {
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
            String url = BASE_URL + SERV_PIANIEDI + edificio;
            downloaded = false;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "piani"
                        JSONArray array = response.getJSONArray(edificio);
                        if(array.length()>0) {
                            for (int j = 0; j < array.length(); j++) {

                                JSONObject current = array.getJSONObject(j);

                                String nomePiano = current.getString("PIANO");
                                //per ogni elemento di array, ricavo il nome del piano e inserisco il piano nell'arraylist
                                //piani.add(new Piano(current.getString("nome")));
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
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediBeaconTroncoTask extends AsyncTask<Integer,Void,HashMap<String,Beacon>> {

        private int tronco;
        private HashMap<String,Beacon> beacons;
        private boolean downloaded;

        @Override
        protected HashMap<String,Beacon> doInBackground(final Integer...tronchi) {

            downloaded = false;
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
                        JSONArray array = response.getJSONArray(tronco+"");
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i);

                            String id = current.getString("ID");
                            PointF posizione = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));

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
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiediPianoTask extends AsyncTask<String,Void,Piano> {
        private String idbeacon;
        private Piano piano;

        @Override
        protected Piano doInBackground(String...beacon) {
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
                    while(piano==null);
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

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediAulePianoTask extends AsyncTask<String,Void,ArrayList<Aula>> {

        private String piano;
        private ArrayList<Aula> aule;
        private boolean downloaded;

        @Override
        protected ArrayList<Aula> doInBackground(String...pianoI) {
            piano = pianoI[0];
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

                                aule.add(new Aula(nomeAula, entrata));
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
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediTronchiPianoTask extends AsyncTask<String,Void,ArrayList<Tronco>> {

        private String piano;
        private ArrayList<Tronco> tronchi;
        private boolean downloaded;

        @Override
        protected ArrayList<Tronco> doInBackground(String...pianoI) {
            piano = pianoI[0];
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
                        JSONArray array = response.getJSONArray(piano);
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i).getJSONObject("TRONCO");

                            PointF inizio = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));
                            PointF fine = new PointF(Float.parseFloat(current.getString("XF")),Float.parseFloat(current.getString("YF")));
                            float larghezza = Float.parseFloat(current.getString("LARGHEZZA"));

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
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediMappaPianoTask extends AsyncTask<String,Void,String> {

        private String piano;
        private String mappa;

        @Override
        protected String doInBackground(String...pianoI) {
            piano = pianoI[0];
            RequestQueue mRequestQueue;
            mappa="";
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
                            mappa = current.getString("MAPPA");

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

            Thread attesa = new Thread() {
                public void run() {
                    while(mappa.isEmpty());
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            return mappa;
        }
    }

}
