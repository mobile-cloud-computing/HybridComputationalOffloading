package ee.ut.cs.d2d.network;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;


/**
 * 
 * @author Huber Flores
 *
 */

public class D2DBluetooth {
	
	private static final String TAG = D2DBluetooth.class.getSimpleName();
	
	BluetoothState btDiscovery;
	BluetoothState btIdle;
	
	BluetoothState btOn;
	BluetoothState btOff;
	
	BluetoothState btState;
	
	private Context context;
	private BluetoothAdapter btAdapter;
	
	private String btMacMyDevice = null;
	
	/**
	 * State machine for Bluetooth device
	 * 
	 */
	public D2DBluetooth(Context context){
		Log.d(TAG, "Main device controller");
		
		this.context = context;
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (btAdapter!=null){
			btMacMyDevice = btAdapter.getAddress();
			
			this.btIdle = new D2DBluetoothIdle(this);
			this.btOn = new D2DBluetoothOn(this);
			this.btOff = new D2DBluetoothOff(this);
			this.btDiscovery = new D2DBluetoothDiscovery(this);
			
			btState = btIdle;
		}
		
	}
	
	public String getMacAdressMyDevice(){
		return this.btMacMyDevice;
	}
	
	public BluetoothAdapter getD2DBluetoothAdapter(){
		return this.btAdapter;
	}
	
	public void setD2DBluetoothState(BluetoothState btState){
		this.btState = btState;
	}
	
	public BluetoothState getD2DBluetoothState(){
		return this.btState;
	}
	
	public BluetoothState getD2DBluetoothDiscovery(){
		return this.btDiscovery;
	}
	

	public BluetoothState getD2DBluetoothIdle(){
		return this.btIdle;
	}
	
	public BluetoothState getD2DBluetoothOn(){
		return this.btOn;
	}
	
	public BluetoothState getD2DBluetoothOff(){
		return this.btOff;
	}
	
	public void D2DDiscovery(){
		btState.discovery();
	}
	
		
	public void D2DIdle(){
		btState.idle();
	}
	
	public void D2DOn(){
		btState.on();
	}
	
	public void D2DOff(){
		btState.off();
	}
	
	public Context getContext(){
		return this.context;
	}
	
	
}
