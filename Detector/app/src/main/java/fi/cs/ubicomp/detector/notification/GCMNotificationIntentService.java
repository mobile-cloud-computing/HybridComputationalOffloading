package fi.cs.ubicomp.detector.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;


import fi.cs.ubicomp.detector.R;
import fi.cs.ubicomp.detector.SurrogateSensor;
import fi.cs.ubicomp.detector.utilities.Commons;

public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				for (int i = 0; i < 3; i++) {
					//Log.d(TAG, "Working..." + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
					//Log.d(TAG, "Working");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}

				}
				//Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				//Log.d(TAG, "Completed work");

				sendNotification("Message Received from HyMobi: "
						+ extras.get(Commons.GCM_MESSAGE_KEY));
				//Log.i(TAG, "Received: " + extras.toString());
				//Log.d(TAG, "Received");
			}
		}
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		//Log.d(TAG, "Preparing to send notification");
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SurrogateSensor.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.gcm_cloud)
				.setContentTitle("Infrastructure Sensor")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		//Log.d(TAG, "Preparing sent successfully");
	}
}
