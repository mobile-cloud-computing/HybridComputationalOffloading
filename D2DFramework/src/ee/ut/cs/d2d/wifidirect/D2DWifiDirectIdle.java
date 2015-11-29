package ee.ut.cs.d2d.wifidirect;

/**
 * Created by huber on 11/29/15.
 */
public class D2DWifiDirectIdle implements WifiDirectState {

    D2DWifiDirect d2DWifiDirect;

    public D2DWifiDirectIdle(D2DWifiDirect d2DWifiDirect){
        this.d2DWifiDirect = d2DWifiDirect;
    }



    @Override
    public void discovery() {
        d2DWifiDirect.setD2DWifiDirectState(d2DWifiDirect.getD2DWifiDirectDiscovery());
        d2DWifiDirect.D2DDiscovery();

    }

    @Override
    public void idle() {

    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }
}
