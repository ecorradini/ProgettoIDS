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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;


import it.getout.Client;
import it.getout.R;

public class NotificaService extends Service {

    //private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;
    private String message;
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

                try {
                    d = new DatagramSocket(9601);
                    //Wait for a response
                    byte[] recvBuf = new byte[500];    //DA CONTROLLARE VALORE 500
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

                    Log.e("edo1", "edo1");

                    d.receive(receivePacket);

                    Log.e("edo2", "edo2");

                    //Check if the message is correct

                    message = new String(receivePacket.getData()).trim();

                    Log.e("message", message);

                    if (message.equals("GETOUT EMERGENZA A: Ingegneria")) {
                        message = "EDOARDOOOO";
                    }
                    //Close the port!
                    d.close();

                }catch (IOException ex) {
                    Log.i("IP","problema nel ricercare server");
                }
            }
        };
        //try {
        //    inizioNotifica.join();
        //}catch (InterruptedException e){
        //    e.printStackTrace();
        //}

        Log.i("edo3","edo3");

        inizioNotifica.start();


            /*
                try {
                    Thread.sleep(10000);
                    try {
                        d = new DatagramSocket(8080, InetAddress.getByName("0.0.0.0"));
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    //Wait for a response
                    byte[] recvBuf = new byte[15];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

                    try {

                        d.receive(receivePacket);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    String m = new String(receivePacket.getData()).trim();
                    message[0] = m;

                    if (!m.equals("GETOUT EMERGENZA A: Ingegneria")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        m = null;
                    }
                    //Close the port!
                    d.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            */

        //creazione notifica funziona !!!

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

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder =
                    new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(message+"ENRIMERDA")
                            .setContentIntent(pending)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else {
            builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(message+"ENRIMERDA")
                            .setContentIntent(pending)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }


        Intent notificationIntent = new Intent(this, Client.class);
        /*
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        */

       notificationManager.notify(NOTIFICATION_EX, builder.build());  //MOSTRA LA NOTIFICA
        //Toast.makeText(this, "Started!", Toast.LENGTH_LONG);
        /*
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Check if there are updates here and notify if true
            }
        }, 0, UPDATE_INTERVAL);
        */
        return START_STICKY;

    }


    private void stopService() {
        if (timer != null) timer.cancel();
    }


}
