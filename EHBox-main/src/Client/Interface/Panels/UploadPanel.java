package Client.Interface.Panels;

import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * This class builds and manages the upload panel
 * @author James Martland 24233781( Edge Hill University )
 */
public class UploadPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable{

    JLabel lblFilePath = new JLabel();
    JScrollPane scrollFilePath = new JScrollPane(lblFilePath);
    JButton btnOpen = new JButton();
    JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    JLabel lblSerDestination = new JLabel();
    JScrollPane scrollServerDest = new JScrollPane(lblSerDestination);
    JComboBox comboServerDest = new JComboBox(dirStructure);

    JButton btnCancel = new JButton();
    JButton btnUpload = new JButton();

    @Override
    public void run() {
        print( "Thread = Building Upload" );
        initUploadPanel();
        print( "Thread - Finished Upload" );

        /**
         * This is to check whether the combobox needs to be updated or not
         */
        Thread updateUpload = new Thread(new Runnable() {
            @Override
            public void run() {
                while( true ) {
                    while ( !Tabs.getUpdateUpload() ) {
                        Thread.yield();
                    }
                    comboServerDest.setModel( new DefaultComboBoxModel(dirStructure));
                    Tabs.setUpdateUpload(false);
                }
            }
        });
        updateUpload.start();
    }

    /**
     * This method initialises and builds the panel
     */
    public void initUploadPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);

        addScrollPane(scrollFilePath, panel, 50, 50, 450, 50);

        addScrollPane(scrollServerDest, panel, 50, 150, 450, 50);
        resetPanel();
        addButton(this, panel, btnOpen, 400, 100, "Open", 100, 50, "This Button is to open a file directory");

        addComboBox(comboServerDest, panel, 350, 200, 150, 50);

        addButton(this, panel, btnCancel, 100, 275, "Cancel", 150, 75, "This button cancels the upload");

        addButton(this, panel, btnUpload, 300, 275, "Upload", 150, 75, "This button uploads the file chosen");
    }

    /**
     * This method resets the panel so that all the data on it is reset
     */
    public void resetPanel() {
        chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        lblFilePath.setText("<None Selected>");
        lblSerDestination.setText("<Server Destination>");

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnCancel ){
            // back to interact panel
            toTab( Tabs.INTERACT );
            resetPanel();
        }
        if(e.getSource() == btnUpload ) {
            //show message dialog when 200 ok received from server if not dont switch

            //call upload method
            try {
                //print("name To upload: " + comboServerDest.getSelectedItem().toString() + "/" + chooser.getSelectedFile().getName() + "\nFrom Path: " + chooser.getSelectedFile().getAbsolutePath() );

                print("Status: " + SC.put(comboServerDest.getSelectedItem().toString() + "/" + chooser.getSelectedFile().getName(),
                        chooser.getSelectedFile().getAbsolutePath() ));

                updateStructure();

            } catch (InterruptedException iE) {
                iE.printStackTrace();
                N.connInterrrupted();
            } catch (IOException ioE) {
                ioE.printStackTrace();
                N.internalError();
            } catch (ExecutionException eE) {
                eE.printStackTrace();
                N.internalError();
            }

            toTab( Tabs.INTERACT );

            try {
                updateStructure();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            resetPanel();
        }
        if(e.getSource() == btnOpen ){

            int r = chooser.showOpenDialog(null);

            if( r == JFileChooser.APPROVE_OPTION) {
                lblFilePath.setText(chooser.getSelectedFile().getAbsolutePath());

            }
        }
    }

}
