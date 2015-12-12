package fi.cs.ubicomp.detector.RESTsupport;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import fi.cs.ubicomp.detector.utilities.Commons;

/**
 * Created by huber on 12/12/15.
 */

public class SendHttpRequestTask extends AsyncTask<String, Void, String> {

    private final String TAG = SendHttpRequestTask.class.getSimpleName();

    private File datafile;

    HttpManager httpManager = HttpManager.getInstance();

    public SendHttpRequestTask(File datafile){
        this.datafile = datafile;
    }


    @Override
    protected String doInBackground(String... params) {

        try {
            httpManager.UploadFile(datafile, Commons.WEB_SERVICE_POST);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String data) {

        Log.d(TAG, "Database file uploaded");

    }

}