package Client.Interface.Panels;
import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class builds and manages the download panel
 * @author James Martland 24233781( Edge Hill University )
 */
public class DownloadPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable {

    JComboBox comboServerDest = new JComboBox(fileStructure);

    JLabel lblSaveLoc = new JLabel();
    JScrollPane scrollSaveLoc = new JScrollPane(lblSaveLoc);
    JButton btnChoose = new JButton();
    JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    JButton btnDelete = new JButton();
    JButton btnCancel = new JButton();
    JButton btnDownload = new JButton();

    @Override
    public void run() {
        print( "Thread = Building Upload" );
        initUploadPanel();
        print( "Thread - Finished Upload" );

        /**
         * This thread is to update the combobox when the structure changes
         */
        Thread checkCondition = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    while (!Tabs.getUpdateDownload()) {
                        Thread.yield();
                    }
                    print("changing Download");
                    comboServerDest.setModel(new DefaultComboBoxModel(fileStructure));
                    Tabs.setUpdateDownload(false);
                }
            }
        });
        checkCondition.start();
    }

    /**
     * This method is to initialise and build the panel and its components
     */
    public void initUploadPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);


        //addScrollPane(scrollSerDest, panel, 50, 50, 450, 50);

        addComboBox(comboServerDest, panel, 50, 50, 450, 50);

        //addLabel(lblSerDestination, panel, 50, 150, "<ServerDest>", 450, 50);

        addScrollPane(scrollSaveLoc, panel, 50, 150, 350, 50);
        resetPanel();

        addButton(this, panel, btnChoose, 400, 150, "Save", 100, 50, "This Button is to open a file directory");

        addButton(this, panel, btnCancel, 50, 275, "Cancel", 150, 75, "This button cancels the upload");

        addButton(this, panel, btnDelete, 200, 275, "Delete", 150, 75, "This button will delete the selected resource");

        addButton(this, panel, btnDownload, 350, 275, "Download", 150, 75, "This button uploads the file chosen");
    }

    /**
     * This method is to reset the panel back to its default after the user has done with it
     */
    public void resetPanel() {
        chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        //lblSerDest.setText("<Server Resource>");
        lblSaveLoc.setText("<Save Location>");

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnCancel ){
            // back to interact panel
            toTab( Tabs.INTERACT );
            resetPanel();
        }
        if(e.getSource() == btnDownload ) {
            //show message dialog when 200 ok recieved from server if not dont switch

            try {
                int status = SC.getAndDownload(comboServerDest.getSelectedItem().toString(), chooser.getSelectedFile().getAbsolutePath() );
                N.fromCode(status, "Downloaded File");
            } catch (IOException iE) {
                iE.printStackTrace();
                N.internalError();
            } catch (InterruptedException iE) {
                iE.printStackTrace();
                N.connInterrrupted();
            }
            //call Download method

            toTab( Tabs.INTERACT );
            resetPanel();
        }
        if(e.getSource() == btnDelete ){
            try {
                int status = SC.delete( comboServerDest.getSelectedItem().toString() );
                N.fromCode(status, "Deleted File");
                updateStructure();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                N.connInterrrupted();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                N.internalError();
            }
        }
        if(e.getSource() == btnChoose ){

            int r = chooser.showSaveDialog(null);

            if( r == JFileChooser.APPROVE_OPTION) {
                lblSaveLoc.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

}
