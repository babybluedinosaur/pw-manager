import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;



public class Login extends JDialog{
    private JLabel pEmail;
    private JLabel pPassword;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JButton btnOK;
    private JLabel createUsr;
    private JLabel status;
    private ResourceBundle reader = null;
    Connection connection = null;

    PreparedStatement stmt = null;

    ResultSet result = null;

    public Login(JFrame frame) {
        //create panels, text fields etc.
        pEmail = new JLabel("Username");
        pPassword = new JLabel("Password");
        tfEmail = new JTextField(20);
        tfPassword = new JPasswordField(25);
        createUsr = new JLabel("Create New User");
        status = new JLabel();
        btnOK = new JButton("OK");

        //set titles, colors etc.
        setUI();

        //check if user exists after ok-button got clicked
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get input email and password
                String email = tfEmail.getText();
                String pw = Arrays.toString(tfPassword.getPassword());

                try {

                    //get login data for db and connect to db
                    reader = ResourceBundle.getBundle("dbconfig");
                    connection = DriverManager.getConnection(
                            reader.getString("db.url"),
                            reader.getString("db.username"),
                            reader.getString("db.password"));

                    //construct statement for query
                    String query = "SELECT * FROM user WHERE email = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, email);

                    //result
                    result = stmt.executeQuery();
                    if (!result.next()) {
                        status.setText("username or password wrong!");
                    } else {
                        String db_name = result.getString("email");
                        String db_pw = result.getString("password_hash");
                        int id = result.getInt("id");
                        if (email.equals(db_name) && BCrypt.checkpw(pw, db_pw)) {
                            frame.setVisible(false);
                            new PWindow(new JFrame(), id, connection);
                        }
                        else status.setText("username or password wrong!");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    //close connection, statements etc.
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

                    /*if (connection != null) {

                        try {

                            connection.close();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }*/
                }
            }
        });

        //create new user, after "Create new User" got clicked
        createUsr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFrame cuFrame = new JFrame("Create User");
                cuFrame.getContentPane().setBackground(new Color(41, 41,41));
                cuFrame.getContentPane().setForeground(new Color(41, 41,41));
                cuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                cuFrame.setSize(450, 450);
                new CreateUser(cuFrame);
            }
        });

        frame.getContentPane().setLayout(new GridLayout(7,1));
        frame.getContentPane().add(pEmail);
        frame.getContentPane().add(tfEmail);
        frame.getContentPane().add(pPassword);
        frame.getContentPane().add(tfPassword);
        frame.getContentPane().add(createUsr);
        frame.getContentPane().add(status);
        frame.getContentPane().add(btnOK);
        frame.setVisible(true);
    }

    public void setUI() {
        pEmail.setBackground(new Color(160, 161, 165));
        pEmail.setForeground(new Color(160, 161, 165));
        pPassword.setBackground(new Color(160, 161, 165));
        pPassword.setForeground(new Color(160, 161, 165));
        tfEmail.setBackground(new Color(160, 161, 165));
        tfEmail.setForeground(new Color(41, 41, 41));
        tfPassword.setBackground(new Color(160, 161, 165));
        tfPassword.setForeground(new Color(41, 41, 41));
        btnOK.setBackground(new Color(160, 161, 165));
        btnOK.setForeground(new Color(41, 41, 41));
        createUsr.setBackground(new Color(160, 161, 165));
        createUsr.setForeground(new Color(160, 161, 165));
        status.setBackground(new Color(64, 174, 96));
        status.setForeground(new Color(64, 174, 96));

        btnOK.setPreferredSize(new Dimension(80, 30));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.getContentPane().setBackground(new Color(41, 41,41));
        frame.getContentPane().setForeground(new Color(41, 41,41));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);
        frame.setLocationRelativeTo(null);
        Login login = new Login(frame);

    }

}
