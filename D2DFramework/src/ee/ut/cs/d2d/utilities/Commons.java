package ee.ut.cs.d2d.utilities;

import android.content.Context;
import android.content.Intent;
import ee.ut.cs.d2d.framework.D2D;

public class Commons {
	
	public static void sendToScreen(Context context, String result){
		Intent intent = new Intent(D2D.OUTPUT_SCREEN_EVENT);
	    intent.putExtra("isPrint", false);
	    intent.putExtra("output_screen_result", result);
	    
	    context.sendBroadcast(intent);
	 }

}
