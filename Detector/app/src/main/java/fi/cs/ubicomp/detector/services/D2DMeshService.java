/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package fi.cs.ubicomp.detector.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import fi.cs.ubicomp.detector.network.NetworkCenter;
import fi.cs.ubicomp.detector.network.NetworkDevice;


public class D2DMeshService extends Service {
	
	private final String TAG = D2DMeshService.class.getSimpleName();
	
	private final IBinder mBinder = new MyBinder();

	NetworkDevice nDevice = null;

	 
	@Override  
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		  

		if (nDevice!=null){
			nDevice.D2DDiscovery();
		}

		//it works as expected
		//proper policies should be put in place
		//works together with startScanScheduler(); 
		forcedStop(); //3
		
		return Service.START_NOT_STICKY;
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	@Override
	public void onDestroy(){
		Log.d(TAG, "service finished");
		
		//scheduler service is stopped here
		//works together with startScanScheduler() and forcedStop();
		stopScanScheduler(); //2
		
		super.onDestroy();
	}
		
	public class MyBinder extends Binder {
		public D2DMeshService getMeshServiceInstance() {
			return D2DMeshService.this;
		}

	}

	public void on(){
		nDevice.D2DOn();
	}

	public void off(){
		nDevice.D2DOff();
	}

	public void discovery(){
		nDevice.D2DDiscovery();
	}


	public void setServiceContext(Context context, String nInterface){

		if (nDevice==null){
			Log.d(TAG, "device instance is null");

			nDevice = new NetworkCenter().getNetworkProvider(context, nInterface);

			//works together with stopScanScheduler() and forcedStop();
			startScanScheduler(); //1
		}

	}

	public void startScanScheduler(){
		Intent intentScheduler = new Intent(D2DScanScheduler.D2DSCANSCHEDULER_ACTION_SCAN);
		sendBroadcast(intentScheduler);
	}
	
	
	public void stopScanScheduler(){
		 Intent intent = new Intent(this, D2DScanService.class);
	 	 PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 
	               PendingIntent.FLAG_CANCEL_CURRENT);
	 	    
	 	 AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
	 	 alarmManager.cancel(pending);

		Log.d(TAG, "alarm service stopped");
	}
	
	
	public void forcedStop(){
		Intent intentStop = new Intent(this, D2DMeshService.class);
 	    stopService(intentStop);
	}

	
	/**
	 * testing method
	 */
	public void print(){
		Log.d(TAG, "Service method called");
	}

}
