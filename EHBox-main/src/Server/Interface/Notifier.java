package Server.Interface;

import javax.swing.*;

/**
 * This class is to aid in keeping the whole gui up to date and also for notifying the user using option panes
 * @author James Martland 24233781
 */
public class Notifier {

    public volatile boolean updateCombo = false;
    public volatile boolean updateInterface = false;

    /**
     * This method calls a JOptionPane and shows it to the user with a particular parameterised message
     * @param message
     */
    public void opPane(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}
