package Server.Interface.Panels;

import Server.Interface.BuildUtilitiesServerV1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class builds and manages the remove user panel
 * @author James Martland 24233781
 */
public class RemoveUserPanel extends BuildUtilitiesServerV1 implements Runnable, ActionListener {

    JComboBox comboUsers = new JComboBox( DB.getUsers() );
    JButton btnRemove = new JButton();
    JButton btnCancel = new JButton();

    @Override
    public void run() {
        print("[Server Control Panel] Building Panel");
        initPanel();
        print("[Server Control Panel] Finished Panel");

        /**
         * This thread is to constantly check if the panel needs to be updated, and if so it will do so
         */
        Thread checkUpdate = new Thread( new Runnable() {
            @Override
            public void run() {
                while(true) {
                    while (!N.updateCombo) {
                        Thread.yield();
                    }
                    update();
                }
            }
        });
        checkUpdate.start();
    }

    /**
     * This method will initialise and build the panel and its components
     */
    public void initPanel(){
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addComboBox(comboUsers, panel, 100, 50, 350, 100);

        addButton(this, panel, btnCancel, 100, 200, "Cancel", 150, 100, "This button will cancel the action");

        addButton(this, panel, btnRemove, 300, 200, "Remove", 150, 100, "This button will remove a user from the database");

    }

    /**
     * This method will update the panel so that it is up to date
     */
    public void update() {
        comboUsers.setModel( new DefaultComboBoxModel(DB.getUsers() ) );
        N.updateCombo = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnRemove) {
            DB.removeFromList( comboUsers.getSelectedItem().toString() );
            N.opPane("User Removed");
            toTab( Tabs.CONTROL );
            update();
        }
        if(e.getSource() == btnCancel) {
            toTab( Tabs.CONTROL );
        }
    }

}
