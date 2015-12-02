/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package ee.ut.cs.d2d.hybridoffloading;

public class NetInfo{

	//The communication negotiation of each particular interface, e.g., bluetooth, wifidirect, etc., it assigns the IP address and port automatically
	//Those values are centralized here in order to accessed by the mobile app.
	//public static byte[] IPAddress =  {Integer.valueOf("127").byteValue(),Integer.valueOf("0").byteValue(),Integer.valueOf("0").byteValue(),Integer.valueOf("1").byteValue()};
	public static byte[] IPAddress =  {Integer.valueOf("192").byteValue(),Integer.valueOf("168").byteValue(),Integer.valueOf("49").byteValue(),Integer.valueOf("1").byteValue()};

	//Choosen as default port
	public static int port = 6000;



	public static int waitTime = 10000;
	  
} 

