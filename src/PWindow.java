import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class PWindow extends JDialog {

    private JFrame frame;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private JButton btnAddPw;
    private JScrollPane leftSide; //contains list of pw's
    private PreparedStatement stmt = null;
    private ResultSet result = null;
    private Connection connection;
    private int id;

    LinkedList<Password> passwords;
    JSplitPane split;

    public PWindow(JFrame frame, int id, Connection connection) {
        init(frame, id, connection);

        //read from db
        refreshPasswords();

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


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (connection != null) {

                    try {

                        connection.close();

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        //add Password
        this.frame.add(this.split, BorderLayout.CENTER);
        this.frame.setVisible(true);
    }

    public void init(JFrame frame, int id, Connection connection) {
        //general prep
        this.id = id;
        this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        this.frame = frame;
        this.connection = connection;
        this.passwords = new LinkedList<>();
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
                Password newPw = new Password(null, null, null, null,-1, id, connection);
                split.setRightComponent(newPw.pwPanel);
            }
        });
        leftSide = new JScrollPane(arg); //inits left side with list and "add new pw"-Button
        split.setLeftComponent(leftSide);

        frame.add(split);
    }

    public void init_frame()  {
        this.frame.setTitle("pwm");
        this.frame.getContentPane().setBackground(new Color(41, 41,41));
        this.frame.getContentPane().setForeground(new Color(41, 41,41));
        this.frame.setBackground(new Color(41, 41,41));
        this.frame.setForeground(new Color(41, 41,41));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(450, 450);
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
                passwords.add(new Password(result.getString("name"), result.getString("email"),
                        result.getString("password"), result.getString("extra"),
                        result.getInt("id"), result.getInt("user_id"), connection));
            }

            for (Password pw : passwords) {
                System.out.println(pw.getTfName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
