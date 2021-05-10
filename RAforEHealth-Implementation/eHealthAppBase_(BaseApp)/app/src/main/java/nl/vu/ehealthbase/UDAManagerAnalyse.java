package nl.vu.ehealthbase;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;


public class UDAManagerAnalyse {
    private UPDatabase dbp;
    private Context context;
    private static final String TAG = "DEBUG:UDAM-A";
    private static final String CHANNEL_ID = "Test";
    private static final int NOTIFICATION_ID = 0;
    public UDAManagerAnalyse(Context appContext, UPDatabase dbp) {
        this.context = appContext;
        this.dbp = dbp;
    }
    private UDAManagerPlan plan;



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void parseUserProcess(String title, String body, PendingIntent resultIntent) {
    //    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    //    Date d = new Date();
    //    String dayOfTheWeek = sdf.format(d);
    //    Log.v(TAG, "Day of the week: " + dayOfTheWeek);
        JSONObject abstractActivities = null;
        Object goal = null;
        try {
            JSONObject jsonObject = new JSONObject(body);
            abstractActivities = jsonObject.getJSONObject("activities");
            Log.v(TAG, "Activities: " + abstractActivities);
            goal = jsonObject.get("goal");
            Log.v(TAG, "Goal: " + goal);
            //plan.convertUserProcess(abstractActivities, goal);

        } catch (Exception e) {
            Log.v(TAG, "JSON exception (this one): " + e);
        }
        plan = new UDAManagerPlan(this.context, this.dbp);
        plan.convertUserProcess(abstractActivities, goal, resultIntent);

    }
}

