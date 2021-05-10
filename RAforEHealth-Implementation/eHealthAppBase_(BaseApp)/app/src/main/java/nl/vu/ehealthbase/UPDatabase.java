package nl.vu.ehealthbase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserProcess.class}, version = 1, exportSchema = false)
public abstract class UPDatabase extends RoomDatabase {
    public abstract UserProcessDao UserProcessDao();
}