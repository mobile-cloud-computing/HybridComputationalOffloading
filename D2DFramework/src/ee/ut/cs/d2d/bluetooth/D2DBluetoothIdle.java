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

import android.util.Log;

public class D2DBluetoothIdle implements BluetoothState {
	
	private static final String TAG = D2DBluetoothIdle.class.getSimpleName();
	
	D2DBluetooth d2DBluetooth;
	
	public D2DBluetoothIdle(D2DBluetooth d2DBluetooth){
		this.d2DBluetooth = d2DBluetooth;
	}

	
	@Override
	public void discovery() {
		// TODO Auto-generated method stub
		d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothDiscovery());
		d2DBluetooth.D2DDiscovery();
		
	}

	@Override
	public void idle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on() {
		// TODO Auto-generated method stub
		Log.d(TAG, "On method");
		d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothOn());
		d2DBluetooth.D2DOn();
	}

	@Override
	public void off() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Off method");
		d2DBluetooth.setD2DBluetoothState(d2DBluetooth.getD2DBluetoothOff());
		d2DBluetooth.D2DOff();
		
	}

}
