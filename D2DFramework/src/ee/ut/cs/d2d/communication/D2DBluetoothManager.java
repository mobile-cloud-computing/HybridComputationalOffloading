package ee.ut.cs.d2d.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import ee.ut.cs.d2d.bluetooth.D2DBluetoothActions;
import ee.ut.cs.d2d.data.DeviceData;
import ee.ut.cs.d2d.data.DeviceListAdapter;

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

}
