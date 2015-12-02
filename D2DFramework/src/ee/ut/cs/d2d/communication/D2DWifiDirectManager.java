package ee.ut.cs.d2d.communication;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ee.ut.cs.d2d.data.DeviceData;
import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.hybridoffloading.NetInfo;
import ee.ut.cs.d2d.hybridoffloading.NetworkManagerServer;
import ee.ut.cs.d2d.utilities.Commons;
import ee.ut.cs.d2d.wifidirect.D2DWifiDirectActions;

/**
 * Created by hflores on 02/12/15.
 */
public class D2DWifiDirectManager {

    private final String TAG = D2DWifiDirectManager.class.getSimpleName();


    WifiP2pManager wfManager;
    WifiP2pManager.Channel wfChannel;
    D2DWifiDirectActions wfReceiver;
    WifiP2pManager.PeerListListener wfPeerListListener;
    WifiP2pManager.ConnectionInfoListener wfPeerConnectionListener;
    private List peers = new ArrayList();

    private Context context;

    private DeviceData D2DPeers;

    private DeviceListAdapter mListAdapter;

    IntentFilter wfIntentFilter;

    public D2DWifiDirectManager(Context context, DeviceData D2DPeers, WifiP2pManager wfManager, WifiP2pManager.Channel wfChannel, DeviceListAdapter mListAdapter){
        this.context = context;
        this.D2DPeers = D2DPeers;
        this.wfManager = wfManager;
        this.wfChannel = wfChannel;
        this.mListAdapter = mListAdapter;

        initialize();

    }


    public void initialize(){
        wfPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                System.out.println("Listening for devices...");
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                if (peers.size() == 0) {
                    System.out.println("No devices found");
                    return;
                }else{
                    Iterator<WifiP2pDevice> i = peers.iterator();
                    while(i.hasNext()){
                        WifiP2pDevice device = i.next();
                        //Log.d(TAG,device.deviceName);
                        if (!D2DPeers.getDevicePeers(Commons.wifiDirect).contains(device)){
                            D2DPeers.addPeer(device);
                            mListAdapter.notifyDataSetChanged();

                        }

                    }

                }
            }
        };




        wfPeerConnectionListener = new WifiP2pManager.ConnectionInfoListener(){

            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {

            try{

                InetAddress groupOwnerAddress = InetAddress.getByName(info.groupOwnerAddress.getHostAddress());
                //Log.d(TAG, "Until here we have the IP address to open socket connections to offload code");
                Log.d(TAG, groupOwnerAddress.toString());

                NetInfo.IPAddress = groupOwnerAddress.getAddress();

                if (info.groupFormed && info.isGroupOwner){
                    Log.d(TAG, "This device is the Group Owner (Server role)"); //Group Owner (GO) is the Access Point (AP)
                    NetworkManagerServer nm = new NetworkManagerServer(NetInfo.port);
                    nm.makeconnection();

                }else{
                    if (info.groupFormed){
                        Log.d(TAG, "This device is a client (Client role) and it should connect to the Group Owner");
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }



        };


        addWifiDirectActions();

    }


    public void addWifiDirectActions(){
        wfIntentFilter = new IntentFilter();
        wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wfReceiver = new D2DWifiDirectActions(wfManager, wfChannel, wfPeerListListener, wfPeerConnectionListener);


    }

    public void registerWifiDirect(){
        context.registerReceiver(wfReceiver, wfIntentFilter);
    }

    public void unregisterWifiDirect(){
        context.unregisterReceiver(wfReceiver);
    }


    public void connect(WifiP2pDevice wfDevice){

        new Thread(
                new D2DWifiDirectConnection(wfManager, wfChannel, wfDevice)
        ).start();


    }



}
