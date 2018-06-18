package it.getout.gestioneconnessioni;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.Timer;
import it.getout.Client;
import it.getout.R;

/**
 * La classe è un service che si occupa di restare in ascolto di comunicazioni dal server (inizio e rientro emergenza)
 * e della creazione della relativa notifica push
 */
public class Notifica extends Service {

    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;

    private Context context;
    private DatagramSocket d; //oggetto che si occupa della comunicazione col server

    /**
     * costruttore della classe
     */
    public Notifica() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // Code to execute when the service is first created
       context = getApplicationContext();
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * la classe, che si attiva dopo l'evento specifico, si occupa di avviare un thread per l'apertura della porta 9601
     * e quindi della ricezione delle comunicazione di inizio e rientro software.
     * @param intent
     * @param flags
     * @param startid
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        Thread inizioNotifica = new Thread() { //definizione del thread
            public void run() {
                Looper.prepare();
                try {
                    d = new DatagramSocket(9601, InetAddress.getByName("0.0.0.0")); //apertura della porta 9601
                    //Wait for a response
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length); //definizione del contenitore in cui verrà inserito il dato ricevuto
                    d.receive(receivePacket); //ricezione del messaggio

                    String message = new String(receivePacket.getData()).trim(); //estrazione del messaggio dalla struttura di ricezione

                    creaNotifica(message); //richiamo il metodo per la creazione della notifica push
                    //Close the port!
                    d.close();

                }catch (IOException ex) {
                    Log.i("IP","problema nel ricercare server");
                }
            }
        };
        inizioNotifica.start();

        return START_STICKY;
    }

    /**
     * metodo che crea e visualizza la notifica push
     * @param message può essere o di rientro o di inizio emergenza
     */
    private void creaNotifica(String message) {
        createNotificationChannel();

        Intent intent = new Intent(this,Client.class); //ritorno del controllo all'activity principale
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder; //instanzio classe builder

        //controllo sulla stringa di arrivo per creare il messaggio sulla notifica
        if (message.substring(0,1).equals("E")) {

            //store flag di inizio emergenza
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Emergenza",true);
            editor.apply();

            Log.e("MODALITA", Boolean.toString(preferences.getBoolean("Emergenza", false)));

            builder = new NotificationCompat.Builder(context, "getout")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Evacuare immediatamente.")
                    .setContentTitle(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
        }
        else{

            //store flag di inizio emergenza
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Emergenza",false);
            editor.apply();

            Log.e("MODALITA", Boolean.toString(preferences.getBoolean("Emergenza", false)));

            builder = new NotificationCompat.Builder(context, "getout")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Keep Calm, Emergenza Rientrata")
                    .setContentTitle(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_EX, builder.build()); //visualizzazione della notifica
    }

    /**
     * creazione del canale per la visualizzazione della notifica su schermo
     */
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GetOut";
            String description = "emergenza";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("getout",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
