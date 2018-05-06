package Main_Spiel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InputField implements ActionListener {

    public int WIDTH = 300, HEIGHT = 110;
    public String SValue;

    public JButton acceptbutton;
    public JTextField T_field;
    public JLabel T_label;
    public String Title = "";

    public InputField(String Title) {
        this.Title = Title;
        final JFrame jframe = new JFrame();

        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);
        jframe.setFocusTraversalKeysEnabled(false);
        acceptbutton = new JButton("set");
        acceptbutton.setBounds(30, 30, 80, 30);
        T_field = new JTextField("");
        T_field.setBounds(130, 30, 160, 30);
        T_label = new JLabel(Title);
        T_label.setLocation(10, 10);
        T_label.setSize(160, 20);
        jframe.add(acceptbutton);
        jframe.add(T_field);
        jframe.add(T_label);
        jframe.setLayout(null);

        acceptbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == acceptbutton) {

                    SValue = T_field.getText();
                    jframe.setVisible(false); //you can't see me!
                    jframe.dispose(); //Destroy the JFrame object
                }
            }
        });

        jframe.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

    }

}
