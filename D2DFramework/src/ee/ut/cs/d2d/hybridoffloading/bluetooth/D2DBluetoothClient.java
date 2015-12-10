package ee.ut.cs.d2d.hybridoffloading.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import ee.ut.cs.d2d.hybridoffloading.NetInfo;
import ee.ut.cs.d2d.hybridoffloading.Pack;
import ee.ut.cs.d2d.hybridoffloading.ResultPack;
import ee.ut.cs.d2d.utilities.Commons;

/**
 * Created by hflores on 09/12/15.
 */
public class D2DBluetoothClient implements Runnable{

    private final String TAG = D2DBluetoothClient.class.getSimpleName();

    private BluetoothSocket mmSocket = null;

    private BluetoothAdapter btAdapter;

    BluetoothController callingparent = null;
    long startTime = 0;

    InputStream in = null;
    OutputStream out = null;


    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    public D2DBluetoothClient(BluetoothDevice btDevice, BluetoothAdapter btAdapter) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final

        this.btAdapter = btAdapter;


        BluetoothSocket tmp = null;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            //Log.i(TAG, "UUID is the app's UUID string, also used by the server code");
            //it works with any connection

            //tmp = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(Commons.UUID));
            tmp = btDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Commons.UUID));

        } catch (IOException e) { }
        mmSocket = tmp;
    }


    public void setNmf(BluetoothController callingparent) {
        this.callingparent = callingparent;
    }


    public boolean connect(){

        // Cancel discovery because it will slow down the connection
        btAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket.
            // Log.i(TAG,"This will block until it succeeds or throws an exception");

            mmSocket.connect();

            return true;

        } catch (IOException connectException) {
            //Log.i(TAG,"Unable to connect; close the socket and get out");

            try {
                mmSocket.close();
            } catch (IOException closeException) {
                callingparent.setResult(null, null);
                closeException.printStackTrace();
                return false;

            }
            callingparent.setResult(null, null);
            return false;
        }

    }

    public void send(String functionName, Class[] paramTypes, Object[] funcArgValues, Object state, Class stateDType){
        try{
            new Sending(new Pack(functionName, stateDType, state, funcArgValues, paramTypes)).send();
        }catch(Exception ex){
            callingparent.setResult(null, null);
        }
    }

    @Override
    public void run() {

    }

    class Sending implements  Runnable{
        Pack MyPack = null;
        ResultPack result = null;

        public Sending(Pack MyPack) {
            this.MyPack = MyPack;
        }


        public void send(){
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            try {

                startTime = System.currentTimeMillis();
                in = mmSocket.getInputStream();
                out = mmSocket.getOutputStream();


                //oos = new ObjectOutputStream(out);
                oos = new ObjectOutputStream(new BufferedOutputStream(out));
                oos.flush();

                //ois = new ObjectInputStream(in);
                ois =new ObjectInputStream(new BufferedInputStream(in));

                oos.writeObject(MyPack);
                oos.flush();

                result = (ResultPack) ois.readObject();

                if((System.currentTimeMillis() - startTime) < NetInfo.waitTime){
                    //if((System.nanoTime() - startTime)/1000000 < NetInfo.waitTime){
                    if(result == null)
                        callingparent.setResult(null, null);
                    else
                        callingparent.setResult(result.getresult(), result.getstate());
                }

                oos.close();
                ois.close();

                in.close();
                out.close();

                mmSocket.close();

                oos = null;
                ois = null;

                in = null;
                out = null;

                mmSocket = null;

            } catch (IOException ex) {
                callingparent.setResult(null, null);

            } catch (ClassNotFoundException ex) {
                callingparent.setResult(null, null);
            }
        }

    }

    public BluetoothSocket getBluetoothSocket(){
        return this.mmSocket;
    }


}
