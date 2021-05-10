package nl.vu.ehealth;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

public class EDAManagerAnalyse {
    private Context context;
    private static final String TAG = "DEBUG:EDAM-A";
    public EDAManagerAnalyse(Context appContext){this.context = appContext;}

    public void parseWeather(){}

    public void parseEnvironment(JSONObject weather, String dayLongName, JSONObject schedule) {
        try {
            EDAManagerPlan plan = new EDAManagerPlan(dayLongName, schedule, context);
            Log.v(TAG, "weather json: "+ weather);
            Log.v(TAG, "schedule json: "+ schedule);
            Log.v(TAG, "Name of day: "+ dayLongName);

            String activity = schedule.getString(dayLongName);
            Log.v(TAG, "Activity of the day: " + activity);
            plan.checkActivity(activity, weather);

        }catch (Exception e) {
            Log.v(TAG, "JSON exception: " + e);
        }

    }
}
