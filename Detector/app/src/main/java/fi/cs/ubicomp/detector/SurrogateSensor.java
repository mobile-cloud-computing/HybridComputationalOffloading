package fi.cs.ubicomp.detector;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import fi.cs.ubicomp.detector.data.DeviceData;
import fi.cs.ubicomp.detector.database.DatabaseCommons;
import fi.cs.ubicomp.detector.database.DatabaseHandler;
import fi.cs.ubicomp.detector.services.D2DMeshService;
import fi.cs.ubicomp.detector.utilities.Commons;

public class SurrogateSensor extends AppCompatActivity {

    private DatabaseHandler dataEvent;

    private Context context;

    private final String TAG = SurrogateSensor.class.getSimpleName();

    //Network device to be used, e.g., WifiDirect or Blueetooth
    private String nDevice = Commons.bluetooth;

    //Contains the list of the peers in which the device can connect (D2D), both WifiDirect and Bluetooth
    DeviceData D2DPeers;


    //Blueetooth implementation
    private BluetoothAdapter btAdapter;


    //WifiDirect implementation
    WifiP2pManager wfManager;
    WifiP2pManager.Channel wfChannel;


    //Bluetooth and WifiDirect services are provided by this service
    private D2DMeshService meshService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrogate_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        context = this;


        dataEvent = DatabaseHandler.getInstance();
        dataEvent.setContext(context);

        D2DPeers = DeviceData.getInstance();

        /*dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "127.0.0.1",
                0,
                0,
                1,
                600);

        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "10.0.0.1",
                0,
                0,
                1,
                700);


        dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
                "surrogate id",
                "192.0.0.1",
                0,
                1,
                0,
                0);

        */

        extractDatabaseFile(new DatabaseCommons());

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

}
