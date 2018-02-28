package app.android.scc331.rest_test.Services.Firebase;

import android.app.NotificationChannel;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.UUID;

import app.android.scc331.rest_test.R;

public class MyFirebaseListeningService extends FirebaseMessagingService {

    private final String TAG = "FBSERV";
    private final String CHANNEL_ID = "CHAN_NOT_SET";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0){
            //TODO BUILD NOTIFICATIONS
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.sound_icon)
                    .setContentTitle("Reminder")
                    .setContentText(remoteMessage.getData().get("message"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            Random r = new Random();
            int id = r.nextInt();

            notificationManager.notify(id, mBuilder.build());

        }
    }
}
