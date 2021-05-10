package nl.vu.ehealth;

import android.util.Log;
import java.util.List;

//PLAN: Change the mean of connection or store data locally
public class NetworkPlan {
    private AppDatabase db;
    private NetworkExecute execute = new NetworkExecute();
    //For the logs used to debug
    private static final String TAG = "DEBUG:ICM-P";
    //To pass the local Room db pointer as the class is intantiated
    public NetworkPlan(AppDatabase db){
        this.db = db;
    }
    //Tell execute to send the data from the local Room database
    public void sendData() {
        //Debug message
        Log.v(TAG, "Tell the execute to send data");
        //Get all the data stored locally
        List<User> userList = db.userDao().getAll();
        //Pass the data to execute to send to backend
        execute.transferData(userList);
        //Delete the locally stored data
        //TODO: put an if to verify that data was transfered before deleting it
        for (User user:userList)
        {
            db.userDao().delete(user);
        }

    }
    //Tell execute to create a new connection
    //TODO: get an available network and pass it to execute
    // -(Not necessary for this stage of the prototype)
    public void findNewConnection() {
        //Debug message
        Log.v(TAG, "Tell execute to find a new connection");
        execute.connectToNewNetwork();
    }
}
