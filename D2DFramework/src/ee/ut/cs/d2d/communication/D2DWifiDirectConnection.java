package ee.ut.cs.d2d.communication;

import android.app.Activity;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


/**
 * Created by hflores on 01/12/15.
 */
public class D2DWifiDirectConnection implements Runnable {

    private final String TAG = D2DWifiDirectConnection.class.getSimpleName();

    private Activity activity;
    WifiP2pManager wfManager;
    WifiP2pDevice wfDevice;
    WifiP2pManager.Channel wfChannel;
    WifiP2pManager.ConnectionInfoListener wfPeerConnectionListener;

    public D2DWifiDirectConnection(Activity activity, WifiP2pManager wfManager, WifiP2pManager.Channel wfChannel, WifiP2pDevice wfDevice,
                                   WifiP2pManager.ConnectionInfoListener wfPeerConnectionListener ){
        this.activity = activity;
        this.wfManager = wfManager;
        this.wfDevice = wfDevice;
        this.wfChannel = wfChannel;
        this.wfPeerConnectionListener =wfPeerConnectionListener;


    }

    public void connect(){

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wfDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wfManager.connect(wfChannel, config, new WifiP2pManager.ActionListener(){

            @Override
            public void onSuccess(){
                Log.d(TAG, "Connection initiation succeeded...");
            }

            @Override
            public void onFailure(int reason){
                Log.d(TAG, "Connection initiation failed...");
            }


        });

    }


    @Override
    public void run() {
        connect();
    }
}
