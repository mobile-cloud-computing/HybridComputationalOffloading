/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package fi.cs.ubicomp.detector.bluetooth;

import java.util.List;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import fi.cs.ubicomp.detector.data.DeviceData;
import fi.cs.ubicomp.detector.database.DatabaseHandler;
import fi.cs.ubicomp.detector.utilities.Commons;

public class D2DBluetoothActions extends BroadcastReceiver{
	
	private final String TAG = D2DBluetoothActions.class.getSimpleName();


	public D2DBluetoothActions(){

	}
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();

		DatabaseHandler dataEvent = DatabaseHandler.getInstance();
		dataEvent.setContext(context);


        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                                 BluetoothAdapter.ERROR);
            switch (state) {
            case BluetoothAdapter.STATE_OFF:
            		Log.i(TAG, "Bluetooth OFF");
            	
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
				Log.i(TAG, "Turning OFF Bluetooth...");
            	
                break;
            case BluetoothAdapter.STATE_ON:
				Log.i(TAG, "Bluetooth ON");
            	
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
				Log.i(TAG, "Turning ON Bluetooth...");
                break;
            }
        }else{
        	if(BluetoothDevice.ACTION_FOUND.equals(action)) {
        	       BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        	       Log.d(TAG, "\n  Device: " + device.getName() + ", " + device);

				dataEvent.getInstance().getDatabaseManager().saveData(System.currentTimeMillis(),
						device.getName(),
						device.getAddress(),
						0,
						1,
						0,
						0);

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

        	              }
        	         }
        	    }
        	}
        } 
    }

		
}


