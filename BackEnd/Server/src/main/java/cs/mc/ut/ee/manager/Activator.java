/*
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * Please send inquiries to huber AT ut DOT ee
 */

package cs.mc.ut.ee.manager;

import fi.cs.ubicomp.detector.wifi.NetInfo;

/**
 * author Huber Flores
 */

public class Activator {
	 
	private static final long activeTime = 1000 * 3600 * 24 *15;  // 15 days
	
	public static void main(String[] args) {
        
		FrontEnd server = new FrontEnd(NetInfo.port);
		System.out.println("RTT Server Monitor");
		new Thread(server).start();
		

		try {
		    Thread.sleep(activeTime);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
		
		
    }

}
