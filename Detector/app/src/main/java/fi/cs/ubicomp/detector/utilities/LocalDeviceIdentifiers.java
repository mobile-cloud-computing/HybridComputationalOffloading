/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Please send inquiries to huber AT ut DOT ee
 *
 */


package fi.cs.ubicomp.detector.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class LocalDeviceIdentifiers {
	
	Map<String, String> device = null;
	
	public static LocalDeviceIdentifiers instance;
	
	private LocalDeviceIdentifiers(){ 
		
	}
	
	public static synchronized LocalDeviceIdentifiers getInstance(){
		if (instance==null){
			instance = new LocalDeviceIdentifiers();
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
	            + Secure.getString(
	                    context.getContentResolver(),
	                    Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice
	            .hashCode() << 32)
	            | tmSerial.hashCode());
	    String uuid = deviceUuid.toString();
	    device.put("tmUUID", uuid);
	    //System.out.println("UUID: " + uuid);
	    
	    return device;
	}
	
	

}
