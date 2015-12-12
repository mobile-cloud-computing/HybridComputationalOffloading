/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package fi.cs.ubicomp.detector.manager;

import java.util.Arrays;
import java.util.HashSet;

import fi.cs.ubicomp.detector.database.DatabaseHelper;
import fi.cs.ubicomp.detector.database.EventDescriptor;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 
 * @author Huber Flores
 *
 */


public class MyEventContentProvider extends ContentProvider {

	private DatabaseHelper database;
	
	private static final String AUTHORITY = "fi.cs.ubicomp.detector.manager";
	
	private static final String BASE_PATH = "events";
	  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);
	
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/events";
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/event";
	
	private static final int EVENTS = 10;
	private static final int EVENT_ID = 20;
	
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, EVENTS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EVENT_ID);
	    
	  }
		  
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case EVENTS:
		  id = sqlDB.insert(EventDescriptor.TABLE_SURROGATE, null, values);
		  break;
		  
		default:
		  throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	  // check if the caller has requested a column which does not exists
			
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case EVENTS:
			// Set the table
			checkColumns(projection);
			queryBuilder.setTables(EventDescriptor.TABLE_SURROGATE);
		  break;
		case EVENT_ID:
		  // adding the ID to the original query
		  queryBuilder.appendWhere(EventDescriptor.COLUMN_EVENT_ID + "="
		      + uri.getLastPathSegment());
		  break;
				  
		default:
		  throw new IllegalArgumentException("Unknown URI: " + uri);
		}  
		  SQLiteDatabase db = database.getWritableDatabase();
		  Cursor cursor = queryBuilder.query(db, projection, selection,
		      selectionArgs, null, null, sortOrder);
		  // make sure that potential listeners are getting notified
		  cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    return cursor;

	}

	@Override
	  public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case EVENTS:
	      rowsDeleted = sqlDB.delete(EventDescriptor.TABLE_SURROGATE, selection,
	          selectionArgs);
	      break;
	    case EVENT_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(EventDescriptor.TABLE_SURROGATE,
	            EventDescriptor.COLUMN_EVENT_ID + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = sqlDB.delete(EventDescriptor.TABLE_SURROGATE,
	            EventDescriptor.COLUMN_EVENT_ID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	  }

	  @Override
	  public int update(Uri uri, ContentValues values, String selection,
	      String[] selectionArgs) {

	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
	    case EVENTS:
	      rowsUpdated = sqlDB.update(EventDescriptor.TABLE_SURROGATE,
	          values, 
	          selection,
	          selectionArgs);
	      break;
	    case EVENT_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsUpdated = sqlDB.update(EventDescriptor.TABLE_SURROGATE,
	            values,
	            EventDescriptor.COLUMN_EVENT_ID + "=" + id, 
	            null);
	      } else {
	        rowsUpdated = sqlDB.update(EventDescriptor.TABLE_SURROGATE,
	            values,
	            EventDescriptor.COLUMN_EVENT_ID + "=" + id 
	            + " and " 
	            + selection,
	            selectionArgs);
	      }
	      break;
	     		      
		      
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	  }
	
	 private void checkColumns(String[] projection) {
		    String[] available = {EventDescriptor.COLUMN_TIMESTAMP, EventDescriptor.COLUMN_OWN_DEVICE_ADDRESS, EventDescriptor.COLUMN_SURROGATE_ID, EventDescriptor.COLUMN_SURROGATE_ADDRESS, EventDescriptor.COLUMN_WIFIDIRECT, EventDescriptor.COLUMN_BLUETOOTH, EventDescriptor.COLUMN_WIFICLOUD, EventDescriptor.COLUMN_CLOUD_RTT, EventDescriptor.COLUMN_BATTERY_LEVEL, EventDescriptor.COLUMN_EVENT_ID};
		    if (projection != null) {
		      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
		      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		      // check if all columns which are requested are available
		      if (!availableColumns.containsAll(requestedColumns)) {
		        throw new IllegalArgumentException("Unknown columns in projection");
		      }
		    }
	 }

}





