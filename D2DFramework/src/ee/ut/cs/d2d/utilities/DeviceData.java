package ee.ut.cs.d2d.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceData {
	
	Map<String, String> device = null;
	
	public static DeviceData instance;
	
	private DeviceData(){ 
		
	}
	
	public static synchronized DeviceData getInstance(){
		if (instance==null){
			instance = new DeviceData();
			return instance;
		}
		
		return instance;
	}
	
	
	public Map<String, String> getDeviceData(Context context){
		device = new HashMap<String, String>();
		
		String android_id = Secure.getString(context.getApplicationContext()
	            .getContentResolver(), Secure.ANDROID_ID);
		
		device.put("android_id", android_id);
		//System.out.println("System out, android_id : " + android_id);

	    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    final String tmDevice, tmSerial, androidId;
	    
	    tmDevice = "" + tm.getDeviceId();
	    device.put("tmDevice", tmDevice);
	    //System.out.println("System out, tmDevice : " + tmDevice);
	    
	    
	    tmSerial = "" + tm.getSimSerialNumber();
	    //System.out.println("System out, tmSerial : " + tmSerial);
	    device.put("tmSerial", tmSerial);
	    
	    
	    androidId = ""
	            + android.provider.Settings.Secure.getString(
	                    context.getContentResolver(),
	                    android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice
	            .hashCode() << 32)
	            | tmSerial.hashCode());
	    String uuid = deviceUuid.toString();
	    device.put("tmUUID", uuid);
	    //System.out.println("UUID: " + uuid);
	    
	    return device;
	}
	
	

}
