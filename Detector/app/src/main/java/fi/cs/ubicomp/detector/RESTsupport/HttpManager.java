package fi.cs.ubicomp.detector.RESTsupport;



import java.io.File;
import java.io.IOException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class HttpManager {
	private static HttpManager instance;

	private HttpManager() {

	}

	public static HttpManager getInstance() {
		if (instance == null)
			instance = new HttpManager();
		return instance;
	}
		

	public boolean UploadFile(File file, String URL) throws IOException {
		HttpClient client = new DefaultHttpClient();
		
		HttpPost post = new HttpPost(URL);

		
		FileBody bin = new FileBody(file);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		reqEntity.addPart("file", bin);
		post.setEntity(reqEntity);
		HttpResponse response = client.execute(post);
		
		
		int status = response.getStatusLine().getStatusCode();
		HttpEntity resEntity = response.getEntity();
		
		if (resEntity != null) {    
             Log.i("RESPONSE",EntityUtils.toString(resEntity));
        }
		return true;
	}

	
	
}
