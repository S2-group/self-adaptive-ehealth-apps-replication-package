package nl.vu.ehealthbase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class ThirdPartyAppsData {
    private static final String TAG = "DEBUG:TPAD";
    public ThirdPartyAppsData(){};
    private Context context = MainActivity.getAppContext();
    public void getStayFitData(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("project-25927932621169391")
                .setApplicationId("1:1025952937144:android:004582d345cbe0de")
                .setApiKey("AIzaSyCl2Sm3mhjqaz6MJAPdEfrvm-T7BgfXO_Q")
                //.setDatabaseURL("https://healthkit.firebaseio.com")
                .setStorageBucket("project-25927932621169391.appspot.com")
                .build();
        // Initialize with secondary app
        FirebaseApp.initializeApp(context /* Context */, options, "secondary");

        // Retrieve secondary FirebaseApp
        FirebaseApp secondary = FirebaseApp.getInstance("secondary");
    }

    public void generateSimulationData() {


    }
    private ArrayList<String> getPreferences(Context context){
        ArrayList<String> preference = new ArrayList<>();
      /* UPrefDatabase db = MainActivity.getUserPreferenceDatabase();
        if (db.UserPreferenceDao().checkIfEmpty() != null){
            UserPreference userPreference = db.UserPreferenceDao().getAll();
            preference = userPreference.getUserPreferenceAsList();
            Log.v(TAG, "Preference database " + preference);
        }
*/
        SharedPreferences sharedpreferences =  context.getSharedPreferences(MainActivity.PREFERENCE_NAME, context.MODE_PRIVATE);
        String pref = sharedpreferences.getString("preferences", "Nothing");
        if (pref.equals("Nothing")){
            preference = null;
        }else{
            preference = new ArrayList<>(Arrays.asList(pref.split(",")));
        }
        Log.v(TAG, "Preference list: " + preference);
        return preference;
    }

    public String convertAbstractActivity(String abstractActivity, Context context) {
        //Put two ifs. One for Cardio, the other for strength. Cardio will have the Walk, Run, Cycle
        //Strength will have the push-ups, jumping jacks, walk the stairs
        String activity = "";
        ArrayList<String> preferences = getPreferences(context);
        Log.v(TAG, "List of preferences " + preferences);
        //String[] CardioGroup = {"Walk", "Run", "Cycle", "Jumping jacks"};
        ArrayList<String> CardioGroup = new ArrayList<>();
        CardioGroup.add("Walk");
        CardioGroup.add("Run");
        CardioGroup.add("Cycle");
        CardioGroup.add("Jumping jacks");
        String[] StrengthGroup = {"Push-ups", "Walk the stairs"};
        ArrayList<String> Cardio = new ArrayList<>();
        ArrayList<String> Strength = new ArrayList<>();
        if (!preferences.isEmpty()){
            for (int i = 0; i < preferences.size(); i++){
                String temp = preferences.get(i);
                //Log.v(TAG, "current temp: " + temp);
                if (CardioGroup.contains(temp)){
                    Cardio.add(temp);
                    //Log.v(TAG, "Cardio: " + temp);
                }
                else {
                    Strength.add(temp);
                }
                //Log.v(TAG, "iterator: " + i);
            }
        }
        //Log.v(TAG, String.valueOf(Cardio));
        if (Objects.equals("Cardio", abstractActivity)) {
            if (Cardio.isEmpty()) {
                //activity = CardioGroup[new Random().nextInt(CardioGroup.length)];
                activity = CardioGroup.get(new Random().nextInt(CardioGroup.size()));
            }
            else {
                activity = Cardio.get(new Random().nextInt(Cardio.size()));
            }
        }
        if (Objects.equals("Strength", abstractActivity)) {
            if (Strength.isEmpty()) {
                activity = StrengthGroup[new Random().nextInt(StrengthGroup.length)];
            }
            else {
                activity = Strength.get(new Random().nextInt(Strength.size()));
            }
        }
        if (Objects.equals("None", abstractActivity)) {
            activity = "Nothing for today";
        }
        Log.v(TAG, "Converted abstract activity into: " + activity);
        return activity;
    }

    public String modifyConcreteActivity(String concreteActivity, String currentWeather){
        String activity = "";
        String[] cardioIndoors = {"Jumping jacks"};
        String[] cardioOutdoors = {"Walk", "Run", "Cycle"};
        String[] strengthOutdoors = {"Push-ups"};
        String[] strengthIndoors = { "Walk the stairs"};
        String[] Outdoors = {"Clear", "Clouds"};
        String[] Indoors = {"Rain", "Thunderstorm","Drizzle", "Snow"};
        if (Arrays.asList(Outdoors).contains(currentWeather) &&
                Arrays.asList(strengthIndoors).contains(concreteActivity)){
            activity = strengthOutdoors[new Random().nextInt(strengthOutdoors.length)];
        }
        else if (Arrays.asList(Outdoors).contains(currentWeather) &&
                Arrays.asList(cardioIndoors).contains(concreteActivity)) {
            activity = cardioOutdoors[new Random().nextInt(cardioOutdoors.length)];
        }
        else {
            activity = concreteActivity;
        }

        Log.v(TAG, "Modified concrete activity into: " + activity);
        return activity;
    }
}