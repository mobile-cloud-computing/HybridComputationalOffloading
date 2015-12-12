package fi.cs.ubicomp.detector.wifi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import fi.cs.ubicomp.detector.database.DatabaseHandler;
import fi.cs.ubicomp.detector.utilities.Commons;

/**
 * Created by hflores on 11/12/15.
 */
public class RTTMonitor  extends CloudRemotable implements Runnable, Serializable{

    private final String TAG = RTTMonitor.class.getSimpleName();

    private Context context;

    DatabaseHandler dataEvent;

    float batteryLevel = 0;


    public RTTMonitor(Context context){
        this.context = context;
    }

    public void measureRTT() {

        WifiManager wifiManager = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiManager.getConnectionInfo();
        final String myDeviceMac = wifiInf.getMacAddress();

        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        batteryLevel = getBatteryLevel(context);

        try{
            long startTime = System.currentTimeMillis();
            Vector results = getCloudController().execute("RTT");
            if(results != null){

                Log.d(TAG, "Network connection available " + (System.currentTimeMillis() - startTime));


                dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                        myDeviceMac,
                        Commons.SERVER_NAME,
                        Commons.SERVER_ADDRESS,
                        0,
                        0,
                        1,
                        (System.currentTimeMillis() - startTime),
                        batteryLevel);



            }else{
                Log.d(TAG, "No Network connection available");

                dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                        myDeviceMac,
                        Commons.SERVER_NAME_ERROR,
                        Commons.SERVER_ADDRESS_ERROR,
                        0,
                        0,
                        1,
                        0,
                        batteryLevel);
            }


        }  catch (SecurityException se){
            se.printStackTrace();
        } catch (Throwable th){
            th.printStackTrace();
        }

    }


    @Override
    public void run() {
        measureRTT();
    }

    public float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

}
