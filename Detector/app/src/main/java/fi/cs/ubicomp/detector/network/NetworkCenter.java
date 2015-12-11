package fi.cs.ubicomp.detector.network;

import android.content.Context;

import fi.cs.ubicomp.detector.bluetooth.D2DBluetooth;
import fi.cs.ubicomp.detector.utilities.Commons;
import fi.cs.ubicomp.detector.wifidirect.D2DWifiDirect;


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
