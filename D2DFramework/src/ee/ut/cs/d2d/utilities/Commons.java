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


	/*
	* GCM Config
	* */

	public static final String GCM_PROJECT_ID = "662678731680";
	public static final String GCM_MESSAGE_KEY = "message";
	public static final String GCM_SERVER_URL = "http://powerful-river-1630.herokuapp.com/register";



	/*
	* Bluetooth
	* */

	public static final String NAME = "HybridOffloading";
	public static final String UUID = "1ed592f8-9928-44ef-9650-4bed9afc5596"; //auto-generated via https://www.uuidgenerator.net/
}
