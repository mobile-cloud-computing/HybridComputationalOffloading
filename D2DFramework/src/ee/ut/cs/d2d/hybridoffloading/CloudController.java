/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package ee.ut.cs.d2d.hybridoffloading;

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
        IPAddress[0] = NetInfo.IPAddress[0];
        IPAddress[1] = NetInfo.IPAddress[1];
        IPAddress[2] = NetInfo.IPAddress[2];
        IPAddress[3] = NetInfo.IPAddress[3];

        Log.d(TAG, IPAddress[0] + "." + IPAddress[1] + "." + IPAddress[2] + "." + IPAddress[3]);
    }

    public Vector execute(Method toExecute, Object[] paramValues, Object state, Class stateDataType) {
        synchronized (waitob){
            this.result = null;
            this.state = null;
            

            if(NM == null){
                NM = new NetworkManagerClient(IPAddress, port);
                NM.setNmf(this);
            }

            new Thread(new StartNetwork(toExecute, paramValues, state, stateDataType)).start();
            
            try {
                waitob.wait(NetInfo.waitTime);
                //System.out.println("Esperando");
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
                NM.send(toExecute.getName(), paramTypes, paramValues, state, stateDataType);
            }
        }

    }
}
