//Code inspired by: https://github.com/pocmo/SensorDashboard
package nl.vu.ehealth;
import android.content.Context;
import android.util.Log;
import android.net.Uri;
import android.provider.Settings;


import androidx.core.app.NotificationCompat;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

//import io.realm.Realm;

//EXECUTE: If not connected or notification ignore, find different data source
public class SmartObjectsExecute {
    //For the logs used to debug
    private static final String TAG = "DEBUG:SOM-E";
    public static final String ACCURACY = "accuracy";
    public static final String TIMESTAMP = "timestamp";
    public static final String VALUES = "values";
    public SmartObjectsExecute(){};

    public void getSOData(){new WearableListenerService(){
        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            Log.d(TAG, "onDataChanged()");

            for (DataEvent dataEvent : dataEvents) {
                if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                    DataItem dataItem = dataEvent.getDataItem();
                    Uri uri = dataItem.getUri();
                    String path = uri.getPath();

                    if (path.startsWith("/sensors/")) {
                        unpackSensorData(
                                Integer.parseInt(uri.getLastPathSegment()),
                                DataMapItem.fromDataItem(dataItem).getDataMap()
                        );
                    }
                }
            }
        }

    };
    }



    private void unpackSensorData(int sensorType, DataMap dataMap) {
        int accuracy = dataMap.getInt(ACCURACY);
        long timestamp = dataMap.getLong(TIMESTAMP);
        float[] values = dataMap.getFloatArray(VALUES);

        Log.d(TAG, "Received sensor data");

        //sensorManager.addSensorData(sensorType, accuracy, timestamp, values);
        //TODO: add data to Room. Need to implement correct user. properties
        //db.userDao().insertAll(user);
        Log.d(TAG, "Saved sensor data in Room");

    }

}
