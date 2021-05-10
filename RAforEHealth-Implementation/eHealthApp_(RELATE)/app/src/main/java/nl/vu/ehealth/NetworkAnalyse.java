package nl.vu.ehealth;

import android.util.Log;

//ANALYSE: has the connection become unavailable? if there is data that needs to be sent go to plan
public class NetworkAnalyse {
    private AppDatabase db;
    private NetworkPlan plan;
    //For the logs used to debug
    private static final String TAG = "DEBUG:ICM-A";
    //To pass the local Room db pointer as the class is instantiated
    public NetworkAnalyse(AppDatabase db){
        //Pointer to the local Room db
        this.db = db;
        //Instantiate the ICM Plan class in case it needs to be invoked
        this.plan = new NetworkPlan(db);
    }
    //If the network connection is seen available by Monitor
    public void connectionAvailable(){
        Log.v(TAG, "Wifi or mobile data is available");
        //If there is data in the local Room db
        if(db.userDao().checkIfEmpty() != null){
            //Tell Plan that data needs to be send to Execute
            plan.sendData();
        }else{
            //There is no data that has to be sent. No need to invoke Plan
            Log.v(TAG, "No data in the database");
        }
    }
    //If there is no network available
    public void connectionLost(){
        Log.v(TAG, "Wifi and mobile data is off");
        //If there is data available in the local Room db
        if(db.userDao().checkIfEmpty() != null){
            //Tell Plan that a new connection needs to be found
            plan.findNewConnection();
        }else{
            //There is no data that has to sent. No need to invoke Plan
            Log.v(TAG, "No data in the database");
        }

    }
}
