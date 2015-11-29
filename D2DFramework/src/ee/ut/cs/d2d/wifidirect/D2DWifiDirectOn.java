package ee.ut.cs.d2d.wifidirect;

import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by huber on 11/29/15.
 */
public class D2DWifiDirectOn implements WifiDirectState {

    D2DWifiDirect d2DWifiDirect;

    WifiManager wifiManager;

    public D2DWifiDirectOn(D2DWifiDirect d2DWifiDirect){
        this.d2DWifiDirect = d2DWifiDirect;
        this.wifiManager = d2DWifiDirect.getWifiManager();
    }


    @Override
    public void discovery() {

    }

    @Override
    public void idle() {

    }

    @Override
    public void on() {
        wifiManager.setWifiEnabled(true);
        d2DWifiDirect.setD2DWifiDirectState(d2DWifiDirect.getD2DWifiDirectIdle());

    }

    @Override
    public void off() {

    }
}
