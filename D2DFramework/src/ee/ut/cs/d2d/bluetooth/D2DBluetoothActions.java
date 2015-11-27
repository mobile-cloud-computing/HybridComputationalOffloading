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

import java.util.List;

import ee.ut.cs.d2d.data.DeviceListAdapter;
import ee.ut.cs.d2d.utilities.Commons;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

public class D2DBluetoothActions extends BroadcastReceiver{
	
	private final String TAG = D2DBluetoothActions.class.getSimpleName();
	//public static ArrayList<BluetoothDevice> btDeviceList = new ArrayList<BluetoothDevice>();
	//int cont;
	
	private Context context;

	private List<String> mDevices;
	private DeviceListAdapter mListAdapter;
	
	
	public D2DBluetoothActions(Context context, List<String> mDevices, DeviceListAdapter mListAdapter){
		this.context = context;
		this.mDevices = mDevices;
		this.mListAdapter = mListAdapter;
		//cont = 0;
	}
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                                 BluetoothAdapter.ERROR);
            switch (state) {
            case BluetoothAdapter.STATE_OFF:
            		Commons.sendToScreen(context, "Bluetooth OFF");
            	
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
            		Commons.sendToScreen(context, "Turning OFF Bluetooth...");
            	
                break;
            case BluetoothAdapter.STATE_ON:
            		Commons.sendToScreen(context, "Bluetooth ON");
            	
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
            		Commons.sendToScreen(context, "Turning ON Bluetooth...");
                break;
            }
        }else{
        	if(BluetoothDevice.ACTION_FOUND.equals(action)) {
        	       BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        	       Log.d(TAG, "\n  Device: " + device.getName() + ", " + device);
        	       
        	       //btDeviceList.add(device);
        	       //cont++;

				  if (!mDevices.contains(device.getName())){
					  mDevices.add(device.getName());
					  mListAdapter.notifyDataSetChanged();
				  }

			}else{
        		if(BluetoothDevice.ACTION_UUID.equals(action)) {
        	         BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        	         Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
					Log.d(TAG, "\n  UUID Device: " + device.getName() + ", " + device);
        	         if (uuidExtra!=null){
        	        	 
        	         }
        	         
        	         
        	    }else{
        	    	if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
        	            Log.d(TAG,"\nDiscovery Started");
        	            
        	         }else{
        	        	 if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
        	                 Log.d(TAG, "\nDiscovery Finished");
        	                 //btDiscoveryFinished();
        	                 
        	                 
        	              }
        	         }
        	    }
        	}
        } 
    }
	
	
	/*public static void clearDeviceList(){
		btDeviceList.clear();
	}
	
	public void btDiscoveryFinished(){
		Intent intent = new Intent(D2DBluetoothDiscovery.BT_DISCOVERY_FINISHED);
		intent.putExtra("IsProximal", cont);
		intent.putExtra("IsFinish", true);
		
		cont = 0;
	
		context.sendBroadcast(intent);
		
	}*/
	

		
}


