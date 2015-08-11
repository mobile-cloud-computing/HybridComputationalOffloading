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

import ee.ut.cs.d2d.network.D2DBluetoothActions;
import ee.ut.cs.d2d.profilers.BatteryProfiler;
import ee.ut.cs.d2d.services.D2DMeshService;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class D2D extends Activity{
	
	private final String TAG = D2D.class.getSimpleName();
	
	public static String OUTPUT_SCREEN_EVENT = "output_screen_event"; 
	MessageReceiver screenReceiver;
	 
	private TextView outputScreen;
	private ImageButton bluetooth;
	private ImageButton clean;
	private ImageButton log;
	
	D2DBluetoothActions btReceiver = new D2DBluetoothActions(this);
	private BluetoothAdapter btAdapter;
	
	private D2DMeshService meshService; 
	Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.d2d_activity_main);
	    context = this;
	    
	    btAdapter = BluetoothAdapter.getDefaultAdapter();
	     
	    outputScreen = (TextView)findViewById(R.id.outputTextViewer);
	     
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
					clearOutputScreen();
					
			
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
						if (meshService!=null){
							meshService.btDiscovery();
						}else{
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
	      	dlgAlert.setMessage("This framework is part of the context-aware hybrid computational offloading project.");
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
		 IntentFilter filter;
	     filter = new IntentFilter(D2D.OUTPUT_SCREEN_EVENT);
	     screenReceiver = new MessageReceiver();
	     registerReceiver(screenReceiver, filter);
	     
	     Intent intent= new Intent(this, D2DMeshService.class);
	     bindService(intent, meshConnection, Context.BIND_AUTO_CREATE);

	 }
	 
	 @Override
	 public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(btReceiver);
		unregisterReceiver(screenReceiver);
	 }
	 
	 @Override
	 public void onPause(){
		 super.onPause();
		 unbindService(meshConnection);
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
	    
	 public void showLogFiles(){
	    	
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
	 
	 
	 
}
