/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package fi.cs.ubicomp.detector.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Huber Flores
 *
 */


public class EventDescriptor {

	//Database schema
	//Table
	public static final String TABLE_SURROGATE = "surrogate";
	//Attributes
	public static final String COLUMN_EVENT_ID = "_id";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_SURROGATE_ID = "surrogateid";
	public static final String COLUMN_SURROGATE_ADDRESS = "address";
	public static final String COLUMN_WIFIDIRECT = "wifidirect";
	public static final String COLUMN_BLUETOOTH = "bluetooth";
	public static final String COLUMN_WIFICLOUD = "wificloud";
	public static final String COLUMN_CLOUD_RTT = "wifirtt";
	
	//Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SURROGATE
			+ "("
			+ COLUMN_EVENT_ID + " integer primary key autoincrement, "
			+ COLUMN_TIMESTAMP + " real not null, "
			+ COLUMN_SURROGATE_ID + " text not null, "
			+ COLUMN_SURROGATE_ADDRESS + " text not null, "
			+ COLUMN_WIFIDIRECT + " real not null, "
			+ COLUMN_BLUETOOTH + " real not null, "
			+ COLUMN_WIFICLOUD + " real not null, "
			+ COLUMN_CLOUD_RTT + " real not null "
			+");";
	
	 //Database creation
	 public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(DATABASE_CREATE);
	 }

	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_SURROGATE);
		    onCreate(database);
	 }
	
}
