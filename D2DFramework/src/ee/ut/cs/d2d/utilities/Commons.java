package ee.ut.cs.d2d.utilities;

import android.content.Context;
import android.content.Intent;
import ee.ut.cs.d2d.framework.D2DLog;

public class Commons {

	public static final String wifiDirect = "wifidirect";
	public static final String bluetooth = "bluetooth";
	
	public static void sendToScreen(Context context, String result){
		Intent intent = new Intent(D2DLog.OUTPUT_SCREEN_EVENT);
	    intent.putExtra("isPrint", false);
	    intent.putExtra("output_screen_result", result);
	    
	    context.sendBroadcast(intent);
	 }

}
