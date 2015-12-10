package ee.ut.cs.d2d.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Created by hflores on 02/12/15.
 */
public class D2DBluetoothResources {

    private final String TAG = D2DBluetoothResources.class.getSimpleName();

    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;

    public static D2DBluetoothResources instance;

    private D2DBluetoothResources(){

    }

    public static synchronized D2DBluetoothResources getInstance(){
        if (instance==null){
            instance = new D2DBluetoothResources();
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
