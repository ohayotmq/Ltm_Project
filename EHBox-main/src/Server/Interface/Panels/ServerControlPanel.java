package Server.Interface.Panels;

import Server.Interface.BuildUtilitiesServerV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class builds and manages the server main control panel
 * @author James Martland 24233781
 */
public class ServerControlPanel extends BuildUtilitiesServerV1 implements Runnable, ActionListener {

    JLabel lblCurrentAddress = new JLabel();
    JButton btnEdit = new JButton();
    JButton btnExit = new JButton();
    JButton btnRestart = new JButton();
    JButton btnAdd = new JButton();
    JButton btnDelete = new JButton();

    @Override
    public void run() {
        print("[Server Control Panel] Building Panel");
        initPanel();
        print("[Server Control Panel] Finished Panel");
        Thread updateLabel = new Thread( new Runnable() {
            @Override
            public void run() {
                while(true){
                    while( !N.updateInterface ) {
                        Thread.yield();
                    }
                    lblCurrentAddress.setText(getHostname() + ":" + getPort() );
                    N.updateInterface = false;
                }
            }
        });
    }

    /**
     * This method initialises and builds the panel and its components
     */
    public void initPanel(){
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addLabel(lblCurrentAddress, panel, 50, 0, getHostname() + ":" + getPort(), 300, 50 );

        addButton(this, panel, btnEdit, 400, 0, "Edit", 100, 50, "This button is to edit the servers address");

        addButton(this, panel, btnExit, 50, 100, "Exit", 150, 100, "This Button is to exit the server application");

        addButton(this, panel, btnRestart, 350, 100, "Restart", 150, 100, "This button is to restart the server");

        addButton(this, panel, btnAdd, 50, 250, "Add User", 150, 100, "This button is to add a user to the server");

        addButton(this, panel, btnDelete, 350, 250, "Delete User", 150, 100, "This buttonis to delete a user");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnEdit) {
            toTab( Tabs.EDIT );
        }
        if(e.getSource() == btnExit) {
            close();
        }
        if(e.getSource() == btnRestart) {
            restartServer();
            N.opPane("Restarting");
        }
        if(e.getSource() == btnAdd) {
            toTab( Tabs.ADD );
        }
        if(e.getSource() == btnDelete) {
            toTab( Tabs.REMOVE );
        }
    }
}
