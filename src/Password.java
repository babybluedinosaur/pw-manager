import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Password {
    private JPanel rightSide;

    public Password() {
        JLabel pName = new JLabel("Name");
        JLabel pEmail = new JLabel("E-Mail or Username");
        JLabel pPassword = new JLabel("Password");
        JLabel pExtra = new JLabel("Extra");
        JButton savePassword = new JButton("Save");
        JTextField tfName = new JTextField(50);
        JTextField tfEmail = new JTextField(100);
        JPasswordField tfPassword = new JPasswordField(250);
        JTextField tfExtra = new JTextField(250);

        rightSide.setBackground(new Color(41, 41, 41));
        pName.setBackground(new Color(160, 161, 165));
        pName.setForeground(new Color(160, 161, 165));
        pEmail.setBackground(new Color(160, 161, 165));
        pEmail.setForeground(new Color(160, 161, 165));
        pPassword.setBackground(new Color(160, 161, 165));
        pPassword.setForeground(new Color(160, 161, 165));
        pExtra.setBackground(new Color(160, 161, 165));
        pExtra.setForeground(new Color(160, 161, 165));
        tfName.setBackground(new Color(160, 161, 165));
        tfName.setForeground(new Color(41, 41, 41));
        tfEmail.setBackground(new Color(160, 161, 165));
        tfEmail.setForeground(new Color(41, 41, 41));
        tfPassword.setBackground(new Color(160, 161, 165));
        tfPassword.setForeground(new Color(41, 41, 41));
        tfExtra.setBackground(new Color(160, 161, 165));
        tfExtra.setForeground(new Color(41, 41, 41));

        rightSide.setLayout(new GridLayout(9, 1));
        rightSide.add(pName);
        rightSide.add(tfName);
        rightSide.add(pEmail);
        rightSide.add(tfEmail);
        rightSide.add(pPassword);
        rightSide.add(tfPassword);
        rightSide.add(pExtra);
        rightSide.add(tfExtra);
        rightSide.add(savePassword);
    }
}
