package ee.ut.cs.d2d.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class D2DScanService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, D2DMeshService.class);
		context.startService(service);
	}

}
