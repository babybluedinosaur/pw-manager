import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class PWindow extends JDialog {

    private JFrame frame;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JButton btnAddPw;
    private JScrollPane leftSide;
    private PreparedStatement stmt = null;
    private ResultSet result = null;
    private Connection connection;
    private int id;
    HashMap<String, Password> passwords;
    JSplitPane split;

    public PWindow(JFrame frame, int id, Connection connection) {
        init(frame, id, connection);

        //read from db
        refreshPasswords();

        for (Password pw : passwords.values()) {
            listModel.addElement(pw.getTfName());
        }

        //create list selection
        list.addListSelectionListener(e -> {
            String name = list.getSelectedValue();
            if (name != null) {
                split.setRightComponent(passwords.get(name).pwPanel);
            }
        });



        //add Password
        this.frame.add(this.split, BorderLayout.CENTER);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void init(JFrame frame, int id, Connection connection) {
        //general prep
        this.id = id;
        JPanel defaultRight = new JPanel();
        defaultRight.setBackground(new Color(41, 41,41));
        this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), defaultRight);
        this.frame = frame;
        this.connection = connection;
        this.passwords = new HashMap<>();
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
                Password newPw = new Password(null, null, null, null,-1, id,
                        connection, listModel, passwords);
                split.setRightComponent(newPw.pwPanel);
            }
        });
        leftSide = new JScrollPane(arg); //inits left side with list and "add new pw"-Button
        leftSide.setPreferredSize(new Dimension(200, getHeight()));
        split.setLeftComponent(leftSide);
        split.getLeftComponent().setMinimumSize(new Dimension(250, 100 ));
        init_colors();

        frame.add(split);
    }

    public void init_frame()  {
        this.frame.setTitle("pwm");
        this.frame.getContentPane().setBackground(new Color(41, 41,41));
        this.frame.getContentPane().setForeground(new Color(41, 41,41));
        this.frame.setBackground(new Color(41, 41,41));
        this.frame.setForeground(new Color(41, 41,41));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(550, 450);
    }

    public void init_colors() {
        this.list.setBackground(new Color(41, 41,41));
        this.list.setForeground(new Color(160, 161, 165));
        this.btnAddPw.setBackground(new Color(41, 41,41));
        this.btnAddPw.setForeground(new Color(160, 161, 165));
    }

    public void refreshPasswords() {
        try {
            //prepare query
            String query = "SELECT * FROM user_data WHERE user_id = ? ORDER BY name";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            //result
            result = stmt.executeQuery();

            while (result.next()) {
                //create password instance and add to passwords
                passwords.put(result.getString("name"),
                        new Password(result.getString("name"), result.getString("email"),
                        result.getString("password"), result.getString("extra"),
                        result.getInt("id"), result.getInt("user_id"), connection, listModel,
                                passwords));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
