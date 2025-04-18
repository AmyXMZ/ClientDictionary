
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
public class ClientDictionary {

    // IP and port
    private static String ip = "localhost";
    private static int port = 3005;
    private ClientGUI gui;
    private Socket socket;
    private DataInputStream input;

    private DataOutputStream output;

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
    public void displayServerResponse(ResponseMessage r) throws IOException {
        /// change!!!
        String status = r.getStatus();
        System.out.println("\n[Server Response]");
        System.out.println("Status: " + status);
        if (status.equalsIgnoreCase("Success")) {
            List<String> meanings = r.getMeanings();
            if (meanings != null && !meanings.isEmpty()) {
                System.out.println("Meanings:");
                for (String meaning : meanings) {
                    System.out.println("- " + meaning);
                }
            } else {
                System.out.println("Operation completed successfully.");
            }
        } else if ("Error".equalsIgnoreCase(status)) {
            String errorMessage = r.getErrorMessage();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                System.out.println("Error: " + errorMessage);
            } else {
                System.out.println("An unknown error occurred.");
            }
        } else {
            System.out.println("Unexpected status from server.");
        }
    }

    public static void main(String[] args)
    {

        try(Scanner scanner = new Scanner(System.in);)
        {
            ClientDictionary clientInstance = new ClientDictionary("localhost", 3005);
            RequestMessage requestMessage = null;
            boolean flag = true;
            while(flag){
                System.out.print("\nSelect a request (addword, removeword, querymeanings, addmeaning, updatemeaning, quit): ");
                String action = scanner.nextLine().trim().toLowerCase();
                switch(action){
                    case "addword":
                        System.out.print("Enter word to add: ");
                        String word_to_add = scanner.nextLine().trim().toLowerCase();
                        System.out.println("Enter the meaning for the word: ");
                        String meaning = scanner.nextLine().trim().toLowerCase();
                        requestMessage = new RequestMessage(action, word_to_add, meaning);
                        break;
                    case "removeword":
                        System.out.print("Enter word to remove: ");
                        String word_to_remove = scanner.nextLine().trim().toLowerCase();
                        requestMessage = new RequestMessage(action, word_to_remove);
                        break;
                    case "querymeanings":
                        System.out.print("Enter word to search: ");
                        String word_query_meanings = scanner.nextLine().trim().toLowerCase();
                        requestMessage = new RequestMessage(action, word_query_meanings);
                        break;
                    case "addmeaning":
                        System.out.print("Enter the existing word: ");
                        String existing_word = scanner.nextLine().trim().toLowerCase();
                        System.out.println("Enter the additional meaning to add: ");
                        String additional_meaning = scanner.nextLine().trim().toLowerCase();
                        requestMessage = new RequestMessage(action, existing_word, additional_meaning);
                        break;
                    case "updatemeaning":
                        System.out.print("Enter the word: ");
                        String word_meaning_update = scanner.nextLine().trim().toLowerCase();
                        System.out.println("Enter the existing meaning: ");
                        String old_meaning = scanner.nextLine().trim().toLowerCase();
                        System.out.println("Enter the new meaning: ");
                        String new_meaning = scanner.nextLine().trim().toLowerCase();
                        requestMessage = new RequestMessage(action, word_meaning_update, old_meaning, new_meaning);
                        break;
                    case "quit":
                        flag = false;
                        continue;
                    default:
                        System.out.println("Invalid request. Please refer to the given action and try again.");
                        continue;
                }
                if (requestMessage != null) {//send the json to server
                    ResponseMessage response = clientInstance.sendToReceive(requestMessage);
                    clientInstance.displayServerResponse(response);
                }



            }
//            RequestMessage request = new RequestMessage("querymeanings", "cc");
//            String jsonRequest = new Gson().toJson(request);
//            output.writeUTF(jsonRequest);
//            //display server response
//            String jsonResponse = input.readUTF();
//            ResponseMessage response = new Gson().fromJson(jsonResponse, ResponseMessage.class);
//
//            System.out.println("\n[Server Response]");
//            System.out.println("Status: " + response.getStatus() + response.getErrorMessage());


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
