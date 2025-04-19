
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;

import javax.swing.*;

public class ClientDictionary {

    // IP and port
    private static String ip = "localhost";
    private static int port = 3005;
    private Socket socket;
    private DataInputStream input;

    private DataOutputStream output;
    private JFrame frame;

    //constructor
    public ClientDictionary(String ip, int port) throws IOException {
        //initializes socket and I/O streams
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        // Read initial connection messages
        System.out.println(input.readUTF());
        System.out.println(input.readUTF());
    }
    //send the json message to the server and return the java object (converted) from server
    public ResponseMessage sendToReceive(RequestMessage r) throws IOException {
        String json = new Gson().toJson(r);
        output.writeUTF(json);
        String jsonResponse = input.readUTF();
        return new Gson().fromJson(jsonResponse, ResponseMessage.class);
    }
    //display server's response
//    public void displayServerResponse(ResponseMessage r) throws IOException {
//        // display the response from server to client
//        String status = r.getStatus();
//        System.out.println("\n[Server Response]");
//        System.out.println("Status: " + status);
//        if (status.equalsIgnoreCase("Success")) {
//            List<String> meanings = r.getMeanings();
//            if (meanings != null && !(meanings.isEmpty())) {
//                System.out.println("Meanings: ");
//                for (String m : meanings) {
//                    System.out.println("-> " + m);
//                }
//            } else {
//                System.out.println("Success: operation completed!");
//            }
//        } else if (status.equalsIgnoreCase("Error")) {
//            String errorMessage = r.getErrorMessage();
//            System.out.println("Error: " + errorMessage);
//        }
//    }

    //client close connection (upon entering "quit" for console only)
    public void clientCloseConnection(){
//        RequestMessage quit = new RequestMessage("quit");
//        String jsonRequest = new Gson().toJson(quit);
//        try {
//            output.writeUTF(jsonRequest);
//        } catch (IOException e) {
//            System.out.println("I/O error: cannot send quit request to the server.");
//        }

        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null){
                socket.close();
            }
            //System.out.println("Client socket closed successfully.");
        } catch (IOException e) {
            //System.out.println("I/O Error: failed to close client socket.");
            JOptionPane.showMessageDialog(frame, "I/O Error: failed to close client socket.");
        }

    }
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI(ip, port);
            }
        });
//        try(Scanner scanner = new Scanner(System.in);)
//        {
//            ClientDictionary clientInstance = new ClientDictionary(ip, port);
//            RequestMessage requestMessage = null;
//            boolean flag = true;
//            while(flag){
//                System.out.print("\nSelect a request (addword, removeword, querymeanings, addmeaning, updatemeaning, quit): ");
//                String action = scanner.nextLine().trim().toLowerCase();
//                switch(action){
//                    case "addword":
//                        System.out.print("Enter word to add: ");
//                        String word_to_add = scanner.nextLine().trim().toLowerCase();
//                        System.out.println("Enter the meaning for the word: ");
//                        String meaning = scanner.nextLine().trim().toLowerCase();
//                        requestMessage = new RequestMessage(action, word_to_add, meaning);
//                        break;
//                    case "removeword":
//                        System.out.print("Enter word to remove: ");
//                        String word_to_remove = scanner.nextLine().trim().toLowerCase();
//                        requestMessage = new RequestMessage(action, word_to_remove);
//                        break;
//                    case "querymeanings":
//                        System.out.print("Enter word to search: ");
//                        String word_query_meanings = scanner.nextLine().trim().toLowerCase();
//                        requestMessage = new RequestMessage(action, word_query_meanings);
//                        break;
//                    case "addmeaning":
//                        System.out.print("Enter the existing word: ");
//                        String existing_word = scanner.nextLine().trim().toLowerCase();
//                        System.out.println("Enter the additional meaning to add: ");
//                        String additional_meaning = scanner.nextLine().trim().toLowerCase();
//                        requestMessage = new RequestMessage(action, existing_word, additional_meaning);
//                        break;
//                    case "updatemeaning":
//                        System.out.print("Enter the word: ");
//                        String word_meaning_update = scanner.nextLine().trim().toLowerCase();
//                        System.out.println("Enter the existing meaning: ");
//                        String old_meaning = scanner.nextLine().trim().toLowerCase();
//                        System.out.println("Enter the new meaning: ");
//                        String new_meaning = scanner.nextLine().trim().toLowerCase();
//                        requestMessage = new RequestMessage(action, word_meaning_update, old_meaning, new_meaning);
//                        break;
//                    case "quit":
//                        clientInstance.clientCloseConnection(requestMessage);
//                        flag = false;
//                        continue;
//                    default:
//                        System.out.println("Invalid request. Please refer to the given action and try again.");
//                        continue;
//                }
//                if (requestMessage != null) {//send the json to server
//                    ResponseMessage response = clientInstance.sendToReceive(requestMessage);
//                    clientInstance.displayServerResponse(response);
//                }
//            }
//
//        }
//        catch (UnknownHostException e)
//        {
//            System.out.println("Error: unknown host. Double check the host ip and port.");
//        } catch (ConnectException e) {
//            System.out.println("Error: unable to connect to server.");
//        } catch (SocketException e) {
//            System.out.println("Error: client lost connection during communication with server.");
//        } catch (IOException e)
//        {
//            System.out.println("I/O Error: client unable to communicate with server.");
//        }
    }

}
