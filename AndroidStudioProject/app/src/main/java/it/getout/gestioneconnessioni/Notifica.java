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

public class Notifica extends Service {

    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;

    private Context context;
    private DatagramSocket d;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        Thread inizioNotifica = new Thread() {
            public void run() {
                Looper.prepare();
                try {
                    d = new DatagramSocket(9601, InetAddress.getByName("0.0.0.0"));
                    //Wait for a response
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    d.receive(receivePacket);
                    //Check if the message is correct
                    String message = new String(receivePacket.getData()).trim();

                    //store flag di inizio emergenza
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Emergenza","vera");
                    editor.apply();

                    creaNotifica(message);
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

    private void creaNotifica(String message) {
        createNotificationChannel();

        Intent intent = new Intent(this,Client.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder;

        //controllo sulla stringa di arrivo per creare il messaggio sulla notifica
        if (message.substring(0,1).equals("E")) {
            builder = new NotificationCompat.Builder(context, "getout")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Evacuare immediatamente.")
                    .setContentTitle(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
        }
        else{
            builder = new NotificationCompat.Builder(context, "getout")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Keep Calm, Emergenza Rientrata")
                    .setContentTitle(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_EX, builder.build());
    }

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
