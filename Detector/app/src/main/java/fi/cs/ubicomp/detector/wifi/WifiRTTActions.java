package fi.cs.ubicomp.detector.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hflores on 11/12/15.
 */
public class WifiRTTActions extends BroadcastReceiver {

    public final static String TAG = WifiRTTActions.class.getCanonicalName();

    public static final String RTT_INTENT = "fi.cs.ubicomp.intent.action.RTT";

    @Override
    public void onReceive(Context context, Intent intent) {
          new Thread(
                new RTTMonitor(context)
        ).start();

    }
}
