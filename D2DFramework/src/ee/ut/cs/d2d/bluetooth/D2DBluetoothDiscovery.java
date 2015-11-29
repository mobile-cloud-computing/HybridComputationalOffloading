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

	private final String TAG = D2DBluetoothDiscovery.class.getSimpleName();

	private BluetoothAdapter btAdapter;
	
	D2DBluetooth d2DBluetooth;
	Context context;
	
	
	public D2DBluetoothDiscovery(D2DBluetooth d2DBluetooth){
		this.d2DBluetooth = d2DBluetooth;
		this.context = d2DBluetooth.getContext();
		this.btAdapter = d2DBluetooth.getD2DBluetoothAdapter();
	}
	

	@Override
	public void discovery() {
		// TODO Auto-generated method stub
		btAdapter.startDiscovery();

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

}
