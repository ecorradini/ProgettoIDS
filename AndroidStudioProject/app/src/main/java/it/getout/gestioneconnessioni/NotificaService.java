package it.getout.gestioneconnessioni;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import it.getout.Client;
import it.getout.R;

public class NotificaService extends Service {

    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;

    public Server server;
    private Context context;
    private DatagramSocket d;

    public NotificaService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("edo1","edo1");
        context = getApplicationContext();

        // Code to execute when the service is first created
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

                    Log.e("edo3", "edo3");

                    d.receive(receivePacket);

                    Log.e("edo4", "edo4");

                    //Check if the message is correct
                    //QUESTA è DA MODIFICARE PER LEGGERE IL JSON CHE FARà PARTIRE EMERGENZA
                    String message = new String(receivePacket.getData()).trim();

                    creaNotifica(message);

                    Log.e("message", message);
                    //Close the port!
                    d.close();

                    //return message;

                }catch (IOException ex) {
                    Log.i("IP","problema nel ricercare server");
                }
            }
        };
        inizioNotifica.start();


        Log.i("edo2","edo2");

        //creazione notifica funziona !!!

        return START_STICKY;
    }

    private void creaNotifica(String message) {
        createNotificationChannel();

        Intent intent = new Intent(this,Client.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder =
                    new NotificationCompat.Builder(context, "getout")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("GetOut")
                            .setContentText(message)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else {
            builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("GetOut")
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_EX, builder.build());
    }

    //Solo per Android >= 8.0
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
