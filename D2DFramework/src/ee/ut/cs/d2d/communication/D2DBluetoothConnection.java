package ee.ut.cs.d2d.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by hflores on 02/12/15.
 */
public class D2DBluetoothConnection {

    private final String TAG = D2DBluetoothConnection.class.getSimpleName();

    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;

    public static D2DBluetoothConnection instance;

    private D2DBluetoothConnection(){

    }

    public static synchronized D2DBluetoothConnection getInstance(){
        if (instance==null){
            instance = new D2DBluetoothConnection();
            return instance;
        }

        return instance;
    }

    public void setD2DBluetoothSurrogate(BluetoothAdapter btAdapter,BluetoothDevice btDevice){
        this.btAdapter = btAdapter;
        this.btDevice = btDevice;
    }


    public BluetoothDevice getD2DBluetoothSurrogate(){
        return this.btDevice;
    }

    public BluetoothAdapter getD2DBluetoothAdapter(){
        return this.btAdapter;
    }



}
