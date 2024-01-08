package Client.Interface.Panels;

import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is to build and manage the setting panel
 * @author James Martland 24233781
 */
public class SettingPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable {

    JTextField txtAddress = new JTextField();
    JTextField txtPort = new JTextField();

    JButton btnCancel = new JButton();
    JButton btnConnect = new JButton();

    @Override
    public void run() {
        print("Thread - Building Setting Panel");
        initSettingPanel();
        print("Thread - Finished Setting Panel");
    }

    /**
     * This method initialises and builds the setting panel
     */
    public void initSettingPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);
        txtAddress.setToolTipText("Server Address");
        txtAddress.setText(SC.getHostAddress());

        addTextField(txtAddress, panel, 50, 50, 300, 50);

        txtPort.setText(SC.getPort());
        txtPort.setToolTipText("Server Port");
        addTextField(txtPort, panel, 50, 150, 150, 50);

        addButton(this, panel, btnCancel, 100, 300, "Cancel", 150, 50, "This Button cancels all of the changes" );

        addButton(this, panel, btnConnect, 300, 300, "Save & Connect", 150, 50, "This Button will save and attempt connecting to the server");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnCancel ) {
            //revert changes and switch back to log in
            txtPort.setText(SC.getPort());
            txtAddress.setText(SC.getHostAddress());
            toTab( Tabs.LOG_IN );
        }
        if(e.getSource() == btnConnect ) {
            //change connection properties and attempt connect
            //  if connection success - go back to log in
            //  if not - go back to other screen - show error message

            try {
                    int code1 = SC.testNewConnection(txtAddress.getText() + ":" + txtPort.getText());
                    int code2 = SC.testNewConnection(txtAddress.getText());
                    print( "Codes: " + code1 + " / " + code2);
                if ( code1 == 200 || code2 == 200) {
                    SC.setHostAddress(txtAddress.getText());
                    SC.setPort(txtPort.getText());
                    print("Changed Server");
                    updateStructure();
                    N.opPane("Successfully changed Server");
                    toTab( Tabs.LOG_IN );
                } else {
                    print("Failed Change");
                    N.opPane("Failed to change Server");
                }


            } catch (IOException ioe) {
                N.internalError();
                ioe.printStackTrace();
            } catch (InterruptedException ex) {
                N.connInterrrupted();
                ex.printStackTrace();
            }

        }
    }
}
