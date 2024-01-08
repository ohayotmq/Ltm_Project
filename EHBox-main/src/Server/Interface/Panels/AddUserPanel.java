package Server.Interface.Panels;

import Server.Interface.BuildUtilitiesServerV1;
import Server.Users.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class builds and manages the add user panel
 * @author James Martland 24233781
 */
public class AddUserPanel extends BuildUtilitiesServerV1 implements Runnable, ActionListener {

    JTextField txtUsername = new JTextField();
    JPasswordField pssPassword = new JPasswordField();
    JPasswordField pssRePassword = new JPasswordField();
    JButton btnAdd = new JButton();
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

        txtUsername.setToolTipText("Username");
        addTextField(txtUsername, panel, 100, 25, 350, 50);

        pssPassword.setToolTipText("Password Input");
        addPasswordField(pssPassword, panel, 100, 125, 350, 50);

        pssRePassword.setToolTipText("Retype Password");
        addPasswordField(pssRePassword, panel, 100, 225, 350, 50);

        addButton(this, panel, btnCancel, 100, 325, "Cancel", 150, 50, "This Button will cancel the action");

        addButton(this, panel, btnAdd, 300, 325, "Add", 150, 50, "This button will add a user to the database");
    }

    /**
     * This method clears the text fields in this panel
     * @param n
     */
    public void clear(int n) {
        if( n == 0 ) {
            //clear all
            txtUsername.setText("");
        }
        pssPassword.setText("");
        pssRePassword.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnAdd) {
            if( pssPassword.getText().equals(pssRePassword.getText())) {
                //same
                if( hasIllegalChars(txtUsername.getText()) || hasIllegalChars(pssPassword.getText())) {
                    N.opPane("You are only allowed to use alphanumeric characters");
                } else {
                    if( DB.match(txtUsername.getText(), pssPassword.getText()) == 0 ) {
                        //as long as the database search returns 0 - no match found
                        DB.addToList(new User(txtUsername.getText(), pssPassword.getText()));
                        N.updateCombo = true;
                        N.opPane("Added User");
                        toTab(Tabs.CONTROL);
                        clear(0);


                    } else {
                        N.opPane("That username is already taken");
                    }
                }
            } else {
                N.opPane("Passwords Do Not Match");
                clear(1);
            }
        }
        if(e.getSource() == btnCancel) {
            toTab( Tabs.CONTROL );
            clear(0);
        }
    }

}
