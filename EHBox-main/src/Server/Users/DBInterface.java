package Server.Users;

/**
 * This is to be used by the users database to interact with the server
 * @author James Martland 24233781
 */
public interface DBInterface <E> {

    /**
     * Generic class to ensure implementation fo add to list method
     * @param e
     */
    public void addToList(E e);

    /**
     * This method removes an object from the list based on a string identifier
     * @param identifier
     */
    public void removeFromList(String identifier);

    /**
     * This method checks if there is a match in the database based on two string idenifiers
     * @param identifier1
     * @param identifier2
     * @return - 0 if no match, 1 if 1st identifier matches, 2 if second identifier matches as well as first
     */
    public int match(String identifier1, String identifier2);

    /**
     * This method is to save the DB to a file
     * @param path
     */
    public void saveTo(String path);

    /**
     * This method is to read the DB from a file
     * @param path
     */
    public void readFrom(String path);

}
