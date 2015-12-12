package fi.cs.ubicomp.detector.utilities;

import android.content.Context;
import android.content.Intent;


public class Commons {

	public static final String wifiDirect = "wifidirect";
	public static final String bluetooth = "bluetooth";


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


	/*
	* RTT
 	* */
	public static final String SERVER_ADDRESS= "ec2-";
	public static final String SERVER_NAME = "Amazon-EC2";
	public static final String SERVER_ADDRESS_ERROR="127.0.0.1";
	public static final String SERVER_NAME_ERROR = "unreachable";


	/*
	* CODE
	* */
	public static final String SECURITY_CODE_STOP = "333";
	public static final String SECURITY_CODE_EXTRACT = "313";


	/*
	* Upload file service
	* */

	public static final String WEB_SERVICE_POST = "http://192.168.43.85:8080/Uploader/UploadServlet";


	/*
	* Database location
	* */
	public static final String DATABASE_PATH = "/data/data/fi.cs.ubicomp.detector/databases/event-surrogate.db";   //better to used - context.getDatabasePath(DataBaseHelper.dbName).toString;
}
