package Client.Interface.Panels;

/**
 * This class is used to make code more readable, such as the Interface controller
 * @author James Martland 24233781
 */
public class Tabs {
    protected static final int LOG_IN = 0;
    protected static final int SETTING = 1;
    protected static final int INTERACT = 2;
    protected static final int UPLOAD = 3;
    protected static final int DOWNLOAD = 4;
    protected static final int VIEW = 5;

    public static volatile boolean updateLogIn = false;
    public static volatile boolean updateUpload = false;
    public static volatile boolean updateDownload = false;
    public static volatile boolean updateView = false;

    /**
     * This method notifies all the panels that they need to update
     */
    public static void updateAll() {
        updateLogIn = true;
        updateUpload = true;
        updateDownload = true;
        updateView = true;
    }

    /**
     * This method is a getter for updateUpload boolean
     * @return updateUplaod
     */
    public static boolean getUpdateUpload() {
        return updateUpload;
    }

    /**
     * This method is a setter for the update upload variable
     * @param updateUpload
     */
    public static void setUpdateUpload(boolean updateUpload) {
        Tabs.updateUpload = updateUpload;
    }

    /**
     * This method is a getter for the update download variable
     * @return updateDownload
     */
    public static boolean getUpdateDownload() {
        return updateDownload;
    }

    /**
     * This method is a setter for the updateDownload variable
     * @param updateDownload
     */
    public static void setUpdateDownload(boolean updateDownload) {
        Tabs.updateDownload = updateDownload;
    }

    /**
     * This method is a getter for the updateView variable
     * @return updateView
     */
    public static boolean getUpdateView() {
        return updateView;
    }

    /**
     * This method is a setter for the updateView variable
     * @param updateView
     */
    public static void setUpdateView(boolean updateView) {
        Tabs.updateView = updateView;
    }

    /**
     * This method is a getter for the variable updateLogIn
     * @return updateLogIn
     */
    public static boolean getUpdateLogIn() {
        return updateLogIn;
    }

    /**
     * This method is a setter for the variable updateLogIn
     * @param updateLogIn
     */
    public static void setUpdateLogIn(boolean updateLogIn) {
        Tabs.updateLogIn = updateLogIn;
    }
}
