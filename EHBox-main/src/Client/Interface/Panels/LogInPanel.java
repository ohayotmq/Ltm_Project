package Client.Interface.Panels;

import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is to build and manage the log in panel
 * @author James Martland 24233781
 */
public class LogInPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable{

    JLabel lblServerStatus = new JLabel();
    JButton btnSettings = new JButton();
    JTextField txtLoginID = new JTextField();
    JPasswordField pssLoginPwd = new JPasswordField();
    JButton btnLogIn = new JButton();

    @Override
    public void run() {
        print( "Thread - Building Log In panel");
        initLogInPanel();
        print( "Thread - Finished Log In Panel");
    }

    /**
     * This initialises and builds the log in panel
     */
    public void initLogInPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addLabel(lblServerStatus, panel, 0, 0, ( SC.getServerAddress() + "\t " + SC.lastStatus ), 200, 50);

        addButton(this, panel, btnSettings, 450, 0, "Setting", 100, 70, "This button allows you to change the connections settings");

        txtLoginID.setToolTipText("Log In ID");
        addTextField(txtLoginID, panel, 100, 100, 350, 50);

        pssLoginPwd.setToolTipText("Password");
        addPasswordField(pssLoginPwd, panel, 100, 200, 350, 50);

        addButton(this, panel, btnLogIn, 150, 300, "Log In", 250, 70, "This button will let the user log into the system");
    }

    /**
     * This clears the editable fields of the panel
     */
    public void clearPanel() {
        txtLoginID.setText("");
        pssLoginPwd.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSettings ) {
            print("Settings");
            toTab( Tabs.SETTING );
        }
        if(e.getSource() == btnLogIn ) {
            print("Log In");
            try {
                switch( SC.post( txtLoginID.getText(), pssLoginPwd.getText()) ) {
                    case 200:
                        //log in successful
                        toTab( Tabs.INTERACT );// to the interact panel
                        clearPanel();
                        break;
                    default:
                        N.opPane( SC.getBodyMessage() );
                        break;
                }

            } catch (InterruptedException interruptedException) {
                N.connInterrrupted();
                interruptedException.printStackTrace();
            } catch (IOException ioException) {
                N.internalError();
                ioException.printStackTrace();
            }
        }
    }

}
