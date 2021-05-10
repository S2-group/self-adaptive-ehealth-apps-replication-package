package nl.vu.ehealth;
//IDEA: have plan split the activities in array form. Have execute call the EDAManager
// (the full MAPE loop) for each activity in the array. This would allow the simulation to not use
//actual days of the week but 'simulated' days.
//Make the conversion of the abstract activities by calling a function from ThirdPartyAppsData
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class UDAManagerPlan {
    private UPDatabase dbp;
    private Context context;
    private static final String TAG = "DEBUG:UDAM-P";
    public UDAManagerPlan(Context appContext, UPDatabase dbp) {
        this.context = appContext;
        this.dbp = dbp;
    }
    private ThirdPartyAppsData  TPAD;
    private UDAManagerExecute execute;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void convertUserProcess(JSONObject abstractActivities, Object goal, PendingIntent resultIntent) {
        TPAD = new ThirdPartyAppsData();
        String convertedActivities = "";
        JSONObject convertedSchedule = new JSONObject();
        try {
            Iterator<String> keys = abstractActivities.keys();
            while(keys.hasNext()) {
                String currentX = keys.next();
                //send the abstract activity to ThirdPartyAppsData
                String convertedActivity = TPAD.convertAbstractActivity(abstractActivities.getString(currentX), this.context);
                convertedActivities = convertedActivities + " " + currentX + ": " + convertedActivity + ".";
                convertedSchedule.put(currentX, convertedActivity);
            }


        }catch (Exception e) {
            Log.v(TAG, "JSON exception: " + e);
        }
        execute = new UDAManagerExecute(this.context, this.dbp);
        //Log.v(TAG, "Notifiying the user " + convertedActivities);
        execute.notifyUser(convertedActivities, resultIntent);
        Log.v(TAG, "Converted schedule: "+ convertedSchedule);
        execute.saveUserProcess(convertedSchedule);
    }

}
