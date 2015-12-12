/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/


package fi.cs.ubicomp.detector.database;

import fi.cs.ubicomp.detector.manager.MyEventContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * 
 * @author Huber Flores
 *
 */

public class DatabaseManager {
		
	private Uri dbUri;
	private Context dContext;
	
	
	public DatabaseManager(Context c){
		this.dContext = c;
	}
	
	public void saveData(double timestamp, String myaddress, String id, String address, double wfValue, double btValue, double wfCloudValue, double wfRTTValue, double batteryLevel ){
		ContentValues values = new ContentValues();
		values.put(EventDescriptor.COLUMN_TIMESTAMP, timestamp);
		values.put(EventDescriptor.COLUMN_OWN_DEVICE_ADDRESS, myaddress);
		values.put(EventDescriptor.COLUMN_SURROGATE_ID, id);
		values.put(EventDescriptor.COLUMN_SURROGATE_ADDRESS, address);
		values.put(EventDescriptor.COLUMN_WIFIDIRECT, wfValue);
		values.put(EventDescriptor.COLUMN_BLUETOOTH, btValue);
		values.put(EventDescriptor.COLUMN_WIFICLOUD, wfCloudValue);
		values.put(EventDescriptor.COLUMN_CLOUD_RTT, wfRTTValue);
		values.put(EventDescriptor.COLUMN_BATTERY_LEVEL, batteryLevel);
		 	
		dbUri = dContext.getContentResolver().insert(MyEventContentProvider.CONTENT_URI, values);
		
	 }
	
	public void setDbUri(Uri cursor){
		this.dbUri = cursor;
	}


}
