package nl.vu.ehealth;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

//ANALYSE: Has the connection status changed?
public class SmartObjectsAnalyse {
    private SmartObjectsPlan plan;
    private SmartObjectsExecute execute = new SmartObjectsExecute();
    //For the logs used to debug
    private static final String TAG = "DEBUG:SOM-A";

    public SmartObjectsAnalyse(Context context){
        this.plan = new SmartObjectsPlan(context);
        //this.context = context;
    }

    public void isBluetoothOn(BluetoothAdapter bluetoothAdapter) {
            //If bluetooth is not on, ask the user to allow the app to turn it on
            if (!bluetoothAdapter.isEnabled()) {
                plan.bluetoothConnect(bluetoothAdapter);
                //Log for debugging
                Log.v(TAG, "The bluetooth is not enabled, go to plan");
            }
            else {
                execute.getSOData();
                //Log for debugging
                Log.v(TAG, "The bluetooth is enabled, go to execute");
            }
    }
}


