package Client.Interface.Panels;


import Client.Interface.BuildUtilitiesClientV1;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class builds and manages the view panel
 * @author James Martland 24233781( Edge Hill University )
 */
public class ViewPanel extends BuildUtilitiesClientV1 implements ActionListener, Runnable {

    JTree structure = new JTree();
    JScrollPane scrollStructure = new JScrollPane(structure);

    JButton btnBack = new JButton();


    @Override
    public void run() {
        print("Thread - Building View panel");
        initViewPanel();
        print("Thread - Finished View Panel");

        /**
         * This thread is to update the tree structure when a server change takes place
         */
        Thread updateView = new Thread( new Runnable(){
            @Override
            public void run() {
                while(true) {
                    while( !Tabs.getUpdateView() ) {
                        Thread.yield();
                    }
                    makeTree();
                    Tabs.setUpdateView(false);
                }
            }
        });
        updateView.start();
    }

    /**
     * This method initialises and builds the panel and its components
     */
    public void initViewPanel() {
        panel = new JPanel(null);
        panel.setSize(550, 400);

        makeTree();
        scrollStructure = new JScrollPane(structure);
        addScrollPane(scrollStructure, panel,50, 50, 450, 225 );

        addButton(this, panel, btnBack, 200, 300, "Back", 150, 75, "This Button will take you back to the Interact Screen");
    }

    /**
     * This method is to populate the tree to show the file structure of the server
     */
    public void makeTree() {
        //get structure (String)
        DefaultMutableTreeNode server_Root = new DefaultMutableTreeNode("Server");

        ArrayList<String> directories = new ArrayList<String>();

        for( String item : fullStructure ) {
            directories.add(item);
            print(item);
        }

        // This algorithm wont let me have Gold ring in test folder so jump from
        // /docs/testFile.txt
        // to
        // pictures/private/test/gold Ring.png


        print("TREE - testing" );
        server_Root = createTree(directories, server_Root);

        structure = new JTree( server_Root );

    }

    /**
     * This is a custom recursive class that I built
     * This class needs the structure to be in a folder first structure
     * @param s - ArrayList<String> - holds a list of directories to show to the user
     * @param n - DefaultMutableTreeNode - holds the current node to add to
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode createTree( ArrayList<String> s, DefaultMutableTreeNode n ) {
        print("TREE - Begin Test");
        ArrayList<String> folder = new ArrayList<String>();
        String currentFolder = "";
        Iterator<String> i = s.iterator();
        String[] item;

        //if there is another line to read
        while( i.hasNext() ) {
            print("TREE - Next item");

            String temp = i.next();
            item = temp.substring(1).split("/");//splits the line into directories and folders in array

            if (item.length == 1) {
                print("\tlen1");
                //must be a leaf node
                if (currentFolder.equals("") == false) { // if it isnt the first iteration
                    print("\t\tcurrentfol = something");
                    n.add( createTree( folder, new DefaultMutableTreeNode(currentFolder)) );
                    currentFolder = "";
                    folder = new ArrayList<String>();
                } else {

                }
                print("\t\tLeaf Node Found: " + item[0]);
                print("\t\t" + temp);
                if (temp.charAt(temp.length() - 1) == '/') {
                    //must be a folder - so try adding blank children
                    print("Empty Folder");
                    DefaultMutableTreeNode tempN = new DefaultMutableTreeNode(item[0]);
                    tempN.add(new DefaultMutableTreeNode("/"));
                    n.add(tempN);
                }
                n.add(new DefaultMutableTreeNode(item[0]));

            } else {
                //has a folder in it
                if( currentFolder.equals("") == true) {
                    //if this is empty - not found folder
                    print("\t\tSetting Current Folder: " + item[0]);
                    currentFolder = item[0];
                    folder.add( fromArray(item) ); // add temp????
                } else {
                    if( currentFolder.equals(item[0]) == true ) {
                        //item is added to arrayList

                        folder.add( fromArray(item) );
                    } else {
                        //finished items in this folder
                        print("TREE - Finished Folder");
                        n.add(createTree(folder, new DefaultMutableTreeNode(currentFolder)));
                        currentFolder = item[0];
                        folder = new ArrayList<String>();
                        folder.add( fromArray( item ));

                    }
                }
            }
        }
        return n;
    }

    /**
     * This method is used to generate the next in iteration filepath based on the new root directory
     * @param arr
     * @return
     */
    public String fromArray(String[] arr ) {
        StringBuilder sb = new StringBuilder();
        for( int i = 1; i < arr.length; i++ ) {
            sb.append("/" + arr[i] );
        }
        print("FROMARRAY - " + sb.toString() );
        return sb.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnBack ) {
            print(Tabs.getUpdateView() + "");
            toTab(Tabs.INTERACT );
        }
    }
}

/**
 *
 * Pseudocode to test for recursion
 * [docs][testfile.txt]
 * [pictures][test][test2][boo.png]
 * [sound.mp3]
 * START
 *
 *
 * FOR ITEM IN LIST
 * Is same as current
 *      YES:
 *          add to list
 *          next item;
 *      NO:
 *          is Current = "" :
 *              YES :
 *                  // either starting list or no list
 *
 *                  is length == 1
 *                      YES:
 *                          node.add ( item )
 *                          clear current
 *                          clear list
 *
 *                          NEXT ITEM
 *                      NO:
 *                          current = item[0]
 *                          clear list
 *                          add item to list
 *
 *                          NEXT ITEM
 *                  ENDIF
 *              NO :
 *                  //next line for past stored data
 *                  node.add ( recurse ( list, new node ) )
 *
 *                  clear list
 *                  is length == 1
 *                      YES:
 *                          node.add ( item )
 *                          current = ""
 *
 *                          NEXT ITEM
 *                      NO:
 *                          add item to list
 *                          current = item[0]
 *
 *                          NEXT ITEM
 *                  ENDIF
 *          ENDIF
 *
 *      ENDIF
 *      END WHILE
 *  IF ( list has still got stuff in it )
 *      YES:
 *          node.add ( recurse ( list ) )
 *      NO :
 *          FINISH ?
 * RETURN node
 * END
 *
 *
 *
 * Issue arises when item too long - doesnt process -
 * for while - while i.hasnext() && highestLen = 1
 */
