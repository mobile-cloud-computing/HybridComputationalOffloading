package ee.ut.cs.d2d.utilities;

import android.content.Context;
import android.util.Log;

public class Logger {
	
	private static Context mContext;
	private static Logger logger;
	
	private Logger() {}
	
	public static Logger getInstance() {
		if (logger == null) {
			logger = new Logger();
		}
		return logger;
	}
	
	public void init(Context context) {
		mContext = context;
	}
	
	
	
	public static void log(String type, String tag, String msg) {
		if (type.equalsIgnoreCase("VERBOSE")) {
			Log.v(tag, msg);
		} else if (type.equalsIgnoreCase("DEBUG")) {
			Log.d(tag, msg);
		} else if (type.equalsIgnoreCase("INFO")) {
			Log.i(tag, msg);
		} else if (type.equalsIgnoreCase("WARNING")) {
			Log.w(tag, msg);
		} else if (type.equalsIgnoreCase("ERROR")) {
			Log.e(tag, msg);
		} else {
			Log.i(tag, msg);
		}
		
	}
	
}