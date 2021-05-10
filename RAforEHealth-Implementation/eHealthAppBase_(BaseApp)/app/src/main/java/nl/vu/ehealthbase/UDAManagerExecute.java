package nl.vu.ehealthbase;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

public class UDAManagerExecute {
    private UPDatabase dbp;
    private Context context;
    private static final String TAG = "DEBUG:UDAM-E";
    private static final String CHANNEL_ID = "Test";
    private static final int NOTIFICATION_ID = 0;
    private String title = "Activities for this week";
    public UDAManagerExecute(Context applicationContext, UPDatabase dbp) {
        this.dbp = dbp;
        this.context = applicationContext;
    }


    public void notifyUser(String body, PendingIntent resultIntent) {
        Log.v(TAG, "Notifiying the user " + body);
        //TODO:Use this notification system to display the activities to do in execute
        createNotificationChannel();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.context, CHANNEL_ID)
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

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//using same string for both channelId and name. Ideally use different strings
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);// Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void saveUserProcess(JSONObject convertedSchedule) {
        Log.v(TAG, "Converted schedule: "+ convertedSchedule);

        UPDatabase db = this.dbp;
        if (db.UserProcessDao().checkIfEmpty() != null){
            UserProcess userProcess = db.UserProcessDao().getAll();
            db.UserProcessDao().delete(userProcess);
            /*for (UserProcess userProcess:userProcessList)
            {
                db.UserProcessDao().delete(userProcess);
            }*/
        }
        UserProcess userProcess = new UserProcess();
        userProcess.setUserProcess(convertedSchedule);
        db.UserProcessDao().insertAll(userProcess);
        SharedPreferences sharedpreferences =  context.getSharedPreferences("PREFERENCE_DATA", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("convertedSchedule", convertedSchedule.toString());
        editor.apply();
        Log.v(TAG, "Save prefs: " + sharedpreferences.getString("convertedSchedule", "NUll pointer"));

        MainActivity.setConvertedSchedule(convertedSchedule, this.context);
/*
        SharedPreferences sharedpreferences =  this.context.getSharedPreferences("PREFERENCE_DATA", this.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("convertedSchedule", convertedSchedule.toString().replace(",", "\n"));
        editor.apply();
        Log.v(TAG, "Save prefs: " + sharedpreferences.getString("convertedSchedule", "NUll pointer"));
 */
    }
}
