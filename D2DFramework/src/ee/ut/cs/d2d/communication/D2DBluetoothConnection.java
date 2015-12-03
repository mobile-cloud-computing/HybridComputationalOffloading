package ee.ut.cs.d2d.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Created by hflores on 02/12/15.
 */
public class D2DBluetoothConnection implements Runnable {

    private final String TAG = D2DBluetoothConnection.class.getSimpleName();

    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;

    public D2DBluetoothConnection(BluetoothAdapter btAdapter,BluetoothDevice btDevice){
        this.btAdapter = btAdapter;
        this.btDevice = btDevice;
    }

    @Override
    public void run() {

    }
}
