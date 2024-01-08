package Server.Users;

import Server.Utilities.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is the database of users, it includes manipulation methods to add or remove users from the database
 * also to check for matches
 * @author James Martland 24233781
 */
public class UserDB implements DBInterface<User>{

    private ArrayList<User> database = new ArrayList<User>();
    FileUtils FU = new FileUtils();

    /**
     * This method adds a user to the list
     * @param user
     */
    @Override
    public void addToList(User user) {
        System.out.println("[DB] Adding User: " + user.getUsername() );
        database.add(user);
    }

    /**
     * This method searches for a user with a particular username and then removes them from the database
     * @param username
     */
    @Override
    public void removeFromList(String username) {
        //find index of element
        Iterator i = database.iterator();
        User current;
        while(i.hasNext()) {
            current = (User) i.next();

            if( current.getUsername().equals( username ) ) {
                i.remove();
                break;
            } else {}
        }
    }

    /**
     * This method checks for matches in the database from the inputs
     * @param username
     * @param password
     * @return 2 (if username and password match) 1 ( if just username match ) 0 ( no match )
     */
    @Override
    public int match(String username, String password) {
        Iterator i = database.iterator();
        User current;
        while(i.hasNext()) {
            current = (User) i.next();

            if( current.getUsername().equals( username) ) {
                if( current.checkPassword(password) == true){
                    return 2; // username and password match
                } else {
                    return 1; // username only match
                }
            } else {}
        }
        return 0; // no match
    }

    /**
     * This method calls fileUtils and saves the database to a file
     * @param path
     */
    @Override
    public void saveTo(String path) {
        System.out.println("[DB] Saving");
        FU.save( database ,path);
        System.out.println("[DB] Saved");
    }

    /**
     * This method converts the database into a string array
     * @return
     */
    public String[] getUsers() {
        Iterator i = database.iterator();
        StringBuilder sb = new StringBuilder();
        while( i.hasNext() ) {
            sb.append( ( (User) i.next() ).getUsername() + "%&" );
        }
        return sb.toString().split("%&");
    }

    /**
     * This method is to read the database in from a file
     * @param path
     */
    @Override
    public void readFrom(String path) {
        System.out.println("[DB] reading");
        this.database = FU.read( path );
    }
}
