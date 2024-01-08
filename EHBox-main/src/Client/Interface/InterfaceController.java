package Client.Interface;

import Client.Connection.ServerConnector;
import Client.Interface.Panels.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * This class manages all of the panels and the whole client
 * @author James Martland 24233781
 */
public class InterfaceController {

    protected static ServerConnector SC = new ServerConnector("http://localhost", "8080");

    private final JFrame frame = new JFrame("EdgeHillBox");
    protected static String[] fileStructure;
    protected static String[] dirStructure;
    protected static String[] fullStructure;
    static JTabbedPane tabs;
    public Notifier N = new Notifier();

    private final WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            print("Exit Event Found");
            //what to do when exiting
            //sever connection with client
        }
    };

    //Panels
    private LogInPanel LIP;
    private JPanel pnlLogInPanel = new JPanel( null );

    private SettingPanel SP;
    private JPanel pnlSettings = new JPanel( null );

    private ServerInteractPanel SIP;
    private JPanel pnlInteract = new JPanel( null );

    private UploadPanel UP;
    private JPanel pnlUpload = new JPanel( null );

    private DownloadPanel DP;
    private JPanel pnlDownload = new JPanel( null );

    private ViewPanel VP;
    private JPanel pnlView = new JPanel( null );
    //End Of Panels

    public static void main(String[] args) {
        InterfaceController c = new InterfaceController();
        c.startController();
    }

    /**
     * This method initialises the frame, sets the UI look
     */
    public void startController() {
        //this is a cool bit of code that will set the ui
        //to look like a windows ui
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch( Exception e ) {}

        print( "Starting Interface" );
        this.frame.setTitle("EdgeHillBox Client");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(566,415);//this is a strange size, but the tabbed pane would appear off the screen
        this.frame.addWindowListener(exitListener);
        this.frame.setLayout(null);
        this.frame.setResizable(false);
        print("Initialisation of frame complete");

        print("Test Connection");
        try {
            updateStructure();
            //connection OK
        } catch (InterruptedException e) {
            e.printStackTrace();
            fileStructure = new String[0];
            dirStructure = new String[0];
            fullStructure = new String[0];
        } catch (IOException e) {
            e.printStackTrace();
            fileStructure = new String[0];
            dirStructure = new String[0];
            fullStructure = new String[0];
        }

        buildPanels();
    }

    /**
     * This method initialises all of the panel threads and then runs them and Synchronises them
     */
    private void buildPanels() {
        print("Starting Panel building");

        //init classes and threads for building
        LIP = new LogInPanel();
        Thread buildLogInPanel = new Thread( LIP );
        SP = new SettingPanel();
        Thread buildSettingPanel = new Thread( SP );
        SIP = new ServerInteractPanel();
        Thread buildInteractPanel = new Thread( SIP );
        UP = new UploadPanel();
        Thread buildUploadPanel = new Thread( UP );
        DP = new DownloadPanel();
        Thread buildDownloadPanel = new Thread( DP );
        VP = new ViewPanel();
        Thread buildViewPanel = new Thread( VP );


        //start all threads
        print("Starting Threads");
        buildLogInPanel.start();
        buildSettingPanel.start();
        buildInteractPanel.start();
        buildUploadPanel.start();
        buildDownloadPanel.start();
        buildViewPanel.start();

        try {

            buildLogInPanel.join();
            this.pnlLogInPanel = LIP.getPanel();
            buildSettingPanel.join();
            this.pnlSettings = SP.getPanel();
            buildInteractPanel.join();
            this.pnlInteract = SIP.getPanel();
            buildUploadPanel.join();
            this.pnlUpload = UP.getPanel();
            buildDownloadPanel.join();
            this.pnlDownload = DP.getPanel();
            buildViewPanel.join();
            this.pnlView = VP.getPanel();

            initTabs();
        } catch( InterruptedException e) {
            print( "Thread has been interrupted" );
        }


        //try statement
        //Join and return
    }

    /**
     * This method sets up the tabbed pane and adds the panels to it
     */
    private void initTabs() {
        print( "Initialising tabs" );

        tabs = new JTabbedPane();
        tabs.setSize(550, 400);
        tabs.setLocation(0, -24);

        print("adding Log In Panel");
        tabs.addTab("Log In", pnlLogInPanel); //0

        tabs.addTab("Settings", pnlSettings); //1

        tabs.addTab("Interact", pnlInteract); //2

        tabs.addTab("Upload", pnlUpload); //3

        tabs.addTab("Download", pnlDownload); //4

        tabs.addTab("View", pnlView); //5

        /*tabs.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI(){
            protected void paintTabArea(Graphics g,int tabPlacement,int selectedIndex){}
        });*/
        this.frame.add(tabs);
        this.frame.setVisible(true);

    }

    /**
     * This method switches the tab on the tabbed pane to the index in parameter
     * @param n - index to switch to
     */
    protected void toTab(int n) {
        tabs.setSelectedIndex(n);
    }

    /**
     * This method is called when the client makes a change to the server and notifies the panels that they need to update
     * @throws InterruptedException
     * @throws IOException
     */
    protected void updateStructure() throws InterruptedException, IOException{
         fileStructure = SC.getStructure( ServerConnector.DownloadType.FILE);
        dirStructure = SC.getStructure( ServerConnector.DownloadType.DIR);
        fullStructure = SC.getStructure( ServerConnector.DownloadType.FULL);
        Tabs.updateAll();
    }

    /**
     * This method allows you to print to the command line
     * @param text - text to print
     */
    public void print(String text) {
        System.out.println(text);
    }
}
