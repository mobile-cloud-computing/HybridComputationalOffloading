/*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* Please send inquiries to huber AT ut DOT ee
*/

package ee.ut.cs.d2d.hybridoffloading;

import java.io.Serializable;
import java.util.List;


public class ResultPack implements Serializable{
    private static final long serialVersionUID = 2;
    Object result = null;
    Object state = null;
    
    List<String> timestamps;

    public ResultPack(Object result, Object state) {
        this.result = result;
        this.state = state;
    }

    public Object getresult(){
        return result;
    }

    public Object getstate(){
        return state;
    }

    
    public void setTimeStamps(List<String> timestamps){
    	timestamps.add(System.currentTimeMillis()+",server2");
    	this.timestamps = timestamps;
	}

	public List<String> getTimeStamps(){
		timestamps.add(System.currentTimeMillis()+",client2");
	return timestamps;
	}
}
