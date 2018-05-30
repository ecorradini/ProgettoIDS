package it.getout.gestioneconnessioni;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
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


import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;


import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;


/**
 * Created by Alessandro on 01/02/2018.
 */

public class Server extends GestoreDati
{
    private static final String SERV_PERCORSO = "/percorso?";            //URL percorso
    private static final String SERV_POSIZIONE = "/posizione?";
    private static final String SERV_PIANIEDI = "/pianiedificio?";      //URL piano da edificio
    private static final String SERV_EDIFICIO = "/edificioattuale?";    //URL edificio da idbeacon
    private static final String SERV_BEACON = "/beacontronco?";         //URL beacon da tronco
    private static final String SERV_PIANIATT = "/pianoattuale?";       //URL pianoattuale da idbeacon
    private static final String SERV_AULEPIANO = "/aulepiano?";         //URL aule da piano
    private static final String SERV_TRONCHIPIANO = "/tronchipiano?";   //URL tronchi da piano
    private static final String SERV_MAPPAPIANO = "/mappapiano?";       //URL tronchi da piano
    private static final String SERV_USCITE = "/uscite?";               //URL beacon uscita edificio
    private static final String SERV_SUMUSER = "/sommautente?";         //UTL aggiunta utente

    private static String BASE_URL;

    private DatagramSocket c;
    private Context context;


    public Server(Context c) {
        context = c;

        Thread discovery = new Thread(){
            public void run(){
                discoverIP();
            }
        };
        discovery.start();
        try {
            discovery.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void discoverIP(){
        // Find the server using UDP broadcast
        try {
            //Open a random port to send the package
            c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "GETOUT".getBytes();

            //Try the 255.255.255.255 first-
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 9600);
                c.send(sendPacket);
                System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
            }

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 9600);
                        c.send(sendPacket);
                    } catch (Exception e) {
                    }

                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            Log.e("message",message);
            if(message.equals("GETOUT_R")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                BASE_URL = "http:/"+receivePacket.getAddress()+":9600";
            }

