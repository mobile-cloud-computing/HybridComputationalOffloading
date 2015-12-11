package fi.cs.ubicomp.detector.wifi;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import fi.cs.ubicomp.detector.database.DatabaseHandler;
import fi.cs.ubicomp.detector.utilities.Commons;

/**
 * Created by hflores on 11/12/15.
 */
public class RTTMonitor  extends CloudRemotable implements Runnable, Serializable{

    private final String TAG = RTTMonitor.class.getSimpleName();

    private Context context;

    DatabaseHandler dataEvent;


    public RTTMonitor(Context context){
        this.context = context;
    }

    public void measureRTT() {
        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        try{
            long startTime = System.currentTimeMillis();
            Vector results = getCloudController().execute("RTT");
            if(results != null){

                Log.d(TAG, "Network connection available " + (System.currentTimeMillis() - startTime));


                dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                        Commons.SERVER_NAME,
                        Commons.SERVER_ADDRESS,
                        0,
                        0,
                        1,
                        (System.currentTimeMillis() - startTime));



            }else{
                Log.d(TAG, "No Network connection available");

                dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                        Commons.SERVER_NAME_ERROR,
                        Commons.SERVER_ADDRESS_ERROR,
                        0,
                        0,
                        1,
                        0);
            }


        }  catch (SecurityException se){
            se.printStackTrace();
        } catch (Throwable th){
            th.printStackTrace();
        }

    }


    @Override
    public void run() {
        measureRTT();
    }

}
