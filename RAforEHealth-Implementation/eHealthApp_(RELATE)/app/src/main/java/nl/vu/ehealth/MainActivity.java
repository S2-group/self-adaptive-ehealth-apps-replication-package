package nl.vu.ehealth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.Settings;
import android.location.Location;
import android.content.pm.PackageManager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mongodb.util.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG: MainActivity";
    private static Context applicationContext;
    private static JSONObject convertedSchedule;
    private static Context activityContext;
    private static UPDatabase dbp;
    private static UPrefDatabase dbup;
    ArrayList<String> preferenceList;
    //Variable to get the user location
    LocationManager locationManager;
    //Variable to make a location request
    private static final int REQUEST_LOCATION = 1;
    //Variables to temp store the latitude and longitude
    String latitude, longitude;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    SharedPreferences sharedpreferences;
    Boolean flag;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.applicationContext = getApplicationContext();
        MainActivity.activityContext = this;
        sharedpreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        //TODO: manually delete the database before making it. For testing only!!
        getApplicationContext().deleteDatabase("user-database");
        //getApplicationContext().deleteDatabase("userProcess-database");
        //getApplicationContext().deleteDatabase("userPreference-database");

        //Initialise local database connection (the room database on the phone)
        //TODO: allowMainThreadQueries is only for debug purposes
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user-database").allowMainThreadQueries().build();
        dbp = Room.databaseBuilder(getApplicationContext(),
                UPDatabase.class, "userProcess-database").allowMainThreadQueries().build();
        dbup = Room.databaseBuilder(getApplicationContext(),
                UPrefDatabase.class, "userPreference-database").allowMainThreadQueries().build();

        if (dbup.UserPreferenceDao().checkIfEmpty() != null) {
            if(dbup.UserPreferenceDao().getAll().getUserPreferenceAsList().contains("")){
                preferenceList = new ArrayList<>();
            }
            else {
                preferenceList = dbup.UserPreferenceDao().getAll().getUserPreferenceAsList();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("preferences", TextUtils.join(",", preferenceList));
                editor.apply();
                updateScreen();

            }
        }
        else {
            flag = false;
            startActivity(new Intent(MainActivity.this, FirstScreen.class));
        }

        //TODO: DEBUG: this is only for debug purposes
        DatabaseInitializer.populateSync(db);

        //This if is added to make the connection with mongodb work (not sure it is necessary)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //This code is for getting the current location
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //showLocation = findViewById(R.id.showLocation);
        //btnGetLocation = findViewById(R.id.btnGetLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        if (dbp.UserProcessDao().checkIfEmpty() != null) {
            Log.v(TAG, "Checking the environment with the EDA");
            JSONObject newSchedule = dbp.UserProcessDao().getAll().getUserProcess();
            //new EDAManagerMonitor(newSchedule, getApplicationContext());
        }
        //Comment out for the non adaptive version !!
        //Calling the Internet Connectivity Manager: Monitor
        //NetworkMonitor network = new NetworkMonitor(getApplicationContext(), db);
        //Calling the Smart Objects Manager: Monitor
        //SmartObjectsMonitor smartObject = new SmartObjectsMonitor(this);
        //smartObject.isBluetoothAvailable();


            // Init
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "View is: " + flag);
                    if(flag) {
                        updateScreen();
                    }
                    if (dbp.UserProcessDao().checkIfEmpty() != null) {
                        Log.v(TAG, "Checking the environment with the EDA");
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            OnGPS();
                        } else {
                            getLocation();
                        }
                        JSONObject newSchedule = dbp.UserProcessDao().getAll().getUserProcess();
                        //new EDAManagerMonitor(newSchedule, getApplicationContext());
                    }
                    handler.postDelayed(this, 600000);
                    //For Debug put to 1 sec
                    //handler.postDelayed(this, 1000);

                }
            };

