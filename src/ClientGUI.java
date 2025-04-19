
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class ClientGUI {
    private ClientDictionary client;

    private JFrame frame;
    private JComboBox<String> select;
    private JTextField word;
    private JTextField meaning;
    private JTextField oldmeaning;
    private JTextField newmeaning;
    private JButton submit;
    private JTextArea responseArea;

    public ClientGUI(String ip, int port) {
        try {
            client = new ClientDictionary(ip, port);
        } catch (UnknownHostException e){
            JOptionPane.showMessageDialog(frame, "Error: unknown host. Double check the host ip and port.");
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "I/O Error: client unable to communicate with server.");
            return;
        }
        initializeFrame();
    }
    // initialize the window (frame)
    private void initializeFrame() {
        frame = new JFrame("Dictionary Client");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.clientCloseConnection();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setSize(550, 350);
        frame.setLayout(new BorderLayout());

        prepareComponents();
        frame.setVisible(true);
    }

    //initialize components within the frame
    private void prepareComponents(){
        JPanel p = new JPanel(new GridLayout(6, 2));

        select = new JComboBox<>(new String[]{
                "querymeanings", "addword", "removeword", "addmeaning", "updatemeaning"
        });
        word = new JTextField();
        meaning = new JTextField();
        oldmeaning = new JTextField();
        newmeaning = new JTextField();

        p.add(new JLabel("Select Request:"));
        p.add(select);
        p.add(new JLabel("Word:"));
        p.add(word);
        p.add(new JLabel("Meaning:"));
        p.add(meaning);
        p.add(new JLabel("Old Meaning (for update):"));
        p.add(oldmeaning);
        p.add(new JLabel("New Meaning:"));
        p.add(newmeaning);

        submit = new JButton("Submit");
        JPanel sp = new JPanel(new BorderLayout());
        sp.add(submit, BorderLayout.CENTER);
        p.add(submit, BorderLayout.CENTER);
        frame.add(sp, BorderLayout.SOUTH);
        frame.add(p, BorderLayout.NORTH);

        responseArea = new JTextArea();
        responseArea.setEditable(false);
        frame.add(new JScrollPane(responseArea), BorderLayout.CENTER);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRequest();
            }
        });
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHideInput();
            }
        });
// show/hide fields based on the request selected
        showHideInput();
    }

    private void showHideInput() {
        String request;
        request = (String) select.getSelectedItem();
        meaning.setEnabled(false);
        oldmeaning.setEnabled(false);
        newmeaning.setEnabled(false);

        if (request.equals("addword")) {
            meaning.setEnabled(true);
        }if(request.equals("addmeaning")){
            meaning.setEnabled(true);
        }else if (request.equals("updatemeaning")) {
            oldmeaning.setEnabled(true);
            newmeaning.setEnabled(true);
        }
    }

    private void handleRequest() {
        String action = (String) select.getSelectedItem();
        String word1 = word.getText().trim().toLowerCase();
        String meaning1 = meaning.getText().trim().toLowerCase();
        String oldMeaning1 = oldmeaning.getText().trim().toLowerCase();
        String newMeaning1 = newmeaning.getText().trim().toLowerCase();
        try {
            RequestMessage request;

            switch (action) {
                case "addword":
                case "addmeaning":
                    request = new RequestMessage(action, word1, meaning1);
                    break;
                case "updatemeaning":
                    request = new RequestMessage(action, word1, oldMeaning1, newMeaning1);
                    break;
                case "querymeanings":
                case "removeword":
                default:
                    request = new RequestMessage(action, word1);
                    break;
            }

            ResponseMessage response = client.sendToReceive(request);
            displayServerResponse(response);
        } catch (ConnectException e) {
            responseArea.setText("Error: unable to connect to server.");
        } catch (SocketException e) {
            responseArea.setText("Error: client lost connection during communication with server.");
        } catch (IOException e) {
            responseArea.setText("Error: client unable to communicate with server: " + e.getMessage());
        }
    }

    private void displayServerResponse(ResponseMessage response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Status").append(": ").append(response.getStatus()).append("\n");

        if (response.getStatus().equalsIgnoreCase("Success")) {
            List<String> meanings = response.getMeanings();
            if (meanings != null && !(meanings.isEmpty())) {
                sb.append("Meanings:\n");
                for (String m : meanings) {
                    sb.append(" -> ").append(m).append("\n");
                }
            } else {
                sb.append("This request has been completed successfully!\n");
            }
        } else {
            sb.append(response.getStatus()).append(": ").append(response.getErrorMessage()).append("\n");
        }

        responseArea.setText(sb.toString());
    }

}

