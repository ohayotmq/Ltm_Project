package Server;

import com.sun.net.httpserver.HttpExchange;

import java.util.Random;

/**
 * This class is to generate an access code for the server
 * making it easier to see if the user is authorised without searching a database every time
 * @author James Martland 24233781
 */
public class ServerPasscodeManager {
    private String passcode;

    private Random r = new Random();

    /**
     * This method checks the inputted passcode against the passcode that has been generated
     * @param passcode
     * @return
     */
    public boolean checkPasscode(String passcode){
        if(this.passcode.equals(passcode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is to check whether passcode sent by the client matches the one that was given out
     * @param exchange
     * @return
     */
    public boolean checkPasscode(HttpExchange exchange) {
        System.out.println("checking");
        System.out.println( exchange.getRequestHeaders().getFirst("key") );
        System.out.println(this.passcode);
        return checkPasscode(exchange.getRequestHeaders().getFirst("key") );
    }

    /**
     * This method generates a passcode that is 8 characters long
     * @return
     */
    public String generate() {
        // 8 long
        StringBuilder sb = new StringBuilder();
        for( int i=0; i<8; i++){
            sb.append(genChar());
        }
        this.passcode = sb.toString();
        return sb.toString();
    }

    /**
     * This method generates a character for use in the generate method
     * 30% chance of number 0-9
     * 35% chance of an upper case letter
     * 35% chance of a lower case letter
     * @return
     */
    public String genChar() {
        if( r.nextInt(100) > 30 ){
            //character generation
            int character = r.nextInt(26) + 65;//65 is the ASCII code for capital A
            if(r.nextInt(100) > 50) {
                return "" + (char) character; // upper case - letter is already uppercase
            } else {
                return ("" + (char) character).toLowerCase();
            }
        } else {
            return r.nextInt(10)+"";
        }
    }
}
