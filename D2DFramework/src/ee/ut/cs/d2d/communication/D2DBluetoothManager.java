package ee.ut.cs.d2d.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import ee.ut.cs.d2d.bluetooth.D2DBluetoothActions;
import ee.ut.cs.d2d.data.DeviceData;
import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.hybridoffloading.bluetooth.D2DBluetoothServer;

/**
 * Created by hflores on 02/12/15.
 */
public class D2DBluetoothManager {

    private final String TAG = D2DBluetoothManager.class.getSimpleName();

    D2DBluetoothActions btReceiver;
    private BluetoothAdapter btAdapter;
    DeviceListAdapter mListAdapter;
    DeviceData D2DPeers;

    IntentFilter btIntentFilter;

    private Context context;

    public D2DBluetoothManager(Context context, BluetoothAdapter btAdapter, DeviceData D2DPeers, DeviceListAdapter mListAdapter){
        this.context = context;
        this.btAdapter = btAdapter;
        this.mListAdapter = mListAdapter;
        this.D2DPeers = D2DPeers;

        initialize();
    }

    public void initialize(){
        btReceiver = new D2DBluetoothActions(context, D2DPeers, mListAdapter);
        addBluetoothActions();

        new Thread(
                new ServerBluetooth()
        ).start();

     }

    public void addBluetoothActions(){
        btIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        btIntentFilter.addAction(BluetoothDevice.ACTION_UUID);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

    }

    public void registerBluetooth(){
        context.registerReceiver(btReceiver, btIntentFilter);
    }

    public void unregisterBluetooth(){
        context.unregisterReceiver(btReceiver);
    }

    public void connect(BluetoothDevice btDevice){
        /*new Thread(
                new D2DBluetoothConnection(btAdapter, btDevice)
        ).start();*/

        D2DBluetoothConnection conn = D2DBluetoothConnection.getInstance();
        conn.setD2DBluetoothSurrogate(btAdapter, btDevice);

    }


    class ServerBluetooth implements Runnable{
        long runningTime = 5 * 60000; //t minutes

        @Override
        public void run() {
            D2DBluetoothServer btServer = new D2DBluetoothServer(btAdapter);
            Log.d(TAG, "Starting Bluetooth server...)");
            new Thread(btServer).start();

            try {
                Thread.sleep(runningTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Stopping Bluetooth server...");
            btServer.stop();
        }
    }

}
