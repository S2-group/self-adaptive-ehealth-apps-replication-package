package nl.vu.ehealth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class EDAManagerMonitor {
    private Context context;
    private static final String TAG = "DEBUG:EDAM-M";
    private WeatherFetch weatherFetch;
    private EDAManagerAnalyse analyse;
    public EDAManagerMonitor(JSONObject schedule, Context appContext){
        this.context = appContext;
        this.weatherFetch = new WeatherFetch(context);
        this.analyse = new EDAManagerAnalyse(context);
        //To test
        //String testWeather = getWeather();
        //For real weather

        Log.v(TAG, "Current schedule: " + schedule);
        SharedPreferences sharedpreferences =  context.getSharedPreferences("PREFERENCE_DATA", context.MODE_PRIVATE);

        //Log.v(TAG, testWeather);
        Calendar sCalendar = Calendar.getInstance();
        String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String savedDay = sharedpreferences.getString("Day", "Nothing");
        Log.v(TAG, "The current day of the week is: " + dayLongName);
        Log.v(TAG, "Saved day: " + savedDay);
        if(!dayLongName.equals(savedDay)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Day", dayLongName);
            editor.apply();
            JSONObject weather = getWeather();
            analyse.parseEnvironment(weather, dayLongName, schedule);
        }

    }

    private JSONObject getWeather(){
        //To test
        //String  weather = weatherFetch.getTestWeather();
        //To get the actual weather
        JSONObject weather = weatherFetch.getWeatherJSON();
        //Log.v(TAG, "The current weather is: " + weather);
        //For the real application
        //weather.getJSON();
        return weather;
    }

}
