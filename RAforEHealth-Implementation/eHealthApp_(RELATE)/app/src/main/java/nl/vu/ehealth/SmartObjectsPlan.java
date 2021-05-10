package nl.vu.ehealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//PLAN: If not connected to bluetooth, reconnect. If reconnecting failed notify user.
public class SmartObjectsPlan {
    private SmartObjectsExecute execute = new SmartObjectsExecute();
    private Context context;
    //For the logs used to debug
    private static final String TAG = "DEBUG:SOM-P";

    public SmartObjectsPlan(Context context) {
        this.context = context;
    }

    public void bluetoothConnect(BluetoothAdapter bluetoothAdapter) {
        //Ask the permission ot enable bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        if(!bluetoothAdapter.isEnabled()) {
            Log.v(TAG, "Bluetooth did not connect");

        } else {
            execute.getSOData();
            Log.v(TAG, "Bluetooth reconnected, go to execute");
        }
    }
}