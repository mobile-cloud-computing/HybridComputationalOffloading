package ee.ut.cs.d2d.network;

import java.util.ArrayList;

import ee.ut.cs.d2d.network.D2DBluetoothDiscovery.DeviceAwareness;

public class OpportunisticDevices {
	
	/** Concurrent safe*/
	public static OpportunisticDevices instance = new OpportunisticDevices();
	
	private ArrayList<DeviceAwareness> btDeviceListProximity = new ArrayList<DeviceAwareness>();
	
	private OpportunisticDevices(){}

	public static OpportunisticDevices getInstance(){
		return instance;
	}
	
	public ArrayList<DeviceAwareness> getOpportunisticDevices(){
		return this.btDeviceListProximity;
	}
	
	public void setOpportunisticDevices(ArrayList<DeviceAwareness> btDeviceListProximity){
		this.btDeviceListProximity = btDeviceListProximity; 
	}
	
}
