package Server.Utilities;

import Server.Users.User;

import java.util.ArrayList;

/**
 * This is the class that will be implemented by the FileUtils and used to make sure that the users can be saved to a file
 * @author James Martland 24233781
 */
public interface DBFiles {

    /**
     * When implemented this method will save the arraylist to a file
     * Not using generics as it kinda broke the ability to save users
     * @param list
     * @param filepath
     */
    public void save(ArrayList<User> list, String filepath);

    /**
     * When implemented this method will read the file and return a string array of objects
     * @param filepath
     * @return
     */
    public ArrayList<User> read(String filepath);
}
