package nl.vu.ehealthbase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;

public class FirstScreen extends AppCompatActivity {
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    SharedPreferences sharedpreferences;
    ArrayList<String> preferenceList;
    private static final String TAG = "DEBUG: FirstScreen";
    private static UPrefDatabase dbup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);
        sharedpreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        dbup = Room.databaseBuilder(getApplicationContext(),
                UPrefDatabase.class, "userPreference-database").allowMainThreadQueries().build();
        if (dbup.UserPreferenceDao().checkIfEmpty() != null) {
            preferenceList = dbup.UserPreferenceDao().getAll().getUserPreferenceAsList();
        }
        else {
            preferenceList = new ArrayList<>();
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
        onBackPressed();
    }
}
