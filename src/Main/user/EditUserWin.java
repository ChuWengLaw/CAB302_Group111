package Main.user;

import Main.Main;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class EditUserWin extends JFrame{
    private JLabel labelUserName = new JLabel("User Name");
    private JLabel labelPassword = new JLabel("Selected password");
    private JLabel labelpermission1 = new JLabel("Create billboards");
    private JLabel labelpermission2 = new JLabel("Edit billboards");
    private JLabel labelpermission3 = new JLabel("Schedule billboards");
    private JLabel labelpermission4 = new JLabel("Edit users");
    private JTextField userNameTextField = new JTextField(20);
    private JTextField passwordTextField = new JTextField(20);
    private JCheckBox checkBox1 = new JCheckBox("Enable");
    private JCheckBox checkBox2 = new JCheckBox("Enable");
    private JCheckBox checkBox3 = new JCheckBox("Enable");
    private JCheckBox checkBox4 = new JCheckBox("Enable");
    private JButton searchButton= new JButton("Search");
    private JButton editButton = new JButton("Edit");
    private JButton cancelButton = new JButton("Cancel");
    private JPanel panel = new JPanel(new GridBagLayout());
    private GridBagConstraints constraints = new GridBagConstraints();

    public EditUserWin(){
        // Setting default value of the frame
        super("Edit User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Text field and checkbox setting
        passwordTextField.setEditable(false);
        checkBox1.setEnabled(false);
        checkBox2.setEnabled(false);
        checkBox3.setEnabled(false);
        checkBox4.setEnabled(false);
        editButton.setEnabled(false);

        // Button setting
        ActionListener searchListener = e -> {
            try {
                if (!CheckUserSQL(userNameTextField.getText())){
                    new ErrorWin("User Name does not exist");
                }
                else{
                    SetUserSQL();
                    userNameTextField.setEditable(false);
                    passwordTextField.setEditable(true);
                    checkBox1.setEnabled(true);
                    checkBox2.setEnabled(true);
                    checkBox3.setEnabled(true);
                    checkBox4.setEnabled(true);
                    editButton.setEnabled(true);
                    searchButton.setEnabled(false);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };
        searchButton.addActionListener(searchListener);

        ActionListener editListener = e ->{
            try{
                EditUserSQL(userNameTextField.getText(), passwordTextField.getText(), checkBox1.isSelected(), checkBox2.isSelected(), checkBox3.isSelected(), checkBox4.isSelected());
                userNameTextField.setEditable(true);
                passwordTextField.setEditable(false);
                checkBox1.setEnabled(false);
                checkBox2.setEnabled(false);
                checkBox3.setEnabled(false);
                checkBox4.setEnabled(false);
                editButton.setEnabled(false);
                searchButton.setEnabled(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };
        editButton.addActionListener(editListener);

        ActionListener cancelListener = e -> {
            passwordTextField.setText(null);
            checkBox1.setSelected(false);
            checkBox2.setSelected(false);
            checkBox3.setSelected(false);
            checkBox4.setSelected(false);
            userNameTextField.setEditable(true);
            passwordTextField.setEditable(false);
            checkBox1.setEnabled(false);
            checkBox2.setEnabled(false);
            checkBox3.setEnabled(false);
            checkBox4.setEnabled(false);
            editButton.setEnabled(false);
            searchButton.setEnabled(true);
        };
        cancelButton.addActionListener(cancelListener);

        // Panel setting
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(labelUserName, constraints);

        constraints.gridx = 1;
        panel.add(userNameTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(labelPassword, constraints);

        constraints.gridx = 1;
        panel.add(passwordTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(labelpermission1, constraints);

        constraints.gridx = 1;
        panel.add(checkBox1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(labelpermission2, constraints);

        constraints.gridx = 1;
        panel.add(checkBox2, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(labelpermission3, constraints);

        constraints.gridx = 1;
        panel.add(checkBox3, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(labelpermission4, constraints);

        constraints.gridx = 1;
        panel.add(checkBox4, constraints);

        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(editButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(searchButton, constraints);

        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(cancelButton, constraints);

        getContentPane().add(panel);

        // Display the window
        setLocation(900,350);
        pack();
        setVisible(true);
    }

    private void SetUserSQL() throws SQLException {
        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM  user");

        while(resultSet.next()) {
            if (userNameTextField.getText().equals(resultSet.getString("userName"))){
                passwordTextField.setText(resultSet.getString("userPassword"));
                checkBox1.setSelected(resultSet.getBoolean("createBillboardsPermission"));
                checkBox2.setSelected(resultSet.getBoolean("editAllBillboardPermission"));
                checkBox3.setSelected(resultSet.getBoolean("scheduleBillboardsPermission"));
                checkBox4.setSelected(resultSet.getBoolean("editUsersPermission"));
                break;
            }
        }

        statement.close();
    }

    private void EditUserSQL(String userName, String userPassword,
                             boolean createBillboardsPermission, boolean editAllBillboardPermission,
                             boolean scheduleBillboardsPermission, boolean editUsersPermission) throws SQLException {
        PreparedStatement Pstatement = Main.connection.prepareStatement("UPDATE user " +
                "SET userPassword = ?,  createBillboardsPermission = ?, editAllBillboardPermission = ?, " +
                "scheduleBillboardsPermission = ?, editUsersPermission = ? WHERE userName = ? " );
        Pstatement.setString(1, userPassword);
        Pstatement.setBoolean(2, createBillboardsPermission);
        Pstatement.setBoolean(3, editAllBillboardPermission);
        Pstatement.setBoolean(4, scheduleBillboardsPermission);
        Pstatement.setBoolean(5, editUsersPermission);
        Pstatement.setString(6, userName);
        Pstatement.executeUpdate();
        Pstatement.close();
    }

    private boolean CheckUserSQL(String userName) throws SQLException {
        boolean existing = false;

        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT userName FROM  user");

        while(resultSet.next()){
            if (userName.equals(resultSet.getString(1))){
                existing = true;
                break;
            }
        }

        statement.close();
        return existing;
    }
}