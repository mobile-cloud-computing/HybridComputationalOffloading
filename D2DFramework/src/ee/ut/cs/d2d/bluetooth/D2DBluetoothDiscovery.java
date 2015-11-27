/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package ee.ut.cs.d2d.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;


public class D2DBluetoothDiscovery implements BluetoothState {
	
	/*public static final String KNOWN = "known";
	public static final String UNKNOWN  = "unkown";
	
	public static String BT_DISCOVERY_STARTED = "android.intent.action.BT_DISCOVERY_STARTED"; 
	public static String BT_DISCOVERY_FINISHED = "android.intent.action.BT_DISCOVERY_FINISHED"; 
	*/

	private final String TAG = D2DBluetoothDiscovery.class.getSimpleName();
	//private ArrayList<BluetoothDevice> btDeviceListHistory = null;
	//private ArrayList<DeviceAwareness> btDeviceListProximity = null;
	private BluetoothAdapter btAdapter;
	
	D2DBluetooth d2DBluetooth;
	Context context;
	
	
	public D2DBluetoothDiscovery(D2DBluetooth d2DBluetooth){
		this.d2DBluetooth = d2DBluetooth;
		this.context = d2DBluetooth.getContext();
		this.btAdapter = d2DBluetooth.getD2DBluetoothAdapter();
		//this.btDeviceListHistory = new ArrayList<BluetoothDevice>();
		//this.btDeviceListProximity = new ArrayList<DeviceAwareness>();
	}
	
	
	/*public void historyDeviceList(){
		if (btAdapter == null) {
			Set<BluetoothDevice> bondedSet = btAdapter.getBondedDevices();
			for (BluetoothDevice device: bondedSet) {
				btDeviceListHistory.add(device);
			}
			
		}
	}*/
	
	
	/*public void proximityDeviceList(){
	
		for (BluetoothDevice current: D2DBluetoothActions.btDeviceList ){
			boolean existent = false; 
			//device is known
			for (BluetoothDevice device: btDeviceListHistory){
				
				if (current.equals(device)){
					existent = true;
					btDeviceListProximity.add(new DeviceAwareness(current, KNOWN));
				}
			
			}
			
			//ensuring no copy of the same device
			for (int i=0; i< btDeviceListProximity.size(); i++){
				if (btDeviceListProximity.get(i).getDevice().equals(current)){
					existent = true;
				}
			}
			
			
			//device is unkown
			if (existent==false){
				btDeviceListProximity.add(new DeviceAwareness(current, UNKNOWN));
			}
		}
		
		
	}
	*/

	

	@Override
	public void discovery() {
		// TODO Auto-generated method stub
	//	btDeviceListProximity.clear();
	//	btDeviceListHistory.clear();
		//D2DBluetoothActions.clearDeviceList();
		
		btAdapter.startDiscovery();
	//	historyDeviceList();
		//new WaitUntilBluetoothScans().execute("");
		
		d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothIdle());
		
	}

	@Override
	public void idle() { 
		// TODO Auto-generated method stub
		
	}


	@Override
	public void on() {
		// TODO Auto-generated method stub
		
	}
 

	@Override
	public void off() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	public void printBtDeviceList(){
		Iterator<DeviceAwareness> iterator = btDeviceListProximity.iterator();
		
		while(iterator.hasNext()){
			DeviceAwareness device = iterator.next();
						
			Log.d(TAG, device.getDevice().getName() + "," + device.getAwareness());
		}
		
		
	}
	

	
	public class DeviceAwareness{
		
		BluetoothDevice device;
		String awareness;
		
		
		DeviceAwareness(BluetoothDevice device, String awareness){
			this.device = device;
			this.awareness = awareness;
		}
			
		public String getAwareness(){
			return this.awareness;
		}
		
		public BluetoothDevice getDevice(){
			return this.device;
		}
		
	}
	
	
	static boolean finished = false;
	static int devices = 0;
	
	public static class CustomBluetoothReceiver extends BroadcastReceiver{
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    devices = intent.getIntExtra("IsProximal", 0);
		    finished = intent.getBooleanExtra("IsFinish", false);
		    
		  }
	};
	
	
    class  WaitUntilBluetoothScans extends AsyncTask<String, Void, String> {
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			while (!finished){
				if (devices > 0){
					proximityDeviceList();
					printBtDeviceList();
					
					OpportunisticDevices.getInstance().setOpportunisticDevices(btDeviceListProximity);
				}else{
					//Log.d(TAG, "No devices in the list");
				}
				
			}
			finished = false;
			devices = 0;
			return null;
		}
		

    }*/


}
