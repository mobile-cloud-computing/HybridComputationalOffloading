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

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class D2DScanScheduler extends BroadcastReceiver {

	public final static String TAG = D2DScanScheduler.class.getCanonicalName();
	
	public static final String D2DSCANSCHEDULER_ACTION_SCAN = "ee.ut.cs.intent.action.SCAN";

	// Restart service every t seconds
	// Scheduling policy
	// There should be multiple policies to scan for surrogates
	//private static final long REPEAT_TIME = 1000 * 120; //(1000 = 1 sec) * 120 (60*2) (60 seconds = 1 minute)
	private static final long REPEAT_TIME = 1000 * 900;
	//private static final long REPEAT_TIME = 1000 * 3600;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		/*The service starts in the onStartCommand method*/
		Intent i = new Intent(context, D2DScanService.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar cal = Calendar.getInstance();
		// Start 30 seconds after boot completed
		cal.add(Calendar.SECOND, 30);
		//
		// Fetch every t seconds
		// InexactRepeating allows Android to optimize the energy consumption
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), REPEAT_TIME, pending);
		
		/*Toast.makeText(context, "Service is schedule to restart each: " + REPEAT_TIME/1000 + " seconds",
		        Toast.LENGTH_LONG).show();*/
		
		
		
		Log.d(TAG, "My scheduler is called...");
		
	} 

}
