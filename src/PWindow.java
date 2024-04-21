import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PWindow extends JDialog {

    private JFrame frame;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JButton btnAddPw;
    private JScrollPane leftSide; //contains list of pw's
    private JPanel rightSide; //contains selected password details
    private PreparedStatement stmt = null;
    private ResultSet result = null;

    private Connection connection;
    private int id;

    JSplitPane split;

    public PWindow(JFrame frame, int id, Connection connection) {
        init(frame, id, connection);

        //create list selection
        list.addListSelectionListener(e -> {
            String name = list.getSelectedValue();
            try {
                //construct statement for query
                String query = "SELECT * FROM user_data WHERE user_id = ? AND name = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, String.valueOf(id));
                stmt.setString(2, name);

                //result
                result = stmt.executeQuery();

                //fill out form and set right side
                split.setRightComponent(rightSide);


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        //add Password
        this.frame.add(this.split, BorderLayout.CENTER);
        this.frame.setVisible(true);
    }

    public JFrame init(JFrame frame, int id, Connection connection) {
        //general prep
        this.id = id;
        this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        this.rightSide = new JPanel();
        this.frame = frame;
        this.connection = connection;
        init_rightSide();
        init_frame();

        //create left side
        this.listModel = new DefaultListModel<>();
        this.list = new JList<>(listModel);
        btnAddPw = new JButton("+");
        JPanel arg = new JPanel(new BorderLayout()); //puts list on top of "add new pw"-Button
        arg.add(new JScrollPane(list), BorderLayout.CENTER);
        arg.add(btnAddPw, BorderLayout.SOUTH);
        btnAddPw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                split.setRightComponent(rightSide);
            }
        });
        leftSide = new JScrollPane(arg); //inits left side with list and "add new pw"-Button
        split.setLeftComponent(leftSide);

        frame.add(split);


        return this.frame;
    }

    public void init_frame()  {
        this.frame.setTitle("pwm");
        this.frame.getContentPane().setBackground(new Color(41, 41,41));
        this.frame.getContentPane().setForeground(new Color(41, 41,41));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(450, 450);
    }

    public void init_rightSide() {
        JLabel pName = new JLabel("Name");
        JLabel pEmail = new JLabel("E-Mail or Username");
        JLabel pPassword = new JLabel("Password");
        JLabel pExtra = new JLabel("Extra");
        JButton savePassword = new JButton("Save");
        JTextField tfName = new JTextField(50);
        JTextField tfEmail = new JTextField(100);
        JPasswordField tfPassword = new JPasswordField(250);
        JTextField tfExtra = new JTextField(250);

        pName.setBackground(new Color(160, 161, 165));
        pName.setForeground(new Color(160, 161, 165));
        pEmail.setBackground(new Color(160, 161, 165));
        pEmail.setForeground(new Color(160, 161, 165));
        pPassword.setBackground(new Color(160, 161, 165));
        pPassword.setForeground(new Color(160, 161, 165));
        tfEmail.setBackground(new Color(160, 161, 165));
        tfEmail.setForeground(new Color(41, 41, 41));
        tfPassword.setBackground(new Color(160, 161, 165));
        tfPassword.setForeground(new Color(41, 41, 41));

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

        savePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get all


                try {
                    String query = "INSERT INTO user_data (name, email, password, extra) VALUES (?,?,?,?)";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, tfName.getText());
                    stmt.setString(2, tfEmail.getText());
                    stmt.setString(3, tfPassword.getText());
                    stmt.setString(4, tfExtra.getText());

                    //execute query
                    int inserted = stmt.executeUpdate();
                    if (inserted > 0) {
                        System.out.println("success!");
                    } else {
                        System.out.println("fail!");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

}
