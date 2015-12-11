package fi.cs.ubicomp.detector.wifidirect;

/**
 * Created by huber on 11/28/15.
 */
public interface WifiDirectState {

    public void discovery();

    public void idle();

    public void on();

    public void off();

}