            //Close the port!
            c.close();
        } catch (IOException ex) {
            Log.i("IP","problema nel ricercare server");

        }
    }


    @Override
    public Edificio richiediEdificioAttuale(String beacon) {

        Log.d("RICHIEDO","EDIFICIO");

        class ThreadAttesaEdificio extends Thread {

            private Edificio edificio;
            private String idBeacon;

            private ThreadAttesaEdificio(String id) {
                super();
                this.idBeacon = id;
            }

            @Override
            public void run() {
                RichiediEdificioTask task = new RichiediEdificioTask();
                task.execute(idBeacon);
                edificio = task.getResult();
                Log.d("SCARICATO", "EDIFICIO " + edificio.toString());

            }

            private Edificio getResult() { return edificio; }
        }

        ThreadAttesaEdificio attesaEdifico = new ThreadAttesaEdificio(beacon);
        attesaEdifico.start();
        try {
            attesaEdifico.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("FINE","EDIFICIO");

        return attesaEdifico.getResult();
    }

    @Override
    public Piano richiediPianoAttuale(String beacon) {

        Log.e("INIZIO","PIANO");

        class ThreadAttesaPiano extends Thread {

            private Piano piano;
            private String idBeacon;

            private ThreadAttesaPiano(String id) {
                super();
                this.idBeacon = id;
            }

            @Override
            public void run() {
                RichiediPianoTask task = new RichiediPianoTask();
                task.execute(idBeacon);
                piano = task.getResult();
                Log.d("SCARICATO PIANO", piano.toString());
            }

            private Piano getResult() { return piano; }
        }

        ThreadAttesaPiano attesaPiano = new ThreadAttesaPiano(beacon);
        attesaPiano.start();
        try {
            attesaPiano.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("FINE","PIANO");

        return attesaPiano.getResult();
    }

    @Override
    public Bitmap richiediMappaPiano(String piano) {

        class ThreadAttesaMappa extends Thread {

            private String piano;
            private Bitmap mappa;

            private ThreadAttesaMappa(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                RichiediMappaPianoTask task = new RichiediMappaPianoTask();
                task.execute(piano);
                mappa = task.getResult();
            }

            private Bitmap getResult() { return mappa; }
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

            private ThreadAttesaBeacon(int idTronco) {
                super();
                this.idTronco = idTronco;
            }

            @Override
            public void run() {
                RichiediBeaconTroncoTask task = new RichiediBeaconTroncoTask();
                task.execute(idTronco);
                beacons = task.getResult();
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

            private ThreadAttesaTronco(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                RichiediTronchiPianoTask task = new RichiediTronchiPianoTask();
                task.execute(piano);
                tronchi = task.getResult();
            }

            private ArrayList<Tronco> getResult() { return tronchi; }
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

            private ThreadAttesaAule(String piano) {
                super();
                this.piano = piano;
            }

            @Override
            public void run() {
                RichiediAulePianoTask task = new RichiediAulePianoTask();
                task.execute(piano);
                aule = task.getResult();
            }

            private ArrayList<Aula> getResult() { return aule; }
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

            private ThreadAttesaPiani(String edificio) {
                super();
                this.edificio = edificio;
            }

            @Override
            public void run() {
                RichiediPianiEdificioTask task = new RichiediPianiEdificioTask();
                task.execute(edificio);
                piani = task.getResult();
            }

            private ArrayList<Piano> getResult() { return piani; }
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

    @Override
    public Beacon richiediPosizione(String beacon) {
        class ThreadAttesaBeacon extends Thread {

            private Beacon posizione;
            private String beacon;

            private ThreadAttesaBeacon(String beacon) {
                super();
                this.beacon = beacon;
            }

            @Override
            public void run() {
                RichiediPosizioneTask task = new RichiediPosizioneTask();
                task.execute(beacon);
                posizione = task.getResult();
            }

            private Beacon getResult() { return posizione; }
        }

        ThreadAttesaBeacon attesaBeacon = new ThreadAttesaBeacon(beacon);
        attesaBeacon.start();
        try {
            attesaBeacon.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaBeacon.getResult();
    }

    @Override
    public ArrayList<Tronco> richiediPercorsoFuga(String beacon) {

        class ThreadAttesaPercorso extends Thread {

            private ArrayList<Tronco> percorso;
            private String beacon;

            private ThreadAttesaPercorso(String beacon) {
                super();
                this.beacon = beacon;
            }

            @Override
            public void run() {
                RichiediPercorsoTask task = new RichiediPercorsoTask();
                task.execute(beacon);
                percorso = task.getResult();
            }

            private ArrayList<Tronco> getResult() { return percorso; }
        }

        ThreadAttesaPercorso attesaPercorso = new ThreadAttesaPercorso(beacon);
        attesaPercorso.start();
        try {
            attesaPercorso.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaPercorso.getResult();
    }

    @Override
    public ArrayList<String> richiediUsciteEdificio(String edificio) {
        class ThreadAttesaUscite extends Thread {

            private ArrayList<String> uscite;
            private String edificio;

            private ThreadAttesaUscite(String edificio) {
                super();
                this.edificio = edificio;
            }

            @Override
            public void run() {
                RichiediUsciteTask task = new RichiediUsciteTask();
                task.execute(edificio);
                uscite = task.getResult();
            }

            private ArrayList<String> getResult() { return uscite; }
        }

        ThreadAttesaUscite attesaUscite = new ThreadAttesaUscite(edificio);
        attesaUscite.start();
        try {
            attesaUscite.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return attesaUscite.getResult();
    }


    public void downloadIniziale(){
        new DownLoadInizialeTask().execute();
    }

    //AsyncTask che richiede la stringa json per il download iniziale di tutto
    private class DownLoadInizialeTask extends AsyncTask<Void,Void,Boolean> {

        private String json= "";
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(Void... params){
            downloaded = false;
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + "/downloaddatabase";
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray edifici = response.getJSONArray("EDIFICI");
                        ContentValues edificiDaAggiungere = new ContentValues();
                        JSONArray piani = response.getJSONArray("PIANI");
                        ContentValues pianiDaAggiungere = new ContentValues();
                        JSONArray aule = response.getJSONArray("AULE");
                        ContentValues auleDaAggiungere = new ContentValues();
                        JSONArray beacon = response.getJSONArray("BEACON");
                        ContentValues beaconDaAggiungere = new ContentValues();
                        JSONArray mappe = response.getJSONArray("MAPPE");
                        ContentValues mappeDaAggiungere = new ContentValues();
                        JSONArray tronchi = response.getJSONArray("TRONCHI");
                        ContentValues tronchiDaAggiungere = new ContentValues();

                        for (int e = 0; e<edifici.length(); e++){
                            String nome = edifici.getString(e);
                            edificiDaAggiungere.put("NOME",nome);}

                        for (int p = 0; p<piani.length(); p++){
                            JSONObject corrente = piani.getJSONObject(p);
                            String nome = corrente.getString("NOME");
                            String edificio = corrente.getString("EDIFICIO");
                            pianiDaAggiungere.put("NOME",nome);
                            pianiDaAggiungere.put("EDIFICIO",edificio);}

                        for (int a = 0; a<aule.length(); a++){
                            JSONObject corrente = aule.getJSONObject(a);
                            String nome = corrente.getString("NOME");
                            String x = corrente.getString("X");
                            String y = corrente.getString("Y");
                            String piano = corrente.getString("PIANO");
                            String entrata = corrente.getString("ENTRATA");
                            auleDaAggiungere.put("NOME",nome);
                            auleDaAggiungere.put("X",x);
                            auleDaAggiungere.put("Y",y);
                            auleDaAggiungere.put("ENTRTA",entrata);}

                        for(int b = 0; b<beacon.length(); b++){
                            JSONObject corrente = beacon.getJSONObject(b);
                            String id = corrente.getString("ID");
                            String x = corrente.getString("X");
                            String y = corrente.getString("Y");
                            String tronco = corrente.getString("TRONCO");
                            String utenti = corrente.getString("UTENTI");
                            String uscita = corrente.getString("USCITA");
                            beaconDaAggiungere.put("ID",id);
                            beaconDaAggiungere.put("X",x);
                            beaconDaAggiungere.put("Y",y);
                            beaconDaAggiungere.put("TRONCO",tronco);
                            beaconDaAggiungere.put("UTENTI",utenti);
                            beaconDaAggiungere.put("USCITA",uscita);}

                        for(int m =0; m<mappe.length();m++){
                            JSONObject corrente = mappe.getJSONObject(m);
                            String piano = corrente.getString("PIANO");
                            String mappa = corrente.getString("MAPPA");
                            mappeDaAggiungere.put("PIANO", piano);
                            mappeDaAggiungere.put("MAPPA",mappa);}

                        for(int t=0; t<tronchi.length(); t++){
                            JSONObject corrente = tronchi.getJSONObject(t);
                            String id = corrente.getString("ID");
                            String x = corrente.getString("X");
                            String y = corrente.getString("Y");
                            String xf = corrente.getString("XF");
                            String yf = corrente.getString("YF");
                            String larghezza = corrente.getString("LARGHEZZA");
                            String piano = corrente.getString("PIANO");
                            String lunghezza = corrente.getString("LUNGHEZZA");
                            tronchiDaAggiungere.put("ID", id);
                            tronchiDaAggiungere.put("X", x);
                            tronchiDaAggiungere.put("Y", y);
                            tronchiDaAggiungere.put("XF", xf);
                            tronchiDaAggiungere.put("YF", yf);
                            tronchiDaAggiungere.put("LARGHEZA", larghezza);
                            tronchiDaAggiungere.put("PIANO", piano);
                            tronchiDaAggiungere.put("LUNGHEZZA", lunghezza);}

                        HashMap<String,ContentValues> hashMap= new HashMap<>();
                        hashMap.put("EDIFICIO",edificiDaAggiungere);
                        hashMap.put("PIANO",pianiDaAggiungere);
                        hashMap.put("AULA",auleDaAggiungere);
                        hashMap.put("BEACON",beaconDaAggiungere);
                        hashMap.put("TRONCO",tronchiDaAggiungere);
                        hashMap.put("MAPPA",mappeDaAggiungere);

                        Database db = new Database(context);
                        db.inserisciValori(hashMap);

                        downloaded = true;
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

            return true;
        }
    }




    //AsyncTask che richiede l'edificio in base all'idbeacon connesso dal Server
    private class RichiediEdificioTask extends AsyncTask<String,Void,Boolean> {

        private String idBeacon;
        private Edificio edificio;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String... idbeacon)
        {
            Log.e("INIZIO TASK","EDIFICIO");
            downloaded = false;
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
                        downloaded = true;
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

            Log.e("FINE TASK","EDIFICIO");

            return true;
        }

        public Edificio getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return edificio;
        }
    }

    //AsyncTask che richiede i piani in base all'edificio al Server
    private class RichiediPianiEdificioTask extends AsyncTask<String,Void,Boolean> {

        private String edificio;
        private boolean downloaded;
        ArrayList<Piano> piani;

        @Override
        protected Boolean doInBackground(String...e) {
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

            return true;
        }

        public ArrayList<Piano> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return piani;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediBeaconTroncoTask extends AsyncTask<Integer,Void,Boolean> {

        private int tronco;
        private HashMap<String,Beacon> beacons;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(final Integer...tronchi) {

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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Prendo l'array "Edificio"
                        JSONArray array = response.getJSONArray(tronco+"");
                        for (int i=0; i < array.length(); i++){
                            JSONObject current = array.getJSONObject(i).getJSONObject("BEACON");

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

           return true;
        }

        public HashMap<String,Beacon> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return beacons;
        }
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiediPianoTask extends AsyncTask<String,Void,Boolean> {

        private String idbeacon;
        private Piano piano;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String...beacon) {
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String nomePiano = response.getString("PIANO_ATTUALE");

                        piano = new Piano(nomePiano);

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

            return true;
        }

        public Piano getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return piano;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediAulePianoTask extends AsyncTask<String,Void,Boolean> {

        private String piano;
        private ArrayList<Aula> aule;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String...pianoI) {
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

            return true;
        }

        public ArrayList<Aula> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return aule;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediTronchiPianoTask extends AsyncTask<String,Void,Boolean> {

        private String piano;
        private ArrayList<Tronco> tronchi;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String...pianoI) {
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

                            int id = current.getInt("ID");
                            PointF inizio = new PointF(Float.parseFloat(current.getString("X")),Float.parseFloat(current.getString("Y")));
                            PointF fine = new PointF(Float.parseFloat(current.getString("XF")),Float.parseFloat(current.getString("YF")));
                            float larghezza = Float.parseFloat(current.getString("LARGHEZZA"));
                            float lunghezza = Float.parseFloat(current.getString("LUNGHEZZA"));

                            tronchi.add(new Tronco(id,inizio, fine, larghezza, lunghezza));
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

            return true;
        }

        public ArrayList<Tronco> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return tronchi;
        }
    }

    //AsyncTask che richiede i beacon in base al tronco dal Server
    private class RichiediMappaPianoTask extends AsyncTask<String,Void,Boolean> {

        private String piano;
        private Bitmap mappa;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String...pianoI) {
            piano = pianoI[0];
            downloaded = false;

            try {
                String src = BASE_URL + SERV_MAPPAPIANO + piano;
                URL url = new URL(src);
                Log.d("URL",url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mappa = BitmapFactory.decodeStream(input);
                downloaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        public Bitmap getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return mappa;
        }
    }

    //AsyncTask che richiede il piano in base all'idbeacon connesso dal Server (PIANOATTUALE)
    private class RichiediPosizioneTask extends AsyncTask<String,Void,Boolean> {

        private String idbeacon;
        private Beacon posizione;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String...beacon) {
            downloaded = false;
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
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject beacon = response.getJSONObject("BEACON");
                        float x = Float.parseFloat(String.valueOf(beacon.getDouble("X")));
                        float y = Float.parseFloat(String.valueOf(beacon.getDouble("Y")));

                        posizione = new Beacon(idbeacon,new PointF(x,y));

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

            return true;
        }

        public Beacon getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return posizione;
        }
    }

    private class RichiediPercorsoTask extends AsyncTask<String,Void,Boolean> {

        private String idBeacon;
        private ArrayList<Tronco> percorso;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String... idbeacon)
        {
            Log.e("INIZIO TASK","PERCORSO");
            downloaded = false;
            idBeacon = idbeacon[0];
            percorso = new ArrayList<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_PERCORSO + idBeacon;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray arrayR = response.getJSONArray("PERCORSO");
                        for(int i=0; i<arrayR.length(); i++) {
                            int id = arrayR.getInt(i);
                            Tronco attuale = null;
                            for(int j=0; j<Posizione.getPianoAttuale().getTronchi().size() && attuale==null; j++) {
                                if(Posizione.getPianoAttuale().getTronchi().get(j).getId()==id) {
                                    attuale = Posizione.getPianoAttuale().getTronchi().get(j);

                                }
                            }
                            percorso.add(attuale);
                        }
                        downloaded = true;
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            Log.e("FINE TASK","PERCORSO");

            return true;
        }

        public ArrayList<Tronco> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return percorso;
        }
    }

    private class RichiediUsciteTask extends AsyncTask<String,Void,Boolean> {

        private String edificio;
        private ArrayList<String> uscite;
        private boolean downloaded;

        @Override
        protected Boolean doInBackground(String... idbeacon)
        {
            downloaded = false;
            edificio = idbeacon[0];
            uscite = new ArrayList<>();
            RequestQueue mRequestQueue;
            //Metodi per il cache delle richieste JSON (Sembra che servano altrimenti non funziona)
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
            //Url per la richiesta del percorso
            String url = BASE_URL + SERV_USCITE + edificio;
            //Instanzio la richiesta JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                //Alla risposta
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray arrayR = response.getJSONArray("USCITE");
                        for(int i=0; i<arrayR.length(); i++) {
                            uscite.add(arrayR.getString(i));
                        }
                        downloaded = true;
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Aggiungo la richiesta alla coda
            mRequestQueue.add(jsonObjectRequest);

            Log.e("FINE TASK","PERCORSO");

            return true;
        }

        public ArrayList<String> getResult() {
            while(!downloaded) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return uscite;
        }
    }

}
