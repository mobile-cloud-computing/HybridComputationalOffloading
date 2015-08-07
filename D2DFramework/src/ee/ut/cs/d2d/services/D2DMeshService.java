package ee.ut.cs.d2d.services;

import ee.ut.cs.d2d.network.D2DBluetooth;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class D2DMeshService extends Service {
	
	private final String TAG = D2DMeshService.class.getSimpleName();
	
	private final IBinder mBinder = new MyBinder();
	
	D2DBluetooth btDevice = null;
	
	@Override  
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
			
		return Service.START_NOT_STICKY;
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		public D2DMeshService getMeshServiceInstance() {
			return D2DMeshService.this;
		}

	}
	
	
	public void btOn(){
		btDevice.D2DOn();
	}
	
	public void btOff(){
		btDevice.D2DOff();
	}
	
	public void btDiscovery(){
		btDevice.D2DDiscovery();
	}
	
	public void print(){
		Log.d(TAG, "Service method called");
	}
	
	public void setServiceContext(Context context){
		if (btDevice==null){
			Log.d(TAG, "device instance is null");
			btDevice = new D2DBluetooth(context);
		}
		
	}

}
