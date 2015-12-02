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

public abstract class CloudRemotable implements Serializable  {
    private static final long serialVersionUID = 3;
    transient protected CloudController cloudController = new CloudController();

    protected CloudRemotable() {
    }

    public CloudRemotable(CloudController cc) {
        cloudController = cc;
    }

    public CloudController getCloudController() {
        return cloudController;
    }

    public void setCloudController(CloudController cloudController) {
        this.cloudController = cloudController;
    }

}
