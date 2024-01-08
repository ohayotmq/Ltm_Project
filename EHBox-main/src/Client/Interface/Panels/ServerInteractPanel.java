package Client.Interface.Panels;

import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is to build and manage the interact panel
 * @author James Martland 24233781
 */
public class ServerInteractPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable {

    JButton btnUpload = new JButton();
    JButton btnView = new  JButton();
    JButton btnDownload = new JButton();
    JButton btnLogOut = new JButton();

    @Override
    public void run() {
        print( "Thread - Building Server Interact" );
        initInteractPanel();
        print( "Thread - Finished Server Interact" );
    }

    /**
     * This method initialises and builds the interact panel
     */
    public void initInteractPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addButton(this, panel, btnUpload, 25, 100, "Upload", 150, 100, "Upload a file to server");

        addButton(this, panel, btnView, 200, 100, "View", 150, 100, "view files on server");

        addButton(this, panel, btnDownload, 375, 100, "Download", 150, 100, "Download a file from server");

        addButton(this, panel, btnLogOut, 200, 250, "Log Out", 150, 100, "Log out of client");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnUpload) {
            toTab( Tabs.UPLOAD ); // to the upload panel
        }
        if(e.getSource() == btnView) {
            toTab( Tabs.VIEW );
        }
        if(e.getSource() == btnDownload) {
            toTab( Tabs.DOWNLOAD );
        }
        if(e.getSource() == btnLogOut) {
            toTab( Tabs.LOG_IN ); // back to log in screen
            SC.clearAccessCode();
        }
    }
}
