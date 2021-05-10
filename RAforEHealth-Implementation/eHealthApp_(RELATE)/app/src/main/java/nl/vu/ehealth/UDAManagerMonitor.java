package nl.vu.ehealth;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class UDAManagerMonitor extends FirebaseMessagingService {
    private static final String TAG = "DEBUG:UDAM-M";
    private Context context = this;
    private UDAManagerAnalyse analyse;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.v(TAG, "Refreshed token: " + token); // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendToServer(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {Log.v(TAG, "Message data payload: "+remoteMessage.getData());}

        if (remoteMessage.getData() != null) {
            Intent notificationIntent = new Intent(getApplicationContext() , MainActivity. class ) ;
            notificationIntent.putExtra( "NotificationMessage" , "I am from Notification" ) ;
            notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
            notificationIntent.setAction(Intent. ACTION_MAIN ) ;
            notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            PendingIntent resultIntent = PendingIntent. getActivity (getApplicationContext() , 0 , notificationIntent , 0 ) ;
            String Title = remoteMessage.getData().get("title");
            String Body = remoteMessage.getData().get("body");
            Log.v(TAG, "Message Notification Body: " + Body);
            Log.v(TAG, "Message Notification Title: " + Title);
            //Re-instantiate uprocess db
            UPDatabase dbp = Room.databaseBuilder(getApplicationContext(),
                    UPDatabase.class, "userProcess-database").allowMainThreadQueries().build();
            //Pass to Analyse
            analyse = new UDAManagerAnalyse(getApplicationContext(), dbp);
            analyse.parseUserProcess(Title, Body, resultIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void DebugNotification(String title, String body, PendingIntent resultIntent){
        analyse.parseUserProcess(title, body, resultIntent);
    }
    private void sendToServer(String token) {

        try {
            //Localhost server
            //URL url = new URL("http://10.0.2.2:5000/");
            //Green lab VM server (used for user trial)
            //URL url = new URL("http://145.108.225.31:5000/");
            //Laptop server (used for in vitro experiments)
            URL url = new URL("http://192.168.1.129:5000/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            JSONObject tokenObject = new JSONObject();
            tokenObject.put("token", token);
            Log.v(TAG, tokenObject.toString());
            dos.write(tokenObject.toString().getBytes());
            //
            //dos.writeBytes(tokenObject.toString());
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Do whatever you want after the
                // token is successfully stored on the server
            }

        } catch (MalformedURLException e) {
            Log.v(TAG, "URL failed");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "Sending token failed" + e);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.v(TAG, "Json failed");
            e.printStackTrace();
        }
    }
}
