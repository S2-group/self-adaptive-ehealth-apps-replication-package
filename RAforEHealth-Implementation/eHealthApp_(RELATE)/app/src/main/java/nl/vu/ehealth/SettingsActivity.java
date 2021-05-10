package nl.vu.ehealth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    SharedPreferences sharedpreferences;
    ArrayList<String> preferenceList;
    private static final String TAG = "DEBUG: SettingsActivity";
    private static UPrefDatabase dbup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sharedpreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        dbup = Room.databaseBuilder(getApplicationContext(),
                UPrefDatabase.class, "userPreference-database").allowMainThreadQueries().build();
        if (dbup.UserPreferenceDao().checkIfEmpty() != null) {
            if(dbup.UserPreferenceDao().getAll().getUserPreferenceAsList().contains("")){
                preferenceList = new ArrayList<>();
            }
            else {
                preferenceList = dbup.UserPreferenceDao().getAll().getUserPreferenceAsList();
            }
        }
        else {
            preferenceList = new ArrayList<>();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Settings");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    /*Called when the user uses the Change Preference button */
    public void onClickChange(View view) {
        setContentView(R.layout.preferences);
        if (sharedpreferences != null) {
            CheckBox walk = (CheckBox) findViewById(R.id.checkbox_walk);
            walk.setChecked(sharedpreferences.getBoolean("walk", false));
            CheckBox run = (CheckBox) findViewById(R.id.checkbox_run);
            run.setChecked(sharedpreferences.getBoolean("run", false));
            CheckBox cycle = (CheckBox) findViewById(R.id.checkbox_cycle);
            cycle.setChecked(sharedpreferences.getBoolean("cycle", false));
            CheckBox jumping = (CheckBox) findViewById(R.id.checkbox_jumping);
            jumping.setChecked(sharedpreferences.getBoolean("jumping", false));
            CheckBox walkStairs = (CheckBox) findViewById(R.id.checkbox_walkStairs);
            walkStairs.setChecked(sharedpreferences.getBoolean("walkStairs", false));
            CheckBox push = (CheckBox) findViewById(R.id.checkbox_push);
            push.setChecked(sharedpreferences.getBoolean("push", false));
        }

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_walk:
                if (checked) {
                    preferenceList.add("Walk");
                }
                else {
                    preferenceList.remove("Walk");
                }
                break;
            case R.id.checkbox_run:
                if (checked) {
                    preferenceList.add("Run");
                }
                else {
                    preferenceList.remove("Run");
                }
                break;
            case R.id.checkbox_cycle:
                if (checked) {
                    preferenceList.add("Cycle");
                }
                else {
                    preferenceList.remove("Cycle");
                }
                break;
            case R.id.checkbox_jumping:
                if (checked) {
                    preferenceList.add("Jumping jacks");
                }
                else {
                    preferenceList.remove("Jumping jacks");
                }
                break;
            case R.id.checkbox_walkStairs:
                if (checked) {
                    preferenceList.add("Walk the stairs");
                }
                else {
                    preferenceList.remove("Walk the stairs");
                }
                break;
            case R.id.checkbox_push:
                if (checked) {
                    preferenceList.add("Push-ups");
                }
                else {
                    preferenceList.remove("Push-ups");
                }
                break;
        }
        Log.v(TAG, "List of preferences " + preferenceList);

    }
    public void savePreferences(View view) {
        // Do something in response to button click
        if (dbup.UserPreferenceDao().checkIfEmpty() != null){
            UserPreference userPreference = dbup.UserPreferenceDao().getAll();
            dbup.UserPreferenceDao().delete(userPreference);
        }
        UserPreference userPreference = new UserPreference();
        userPreference.setUserPreference(preferenceList);
        dbup.UserPreferenceDao().insertAll(userPreference);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("preferences", TextUtils.join(",", preferenceList));
        editor.apply();
        if(preferenceList.contains("Walk")){editor.putBoolean("walk", true); editor.apply();}
        else{editor.putBoolean("walk", false); editor.apply();}
        if(preferenceList.contains("Run")) {editor.putBoolean("run", true); editor.apply();}
        else{editor.putBoolean("run", false); editor.apply();}
        if(preferenceList.contains("Cycle")){editor.putBoolean("cycle", true); editor.apply();}
        else{editor.putBoolean("cycle", false); editor.apply();}
        if(preferenceList.contains("Jumping jacks")){editor.putBoolean("jumping", true); editor.apply();}
        else{editor.putBoolean("jumping", false); editor.apply();}
        if(preferenceList.contains("Walk the stairs")){editor.putBoolean("walkStairs", true); editor.apply();}
        else{editor.putBoolean("walkStairs", false); editor.apply();}
        if(preferenceList.contains("Push-ups")){editor.putBoolean("push", true); editor.apply();}
        else{editor.putBoolean("push", false); editor.apply();}
        Log.v(TAG, "List of preferences " + preferenceList);
        Log.v(TAG, "Database of preferences " + MainActivity.getUserPreferenceDatabase().UserPreferenceDao().getAll().getUserPreferenceAsList());
        setContentView(R.layout.settings_activity);
    }
    public void onClickAbout(View view){
        setContentView(R.layout.about);
        TextView textView = (TextView) findViewById(R.id.About);
        textView.setText("This app was developed by PhD candidate Eoin Grua. For this study " +
                "you are to use this app on a daily basis. Remember to complete the given to " +
                "you questionnaire at the end of each day. If you have any questions or doubts " +
                "feel free to send me an e-mail at: e.m.grua@vu.nl" + "\nThank you for your participation");
    }

    /*Called when the user uses the Back button */
    public void backToMain(View view){
        setContentView(R.layout.settings_activity);
    }

}