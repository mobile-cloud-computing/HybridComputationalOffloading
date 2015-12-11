/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package fi.cs.ubicomp.detector.wifi;


import java.io.Serializable;


/**
 * 
 * @author Huber Flores
 *
 */

public class Pack implements Serializable{
    private static final long serialVersionUID = 1;
    String RTT = null;


    public Pack(String rtt) {
        this.RTT = rtt;

    }

    public String getRTT(){
        return this.RTT;
    }
    

 }