package fi.cs.ubicomp.detector.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fi.cs.ubicomp.detector.database.DatabaseHandler;

/**
 * Created by hflores on 14/01/16.
 */
public class ChargeNotifier extends BroadcastReceiver {

    public final static String TAG = ChargeNotifier.class.getCanonicalName();

    DatabaseHandler dataEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        if (Intent.ACTION_POWER_CONNECTED.equals(action)){
            dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                    "Device plugged to power source.",
                    "-",
                    "-",
                    0,
                    0,
                    0,
                    0,
                    0);

            Log.d(TAG, "Device plugged to power source.");


        }else{
            if (Intent.ACTION_POWER_DISCONNECTED.equals(action)){
                dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                        "Device unplugged from power source.",
                        "-",
                        "-",
                        0,
                        0,
                        0,
                        0,
                        0);

                Log.d(TAG, "Device unplugged from power source.");

            }
        }

    }
}
