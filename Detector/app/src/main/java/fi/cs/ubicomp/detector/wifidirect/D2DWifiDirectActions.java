package fi.cs.ubicomp.detector.wifidirect;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.cs.ubicomp.detector.database.DatabaseHandler;



/**
 * Created by huber on 11/3/15.
 */
public class D2DWifiDirectActions extends BroadcastReceiver {

    private final String TAG = D2DWifiDirectActions.class.getSimpleName();


    private WifiP2pManager wfManager;
    private WifiP2pManager.Channel wfChannel;

    private String myDeviceAddress;


    private List peers = new ArrayList();

    public D2DWifiDirectActions() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        myDeviceAddress = "";

        final DatabaseHandler dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        WifiP2pManager.PeerListListener wfPeerListListener = new WifiP2pManager.PeerListListener() {

            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                System.out.println("Listening for devices...");
                peers.clear();
                peers.addAll(peerList.getDeviceList());


                if (peers.size() == 0) {
                    //This is particularly important as it means that devices around are not addressable via WifiDirect
                    System.out.println("No-devices-found");
                    dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                            myDeviceAddress,
                            "No-devices-found",
                            "No-devices-found",
                            1,
                            0,
                            0,
                            0,
                            0);

                    return;
                }else{
                    Iterator<WifiP2pDevice> i = peers.iterator();
                    while(i.hasNext()){
                        WifiP2pDevice device = i.next();
                        //Log.d(TAG,device.deviceName);
                        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                                myDeviceAddress,
                                device.deviceName,
                                device.deviceAddress,
                                1,
                                0,
                                0,
                                0,
                                0);

                    }

                }


            }
        };

        wfManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        wfChannel = wfManager.initialize(context, context.getMainLooper(), null);



        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            //Log.d(TAG, "1");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.d(TAG, "Wifi direct is enabled");


            } else {
                // Wi-Fi P2P is not enabled
                Log.d(TAG, "Wifi direct is not enabled");
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            //Log.d(TAG, "2");
            if (wfManager != null) {

                wfManager.requestPeers(wfChannel, wfPeerListListener);


            }


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            //System.out.println("3");
            Log.d(TAG, "3");
            if (wfManager == null){
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo!=null){
                System.out.println(networkInfo.getState());
            }



        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            //Log.d(TAG, "4");
        }

    }


}