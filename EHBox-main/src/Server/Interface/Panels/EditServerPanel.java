package Server.Interface.Panels;

import Server.Interface.BuildUtilitiesServerV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class builds and manages the edit server panel
 * @author James Martland 24233781
 */
public class EditServerPanel extends BuildUtilitiesServerV1 implements Runnable, ActionListener {

    JTextField txtHostname = new JTextField();
    JTextField txtPort = new JTextField();
    JButton btnChange = new JButton();
    JButton btnCancel = new JButton();

    @Override
    public void run() {
        print("[Server Control Panel] Building Panel");
        initPanel();
        print("[Server Control Panel] Finished Panel");
    }

    /**
     * This method initialises and builds the panel and its components
     */
    public void initPanel(){
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addTextField(txtHostname, panel, 100, 50, 350, 50);

        addTextField(txtPort, panel, 100, 150, 200, 50);

        addButton(this, panel, btnCancel, 100, 250, "Cancel", 150, 100, "This button will cancel the action");

        addButton(this, panel, btnChange, 300, 250, "Change", 150, 100, "This button will change the connection details");

        reset();
    }

    /**
     * This method resets the panel to be its default values
     */
    public void reset() {
        txtHostname.setText(getHostname() );
        txtPort.setText("" + getPort() );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnChange ) {
            try {
                if( Integer.parseInt(txtPort.getText()) < 65354 && Integer.parseInt(txtPort.getText()) > 1023) {
                    setPort( Integer.parseInt(txtPort.getText()) );
                    setHostname( txtHostname.getText() );
                    N.opPane("New Port Set Will restart Server");
                    restartServer();
                    N.updateInterface = true;
                    toTab( Tabs.CONTROL );
                } else {
                    N.opPane("Port out of bounds: 1023 to 65353");
                }
            } catch (NumberFormatException ex ){
                N.opPane("The Port you entered was not an integer");
            }
        }
        if(e.getSource() == btnCancel ) {
            toTab( Tabs.CONTROL );
            reset();
        }
    }

}
