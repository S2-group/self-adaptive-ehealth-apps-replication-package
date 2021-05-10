package nl.vu.ehealth;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;


//MONITOR: Connection status on/off with smart object?
public class SmartObjectsMonitor {
    private SmartObjectsAnalyse analyse;
    //For the logs used to debug
    private static final String TAG = "DEBUG:SOM-M";

    public SmartObjectsMonitor(Context context){
        this.analyse = new SmartObjectsAnalyse(context);
    }

    public void isBluetoothAvailable() {
        //Get bluetooth adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If it is null, then there is no bluetooth capability
        if (bluetoothAdapter != null) {
            //There is bluetooth capability
            analyse.isBluetoothOn(bluetoothAdapter);
            //Log for debugging
            Log.v(TAG, "This device has bluetooth capabilities, go to analyse");
        }
        // Device doesn't support Bluetooth
        else {
            //Log for debugging
            Log.v(TAG, "This device does not have bluetooth capabilities");
        }
    }


}
