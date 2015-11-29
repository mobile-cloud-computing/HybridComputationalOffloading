package ee.ut.cs.d2d.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;


/**
 * Created by huber on 11/3/15.
 */
public class D2DWifiDirectActions extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    //private D2DWifi mActivity;

    WifiP2pManager.PeerListListener mPeerListListener;
    WifiP2pManager.ConnectionInfoListener mPeerConnectionListener;

    public D2DWifiDirectActions(WifiP2pManager manager, WifiP2pManager.Channel channel, /*D2DWifi activity,*/ WifiP2pManager.PeerListListener mPeerListListener, WifiP2pManager.ConnectionInfoListener mPeerConnectionListener) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        //this.mActivity = activity;
        this.mPeerListListener = mPeerListListener;
        this.mPeerConnectionListener = mPeerConnectionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            System.out.println("1");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                System.out.println("Wifi direct is enabled");
            } else {
                // Wi-Fi P2P is not enabled
                System.out.println("Wifi direct is not enabled");
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            System.out.println("2");
            if (mManager != null) {
                mManager.requestPeers(mChannel, mPeerListListener);
            }


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            System.out.println("3");
            if (mManager == null){
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo!=null){
                System.out.println(networkInfo.getState());
            }

            if (networkInfo.isConnected()){
                System.out.println("This device is connected with other device");

                mManager.requestConnectionInfo(mChannel, mPeerConnectionListener);
            }else{

                System.out.println("Something went wrong");
            }



        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            System.out.println("4");
        }
    }

}

