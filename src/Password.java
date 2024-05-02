import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Password extends JDialog{
    public JPanel pwPanel; //window with password
    private JLabel pName;
    private JLabel pEmail;
    private JLabel pPassword;
    private JLabel pExtra;
    private JButton savePassword;

    private JButton deletePassword;
    private JTextField tfName;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JTextField tfExtra;
    
    private int pw_id;

    private int user_id;
    private Connection connection;

    private PreparedStatement stmt = null;

    private ResultSet result = null;

    private DefaultListModel<String> listModel;

    private HashMap<String, Password> passwords;

    String currentName;


    //read password from db
    public Password(String name, String email, String password, String extra, int pw_id, int user_id,
                    Connection connection, DefaultListModel<String> listModel, HashMap<String, Password> passwords) {
        pName = new JLabel("Name");
        pEmail = new JLabel("E-Mail or Username");
        pPassword = new JLabel("Password");
        pExtra = new JLabel("Extra");
        savePassword = new JButton("Save");
        deletePassword = new JButton("Delete");
        tfName = new JTextField(50);
        tfEmail = new JTextField(100);
        tfPassword = new JPasswordField(250);
        tfExtra = new JTextField(250);
        this.pw_id = pw_id;
        this.user_id = user_id;
        this.connection = connection;
        this.listModel = listModel;
        this.passwords = passwords;
        this.currentName = name;
        if (name != null) tfName.setText(name);
        if (email != null) tfEmail.setText(email);
        if (password != null) tfPassword.setText(password);
        if (extra != null) tfExtra.setText(extra);
        setUI();

        deletePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pw_id > 0) { //delete pw
                    String query = "DELETE FROM user_data WHERE id=?";
                    try {
                        stmt = connection.prepareStatement(query);
                        stmt.setInt(1, pw_id);

                        int bruh = stmt.executeUpdate();
                        if (bruh == 0) {
                            System.out.println("bruh");
                        } else {
                            assert currentName != null;
                            listModel.removeElement(currentName);
                            passwords.remove(currentName);
                            pwPanel.setVisible(false);
                            System.out.println("password successfuly deleted!");
                        }

                    } catch (SQLException ex){
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

        savePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                if (pw_id > 0) { //update
                    System.out.println("test");
                    //first update
                    String query = "UPDATE user_data SET name=?, email=?, password=?, extra=? WHERE id=?";
                    try {
                        stmt = connection.prepareStatement(query);
                        stmt.setString(1, tfName.getText());
                        stmt.setString(2, tfEmail.getText());
                        stmt.setString(3, tfPassword.getText());
                        stmt.setString(4, tfExtra.getText());
                        stmt.setInt(5, pw_id);


                        int bruh = stmt.executeUpdate();
                        if (bruh == 0) {
                            System.out.println("bruh");
                        } else {
                            assert currentName != null;
                            if (!currentName.equals(tfName.getText())) {
                                //hier jetzt listmodel verändern
                                listModel.removeElement(currentName);
                                listModel.addElement(tfName.getText());

                                //passwords
                                passwords.remove(currentName);
                                passwords.put(tfName.getText(), new Password(tfName.getText(), tfEmail.getText(),
                                        tfPassword.getText(), tfExtra.getText(), pw_id, user_id, connection, listModel,
                                        passwords));
                                currentName = tfName.getText();

                            }
                            System.out.println("password successfuly updated!");
                        }

                    } catch (SQLException ex){
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

                    //then clear local passwords and load them again from db
                } else { //create
                    //save new password (push to db and save it locally in datastructure)
                    String query = "INSERT INTO user_data (user_id, name, email, password, extra) VALUES (?,?,?,?,?)";
                    try {
                        stmt = connection.prepareStatement(query);
                        stmt.setInt(1, user_id);
                        stmt.setString(2, tfName.getText());
                        stmt.setString(3, tfEmail.getText());
                        stmt.setString(4, tfPassword.getText());
                        stmt.setString(5, tfExtra.getText());

                        int bruh = stmt.executeUpdate();
                        if (bruh == 0) {
                            System.out.println("bruh");
                        } else {
                            System.out.println("password successfuly inserted!");
                        }

                        listModel.addElement(tfName.getText());

                        //passwords
                        passwords.remove(currentName);
                        passwords.put(tfName.getText(), new Password(tfName.getText(), tfEmail.getText(),
                                tfPassword.getText(), tfExtra.getText(), pw_id, user_id, connection, listModel,
                                passwords));

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
        JPanel buttons = new JPanel(new GridLayout(1, 2));
        buttons.add(savePassword);
        buttons.add(deletePassword);
        pwPanel.add(buttons);

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
        deletePassword.setBackground(new Color(41, 41, 41));
        deletePassword.setForeground(new Color(160, 161, 165));
    }
    public String getTfName() {
        return this.tfName.getText();
    }
}
