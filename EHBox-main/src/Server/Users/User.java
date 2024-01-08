package Server.Users;

/**
 * This class will store the necessary information about users that are going to be logging into the server
 * @author James Martland 24233781
 */
public class User {
    private String password;
    private String username;

    /**
     * This is the constructor for the user - this will be used to automatically set up the User Object
     * @param uname
     * @param pwd
     */
    public User(String uname, String pwd) {
        this.username = uname;
        this.password = pwd;
    }

    /**
     * This is another constructor that allows a user to be created from an array
     * @param array
     */
    public User(String[] array) {
        this.username = array[0];
        this.password = array[1];
    }

    /**
     * This method is to check whether an input is equal to the password that is saved
     * @param pwd
     * @return
     */
    public boolean checkPassword(String pwd) {
        if(password.equals(pwd)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the setter method for the password of the user
     * @param pwd
     */
    public void setPassword(String pwd) {
        this.password = pwd;
    }

    /**
     * this is the getter method for the username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * This is the setter method for the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method is for saving to files
     * @return
     */
    public String makeString() {
        return username + "," + password;
    }
}
