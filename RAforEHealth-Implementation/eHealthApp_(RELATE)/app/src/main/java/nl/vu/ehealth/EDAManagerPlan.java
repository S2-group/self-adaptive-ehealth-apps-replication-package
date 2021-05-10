package nl.vu.ehealth;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class EDAManagerPlan {
    private static final String TAG = "DEBUG:EDAM-P";
    private Context context;
    private ThirdPartyAppsData  TPAD;
    private EDAManagerExecute execute;
    private JSONObject activities;
    private String day;
    public EDAManagerPlan(String currentDay, JSONObject schedule, Context appContext){
        this.day = currentDay;
        this.activities = schedule;
        this.context = appContext;
    }


    public void checkActivity(String activity, JSONObject weatherJSON) {
        Log.v(TAG, "schedule json: "+ this.activities);
        Log.v(TAG, "weather json: "+ weatherJSON);
        String newActivity = "";
        String weather = parseWeatherJSON(weatherJSON);
        TPAD = new ThirdPartyAppsData();
        String modifiedActivity = TPAD.modifyConcreteActivity(activity, weather);
        if (Objects.equals(activity, modifiedActivity)) {
            newActivity = "Today's activity is: " + modifiedActivity;
            execute = new EDAManagerExecute(context);
            execute.notifyUser(newActivity);
        }
        else {
            newActivity = "To better suit your current environment today's activity changed to " + modifiedActivity;
            modifiedActivity = activity + "->" + modifiedActivity;
            execute = new EDAManagerExecute(context);
            execute.notifyUser(newActivity);
            execute.modifySharedPreference(modifiedActivity, this.day, this.activities);
        }
    }

    private String parseWeatherJSON(JSONObject weatherJSON){
        String weather = "";
        try {
            weather = weatherJSON.getJSONArray("weather").getJSONObject(0).getString("main");
        }
        catch(Exception e){
            Log.v(TAG, "JSON exception (this one): " + e);

        }
        return weather;
    }
}
