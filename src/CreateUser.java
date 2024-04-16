import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;


public class CreateUser extends JDialog{
    private JLabel lEmail;
    private JLabel lPassword;
    private JLabel lPasswordRep;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JPasswordField tfPasswordRep;
    private JButton btnOK;
    private JLabel status;
    private ResourceBundle reader = null;
    Connection connection = null;
    PreparedStatement insert = null;

    PreparedStatement get = null;

    ResultSet get_result = null;

    public CreateUser(JFrame frame) {
        //create panels, text fields etc.
        lEmail = new JLabel("Username");
        lPassword = new JLabel("Password");
        lPasswordRep = new JLabel("Repeat Password");
        tfEmail = new JTextField(20);
        tfPassword = new JPasswordField(20);
        tfPasswordRep = new JPasswordField(20);
        status = new JLabel();
        btnOK = new JButton("OK");

        //set titles, colors etc.
        setUI();

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get input email and password
                String username = tfEmail.getText();
                String pw = Arrays.toString(tfPassword.getPassword());
                String pwRep = Arrays.toString(tfPasswordRep.getPassword());
                try {
                    //get login data for db and connect to db
                    reader = ResourceBundle.getBundle("dbconfig");
                    System.out.println("connecting to database...");
                    connection = DriverManager.getConnection(
                            reader.getString("db.url"),
                            reader.getString("db.username"),
                            reader.getString("db.password"));

                    //prepare check if user already exists
                    String query = "SELECT * FROM user WHERE email = ?";
                    get = connection.prepareStatement(query);
                    get.setString(1, username);
                    get_result = get.executeQuery();


                    //check if input violates rules
                    if (username.length() < 4) status.setText("username too short!");
                    else if (get_result.next()) status.setText("username already exists!");
                    else if (pw.length() < 8) status.setText("password too short!");
                    else if (!safePw(pw)) status.setText("use at least one letter, number and special sign for your" +
                            " password!");
                    else if (!pw.equals(pwRep)) status.setText("passwords are not identical!");
                    else {
                        //construct statement for query
                        String sql = "INSERT INTO user (email, password_hash) VALUES (?, ?)";
//                        System.out.println("preparing query...");

                        //create statement
                        String hashed_pw = BCrypt.hashpw(pw, BCrypt.gensalt());
                        insert = connection.prepareStatement(sql);
                        insert.setString(1, username);
                        insert.setString(2, hashed_pw);

                        //insert new user
                        int  rowsInserted = insert.executeUpdate();
//                        if (rowsInserted > 0) System.out.println("new user created!");
                        setVisible(false);
                        System.exit(0);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    //close connection, statements etc.
                    if (get_result != null) {
                        try {

                            get_result.close();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    if (insert != null) {
                        try {

                            insert.close();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    if (connection != null) {

                        try {

                            connection.close();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        frame.getContentPane().setLayout(new GridLayout(8,1));
        frame.getContentPane().add(lEmail);
        frame.getContentPane().add(tfEmail);
        frame.getContentPane().add(lPassword);
        frame.getContentPane().add(tfPassword);
        frame.getContentPane().add(lPasswordRep);
        frame.getContentPane().add(tfPasswordRep);
        frame.getContentPane().add(status);
        frame.getContentPane().add(btnOK);
        frame.setVisible(true);
    }

    public void setUI() {
        lEmail.setBackground(new Color(160, 161, 165));
        lEmail.setForeground(new Color(160, 161, 165));
        lPassword.setBackground(new Color(160, 161, 165));
        lPassword.setForeground(new Color(160, 161, 165));
        lPasswordRep.setBackground(new Color(160, 161, 165));
        lPasswordRep.setForeground(new Color(160, 161, 165));
        tfEmail.setBackground(new Color(160, 161, 165));
        tfEmail.setForeground(new Color(41, 41, 41));
        tfPassword.setBackground(new Color(160, 161, 165));
        tfPassword.setForeground(new Color(41, 41, 41));
        tfPasswordRep.setBackground(new Color(160, 161, 165));
        tfPasswordRep.setForeground(new Color(41, 41, 41));
        btnOK.setBackground(new Color(160, 161, 165));
        btnOK.setForeground(new Color(41, 41, 41));
        status.setBackground(new Color(64, 174, 96));
        status.setForeground(new Color(64, 174, 96));

        btnOK.setPreferredSize(new Dimension(80, 30));
    }

    public static boolean safePw(String input) {
        //match
        String letters = "[a-zA-Z]+";
        String numbers = "0123456789";
        String special = "[^a-zA-Z0-9]+";

        //create regex
        String letters_reg = "[" + letters + "]+";
        String numbers_reg = "[" + numbers + "]+";
        String special_reg = "[" + special + "]+";

        //check if input contains at least one number, letter and special sign
        if (!input.matches(".*" + letters_reg + ".*")) return false;
        if (!input.matches(".*" + numbers_reg + ".*")) return false;
        if (!input.matches(".*" + special_reg + ".*")) return false;
        return true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Create User");
        frame.getContentPane().setBackground(new Color(41, 41,41));
        frame.getContentPane().setForeground(new Color(41, 41,41));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);
        CreateUser create = new CreateUser(frame);
    }
}
