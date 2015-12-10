/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to hflores AT ee DOT oulu DOT fi
 *
 */


package ee.ut.cs.d2d.framework;


import ee.ut.cs.d2d.communication.D2DBluetoothManager;
import ee.ut.cs.d2d.communication.D2DWifiDirectManager;
import ee.ut.cs.d2d.data.DeviceData;
import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.hybridoffloading.task.Queens;
import ee.ut.cs.d2d.hybridoffloading.task.SelectionSort;
import ee.ut.cs.d2d.notification.GCMNotifier;
import ee.ut.cs.d2d.services.D2DMeshService;
import ee.ut.cs.d2d.utilities.Commons;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


/**
 * author Huber Flores
 */

public class D2D extends Activity{
	
	private final String TAG = D2D.class.getSimpleName();

	//Network device to be used, e.g., WifiDirect or Blueetooth
	private String nDevice = Commons.wifiDirect;

	//Contains the list of the peers in which the device can connect (D2D), both WifiDirect and Bluetooth
	DeviceData D2DPeers;
	private DeviceListAdapter mListAdapter;
	private ListView mDeviceListView;

	//Blueetooth implementation
	private BluetoothAdapter btAdapter;
	D2DBluetoothManager btD2DManager;

	//WifiDirect implementation
	WifiP2pManager wfManager;
	WifiP2pManager.Channel wfChannel;
	D2DWifiDirectManager wfD2DManager;

	//Bluetooth and WifiDirect services are provided by this service
	private D2DMeshService meshService;

	//App context
	Context context;

	//GUI interface
	private ImageButton bluetooth;
	private ImageButton clean;
	private ImageButton log;


	//Notification
	GCMNotifier notifier;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.d2d_activity_main);
	    context = this;


		D2DPeers = DeviceData.getInstance();

		initViews();

		/**
		 * List to show the devices available
		 */
		mListAdapter = new DeviceListAdapter(this, D2DPeers, nDevice);
		mDeviceListView.setAdapter(mListAdapter);


		/**
		 * Bluetooth listener
		 */
	    btAdapter = BluetoothAdapter.getDefaultAdapter();
		btD2DManager = new D2DBluetoothManager(context, btAdapter, D2DPeers, mListAdapter);



		/**
		 * WifiDirect listener
		 */

		wfManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wfChannel = wfManager.initialize(this, getMainLooper(), null);
		wfD2DManager = new D2DWifiDirectManager(context, D2DPeers, wfManager, wfChannel, mListAdapter);


		/**
		 * System profilers
		 */

		//BatteryProfiler bp = new BatteryProfiler(this);
		//bp.getBatteryLevel();


		/**
		 * GUI buttons
		 */
		bluetooth = (ImageButton) findViewById(R.id.bluetoothButton);
		bluetooth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (btAdapter.isEnabled()) {
						if (meshService!=null){
							//meshService.btOff();
							meshService.off();
						}else{
							Log.d(TAG, "service is not connected");
						}

					} else {
						if (!btAdapter.isEnabled()){
							if (meshService!=null){
								//	meshService.btOn();
								meshService.on();
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


		/**
		 * Device pairing by user
		 */
		mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {

				String peer = (String) parent.getItemAtPosition(position);
				String[] separated = peer.split("/");

				String peerAddress = separated[0];
				//Log.d(TAG, peerAddress);

				if (nDevice.equals(Commons.bluetooth)) { //connect to peer using Bluetooth
					BluetoothDevice btPeer = (BluetoothDevice) D2DPeers.searchForPeer(peerAddress, nDevice);
					//Log.d(TAG, "connect using: " + btPeer.getAddress() + "," + btPeer.getBondState());
					btD2DManager.connect(btPeer);

				} else {
					if (nDevice.equals(Commons.wifiDirect)) { //connect to peer using wifidirect
						WifiP2pDevice wfPeer = (WifiP2pDevice) D2DPeers.searchForPeer(peerAddress, nDevice);
						//Log.d(TAG, "connect using: " + wfPeer.deviceName + "," + wfPeer.status);

						wfD2DManager.connect(wfPeer);
					}
				}

			}
		});


		//Notification
		//notifier = new GCMNotifier(context);
		//notifier.registerNotifier();


	}

	/**
	 * Native Android stuff
	 */

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


	/**
	 * Helpers
	 */
	private void initViews() {
		mDeviceListView = (ListView) findViewById(R.id.deviceList);

	}


	 @Override
	 public void onResume() {
		 super.onResume();

	     Intent intent= new Intent(this, D2DMeshService.class);
		 bindService(intent, meshConnection, Context.BIND_AUTO_CREATE);

		 btD2DManager.registerBluetooth();
		 wfD2DManager.registerWifiDirect();



	 }
	 
	 @Override
	 public void onDestroy() {
		 super.onDestroy();

		 btD2DManager.unregisterBluetooth();
		 wfD2DManager.unregisterWifiDirect();

		 //notifier.unregisterMessage();
	 }

	 @Override
	 public void onPause() {
		 super.onPause();

		 unbindService(meshConnection);
	 }

     @Override
	 public void onStop(){
		 if (wfManager != null && wfChannel != null) {
			 wfManager.removeGroup(wfChannel, new WifiP2pManager.ActionListener() {
				 @Override
				 public void onFailure(int reasonCode) {
					 Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
				 }
				 @Override
				 public void onSuccess() {
				 }
			 });
		 }

		 super.onStop();
	 }


	 private ServiceConnection meshConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder binder) {
				D2DMeshService.MyBinder connector = (D2DMeshService.MyBinder) binder;

				meshService = connector.getMeshServiceInstance();
				meshService.setServiceContext(context, nDevice);
				
				
				Toast.makeText(D2D.this, "Connected", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				meshService = null;
			}
	 };





	/*
	* eventually to put some actions or delete
	 */
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.discoverButton:

				//to test manual discovery
				meshService.discovery();

				break;


			case R.id.testButton:

				//move to the right place in the GUI
				/*WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

				boolean wifiEnabled = wifiManager.isWifiEnabled();

				if (wifiEnabled==true){
					meshService.off();
				}else{
					meshService.on();
				}*/


				//computational task to offload
				if (nDevice.equals(Commons.wifiDirect)){
					new Thread(
							new Queens(Queens.N)
					).start();
				}else{
					if (nDevice.equals(Commons.bluetooth)){
						new Thread(
								//Unlike Wifi, Bluetooth offloading needs to extend Serializable
								new SelectionSort()
						).start();

					}
				}

				break;

		}


	}


}
