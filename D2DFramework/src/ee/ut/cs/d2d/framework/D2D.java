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

import ee.ut.cs.d2d.bluetooth.D2DBluetoothActions;
import ee.ut.cs.d2d.data.DeviceData;
import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.profilers.BatteryProfiler;
import ee.ut.cs.d2d.services.D2DMeshService;
import ee.ut.cs.d2d.utilities.Commons;
import ee.ut.cs.d2d.wifidirect.D2DWifiDirectActions;
import ee.ut.cs.d2d.wifidirect.D2DWifiDirectConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	D2DBluetoothActions btReceiver;
	private BluetoothAdapter btAdapter;

	//WifiDirect implementation
	WifiP2pManager wfManager;
	WifiP2pManager.Channel wfChannel;
	D2DWifiDirectActions wfReceiver;
	WifiP2pManager.PeerListListener wfPeerListListener;
	private List peers = new ArrayList();
	WifiP2pManager.ConnectionInfoListener wfPeerConnectionListener;

	//Bluetooth and WifiDirect services are provided by this service
	private D2DMeshService meshService;

	//App context
	Context context;

	//GUI interface
	private ImageButton bluetooth;
	private ImageButton clean;
	private ImageButton log;

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

		btReceiver = new D2DBluetoothActions(this, D2DPeers, mListAdapter);
	    btAdapter = BluetoothAdapter.getDefaultAdapter();

		registerBluetooth();


		/**
		 * WifiDirect listener
		 */

		wfManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wfChannel = wfManager.initialize(this, getMainLooper(), null);


		wfPeerListListener = new WifiP2pManager.PeerListListener() {
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peerList) {
				System.out.println("Listening for devices...");
				peers.clear();
				peers.addAll(peerList.getDeviceList());

				if (peers.size() == 0) {
					System.out.println("No devices found");
					return;
				}else{
					Iterator<WifiP2pDevice> i = peers.iterator();
					while(i.hasNext()){
						WifiP2pDevice device = i.next();
						//Log.d(TAG,device.deviceName);
						if (!D2DPeers.getDevicePeers(Commons.wifiDirect).contains(device)){
							D2DPeers.addPeer(device);
							if (nDevice.equals(Commons.wifiDirect)){
								mListAdapter.notifyDataSetChanged();
							}
						}

					}

				}
			}
		};


		wfPeerConnectionListener = new WifiP2pManager.ConnectionInfoListener(){

			@Override
			public void onConnectionInfoAvailable(WifiP2pInfo info) {
				try {
					InetAddress groupOwnerAddress = InetAddress.getByName(info.groupOwnerAddress.getHostAddress());


					if (info.groupFormed && info.isGroupOwner){
						System.out.println("This device acts as client");
					}else{
						if (info.groupFormed){
							System.out.println("Other device acts as client");
						}
					}

				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

			}

		};

		registerWifiDirect();


		/**
		 * System profilers
		 */

		BatteryProfiler bp = new BatteryProfiler(this);
		bp.getBatteryLevel();


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

				String peer =	(String) parent.getItemAtPosition(position);
				String[] separated = peer.split("/");

				String peerAddress = separated[0];
				//Log.d(TAG, peerAddress);

				if (nDevice.equals(Commons.bluetooth)){ //connect to peer using Bluetooth
					BluetoothDevice btPeer = (BluetoothDevice) D2DPeers.searchForPeer(peerAddress ,nDevice);
					//Log.d(TAG, "connect using: " + btPeer.getAddress() + "," + btPeer.getBondState());


				}else{
					if (nDevice.equals(Commons.wifiDirect)){ //connect to peer using wifidirect
						WifiP2pDevice wfPeer = (WifiP2pDevice) D2DPeers.searchForPeer(peerAddress, nDevice);
						//Log.d(TAG, "connect using: " + wfPeer.deviceName + "," + wfPeer.status);

						new D2DWifiDirectConnection(D2D.this, wfManager, wfChannel, wfPeer).connect();
					}
				}

			}
		});
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


	//Bluetooth filter
	public void registerBluetooth(){
		 IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	     filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
	     filter.addAction(BluetoothDevice.ACTION_UUID);
	     filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	     filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		 registerReceiver(btReceiver, filter);
	 }


	//WifiDirect filter
	public void registerWifiDirect(){
		IntentFilter wfIntentFilter = new IntentFilter();
		wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		wfIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		wfReceiver = new D2DWifiDirectActions(wfManager, wfChannel, /*this,*/ wfPeerListListener, wfPeerConnectionListener);

		registerReceiver(wfReceiver, wfIntentFilter);
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
		 unregisterReceiver(wfReceiver);
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
				//meshService.discovery();

				break;


			case R.id.testButton:

				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

				boolean wifiEnabled = wifiManager.isWifiEnabled();

				if (wifiEnabled==true){
					meshService.off();
				}else{
					meshService.on();
				}


				break;

		}


	}


}
