package ee.ut.cs.d2d.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import ee.ut.cs.d2d.services.D2DMeshService;

/**
 * Created by hflores on 27/11/15.
 */
public class D2DLog extends Activity {

    public static String OUTPUT_SCREEN_EVENT = "output_screen_event";
    MessageReceiver screenReceiver;

    private TextView outputScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_log_layout);

    }

    public void initViews(){
        outputScreen = (TextView)findViewById(R.id.outputTextViewer);
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter;
        filter = new IntentFilter(OUTPUT_SCREEN_EVENT);
        screenReceiver = new MessageReceiver();
        registerReceiver(screenReceiver, filter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
    }

    @Override
    public void onPause(){
        super.onPause();

    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isPrint = intent.getBooleanExtra("isPrint", true);
            if(isPrint){
                String output_screen_result = intent.getStringExtra("output_screen_result");

                printLnInScreen(output_screen_result);
            }else{
                String output_screen_result = intent.getStringExtra("output_screen_result");

                printLnInScreen(output_screen_result);
            }
            try {
                Uri notification = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION);
                android.media.Ringtone r = android.media.RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {}
        }
    }

    public void printLnInScreen(String line) {
        outputScreen.append(line + "\n");
    }

    public void clearOutputScreen() {
        outputScreen.setText("");
    }


}
