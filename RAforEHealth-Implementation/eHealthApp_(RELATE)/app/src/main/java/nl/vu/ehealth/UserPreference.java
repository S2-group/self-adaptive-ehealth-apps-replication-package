package nl.vu.ehealth;
import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;

@Entity
public class UserPreference {
    @PrimaryKey
    public int uprid;

    @ColumnInfo(name = "UserPreference")
    public String userPreference;

    public void setUserPreference(ArrayList<String> preference) {
        this.userPreference = TextUtils.join(",", preference);
    }
    public ArrayList<String> getUserPreferenceAsList(){
        return new ArrayList<>(Arrays.asList(this.userPreference.split(",")));
    }

//    @ColumnInfo(name = "data")
//    public double data;
}
