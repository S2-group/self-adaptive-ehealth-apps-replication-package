package nl.vu.ehealthbase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserProcessDao {
    @Query("SELECT * FROM userProcess LIMIT 1")
    UserProcess checkIfEmpty();

    @Query("SELECT * FROM userProcess")
    UserProcess getAll();

    @Query("SELECT * FROM userProcess WHERE upid IN (:userIds)")
    List<UserProcess> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert
    void insertAll(UserProcess... users);

    @Delete
    void delete(UserProcess user);
}