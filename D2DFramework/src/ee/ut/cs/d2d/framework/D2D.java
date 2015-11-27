/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package ee.ut.cs.d2d.framework;

import ee.ut.cs.d2d.bluetooth.D2DBluetoothActions;
import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.profilers.BatteryProfiler;
import ee.ut.cs.d2d.services.D2DMeshService;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class D2D extends Activity{
	
	private final String TAG = D2D.class.getSimpleName();

	private ImageButton bluetooth;
	private ImageButton clean;
	private ImageButton log;

	private List<String> mDevices;
	private DeviceListAdapter mListAdapter;
	private ListView mDeviceListView;


	D2DBluetoothActions btReceiver;
	private BluetoothAdapter btAdapter;
	
	private D2DMeshService meshService; 
	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.d2d_activity_main);
	    context = this;

		initViews();

		mDevices = new ArrayList<String>();
		mListAdapter = new DeviceListAdapter(this, mDevices);
		mDeviceListView.setAdapter(mListAdapter);

		btReceiver = new D2DBluetoothActions(this, mDevices, mListAdapter);
	    btAdapter = BluetoothAdapter.getDefaultAdapter();
	     

	    bluetooth = (ImageButton) findViewById(R.id.bluetoothButton);
        bluetooth.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				try {
					if (btAdapter.isEnabled()) {
						if (meshService!=null){
							meshService.btOff();
						}else{
							Log.d(TAG, "service is not connected");
						}
					
					} else {
						if (!btAdapter.isEnabled()){
							if (meshService!=null){
								meshService.btOn();
							}else{
								Log.d(TAG, "service is not connected");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		 });
	     
	     clean = (ImageButton) findViewById(R.id.cleanButton);
	     clean.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 try {
					 Toast.makeText(getBaseContext(), "Cleaning screen..", Toast.LENGTH_SHORT).show();


				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }
		 });
	        
	     log = (ImageButton) findViewById(R.id.logButton);
		 log.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Toast.makeText(getBaseContext(), "Showing logs..", Toast.LENGTH_SHORT).show();
					if (meshService != null) {
						//meshService.btDiscovery();

						Intent d2dLog = new Intent(D2D.this, D2DLog.class);
						startActivity(d2dLog);


					} else {
						Log.d(TAG, "service is not connected");
					}

				} catch (Exception e) {
					e.printStackTrace();
					}
				}
			});


		registerBluetooth();
	    
	     BatteryProfiler bp = new BatteryProfiler(this);
	     bp.getBatteryLevel();


		//mDevices = new ArrayList<String>();
		//mListAdapter = new DeviceListAdapter(this, mDevices);
		//mDeviceListView.setAdapter(mListAdapter);


	}


	private void initViews() {
		mDeviceListView = (ListView) findViewById(R.id.deviceList);

	}


	public void registerBluetooth(){
		 IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	     filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
	     filter.addAction(BluetoothDevice.ACTION_UUID);
	     filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	     filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		 registerReceiver(btReceiver, filter);
	 }
	 
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	     // Inflate the menu; this adds items to the action bar if it is present.
	     getMenuInflater().inflate(R.menu.d2_d, menu); 
	     return true;
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     // Handle action bar item clicks here. The action bar will
	     // automatically handle clicks on the Home/Up button, so long
	     // as you specify a parent activity in AndroidManifest.xml.
	     int id = item.getItemId();
	     if (id == R.id.action_about) {
	     	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
	      	dlgAlert.setMessage("This framework is part of the social-aware hybrid mobile offloading project.");
	       	dlgAlert.setTitle("Device-to-Device Offloading");
	       	dlgAlert.setPositiveButton("OK", null);
	       	dlgAlert.setCancelable(true);
	       	dlgAlert.create().show();
	       	
	        return true;
	      }
	     return super.onOptionsItemSelected(item);
	 }
	 
	 
	 @Override
	 public void onResume(){
		 super.onResume();

	     
	     Intent intent= new Intent(this, D2DMeshService.class);
	     bindService(intent, meshConnection, Context.BIND_AUTO_CREATE);

	 }
	 
	 @Override
	 public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(btReceiver);

	 }

	 @Override
	 public void onPause(){
		 super.onPause();
		 unbindService(meshConnection);
	 }


	 private ServiceConnection meshConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder binder) {
				D2DMeshService.MyBinder connector = (D2DMeshService.MyBinder) binder;

				meshService = connector.getMeshServiceInstance();
				meshService.setServiceContext(context);
				
				
				Toast.makeText(D2D.this, "Connected", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				meshService = null;
			}
	 };


	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.discoverButton:
				meshService.btDiscovery();

				break;


		}


	}

	 
	 
}
