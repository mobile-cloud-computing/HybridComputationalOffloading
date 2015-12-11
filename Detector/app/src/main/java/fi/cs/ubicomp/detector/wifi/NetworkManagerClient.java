package fi.cs.ubicomp.detector.wifi;



import android.util.Log;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

import fi.cs.ubicomp.detector.database.DatabaseHandler;


public class NetworkManagerClient {

    private final String TAG = NetworkManagerClient.class.getSimpleName();

    int portnum;
    Socket mysocket = null;
    InputStream in = null;
    OutputStream out = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    byte []serverAddress =new byte[4];
    CloudController callingparent = null;
    long startTime = 0;


    public NetworkManagerClient(byte[] serverAddress, int port) {

        this.serverAddress = serverAddress;
        portnum = port;
    }

    public void setNmf(CloudController callingparent) {
        this.callingparent = callingparent;
    }


    public boolean connect(){
        mysocket = new Socket();
        try {
            mysocket.connect(new InetSocketAddress(Inet4Address.getByAddress(serverAddress), portnum), NetInfo.waitTime);

            startTime = System.currentTimeMillis();
            in = mysocket.getInputStream();
            out = mysocket.getOutputStream();

            //oos = new ObjectOutputStream(out);
            oos = new ObjectOutputStream(new BufferedOutputStream(out));
            oos.flush();
            //ois = new ObjectInputStream(in);
            ois =new ObjectInputStream(new BufferedInputStream(in));

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            callingparent.setResult(null, null);

            return false;
        }
    }



    public void send(String rtt){
        try{
            new Sending(new Pack(rtt)).send();
        }catch(Exception ex){
            callingparent.setResult(null, null);
        }
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

        
        public void run() {
            try {
                long startTime = System.currentTimeMillis();

                oos.writeObject(MyPack);
                oos.flush();

                result = (ResultPack) ois.readObject();

                long rtt = System.currentTimeMillis() - startTime;
                Log.d(TAG, "RTT: " + rtt);

                if((System.currentTimeMillis() - startTime) < NetInfo.waitTime){
                    if(result == null)
                        callingparent.setResult(null, null);
                    else
                        callingparent.setResult(result.getresult(), result.getstate());
                }

                oos.close();
                ois.close();

                in.close();
                out.close();

                mysocket.close();

                oos = null;
                ois = null;

                in = null;
                out = null;
                mysocket = null;

            } catch (IOException ex) {
                ex.printStackTrace();
                callingparent.setResult(null, null);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                callingparent.setResult(null, null);
            }
        }

    }


}
