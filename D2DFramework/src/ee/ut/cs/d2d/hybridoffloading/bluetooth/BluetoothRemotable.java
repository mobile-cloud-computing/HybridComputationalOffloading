package ee.ut.cs.d2d.hybridoffloading.bluetooth;

/**
 * Created by hflores on 09/12/15.
 */
public abstract class  BluetoothRemotable {
    private static final long serialVersionUID = 3;
    transient protected BluetoothController btController = new BluetoothController();

    protected BluetoothRemotable() {
    }

    public BluetoothRemotable(BluetoothController cc) {
        btController = cc;
    }

    public BluetoothController getBluetoothController() {
        return btController;
    }

    public void setCloudController(BluetoothController btController) {
        this.btController = btController;
    }
}
