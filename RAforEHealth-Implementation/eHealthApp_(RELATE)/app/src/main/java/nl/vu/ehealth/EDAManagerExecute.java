package nl.vu.ehealth;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;


public class EDAManagerExecute {
    private Context context;
    private static final String TAG = "DEBUG:EDAM-E";
    private static final String CHANNEL_ID = "Test2";
    private static final int NOTIFICATION_ID = 1;
    private String title = "Activities for today";
    public EDAManagerExecute(Context applicationContext) {
        this.context = applicationContext;
    }

    public void notifyUser(String body) {
        Log.v(TAG, "Notifiying the user " + body);
        Intent notificationIntent = new Intent(context , MainActivity.class) ;
        notificationIntent.putExtra( "NotificationMessage" , "I am from Notification" ) ;
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        PendingIntent resultIntent = PendingIntent. getActivity (context , 0 , notificationIntent , 0 ) ;
        createNotificationChannel();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .setContentText(body)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(resultIntent)
                        .setAutoCancel(true);

        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//using same string for both channelId and name. Ideally use different strings
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);// Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void modifySharedPreference(String modifiedActivity, String day, JSONObject schedule) {
        JSONObject convertedSchedule = schedule;
        try {
            convertedSchedule.put(day, modifiedActivity);
        }catch (Exception e) {
        Log.v(TAG, "JSON exception: " + e);
    }
        SharedPreferences sharedpreferences =  context.getSharedPreferences("PREFERENCE_DATA", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("convertedSchedule", convertedSchedule.toString());
        editor.apply();
        Log.v(TAG, "Save prefs: " + sharedpreferences.getString("convertedSchedule", "NUll pointer"));
    }
}
