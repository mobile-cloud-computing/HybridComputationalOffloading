package ee.ut.cs.d2d.hybridoffloading.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;


import ee.ut.cs.d2d.utilities.Commons;

/**
 * Created by hflores on 09/12/15.
 */
public class D2DBluetoothServer implements Runnable {

    private final String TAG = D2DBluetoothServer.class.getSimpleName();


    private final BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter btAdapter;

    private BluetoothSocket mysocket = null;

    //This can be used to get UUIDs from the local device. However, it has to be implemented how to share it.
    //LocalDeviceIdentifiers mIDs = LocalDeviceIdentifiers.getInstance();

    protected boolean   isStopped     = false;
    protected Thread    runningThread = null;


    InputStream in = null;
    OutputStream out = null;

    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    long processTime;

    public D2DBluetoothServer(BluetoothAdapter btAdapter){

        this.btAdapter = btAdapter;


        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(Commons.NAME, UUID.fromString(Commons.UUID));
        } catch (IOException e) { }
        mmServerSocket = tmp;

    }


    @Override
    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }

        mysocket = null;
        // Keep listening until exception occurs or a socket is returned
        while(!isStopped()){
            try {
                mysocket = mmServerSocket.accept();
                Log.i(TAG, "Request accepted");
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (mysocket != null) {
                // Do work to manage the connection (in a separate thread)

                Log.i(TAG, "Processing request");

                new Thread(
                        new MakeConnection(mysocket)
                ).start();




                /*try {
                    Log.i(TAG, "Processing request");
                    makeconnection();

                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;*/
            }
        }

        Log.d(TAG, "Server Stopped.") ;


    }



    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.mmServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }



}
