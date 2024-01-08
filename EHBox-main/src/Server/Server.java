package Server;

import Server.Users.UserDB;
import Server.Utilities.FileUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class is the top of the hierarchical structure - this enables all of its sub classes to access its methods and attributes
 * within this class (A Simple HTTP Server in Java - DZone Java, 2020) was used to create startServer ( see Report references )
 * @author James Martland 24233781
 */
public class Server {

    protected static String serverHome = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/ServerHome";
    protected static UserDB DB = new UserDB();
    protected static FileUtils FU = new FileUtils();
    private static String hostname = "localhost";
    private static int port = 8080;
    private static ServerSocket serverSocket;

    /**
     * This method starts the server
     * @param threads number of threads allowed to handle clients ( how many clients simultaneously )
     */
    public void startServer(int threads) {
        print(serverHome);
        print(System.getProperty("user.dir").replaceAll("\\\\", "/")); // /httpserver
        try {
            serverSocket = new ServerSocket(port);
            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);

            print("Server has been started on port " + getPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ServerHandler(clientSocket, serverHome + "/files"));
            }

        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * This method stops the server
     */
    public void stopServer() {
        try {
            serverSocket.close();
            print("[Server] Stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is a getter to retrieve the port the server is on
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * This method is a getter to retrieve the hostname of the server
     * @return hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * This method is a setter to set the value of hostname
     * @param hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * This method is a setter to set the value of port
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * this method is used to print statements to command line
     * @param text
     */
    public static void print(String text) {
        System.out.println(text);
    }
}
