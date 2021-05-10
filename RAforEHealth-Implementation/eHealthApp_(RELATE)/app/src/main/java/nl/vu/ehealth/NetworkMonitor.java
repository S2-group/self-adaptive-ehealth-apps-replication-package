/*Observation so far:
App crashes on API 22. Should be due to registerDefaultNetworkCallback that needs API 24
-with emulator: pc wifi on: if I put airplain mode i get the onLost, if either the wifi or mobile data is on I get onAvailable
                pc wifi is off: I get the same as above. Could be that the methods don't check for internet connectivity
-with phone: have to try it.
 */
//MONITOR: How is the connection quality? Is there a connection or not?
package nl.vu.ehealth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;
//@RequiresApi(api = Build.VERSION_CODES.N)

public class NetworkMonitor{
    //For the current application context (getApplicationContext)
    private Context context;
    //Instantiate the ICM-Analyse class, so that Monitor can use the functions if necessary
    private NetworkAnalyse analyse;
    //For the logs used to debug
    private static final String TAG = "DEBUG:ICM-M";
    //To pass the context as the class is instantiated and the local db for the NetworkAnalyse class
    public NetworkMonitor(Context context, AppDatabase db){
        //The application context passed by the main activity
        this.context = context;
        //Making an instance of NetworkAnalyse
        this.analyse = new NetworkAnalyse(db);
        try{
            ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //Comment the line underneath (for Samsung) or write it as such NetworkRequest.Builder builder=new NetworkRequest.Builder();
            NetworkRequest builder=new NetworkRequest.Builder().build();

            //Comment the line underneath (for Samsung)
            assert connectivityManager != null;
            //Write line underneath as such (for Samsung) connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
            connectivityManager.registerNetworkCallback(builder, new ConnectivityManager.NetworkCallback(){
                                                                   //This function is called when a network is available to the app
                                                                   @Override
                                                                   public void onAvailable(Network network){
                                                                       //Debug message
                                                                       Log.v(TAG, "Wifi or mobile data is available");
                                                                       //Call Analyse to see if there is data to send
                                                                       analyse.connectionAvailable();

                                                                   }
                                                                   //This function is called when there is no network available
                                                                   @Override
                                                                   public void onLost(Network network){
                                                                       //Debug message
                                                                       Log.v(TAG, "Wifi and mobile data is lost");
                                                                       //Call Analyse to see if the loop should go to plan
                                                                       analyse.connectionLost();

                                                                   }
                                                               }
            );
            //Wasn´t able to use the ConnectivityManager API
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "Couldn't use ConnectivityManager");
        }
    }


/*
//The monitoring function of the class
public void registerNetworkCallback(){

    }
 */
}
