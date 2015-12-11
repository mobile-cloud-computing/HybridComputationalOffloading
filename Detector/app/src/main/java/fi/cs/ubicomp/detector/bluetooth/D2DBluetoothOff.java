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

import android.bluetooth.BluetoothAdapter;
import android.util.Log;



public class D2DBluetoothOff implements BluetoothState {
	
	private static final String TAG = D2DBluetoothOff.class.getSimpleName();

	D2DBluetooth d2DBluetooth;
	private BluetoothAdapter btAdapter;
	
	
	public D2DBluetoothOff(D2DBluetooth d2DBluetooth){
		this.d2DBluetooth = d2DBluetooth;
		btAdapter = d2DBluetooth.getD2DBluetoothAdapter();
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
		
	}

	@Override
	public void off() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Off method");
		btAdapter.disable();
		d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothIdle());
		
	}

}
