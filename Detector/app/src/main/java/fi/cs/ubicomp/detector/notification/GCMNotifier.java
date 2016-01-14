package fi.cs.ubicomp.detector.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import fi.cs.ubicomp.detector.utilities.Commons;

/**
 * Created by hflores on 08/12/15.
 */
public class GCMNotifier {

    private static String TAG = GCMNotifier.class.getSimpleName();

    public static String isRegistered = "isRegistered";

    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    MessageReceiver receiver;

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    public GCMNotifier(Context context){
        this.context = context;
    }

    public void registerNotifier(){
        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            /*Toast.makeText(context.getApplicationContext(),
                    "Already Registered with HyMobi!",
                    Toast.LENGTH_LONG).show();*/
        }
    }

    public void sendToAppServer(){
        if (TextUtils.isEmpty(regId)) {
            /*Toast.makeText(context.getApplicationContext(), "RegId is empty!",
                    Toast.LENGTH_LONG).show();*/
        } else {

            new AsyncGCMExternalServer(context, regId).sendToAppServer();

        }
    }


    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(context);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            /*Toast.makeText(context.getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();*/
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                AsyncGCMExternalServer.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Commons.GCM_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);

                IntentFilter filter;
                filter = new IntentFilter(GCMNotifier.isRegistered);
                receiver = new MessageReceiver();
                context.registerReceiver(receiver, filter);

                Intent intent = new Intent(GCMNotifier.isRegistered);
                intent.putExtra("isRegistered", true);

                context.sendBroadcast(intent);


                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                /*Toast.makeText(context.getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();*/
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = context.getSharedPreferences(
                AsyncGCMExternalServer.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isRegistered = intent.getBooleanExtra("isRegistered", true);

            if (isRegistered==true){
                sendToAppServer();
                Log.d(TAG, "Id was sent to the App server");
            }else{
               Log.d(TAG, "The device is not registered at the app server");
            }


        }
    }

    public void unregisterMessage(){
        context.unregisterReceiver(receiver);
    }


}
