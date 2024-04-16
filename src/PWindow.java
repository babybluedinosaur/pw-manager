import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PWindow extends JDialog {

    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JButton btnAddPw;
    private JScrollPane leftSide;
    private JPanel rightSide;
    private PreparedStatement stmt = null;
    private ResultSet result = null;
    private int id;

    public PWindow(JFrame frame, int id, Connection connection) {
        init(frame, id);

        //create split
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);

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

        frame.add(split, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void init_defaultPanel() {
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

    }

    public JFrame init(JFrame frame, int id) {
        //general prep
        this.id = id;
        rightSide = new JPanel();
        init_defaultPanel();


        //prep for left side
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        btnAddPw = new JButton("+");

        //create left side
        JPanel arg = new JPanel(new BorderLayout());
        arg.add(new JScrollPane(list), BorderLayout.CENTER);
        arg.add(btnAddPw, BorderLayout.SOUTH);
        btnAddPw.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        leftSide = new JScrollPane(arg);

        //set main frame
        frame.setTitle("pwm");
        frame.getContentPane().setBackground(new Color(41, 41,41));
        frame.getContentPane().setForeground(new Color(41, 41,41));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);

        return frame;
    }

}