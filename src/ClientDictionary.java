
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import com.google.gson.Gson;
public class ClientDictionary {

    // IP and port
    private static String ip = "localhost";
    private static int port = 3005;
    private Gson gson = new Gson();

    public static void main(String[] args)
    {
        // read message from the server
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        //to improve the efficiency of reading (inputStreadReader wrapped by bufferedReader)
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try(Socket socket = new Socket(ip, port);)
        {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);
            // Output and Input Stream
            DataInputStream input = new DataInputStream(socket.getInputStream());

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String sendData ="I want to connect";

            output.writeUTF(sendData);
            System.out.println("Data sent to Server--> " + sendData);
            output.flush();

            boolean flag=true;
            while(flag)
            {
                if(input.available()>0) {
                    String message = input.readUTF();
                    System.out.println(message);
                    flag= false;;
                }
            }

        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
