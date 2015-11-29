package ee.ut.cs.d2d.wifidirect;

import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by huber on 11/28/15.
 */
public class D2DWifiDirectDiscovery implements WifiDirectState {

    private static final String TAG = D2DWifiDirectDiscovery.class.getSimpleName();


    D2DWifiDirect d2DWifiDirect;

    WifiP2pManager.Channel wfChannel;
    WifiP2pManager wfManager;


    public D2DWifiDirectDiscovery(D2DWifiDirect d2DWifiDirect){
        this.d2DWifiDirect = d2DWifiDirect;
        this.wfManager = d2DWifiDirect.getWifiDirectManager();
        this.wfChannel = d2DWifiDirect.getWifiDirectChannel();

    }


    @Override
    public void discovery() {
        wfManager.discoverPeers(wfChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Succeed");
            }

            @Override
            public void onFailure(int reasonCode) {
                System.out.println("Failed");
            }
        });

        d2DWifiDirect.setD2DWifiDirectState(d2DWifiDirect.getD2DWifiDirectIdle());

    }

    @Override
    public void idle() {

    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }
}
