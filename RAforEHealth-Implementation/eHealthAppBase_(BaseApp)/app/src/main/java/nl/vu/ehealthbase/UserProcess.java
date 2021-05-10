package nl.vu.ehealthbase;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class UserProcess {
        private static final String TAG = "UserProcess";
        @PrimaryKey
        public int upid;

        @ColumnInfo(name = "UserProcess")
        public String userProcess;


        public void setUserProcess(JSONObject uProcess) {
            this.userProcess = uProcess.toString();
        }

        public JSONObject getUserProcess() {
                try {
                        return new JSONObject(this.userProcess);
                } catch (JSONException e) {
                        Log.v(TAG, "JSON exception: " + e);
                        return null;
                }
        }
//    @ColumnInfo(name = "data")
//    public double data;
}
