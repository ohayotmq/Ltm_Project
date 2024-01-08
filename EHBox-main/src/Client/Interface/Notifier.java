package Client.Interface;

import javax.swing.*;

/**
 * This class is to aid in keeping the whole gui up to date and also for notifying the user using option panes
 * @author James Martland 24233781
 */
public class Notifier {

    /**
     * This sends a status code based on the http response code
     * @param code
     * @param action
     */
    public void fromCode(int code, String action) {
        switch (code) {
            case 200:
                opPane("Successfully " + action);
                break;
            case 404:
                opPane("Requested Resource could not be found");
                break;
            case 406:
                opPane("Not Acceptable - Something in your input is wrong");
                break;
            case 401:
                opPane("Unauthorized - make sure that you have logged in");
                break;
            default:
                internalError();
                break;
        }
    }

    /**
     * This method calls a JOptionPane and shows it to the user with the message that is parameterised
     * @param message
     */
    public void opPane(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * This method is shows the user an option pane that tells them that the connection was interrupted
     */
    public void connInterrrupted() {
        opPane("Connection was Interrupted. Please Try again Later");
    }

    /**
     * This method shows the user an option pane to tell them that there was an internal error
     */
    public void internalError() {
        opPane("An Internal Error has Occurred, Please Try again later");
    }
}
