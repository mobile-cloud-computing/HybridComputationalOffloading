package ee.ut.cs.d2d.wifidirect;

import android.app.Activity;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

/**
 * Created by huber on 11/29/15.
 */
public class D2DWifiDirectConnection {

    WifiP2pManager wfManager;
    WifiP2pManager.Channel wfChannel;
    WifiP2pDevice wfDevice;

    Activity activity;


    public D2DWifiDirectConnection(Activity activity, WifiP2pManager wfManager, WifiP2pManager.Channel wfChannel, WifiP2pDevice wfDevice){
        this.wfManager = wfManager;
        this.wfChannel = wfChannel;
        this.wfDevice = wfDevice;

        this.activity = activity;

    }

    public void connect(){

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wfDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wfManager.connect(wfChannel, config, new WifiP2pManager.ActionListener(){

            @Override
            public void onSuccess(){
                Toast.makeText(activity, "Connection initiation succeeded...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason){
                Toast.makeText(activity, "Connection initiation failed...", Toast.LENGTH_SHORT).show();
            }


        });


    }

}
