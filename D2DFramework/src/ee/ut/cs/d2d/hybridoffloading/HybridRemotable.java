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

public abstract class HybridRemotable implements Serializable  {
    private static final long serialVersionUID = 3;
    transient protected HybridController cloudController = new HybridController();

    protected HybridRemotable() {
    }

    public HybridRemotable(HybridController cc) {
        cloudController = cc;
    }

    public HybridController getHybridController() {
        return cloudController;
    }

    public void setCloudController(HybridController cloudController) {
        this.cloudController = cloudController;
    }

}
