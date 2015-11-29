package ee.ut.cs.d2d.wifidirect;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;

import ee.ut.cs.d2d.network.NetworkDevice;

/**
 * Created by huber on 11/28/15.
 */
public class D2DWifiDirect implements NetworkDevice {

    private static final String TAG = D2DWifiDirect.class.getSimpleName();

    WifiDirectState wfDiscovery;
    WifiDirectState wfIdle;
    WifiDirectState wfOn;
    WifiDirectState wfOff;

    //initial state
    WifiDirectState wfState;

    private Context context;

    WifiP2pManager wfManager;
    WifiP2pManager.Channel wfChannel;

    WifiManager wifiManager;


    public D2DWifiDirect(Context context){
        this.context = context;
        wfManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        wfChannel = wfManager.initialize(context, context.getMainLooper(), null);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        this.wfIdle = new D2DWifiDirectIdle(this);
        this.wfOn = new D2DWifiDirectOn(this);
        //this.wfOff = new D2DWifiDirectOff(this);
        this.wfDiscovery = new D2DWifiDirectDiscovery(this);

        wfState = wfIdle;

    }

    public void setD2DWifiDirectState(WifiDirectState wfState){
        this.wfState = wfState;
    }

    public WifiDirectState getD2DWifiDirectState(){
        return this.wfState;
    }

    public WifiDirectState getD2DWifiDirectDiscovery(){
        return  this.wfDiscovery;
    }

    public WifiDirectState getD2DWifiDirectIdle(){
        return this.wfIdle;
    }

    public WifiDirectState getD2DWifiDirectOn(){
        return this.wfOn;
    }

    public WifiDirectState geD2DtWifiDirectOff(){
        return this.wfOff;
    }

    public WifiP2pManager getWifiDirectManager(){
        return this.wfManager;
    }

    public WifiP2pManager.Channel getWifiDirectChannel(){
        return  this.wfChannel;
    }

    public Context getContext(){
        return this.context;
    }


    public WifiManager getWifiManager(){
        return this.wifiManager;
    }


    @Override
    public void D2DIdle() {

    }

    @Override
    public void D2DOn() {

    }

    @Override
    public void D2DOff() {

    }

    @Override
    public void D2DDiscovery() {
        wfState.discovery();
    }
}
