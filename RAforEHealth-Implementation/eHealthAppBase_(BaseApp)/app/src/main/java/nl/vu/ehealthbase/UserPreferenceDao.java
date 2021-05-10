package nl.vu.ehealthbase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserPreferenceDao {
    @Query("SELECT * FROM userPreference LIMIT 1")
    UserPreference checkIfEmpty();

    @Query("SELECT * FROM userPreference")
    UserPreference getAll();

    @Query("SELECT * FROM userPreference WHERE uprid IN (:userIds)")
    List<UserPreference> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert
    void insertAll(UserPreference... users);

    @Delete
    void delete(UserPreference user);
}