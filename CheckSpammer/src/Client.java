import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static Scanner in;
    private static Socket mySock;
    private static SocketAddress endpoint;
    static InetAddress ipAddress = null;

    public static void main(String[] args) throws IOException {
        System.out.println("INPUT IP Address:");
        in = new Scanner(System.in);

        String userInput = in.next();
        do{
            try{
                ipAddress = InetAddress.getByName(userInput);
                break;
            }catch(UnknownHostException e){
                System.out.println("Enter valid host name ot IP Address");
                userInput = in.next();
            }
        }while(true);

        try{
            mySock = new Socket();
            endpoint = new InetSocketAddress(ipAddress, 8088);
            mySock.connect(endpoint);
        }catch(ConnectException e){
            e.printStackTrace();
        }catch(SocketException se){
            se.printStackTrace();
        }

        System.out.println("Connected!");

        PrintWriter out = new PrintWriter(mySock.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String address = reader.readLine();
        while(address != "Q"){
            out.println(address);
            String response = input.readLine();
            System.out.println(response);
            address = reader.readLine();
        }
        out.println(address);
        mySock.close();

    }
}