//Start
            handler.postDelayed(runnable, 600000);
        //For Debug put to 1 sec
       //handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onResume(){
        super.onResume();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user-database").allowMainThreadQueries().build();
        dbp = Room.databaseBuilder(getApplicationContext(),
                UPDatabase.class, "userProcess-database").allowMainThreadQueries().build();
        dbup = Room.databaseBuilder(getApplicationContext(),
                UPrefDatabase.class, "userPreference-database").allowMainThreadQueries().build();
        if (dbup.UserPreferenceDao().checkIfEmpty() != null) {
            preferenceList = dbup.UserPreferenceDao().getAll().getUserPreferenceAsList();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("preferences", TextUtils.join(",", preferenceList));
            editor.apply();
            updateScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            flag = false;
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Context getAppContext() {return MainActivity.applicationContext;}
    public static Context getActivityContext() {return MainActivity.activityContext;}
    public static UPDatabase getUserProcessDatabase() {return dbp;}
    public static UPrefDatabase getUserPreferenceDatabase() {return dbup;}

    public static void setConvertedSchedule(JSONObject newSchedule, Context context) {
        SharedPreferences sharedpreferences =  context.getSharedPreferences("PREFERENCE_DATA", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Day", "Nothing");
        editor.apply();
        //new EDAManagerMonitor(newSchedule, context);
    }

    public void updateScreen(){
        flag = true;
        setContentView(R.layout.activity_main);
        String text = sharedpreferences.getString("convertedSchedule", "No activities planned");
        if(text.equals("No activities planned")) {
            TextView textView = (TextView) findViewById(R.id.UserProcess);
            textView.setText(text);
        }else {
            try {
                JSONObject activities = new JSONObject(text);
                TextView monView = (TextView) findViewById(R.id.monday);
                monView.setText("Monday: "+ activities.get("Monday"));
                TextView monDescripionView = (TextView) findViewById(R.id.monday_description);
                monDescripionView.setText(activityDescription((String) activities.get("Monday")));
                TextView tueView = (TextView) findViewById(R.id.tuesday);
                tueView.setText("Tuesday: "+ activities.get("Tuesday"));
                TextView tueDescriptionView = (TextView) findViewById(R.id.tuesday_description);
                tueDescriptionView.setText(activityDescription((String) activities.get("Tuesday")));
                TextView wenView = (TextView) findViewById(R.id.wednesday);
                wenView.setText("Wednesday: "+ activities.get("Wednesday"));
                TextView wenDescriptionView = (TextView) findViewById(R.id.wednesday_description);
                wenDescriptionView.setText(activityDescription((String) activities.get("Wednesday")));
                TextView thuView = (TextView) findViewById(R.id.thursday);
                thuView.setText("Thursday: "+ activities.get("Thursday"));
                TextView thuDescriptionView = (TextView) findViewById(R.id.thursday_description);
                thuDescriptionView.setText(activityDescription((String) activities.get("Thursday")));
                TextView friView = (TextView) findViewById(R.id.friday);
                friView.setText("Friday: "+ activities.get("Friday"));
                TextView friDescriptionView = (TextView) findViewById(R.id.friday_description);
                friDescriptionView.setText(activityDescription((String) activities.get("Friday")));
                TextView satView = (TextView) findViewById(R.id.saturday);
                satView.setText("Saturday: "+ activities.get("Saturday"));
                TextView satDescriptionView = (TextView) findViewById(R.id.saturday_description);
                satDescriptionView.setText(activityDescription((String) activities.get("Saturday")));
                TextView sunView = (TextView) findViewById(R.id.sunday);
                sunView.setText("Sunday: "+ activities.get("Sunday"));
                TextView sunDescriptionView = (TextView) findViewById(R.id.sunday_description);
                sunDescriptionView.setText(activityDescription((String) activities.get("Sunday")));
            }catch(Exception e){
                Log.v(TAG, "Couldn't convert weekly activities to JSON: " + e);
            }
        }
    }

    public String activityDescription(String activity){
        Integer index = Integer.MIN_VALUE;
        String description = "You don't have any suggested activity for today";
        if(activity.contains("Walk") && activity.indexOf("Walk") > index) {
            index = activity.indexOf("Walk");
            description = "Take a short walk outside. Possibly for 10 minutes";
        }
        if(activity.contains("Run") && activity.indexOf("Run") > index) {
            index = activity.indexOf("Run");
            description = "Take a short run outside. Possibly for 5 minutes";
        }
        if(activity.contains("Cycle") && activity.indexOf("Cycle") > index) {
            index = activity.indexOf("Cycle");
            description = "Take a short bicycle ride. Possibly for 10 minutes";
        }
        if(activity.contains("Walk the stairs") && activity.indexOf("Walk the stairs") > index) {
            index = activity.indexOf("Walk the stairs");
            description = "Walk up some stairs. Possibly do it for 5 minutes";
        }
        if(activity.contains("Push-ups") && activity.indexOf("Push-ups") > index) {
            index = activity.indexOf("Push-ups");
            description = "Do some push-ups. We recommend 15 seconds done twice";
        }
        if(activity.contains("Jumping jacks") && activity.indexOf("Jumping jacks") > index) {
            index = activity.indexOf("Jumping jacks");
            description = "Do some jumping jacks. We recommend 15 seconds done three times";
        }
        return description;

    }
    //Create a dialog box to ask the user for permission to use the GPS and get location
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //Get the location of the user and store the latitude and longitude
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            //Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Use my custom function to get location
            Location locationGPS = getLastKnownLocation();
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("latitude", latitude);
                editor.apply();
                editor.putString("longitude", longitude);
                editor.apply();
                Log.v(TAG, "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Log.v(TAG, "Unable to find location");
            }
        }
    }
    LocationManager mLocationManager;
    //Location myLocation = getLastKnownLocation();

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
