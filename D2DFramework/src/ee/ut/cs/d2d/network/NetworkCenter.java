package ee.ut.cs.d2d.network;

import android.content.Context;

import ee.ut.cs.d2d.bluetooth.D2DBluetooth;
import ee.ut.cs.d2d.utilities.Commons;
import ee.ut.cs.d2d.wifidirect.D2DWifiDirect;

/**
 * Created by huber on 11/28/15.
 */
public class NetworkCenter {

    public NetworkDevice getNetworkProvider(Context mContext, String nInterface){

            if (nInterface.equals(Commons.wifiDirect)){
                return new D2DWifiDirect(mContext);

            }else{
                if (nInterface.equals(Commons.bluetooth)){
                    return new D2DBluetooth(mContext);
                }
            }

        return null;
    }

}
