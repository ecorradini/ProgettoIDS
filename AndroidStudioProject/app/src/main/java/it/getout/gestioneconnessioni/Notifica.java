package it.getout.gestioneconnessioni;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import it.getout.Client;
import it.getout.R;

public class Notifica extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 5555;

    @Override
    public void onReceive(Context context, Intent intent) {

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
                    new NotificationCompat.Builder(context.getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("EMERGENZA: SCAPPA")
                            .setContentIntent(pending)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else {
            builder =
                    new NotificationCompat.Builder(context.getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("EMERGENZA: SCAPPA")
                            .setContentIntent(pending)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }





        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());

    }
}
