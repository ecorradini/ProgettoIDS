package it.getout.gestioneconnessioni;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;

import it.getout.Client;
import it.getout.R;

public class NotificaService extends Service {

    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;

    public Server server;
    private Context context;
    private DatagramSocket c;

    public NotificaService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("dio","madonna");
        context = getApplicationContext();
        server = new Server(context);
        // Code to execute when the service is first created
    }

   /*
    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }
    */


    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        Log.i("dio cane","cagna madonna");


        notificationManager = (NotificationManager)
        getSystemService(Context.NOTIFICATION_SERVICE);


        Intent actionIntent = new Intent(context, Client.class);

        PendingIntent pending =
                PendingIntent.getActivity(
                        context,
                        0,
                        actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                builder =
                        new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(server.discoverEmergenza())
                                .setContentIntent(pending)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(server.discoverEmergenza())
                                .setContentIntent(pending)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        Intent notificationIntent = new Intent(this, Client.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

       notificationManager.notify(NOTIFICATION_EX, builder.build());
        Toast.makeText(this, "Started!", Toast.LENGTH_LONG);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Check if there are updates here and notify if true
            }
        }, 0, UPDATE_INTERVAL);
        return START_STICKY;
    }

    private void stopService() {
        if (timer != null) timer.cancel();
    }


}
