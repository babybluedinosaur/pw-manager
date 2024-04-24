import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Password extends JDialog{
    public JPanel pwPanel; //window with password
    private JLabel pName;
    private JLabel pEmail;
    private JLabel pPassword;
    private JLabel pExtra;
    private JButton savePassword;
    private JTextField tfName;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JTextField tfExtra;
    
    private int id;
    private Connection connection;

    private PreparedStatement stmt = null;

    private ResultSet result = null;



    //read password from db
    public Password(String name, String email, String password, String extra, int id, Connection connection) {
        pName = new JLabel("Name");
        pEmail = new JLabel("E-Mail or Username");
        pPassword = new JLabel("Password");
        pExtra = new JLabel("Extra");
        savePassword = new JButton("Save");
        tfName = new JTextField(50);
        tfEmail = new JTextField(100);
        tfPassword = new JPasswordField(250);
        tfExtra = new JTextField(250);
        this.id = id;
        this.connection = connection;
        if (name != null) tfName.setText(name);
        if (email != null) tfEmail.setText(email);
        if (password != null) tfPassword.setText(password);
        if (extra != null) tfExtra.setText(extra);
        setUI();
    }

    public void setUI() {
        pwPanel = new JPanel();
        pwPanel.setBackground(new Color(41, 41, 41));
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
        savePassword.setBackground(new Color(41, 41, 41));
        savePassword.setForeground(new Color(160, 161, 165));
        savePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                if (id > 0) { //update
                    //first update
                    //then clear local passwords and load them again from db
                } else { //create
                    //save new password (push to db and save it locally in datastructure)
                    String query = "INSERT INTO user_data (name, email, password, extra) VALUES (?,?,?,?)";
                    try {
                        stmt = connection.prepareStatement(query);
                        stmt.setString(1, tfName.getText());
                        stmt.setString(2, tfEmail.getText());
                        stmt.setString(3, tfPassword.getText());
                        stmt.setString(4, tfExtra.getText());

                        result = stmt.executeQuery();
                        if (!result.next()) {
                            System.out.println("bruh");
                        } else {
                            System.out.println("success!");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } finally {

                        if (result != null) {
                            try {

                                result.close();

                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                        if (stmt != null) {
                            try {

                                stmt.close();

                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            }
        });

        pwPanel.setLayout(new GridLayout(9, 1));
        pwPanel.add(pName);
        pwPanel.add(tfName);
        pwPanel.add(pEmail);
        pwPanel.add(tfEmail);
        pwPanel.add(pPassword);
        pwPanel.add(tfPassword);
        pwPanel.add(pExtra);
        pwPanel.add(tfExtra);
        pwPanel.add(savePassword);
    }
}
