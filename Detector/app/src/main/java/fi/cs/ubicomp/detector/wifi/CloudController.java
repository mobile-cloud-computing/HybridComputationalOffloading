package fi.cs.ubicomp.detector.wifi;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Vector;

public class CloudController {

    private final String TAG = CloudController.class.getSimpleName();

    private NetworkManagerClient NM = null;
    byte[] IPAddress = new byte[4];  // cloud address
    int port;                        // cloud port
    Object result = null;
    Object state = null;
    final Object waitob = new Object();
    Vector results = new Vector();

    public CloudController(){
        port = NetInfo.port;
        IPAddress[0] = NetInfo.ipAddress[0];
        IPAddress[1] = NetInfo.ipAddress[1];
        IPAddress[2] = NetInfo.ipAddress[2];
        IPAddress[3] = NetInfo.ipAddress[3];
    }

    public Vector execute(String rtt) {
        synchronized (waitob){
            this.result = null;
            this.state = null;
            

            if(NM == null){
                NM = new NetworkManagerClient(IPAddress, port);
                NM.setNmf(this);
            }

            new Thread(new StartNetwork(rtt)).start();
            
            try {
                waitob.wait(NetInfo.waitTime);
            } catch (InterruptedException e) {
            }
            
            if(this.state != null){
                results.removeAllElements();
                results.add(this.result);
                results.add(this.state);
                Log.d(TAG, "RTT received");
                return results;
            }else{
                Log.d(TAG, "RTT not received");
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
        String RTT;


        public StartNetwork(String rtt) {
            this.RTT = rtt;
        }


        
        public void run() {
            boolean isconnected = NM.connect();
            if(isconnected){
                NM.send(RTT);
            }
        }

    }
}
