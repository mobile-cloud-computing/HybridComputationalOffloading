package ee.ut.cs.d2d.hybridoffloading.bluetooth;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Vector;

import ee.ut.cs.d2d.communication.D2DBluetoothResources;
import ee.ut.cs.d2d.hybridoffloading.NetInfo;

/**
 * Created by hflores on 09/12/15.
 */
public class BluetoothController {
    private final String TAG = BluetoothController.class.getSimpleName();

    private D2DBluetoothClient NM = null;
    D2DBluetoothResources conn = D2DBluetoothResources.getInstance();

    Object result = null;
    Object state = null;
    final Object waitob = new Object();
    Vector results = new Vector();

    public BluetoothController(){

    }

    public Vector execute(Method toExecute, Object[] paramValues, Object state, Class stateDataType) {
        synchronized (waitob){
            this.result = null;
            this.state = null;


            if (conn.getD2DBluetoothSurrogate() == null){
                Log.d(TAG, "No surrogate available to offload");
                return null;
            }


            if(NM == null){
                NM = new D2DBluetoothClient(conn.getD2DBluetoothSurrogate(), conn.getD2DBluetoothAdapter());
                NM.setNmf(this);
                new Thread(NM).start();

            }

            new Thread(new StartNetwork(toExecute, paramValues, state, stateDataType)).start();

            try {
                waitob.wait(NetInfo.waitTime);
                //Log.i(TAG, "Waiting for the result to arrive");
            } catch (InterruptedException e) {
            }

            if(this.state != null){
                System.out.println("Finished offloaded task");
                results.removeAllElements();
                results.add(this.result);
                results.add(this.state);
                return results;
            }else{
                System.out.println("Finished, but offloaded task result was not obtained");
                return null;
            }
        }

    }

    public void setResult(Object result, Object cloudModel){
        synchronized (waitob){
            this.result = result;
            this.state = cloudModel;
            waitob.notify();
        }
    }

    class StartNetwork implements Runnable{
        Method toExecute;
        Class[] paramTypes;
        Object[] paramValues;
        Object state = null;
        Class stateDataType = null;


        public StartNetwork(Method toExecute, Object[] paramValues, Object state, Class stateDataType) {
            this.toExecute = toExecute;
            this.paramTypes = toExecute.getParameterTypes();
            this.paramValues = paramValues;
            this.state = state;
            this.stateDataType = stateDataType;
        }


        @Override
        public void run() {
            boolean isconnected = NM.connect();
            if(isconnected){
                Log.i(TAG, "D2D established....sending the code");
                NM.send(toExecute.getName(), paramTypes, paramValues, state, stateDataType);
            }


        }

    }
}
