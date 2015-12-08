package ee.ut.cs.d2d.notification;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hflores on 08/12/15.
 */
public class AsyncGCMExternalServer {

    private final String TAG = AsyncGCMExternalServer.class.getSimpleName();

    private Context context;


    ShareExternalServer appUtil;
    String regId;
    AsyncTask<Void, Void, String> shareRegidTask;


    public AsyncGCMExternalServer(Context context, String regId){
        this.context = context;
        this.regId = regId;

    }

    public void sendToAppServer(){

        appUtil = new ShareExternalServer();

        Log.d(TAG, "regId: " + regId);


        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, regId);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Toast.makeText(context.getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);

    }





}



