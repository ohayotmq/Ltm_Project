package Client.Connection;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This is the interface that will be implemented for use in the client part of the application
 */
public interface ClientInterface {

    /**
     * This method is to send a http get request
     * @param serverUri
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    HttpResponse get(String serverUri) throws IOException, InterruptedException;

    /**
     * This method is to send a get request and download the file
     * @param serverUri
     * @param clientSaveAddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    int getAndDownload(String serverUri, String clientSaveAddress) throws IOException, InterruptedException;

    /**
     * This method is to send a http put request
     * @param serverAddress
     * @param fileToSend
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    int put( String serverAddress, String fileToSend ) throws IOException, InterruptedException, ExecutionException;

    /**
     * This method is to send a http delete request
     * @param serverAddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    int delete( String serverAddress ) throws IOException, InterruptedException;

    /**
     * This method is to send a http post request - this is going to mainly be used to see if username and password is correct
     * @param username
     * @param password
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    int post( String username, String password) throws IOException, InterruptedException;
}
