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
import android.content.Intent;
import android.util.Log;


public class D2DBluetoothOn implements BluetoothState {
	
	private static final String TAG = D2DBluetoothOn.class.getSimpleName();
	
	private BluetoothAdapter btAdapter;
	private Context btContext;
	
	D2DBluetooth d2DBluetooth;
	
	public D2DBluetoothOn(D2DBluetooth d2DBluetooth){
		this.d2DBluetooth = d2DBluetooth;
		btAdapter = d2DBluetooth.getD2DBluetoothAdapter();
		btContext = d2DBluetooth.getContext();
		
	}
	

	@Override
	public void discovery() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void idle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on() {
		// TODO Auto-generated method stub
		Log.d(TAG, "On method");

		//Just turns on blueetooth
		Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
        btContext.startActivity(enableBtIntent);

		//Turns on bluetooth and enables the device to be discover t seconds, t 0 always, t interval
		/*Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
		btContext.startActivity(discoverableIntent);

        d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothIdle());*/
		
	}

	@Override
	public void off() {
		// TODO Auto-generated method stub
		
	}
	

}
