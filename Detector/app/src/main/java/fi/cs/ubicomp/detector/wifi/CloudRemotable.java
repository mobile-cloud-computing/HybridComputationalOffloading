package fi.cs.ubicomp.detector.wifi;


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
