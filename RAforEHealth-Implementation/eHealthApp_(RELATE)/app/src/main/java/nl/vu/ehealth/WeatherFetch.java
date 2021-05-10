 //Code inspired by https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
//and https://www.tutorialspoint.com/how-to-get-current-location-latitude-and-longitude-in-android
package nl.vu.ehealth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class WeatherFetch {
    private static final String TAG = "DEBUG:WeatherFetch";
    private static final String OPEN_WEATHER_MAP_API_DEFAULT =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API =
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    String latitude, longitude;
    Context context;
    public WeatherFetch(Context context){
        this.context = context;
    }

    public JSONObject getWeatherJSON(){
        JSONObject data;
        try {
            SharedPreferences sharedpreferences =  context.getSharedPreferences("PREFERENCE_DATA", context.MODE_PRIVATE);
            latitude = sharedpreferences.getString("latitude", "Nothing");
            longitude = sharedpreferences.getString("longitude", "Nothing");
            URL url;
            Log.v(TAG, "longitude: " + longitude);
            Log.v(TAG, "latitude: " + latitude);
            if(longitude.equals("Nothing"))  {
                url = new URL(String.format(OPEN_WEATHER_MAP_API_DEFAULT, "Amsterdam"));
                Log.v(TAG, "Using fallback url");
            }
            else {
                url = new URL(String.format(OPEN_WEATHER_MAP_API, this.latitude, this.longitude));
                Log.v(TAG, "Using location finder url");
            }

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key", "f40105bb2f3247ba8b3cf19621a6f443");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                Log.v(TAG, "API call failed");
                return null;
            }
            Log.v(TAG, "Got the weather: " + data);
            return data;
        } catch (Exception e) {
            Log.v(TAG, "JSON exception: " + e);
            return null;
        }
    }

    public String getTestWeather(){
        String[] testWeatherTypes = {"Sun", "Clouds", "Rain", "Thunder"};
        String randomStr = testWeatherTypes[new Random().nextInt(testWeatherTypes.length)];
        return randomStr;
    }

}
