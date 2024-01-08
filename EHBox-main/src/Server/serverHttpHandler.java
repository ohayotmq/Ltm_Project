package Server;

import Server.Utilities.FileUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

/**
 * This class handles all of the requests from the server, using HttpHandler class
 * from com.sun.net.httpserver
 * @author James Martland 24233781
 */
public class serverHttpHandler extends Server implements HttpHandler {
    ServerPasscodeManager SPM = new ServerPasscodeManager();
    private String serverHome;
    private static FileUtils FU = new FileUtils();

    /**
     * This is the constructor that sets the server root directory
     * @param rootdir
     */
    public serverHttpHandler( String rootdir ) {
        this.serverHome = rootdir;
    }

    /**
     * This method figures out request type and then directs request
     * it is called when a request is recieved
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle( HttpExchange exchange) throws IOException {
        print("\n\nClient: " + exchange.getLocalAddress() );
        print("Method: " + exchange.getRequestMethod() );
        print("URI: " + exchange.getRequestURI().toString() );
        switch( exchange.getRequestMethod() ) {
            case "GET":
                handleGet(exchange);
                break;
            case "PUT":
                //use 100 Continue response then wait for stream I guess
                handlePut(exchange);
                // use 201 Created
                break;
            case "DELETE":
                handleDel(exchange);
                break;
            case "POST":
                //logging in
                handlePost(exchange);
                break;
            default:
                String body = htmlBuilder("Not Implemented", "HTTP Error 501: Not Implemented");
                sendResponse(exchange, body.getBytes(), 501);
                break;
        }
    }

    /**
     * This method handles the get request for from the client to the server
     * @param exchange
     * @return
     * @throws IOException
     */
    private void handleGet( HttpExchange exchange) throws IOException {
        switch ( exchange.getRequestURI().toString().replace("%20", " ") ){
            case "/~fileStructure":
                //user is requesting file structure
                print("Sending file structure");
                sendResponse(exchange, FU.getFileStructure(serverHome).getBytes(), 200);
                break;
            case "/~dirStructure":
                //user wants directories only
                print("Sending directories structure");
                sendResponse(exchange, FU.getDirStructure(serverHome).getBytes(), 200);
                break;
            case "/~fullStructure":
                //user wants to see everything
                print("Sending full structure");
                sendResponse(exchange, FU.getFullStructure(serverHome).getBytes(), 200);
            case "~testConnection":
                sendResponse(exchange, "1".getBytes(), 200);
                break;
            default:
                if( SPM.checkPasscode( exchange ) ) {
                    print("Verified");
                    String directory = getReqDir(exchange).replace("%20", " ");
                    File requestedFile = new File( directory );
                    print("Exists / Readable: " + requestedFile.exists() + " / " + requestedFile.canRead() );
                    if ( requestedFile.exists() && requestedFile.canRead() ) {
                        //going ahead...
                        sendResponse(exchange,
                                FU.readFromFile( directory ),
                                200);
                    } else {
                        //404 not found

                        sendResponse(exchange,
                                htmlBuilder("File Not Found", "HTTP Error 404: File Not Found").getBytes(),
                                404);
                    }
                } else {
                    print("Incorrect Access Code");
                    sendResponse( exchange, htmlBuilder("Unauthorized", "Http Error 401 Unauthorized").getBytes(), 401);
                }
                break;
        }
    }

    /**
     * This method is called when a resource is to be deleted from the server
     * @param exchange
     * @return
     * @throws IOException
     */
    private void handleDel(HttpExchange exchange) throws IOException{
        if( SPM.checkPasscode(exchange ) ) {
            File toDel = new File( getReqDir(exchange).replace("%20", " ") );
            if( toDel.delete() ) {
                //deleted file
                sendResponse(exchange,
                        htmlBuilder("200 OK", "The File has been Deleted From the Server").getBytes(),
                        200);

            } else {
                if( toDel.exists() ) {
                    //failed to delete file
                    sendResponse(exchange,
                            htmlBuilder("500 Internal Server Error", "The Resource that has been selected failed to be deleted").getBytes(),
                            500 );
                } else {
                    //file could not be found
                    sendResponse(exchange,
                            htmlBuilder("404 Resource Not Found", "The Resource that was requested to be deleted could not be found by the server").getBytes(),
                            404);
                }
            }
        } else {
            print("Incorrect Access code");
            sendResponse( exchange, htmlBuilder("Unauthorized", "Http Error 401 Unauthorized").getBytes(), 401);
        }
    }

