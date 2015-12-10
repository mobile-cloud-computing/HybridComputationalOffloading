/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/


package fi.cs.ubicomp.detector.database;

import android.content.Context;
import android.net.Uri;

/**
 * 
 * @author Huber Flores
 *
 */

public class DatabaseHandler {

	public static DatabaseHandler instance;
	
	private Uri dbUri; 
	private static DatabaseManager dManager = null;
	
	private Context context;

	private DatabaseHandler(){ 

	}

	public static synchronized DatabaseHandler getInstance(){
		if (instance==null){
			instance = new DatabaseHandler();
			return instance;
		}

		return instance;
	}
	
	public void setContext(Context context){
		this.context = context;
		dManager = new DatabaseManager(context);
	    dManager.setDbUri(dbUri);
	}
	
	public static DatabaseManager getDatabaseManager(){
		return dManager;
	}


	
	
	
}
