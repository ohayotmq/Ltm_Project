package Server.Utilities;

import Server.Users.User;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is to handle all of the file handling for the server
  * the saveFile method modified from (java, GlassEditor.com, Sheikh and Joshi, 2020)
   * the recurse method modified from (Java et al., 2020)
    * see Report References for links
 * @author James Martland 24233781
 */
public class FileUtils implements DBFiles, ServerFiles {

    /**
     * This method is to save a file from an output stream
     * @param in
     * @param path
     * @return true - if method completes
     * @throws IOException
     */
    public boolean saveFile(InputStream in, String path) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int count = 0;

        while( (count = in.read(buffer) ) != -1 ) {
            out.write(buffer, 0, count);
        }

        out.close();
        in.close();
        FileOutputStream fos = new FileOutputStream(path);
        fos.write( out.toByteArray() );
        fos.close();

        return true;
    }

    /**
     * This is the method that invokes the recursive method to get the file structure
     * @param rootDir
     * @return
     */
    public String getFileStructure(String rootDir) {

        ArrayList<String> files = recurse( "", rootDir, new ArrayList<String>() );

        Iterator<String> i = files.iterator();
        while( i.hasNext() ) {
            System.out.println( i.next() ) ;
        }

        return files.toString();
    }

    /**
     * This is the recursive methood that will get the file structure of the server files
     * @param newRoot
     * @param fileHome
     * @param list
     * @return
     */
    public ArrayList<String> recurse(String newRoot, String fileHome, ArrayList<String> list) {
        //new folder
        File root = new File(fileHome + newRoot);
        //list all folders
        String[] dirs = root.list( new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for( String item : dirs ) {
            list.addAll(recurse(newRoot + "/" + item, fileHome, new ArrayList<String>() ) );
        }

        // list all files
        dirs = root.list( new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        for( String item : dirs ) {
            list.add(newRoot + "/" + item);
        }

        return list;
    }

    public String getDirStructure(String rootDir) {

        ArrayList<String> files = recurseDir("", rootDir, new ArrayList<String>() );
        files.set(0, "/");
        Iterator<String> i = files.iterator();
        while( i.hasNext() ) {
            System.out.println( i.next() ) ;
        }

        return files.toString();
    }

    public ArrayList<String> recurseDir(String newRoot, String fileHome, ArrayList<String> list) {
        File root = new File(fileHome + newRoot);
        list.add(newRoot);
        String[] dirs = root.list( new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for( String item : dirs ) {
            list.addAll( recurseDir( newRoot + "/" + item, fileHome, new ArrayList<String>() ) );
        }
        return list;
    }

    /**
     * This is the method that invokes the recursive method to get the file structure
     * @param rootDir
     * @return
     */
    public String getFullStructure(String rootDir) {

        ArrayList<String> files = recurseFull( "", rootDir, new ArrayList<String>() );

        Iterator<String> i = files.iterator();
        while( i.hasNext() ) {
            System.out.println( i.next() ) ;
        }

        return files.toString();
    }

    /**
     * This is the recursive methood that will get the file structure of the server files
     * @param newRoot
     * @param fileHome
     * @param list
     * @return
     */
    public ArrayList<String> recurseFull(String newRoot, String fileHome, ArrayList<String> list) {
        //new folder
        boolean empty = false;
        File root = new File(fileHome + newRoot);
        //list all folders
        String[] dirs = root.list( new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for( String item : dirs ) {
            list.addAll(recurseFull(newRoot + "/" + item, fileHome, new ArrayList<String>() ) );
        }
        if( dirs.length == 0 ) {
            // no directories in this
            empty = true;
        }
        // list all files
        dirs = root.list( new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        for( String item : dirs ) {
            list.add(newRoot + "/" + item);
        }
        if( dirs.length == 0 && empty) {
            //if it is an empty folder
            list.add(newRoot + "/");
        }
        return list;
    }

    /**
     * This method takes an array list and saves it to a file at the designated filepath
     * @param list
     * @param filepath
     */
    public void save(ArrayList<User> list, String filepath) {
        try {
            FileWriter writer = new FileWriter( filepath );
            Iterator i = list.iterator();

            while(i.hasNext()) {
                writer.write( ((User) i.next()).makeString() + "\n");
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("An Error Occurred When Attempting User Write: " + e);
        }

    }

    /**
     * This method takes a filepath and reads it to an arraylist of type user
     * @param filepath
     * @return
     */
    @Override
    public ArrayList<User> read(String filepath) {
        ArrayList<User> list = new ArrayList<User>();
        try {
            BufferedReader br = new BufferedReader( new FileReader( filepath ) );
            String currentL = "";

            while( (currentL = br.readLine() ) != null ) {
                list.add( new User( currentL.split(",") ) );
                System.out.println("[FileUtils] Added To List");
            }

        } catch (FileNotFoundException e) {
            System.out.println("[FileUtils] File Was Not Found");
        } catch (IOException e) {
            System.out.println("[FileUtils] An error has occurred during read: " + e);
        }

        return list;
    }

    /**
     * This method saves files from an input stream
     * @param in
     * @param filePath
     * @throws IOException
     */
    public void saveToFile(InputStream in, String filePath) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int count;

        while( (count = in.read(buffer) ) != -1){
            out.write( buffer, 0, count );
        }

        out.close();
        in.close();
        System.out.println("[FU] Finished Read... ");

        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write( out.toByteArray() );
        fos.close();

    }

    public byte[] readFromFile( String directory ) throws IOException {
        return Files.readAllBytes( (new File(directory)).toPath() );
    }

}
