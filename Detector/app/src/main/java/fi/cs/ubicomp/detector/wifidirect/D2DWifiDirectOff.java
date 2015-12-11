package fi.cs.ubicomp.detector.wifidirect;

import android.net.wifi.WifiManager;

/**
 * Created by huber on 11/29/15.
 */
public class D2DWifiDirectOff implements WifiDirectState {

    D2DWifiDirect d2DWifiDirect;

    WifiManager wifiManager;

    public D2DWifiDirectOff(D2DWifiDirect d2DWifiDirect){
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


    }

    @Override
    public void off() {
        wifiManager.setWifiEnabled(false);
        d2DWifiDirect.setD2DWifiDirectState(d2DWifiDirect.getD2DWifiDirectIdle());
    }
}

