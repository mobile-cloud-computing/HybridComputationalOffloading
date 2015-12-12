package fi.cs.ubicomp.detector;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import fi.cs.ubicomp.detector.RESTsupport.TimeScheduler;
import fi.cs.ubicomp.detector.data.DeviceData;
import fi.cs.ubicomp.detector.database.DatabaseCommons;
import fi.cs.ubicomp.detector.database.DatabaseHandler;
import fi.cs.ubicomp.detector.services.D2DMeshService;
import fi.cs.ubicomp.detector.services.D2DScanService;
import fi.cs.ubicomp.detector.utilities.Commons;
import fi.cs.ubicomp.detector.wifi.RTTMonitor;

public class SurrogateSensor extends AppCompatActivity {

    private DatabaseHandler dataEvent;

    private Context context;

    private final String TAG = SurrogateSensor.class.getSimpleName();

    //Network device to be used, e.g., WifiDirect or Blueetooth
    private String nDevice = Commons.wifiDirect;


    //Bluetooth and WifiDirect services are provided by this service
    private D2DMeshService meshService;

    private String codeResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrogate_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        context = this;


        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            // device is discoverable & accessible
        } else {
            // device is not discoverable & accessible
            //Turns on bluetooth and enables the device to be discover t seconds, t 0 always, t interval
            Intent discoverableIntent = new
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surrogate_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent= new Intent(this, D2DMeshService.class);
        bindService(intent, meshConnection, Context.BIND_AUTO_CREATE);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onPause() {
        super.onPause();

        unbindService(meshConnection);
    }


    private ServiceConnection meshConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            D2DMeshService.MyBinder connector = (D2DMeshService.MyBinder) binder;

            meshService = connector.getMeshServiceInstance();
            meshService.setServiceContext(context, nDevice);


            Toast.makeText(SurrogateSensor.this, "Connected", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            meshService = null;
        }
    };



    //Extract database
    public void extractDatabaseFile(DatabaseCommons db){
        try {
            db.copyDatabaseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopScanScheduler(){
        Intent intent = new Intent(this, D2DScanService.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pending);

        Log.d(TAG, "alarm service stopped");
    }


    public void forcedStop(){
        Intent intentStop = new Intent(this, D2DMeshService.class);
        stopService(intentStop);
    }



    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.stopMonitor:
                codeResult = null;
                getCode();

                break;


            case R.id.extract:
                codeResult = null;
                getCode();


                break;

        }


    }

    public void getCode(){

        // inflate alert dialog xml
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle("Introduce code:");
        // set custom dialog icon
        //alertDialogBuilder.setIcon(R.drawable.ic_launcher);
        // set custom_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);
        final EditText userInput = (EditText) dialogView
                .findViewById(R.id.et_input);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // get user input and set it to etOutput
                                // edit text
                                codeResult = userInput.getText().toString();
                                if (codeResult.trim().equals(Commons.SECURITY_CODE_STOP)){
                                    stopScanScheduler();
                                    forcedStop();
                                }else{
                                    if (codeResult.trim().equals(Commons.SECURITY_CODE_EXTRACT)){
                                        extractDatabaseFile(new DatabaseCommons());
                                    }
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }


}
