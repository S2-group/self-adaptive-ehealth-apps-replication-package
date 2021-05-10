package nl.vu.ehealth;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserPreference.class}, version = 1, exportSchema = false)
public abstract class UPrefDatabase extends RoomDatabase {
    public abstract UserPreferenceDao UserPreferenceDao();
}