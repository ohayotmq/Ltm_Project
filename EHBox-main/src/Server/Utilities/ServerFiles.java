package Server.Utilities;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface should be implemented by fileutils and
 * used to make sure that it can communicate with the server http handler
 */
public interface ServerFiles {

    /**
     * When implemented this method should allow files to be saved from an inputstream at a designated filepath destination
     * @param in
     * @param filePath
     */
    public void saveToFile(InputStream in, String filePath) throws IOException;

    /**
     * When implemented this method should allow files to be read to a byte array and returned
     * @param directory
     * @return
     */
    public byte[] readFromFile( String directory ) throws IOException;
}
