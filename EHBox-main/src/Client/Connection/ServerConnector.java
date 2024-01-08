package Client.Connection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class is to enable the program to connect to the server
 * This class was created using this resource : 
 *		(How to send HTTP request GET/POST in Java - Mkyong.com, 2020)
 *		(java, GlassEditor.com, Sheikh and Joshi, 2020)
 *		See Report References
 * @author James Martland 24233781
 */
public class ServerConnector implements ClientInterface {

    private String hostAddress;
    private String port;
    public int lastStatus;
    private String accessCode = "";
    private String bodyMessage;

    private static HttpClient client;

    /**
     * This is an enum that describes the type of download that is taking place
     */
    public enum DownloadType{
        FULL,
        DIR,
        FILE
    }

    /**
     * This method is the constructor that sets the core attributes for the class
     * @param hostAddress
     * @param portNo
     */
    public ServerConnector(String hostAddress, String portNo){
        this.hostAddress = hostAddress;
        this.port = portNo;

        client = HttpClient.newHttpClient();
        
    }

    /**
     * This method is used to test if a connection is available, this is used when changing server
     * @param newAddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int testNewConnection(String newAddress) throws IOException, InterruptedException{
        HttpClient newConnection = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri( URI.create(newAddress + "/") )
                .GET()
                .build();
        HttpResponse<String> r = newConnection.send(request, HttpResponse.BodyHandlers.ofString());
        print("Body: " + r.body().toString() );
        return r.statusCode();
    }

    /**
     * This is a getter method for the port
     * @return port
     */
    public String getPort() {
        return port;
    }

    /**
     * This is a setter method for the port
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * This is a getter method for the host address
     * @return hostAddress
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * This is a setter method for the host Address
     * @param hostAddress
     */
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    /**
     * This method returns the servers full address in the form hostname:port
     * @return
     */
    public String getServerAddress() {
        if(port == null || port.equals("")) {
            return hostAddress;
        } else{
            return hostAddress + ":" + port;
        }
    }

    /**
     * This is the method to get a specific thing from the server
     * @param resourceUri
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public HttpResponse get( String resourceUri) throws IOException, InterruptedException {
        print("Getting");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .setHeader("key", accessCode)
                .uri( URI.create( getServerAddress() + resourceUri.replace(" ", "%20")) )
                .GET()
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        lastStatus = response.statusCode();
        print("GET Status: " + response.statusCode() + "" );
        return response;
    }

    /**
     * This method returns the body of the response as a string
     * @param r
     * @return
     */
    public String getBody( HttpResponse r ) {
        return r.body().toString();
    }

    /**
     * This method gets the file and downloads it
     * @param resourceUri
     * @param clientSaveAddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int getAndDownload( String resourceUri, String clientSaveAddress) throws IOException, InterruptedException {
        HttpResponse response = get( resourceUri );
        saveToFile(resourceUri, clientSaveAddress, response);
        return response.statusCode();
    }

    /**
     * This method saves the resource in the uri to the file
     * @param resourceUri
     * @param saveTo
     * @param response
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean saveToFile(String resourceUri, String saveTo, HttpResponse response ) throws FileNotFoundException, IOException {
        print("\nGet" );
        URL url = new URL("http://localhost:8080"+resourceUri.replace(" ", "%20"));
        HttpURLConnection httpurl = (HttpURLConnection) url.openConnection();
        httpurl.setRequestProperty("key", accessCode);

        // https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
        InputStream in = new BufferedInputStream( httpurl.getInputStream() );
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int count = 0;

        while( (count = in.read(buffer) ) != -1 ) {
            out.write(buffer, 0, count);
        }
        out.close();
        in.close();
        FileOutputStream fos = new FileOutputStream(saveTo + getExtension(resourceUri) );
        fos.write( out.toByteArray() );
        fos.close();

        return true;
    }

    /**
     * This method is for putting files on the server
     * @param resourceUri
     * @param fileToSend
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public int put( String resourceUri, String fileToSend ) throws InterruptedException, IOException, ExecutionException {
        print("\nPut");

        byte[] data = Files.readAllBytes( (new File(fileToSend)).toPath() );

        print("Len: " + data.length);

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .setHeader("key", accessCode)
                .uri( URI.create(getServerAddress() + resourceUri.replace(" ", "%20")) )
                .PUT( HttpRequest.BodyPublishers.ofFile( Paths.get(fileToSend) ) )
                .build();


        CompletableFuture <HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        while( !response.isDone() ) {
            // do nothing
        }
        lastStatus = response.get().statusCode();
        return response.get().statusCode();
    }

    /**
     * This method requests server to delete a file
     * @param resourceUri
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int delete( String resourceUri) throws IOException, InterruptedException {
        print("\nDelete");
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("key", accessCode)
                .uri( URI.create(getServerAddress() + resourceUri) )
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        lastStatus = response.statusCode();
        return response.statusCode();
    }

    /**
     * This method sends a post request to the server, mainly containing the username and password to test
     * @param username
     * @param password
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int post( String username, String password ) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .version( HttpClient.Version.HTTP_1_1)
                .setHeader("key", accessCode)
                .uri( URI.create( getServerAddress() ) )
                .POST( HttpRequest.BodyPublishers.ofString( formatLogin(username, password)) )
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        print("POST Status: " + response.statusCode() + "" );
        switch( response.statusCode() ){
            case 200:
                accessCode = response.body().toString();
                break;
            default:
                this.bodyMessage = response.body().toString();
        }
        print(accessCode);
        return response.statusCode();
    }

    /**
     * this is a getter message for the body variable that is set in the post method
     * @return
     */
    public String getBodyMessage() {
        return bodyMessage;
    }

    /**
     * This method clears the access code to make sure that when a user logs out the blank access code is sent
     */
    public void clearAccessCode(){
        this.accessCode = "";
    }

    /**
     * This method formats the body of the post request in the correct way to send
     * @param username
     * @param password
     * @return
     */
    public String formatLogin(String username, String password) {
        return "username="+username.replace(" ", "+")+"&password="+password.replace(" ", "+");
    }

    /**
     * This method gets the file structures based on the enum download type
     * it sends a get request to the server
     * @param type
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public  String[] getStructure(DownloadType type) throws IOException, InterruptedException {
        String body;
        switch(type) {
            case FILE:
                //file
                body = getBody( get("/~fileStructure") );
                break;
            case DIR:
                //dir
                body = getBody( get("/~dirStructure") );
                break;
            case FULL:
                //full
                body = getBody( get("/~fullStructure") );
                break;
            default:
                body = "/";
                break;
        }
        return body.substring(1, body.length()-1).split(", ");
    }

    /**
     * This method is to get the file extension from file paths to aid in saving them
     * @param resourceUri
     * @return
     */
    public String getExtension(String resourceUri ) {
        return resourceUri.substring( resourceUri.lastIndexOf('.') );
    }

    /**
     * This is a method for printing strings to the command line
     * @param text
     */
    public void print(String text) {
        System.out.println(text);
    }
}
