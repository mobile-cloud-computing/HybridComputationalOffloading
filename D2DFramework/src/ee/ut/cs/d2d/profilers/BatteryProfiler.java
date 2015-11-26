package ee.ut.cs.d2d.profilers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import ee.ut.cs.d2d.utilities.Commons;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryProfiler {
	
	private final String TAG = BatteryProfiler.class.getSimpleName();
	
	public static int batteryLevel;
	public static boolean batteryTrackingOn = false;
	
	private Context context;
	
	private Long sampleBatteryVoltage;
	private Long deltaBatteryVoltage;
		
	public BatteryProfiler(Context context){
		this.context = context;
		Commons.sendToScreen(context, "AQUI");
		
		deltaBatteryVoltage = null;
	}
	
	/**
	 * 
	 * declared in the Manifest.xml
	 * receives the Intent from the battery
	 *
	 */
	public static class BatteryBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);				
				int scaleLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				
				if (rawLevel >= 0 && scaleLevel > 0) {
					batteryLevel = (rawLevel * 100) / scaleLevel;
				}else{
					batteryLevel = 0;
				}
			}else{
				if (Intent.ACTION_POWER_CONNECTED.equals(action)){
					Commons.sendToScreen(context, "Device plugged to power source...");
					
				}else{
					if (Intent.ACTION_POWER_DISCONNECTED.equals(action)){
						Commons.sendToScreen(context, "Device unplugged from power source...");
					}
				}
			}
			
		}
	}
	
	
	public void getBatteryLevel(){
		BatteryBroadcastReceiver receiver = new BatteryBroadcastReceiver();
		IntentFilter bFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(receiver, bFilter);
		
	}
	
	private static class SystemClassBattery {
		private final static String SYSTEM_POWER_CLASS = "/sys/class/power_supply";
		private final static String VOLTAGE = "/batt_vol";
		private final static String VOLTAGE_ALT = "/voltage_now";
		private final static String BATTERY = "/battery";
	
		
		public static Long getCurrentVoltage() {
			StringBuilder sb = new StringBuilder();
			sb.append(SYSTEM_POWER_CLASS).append(BATTERY).append(VOLTAGE);
			Long result = readLong(sb.toString());
			if (result != -1)
				return result;
			else {
				sb = new StringBuilder();
				sb.append(SYSTEM_POWER_CLASS).append(BATTERY).append(VOLTAGE_ALT);
				result = readLong(sb.toString());
				return result;
			}

		}

		private static RandomAccessFile getFile(String filename)
				throws IOException {
			File file = new File(filename);
			return new RandomAccessFile(file, "r");
		}

		private static long readLong(String file) {
			RandomAccessFile randomFile = null;
			try {
				randomFile = getFile(file);
				return Long.valueOf(randomFile.readLine());
			} catch (Exception e) {
				return -1;
			} finally {
				if (randomFile != null) {
					try {
						randomFile.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	
	public void activateBatteryVoltageSamples() {
		sampleBatteryVoltage = SystemClassBattery.getCurrentVoltage();
	}
	
	public void finalizeBatteryVoltageSamples(){
		deltaBatteryVoltage = SystemClassBattery.getCurrentVoltage()- sampleBatteryVoltage;
	}

	
	
}
