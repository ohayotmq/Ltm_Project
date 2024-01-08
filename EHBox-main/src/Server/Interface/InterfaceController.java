package Server.Interface;

import Server.Interface.Panels.AddUserPanel;
import Server.Interface.Panels.EditServerPanel;
import Server.Interface.Panels.RemoveUserPanel;
import Server.Interface.Panels.ServerControlPanel;
import Server.Server;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * This class manages all of the panels
 */
public class InterfaceController extends Server {

    private JFrame frame = new JFrame();

    private static JTabbedPane tabs;
    protected static Notifier N = new Notifier();

    private final WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            print("Exit Event Found");
            //what to do when exiting
            //sever connection with client
            close();
        }
    };

    private ServerControlPanel SC;
    private JPanel pnlServerControl = new JPanel(null);

    private RemoveUserPanel RU;
    private JPanel pnlRemoveUser = new JPanel(null);

    private AddUserPanel AU;
    private JPanel pnlAddUser = new JPanel(null);

    private EditServerPanel ES;
    private JPanel pnlEditServer = new JPanel(null);

    /**
     * This is the main method for the Server
     * @param args
     */
    public static void main(String[] args) {
        InterfaceController IC = new InterfaceController();
        IC.startController();
    }

    /**
     * This method initialises the frame and also sets the ui look and feel, as well as notifying the database that it needs to read and starting the server
     */
    public void startController() {
        DB.readFrom(serverHome + "/users.txt");

        startServer(8);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        print("[Server] Starting Interface");
        this.frame.setTitle("EdgeHillBox Server");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(566,415);//this is a strange size, but the tabbed pane would appear off the screen
        this.frame.addWindowListener(exitListener);
        this.frame.setLayout(null);
        this.frame.setResizable(false);
        print("[Server] Initialisation of frame complete");

        buildPanels();
    }

    /**
     * This method initialises the threads to build the panels and the starts them and then synchronises them
     */
    public void buildPanels() {
        print("[Server-Interface] Starting Build of Panels");

        SC = new ServerControlPanel();
        Thread buildServerControlPanel = new Thread( SC );
        RU = new RemoveUserPanel();
        Thread buildRemoveUserPanel = new Thread( RU );
        AU = new AddUserPanel();
        Thread buildAddUserPanel = new Thread( AU );
        ES = new EditServerPanel();
        Thread buildEditServerPanel = new Thread( ES );

        print("[Server-Interface] Starting Threadded Building");
        buildServerControlPanel.start();
        buildRemoveUserPanel.start();
        buildAddUserPanel.start();
        buildEditServerPanel.start();

        try {
            buildServerControlPanel.join();
            this.pnlServerControl = SC.getPanel();
            buildRemoveUserPanel.join();
            this.pnlRemoveUser = RU.getPanel();
            buildAddUserPanel.join();
            this.pnlAddUser = AU.getPanel();
            buildEditServerPanel.join();
            this.pnlEditServer = ES.getPanel();

            initTabs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This mehtod is to set up the tabbed pane and then add all of the panels to it
     */
    public void initTabs(){
        print("[Server-Interface] Initialising Tabs");

        tabs = new JTabbedPane();
        tabs.setSize(550, 400);
        tabs.setLocation(0, -24);

        print("[Server-Interface] Adding Panels");
        tabs.addTab("Control", pnlServerControl);

        tabs.addTab("Remove", pnlRemoveUser);

        tabs.addTab("Add", pnlAddUser);

        tabs.addTab("Edit", pnlEditServer);

        this.frame.add(tabs);
        this.frame.setVisible(true);
        print("[Server-Interface] Finished Build");
    }

    /**
     * This method is to set the selected index on the tabbed pane to the parameterised index
     * @param n - index to switch to
     */
    protected void toTab(int n){
        tabs.setSelectedIndex(n);
    }

    /**
     * This method notifies the remove user panel that it needs to update
     */
    protected void update() {
        N.updateCombo = true;
    }

    /**
     * This method is called to close the program and save the database
     */
    public void close() {
        print("[Server-Interface] Closing");
        stopServer();

        DB.saveTo(serverHome + "/users.txt");
        System.exit(1);
    }

    /**
     * This method restarts the server
     */
    public void restartServer() {
        print(getHostname());
        print("" + getPort());
        stopServer();
        startServer(8);
    }


}