    /**
     * This method is to print out the headers of the request
     * @param e
     */
    public void printHeaders(HttpExchange e) {
        print("Headers:");
        Headers headers = e.getRequestHeaders();
        Object[] head = headers.values().toArray();
        for( Object i : head ){
            print(i.toString());
        }
    }

    /**
     * This method is to handle the file upload requests
     * @param exchange
     * @throws IOException
     */
    private void handlePut(HttpExchange exchange)  throws IOException{
        if( SPM.checkPasscode( exchange ) ) {

            String saveTo = getReqDir(exchange).replace("%20", " ");
            printHeaders(exchange);

            print( "" + exchange.getRequestHeaders().get("content-length") ); // form of [number]
            print("" + exchange.getRequestBody().toString() );

            FU.saveToFile( new BufferedInputStream( exchange.getRequestBody() ), saveTo );

            print("Finished Request / Responding 200 OK");

            sendResponse(exchange, htmlBuilder("200 OK", "Upload OK").getBytes(), 200);
        } else {
            print("Incorrect Access Code");
            sendResponse( exchange, htmlBuilder("Unauthorized", "Http Error 401 Unauthorized").getBytes(), 401);
        }
        print("Finished");
    }

    /**
     * This method is to handle the post requests
     * these requests are for logging in and obtaining an access key
     * @param exchange
     * @throws IOException
     */
    public void handlePost(HttpExchange exchange) throws IOException{
        //one of the few things you can do without authorisation
        String body;
        int code;

        switch( checkLogin(exchange) ) {
            case 0:
                //no match
                body = "Username and Password Combination do not match";
                code = 406; // 406 - not acceptable
                break;
            case 1:
                //username match
                body = "Password Incorrect";
                code = 406; // 406 - not acceptable
                break;
            case 2 :
                //match
                body = SPM.generate();
                code = 200; // 200 OK
                break;
            default:
                //internal error
                body = "500 Server Encountered An Error";
                code = 400;
                break;
        }

        sendResponse(exchange, body.getBytes(), code);
    }

    /**
     * This method gets the body as a string to make it easier to process
     * @param exchange
     * @return
     * @throws IOException
     */
    public String getStringFromBody(HttpExchange exchange) throws IOException {
        InputStream in = new BufferedInputStream( exchange.getRequestBody() );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[1024];
        int count = 0;

        while( (count = in.read(buffer) ) != -1) {
            sb.append( new String(buffer, 0, count));
        }
        in.close();
        print( "Body: " + sb.toString() );
        return sb.toString();
    }

    /**
     * This method checks if the users login is in the database
     * @param exchange
     * @return
     * @throws IOException
     */
    public int checkLogin(HttpExchange exchange) throws IOException {
        String body = getStringFromBody(exchange).replace("+", " ");
        String[] bodySplit = body.split("&");
        String username = "";
        String password = "";
        String[] itemSplit;
        for( String item : bodySplit ) {
            itemSplit = item.split("=");
            if( itemSplit[0].equals("username") ) {
                username = itemSplit[1];
            } else if( itemSplit[0].equals("password") ) {
                password = itemSplit[1];
            }
            if( !username.equals("") && !password.equals("") ) {
                break;
            }
        }

        return DB.match(username, password);

    }

    /**
     * This method is to get the directory that was requested as a string
     * @param exchange
     * @return
     */
    public String getReqDir(HttpExchange exchange) {
        //need to improve for efficiency
        String requestDir = exchange.getRequestURI().toString();
        if ( requestDir == "/" ) {
            requestDir += "index.html";
        }
        print("Dir: " + requestDir);
        requestDir = serverHome + requestDir;
        return requestDir;
    }

    /**
     * This method is to send the response to the client
     * @param exchange
     * @param file
     * @param code
     * @throws IOException
     */
    private void sendResponse( HttpExchange exchange, byte[] file, int code ) throws IOException {
        if(file == null) {
            exchange.sendResponseHeaders(code, -1);
        } else {
            OutputStream out = exchange.getResponseBody();
            exchange.sendResponseHeaders(code, file.length);
            out.write( file );
            out.flush();
            out.close();
        }

        print("Response-Code: " + code);
    }

    /**
     * This method builds html code to be used with the responses of error messages and such
     * @param title
     * @param header
     * @return
     */
    public String htmlBuilder(String title, String header) {
        StringBuilder body = new StringBuilder("<HTML>\r\n");
        body.append("<HEAD><TITLE>" + title + "</TITLE>\r\n");
        body.append("</HEAD>\r\n");
        body.append("<BODY>");
        body.append("<H1>" + header + "</H1>\r\n");
        body.append("</BODY></HTML>\r\n");
        return body.toString();
    }

}
