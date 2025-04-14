import javax.swing.*;
import java.awt.event.*;
public class DictionaryGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("My Dictionary");

        JButton button = new JButton("Search");
        button.setBounds(130, 100, 100, 40);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Button clicked!");
            }
        });

        frame.add(button);
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

