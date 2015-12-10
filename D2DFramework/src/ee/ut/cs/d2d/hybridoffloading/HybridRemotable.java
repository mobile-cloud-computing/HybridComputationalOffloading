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
    transient protected HybridController hybridController = new HybridController();

    protected HybridRemotable() {
    }

    public HybridRemotable(HybridController cc) {
        hybridController = cc;
    }

    public HybridController getHybridController() {
        return hybridController;
    }

    public void setCloudController(HybridController cloudController) {
        this.hybridController = cloudController;
    }

}
