package ee.ut.cs.d2d.data;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.p2p.WifiP2pDevice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ee.ut.cs.d2d.utilities.Commons;

public class DeviceData {

    public static DeviceData instance;

    List bluetoothDevicePeers = new ArrayList();
    List<String> btDevices = new ArrayList<String>();

    List wifiDirectDevicePeers = new ArrayList();
    List<String> wfDevices = new ArrayList<String>();

    private DeviceData(){ //constructor should be private

    }

    public static synchronized DeviceData getInstance(){
        if (instance==null){
            instance = new DeviceData();
            return instance;
        }

        return instance;
    }


    public List<String> getPeers(String nInterface){

        if (nInterface.equals(Commons.bluetooth)){
            return this.btDevices;
        }else{
            if (nInterface.equals(Commons.wifiDirect)){
                return this.wfDevices;
            }
        }

        return null;
    }


    public List getDevicePeers(String nInterface){
        if (nInterface.equals(Commons.bluetooth)){
            return this.bluetoothDevicePeers;
        }else{
            if (nInterface.equals(Commons.wifiDirect)){
                return this.wifiDirectDevicePeers;
            }
        }

        return null;
    }


    public void addPeer(BluetoothDevice device){
        bluetoothDevicePeers.add(device);
        btDevices.add(device.getAddress() + "/" + device.getName());
    }

    public void addPeer(WifiP2pDevice device){
        wifiDirectDevicePeers.add(device);
        wfDevices.add(device.deviceAddress + "/" + device.deviceName);
    }


    public Object searchForPeer(String id, String nInterface){
        if (nInterface.equals(Commons.bluetooth)){
            Iterator i = bluetoothDevicePeers.iterator();
            while(i.hasNext()){
                BluetoothDevice btDevice =  (BluetoothDevice) i.next();
                if (btDevice.getAddress().equals(id)){
                    return btDevice;
                }
            }

        }else{
            if (nInterface.equals(Commons.wifiDirect)){
                Iterator k = wifiDirectDevicePeers.iterator();
                while(k.hasNext()){
                    WifiP2pDevice wfDevice =  (WifiP2pDevice) k.next();
                    if (wfDevice.deviceAddress.equals(id)){
                        return wfDevice;
                    }
                }

            }
        }

        return null;
    }

}
