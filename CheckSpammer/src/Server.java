import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {

    private static final String BLACKHOLE = "zen.spamhaus.org";

    public static void main(String[] args) throws IOException {
        check();
    }

    private static void check() throws IOException {
        ServerSocket server = new ServerSocket();
        SocketAddress endpoint = new InetSocketAddress("0.0.0.0", 8088);
        server.bind(endpoint);
        System.out.println("server started on port 8088");

        while (true) {
            Socket socket = server.accept();

            BufferedReader input = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // BlackList Verification
            while(true){
                String address = input.readLine();
                if(address == "Q"){
                    break;
                }
                if(isSpammer(address)){
                    out.println(address + " is a known spammer");
                }
                else{
                    out.println(address + " not spammer");
                }
            }
            System.out.println("Connection with" + socket.getInetAddress() + "closed");
            socket.close();
        }
    }

    private static boolean isSpammer(String arg) {
        try{
            InetAddress address = InetAddress.getByName(arg);
            byte[] quad = address.getAddress();
            String query = BLACKHOLE;

            for(byte octet : quad){
                int unsignedByte = octet < 0 ? octet + 256 : octet;
                query = unsignedByte + "." + query;
            }
            InetAddress.getByName(query);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
