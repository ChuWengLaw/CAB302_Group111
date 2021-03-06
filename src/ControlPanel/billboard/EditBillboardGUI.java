package ControlPanel.billboard;

import ControlPanel.Client;
import ControlPanel.Main;
import ControlPanel.schedule.CalanderScheduleGUI;
import Server.Request.EditBBRequest;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Base64;

/**
 * This class creates the GUI to be used to edit a billboard
 */
public class EditBillboardGUI extends JFrame {
    //set the width of the GUI
    public static final int WIDTH = 350;
    public static final int HEIGHT = 400;

    //define element to be used
    private JButton btnSubmit;
    private JButton btnBrowse;

    //define the labels
    private JLabel lblBillboardName;
    private JLabel lblTextColour;
    private JLabel lblBackgroundColour;
    private JLabel lblMessage;
    private JLabel lblImage;
    private JLabel lblInformation;
    private JLabel lblInformationColour;

    //define the text boxes
    private JTextField txtBillboardName;
    private JTextField txtTextColour;
    private JTextField txtBackgroundColour;
    private JTextField txtMessage;
    private JTextField txtImage;
    private JTextField txtInformation;
    private JTextField txtInformationColour;

    //define the strings to be used in the SQL
    private String strBillboardName;
    private JPanel editBBPanel = new JPanel(new GridBagLayout());
    private JPanel panel = new JPanel(new GridBagLayout());
    private GridBagConstraints editBBConstraints = new GridBagConstraints();
    private GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Constructor initialises the GUI creation.
     *
     * @throws HeadlessException
     */
    public EditBillboardGUI() throws HeadlessException {
        super("Edit Billboard");
        createGUI();
    }

    /**
     * Create the base GUI to be used to find the billboard
     *
     * @author Law
     */
    private void createGUI() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //create the labels
        lblBillboardName = createLabel("Billboard Name:");

        //create the text boxes to receive the data
        txtBillboardName = createText();

        //create the button and define what text it will contain
        btnSubmit = createButton("Submit");

        //create and actionListener for the submit button
        btnSubmit.addActionListener(new ActionListener() {
            // sends request to server
            @Override
            public void actionPerformed(ActionEvent e) {
                EditBBRequest temp = new EditBBRequest(Main.loginUser.getSessionToken(), Main.loginUser.getUserName(),
                        txtBillboardName.getText().toLowerCase(), Main.loginUser.getEditAllBillboardPermission(),
                        Main.loginUser.getCreateBillboardsPermission(), CalanderScheduleGUI.IsCurrentlyScheduled(txtBillboardName.getText().toLowerCase()));

                try {
                    Client.connectServer(temp);
                    if (Client.isRequestState()) {
                        JOptionPane.showMessageDialog(null, "Opening edit window...");
                        // hide the previous panel
                        panel.setVisible(false);
                        btnSubmit.setVisible(false);
                        lblBillboardName.setVisible(false);
                        txtBillboardName.setVisible(false);
                        createEditGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "No permission/invalid billboard name/Currently Scheduled");
                    }
                } catch (ConnectException ex) {
                    JOptionPane.showMessageDialog(null, "Connection fail.");
                    System.exit(0);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 2, 2);

        //add labels to panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(lblBillboardName, constraints);
        constraints.gridy = 1;

        //add text fields to panel
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(txtBillboardName, constraints);

        //add button to panel
        constraints.gridwidth = 1;
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(btnSubmit, constraints);

        getContentPane().add(panel);
        //set the location of the GUI
        setLocation(900, 350);

        //make changes and then send to GUI
        pack();
        setVisible(true);
    }

    /**
     * Create the base GUI to be used to edit the billboard
     *
     * @author Law
     */
    private void createEditGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //create the button and define what text it will contain
        btnSubmit = createButton("Submit");
        //create and actionListener for the submit button
        btnSubmit.addActionListener(new ActionListener() {
            // sends request to server
            @Override
            public void actionPerformed(ActionEvent e) {
                // if the colour texts are all valid, send request to server
                if (txtTextColour.getText().isBlank()) {
                    txtTextColour.setText("black");
                }
                if (txtBackgroundColour.getText().isBlank()) {
                    txtBackgroundColour.setText("white");
                }
                if (txtInformationColour.getText().isBlank()) {
                    txtInformationColour.setText(txtTextColour.getText());
                }
                if (isColourValid()) {
                    EditBBRequest temp = new EditBBRequest(Main.loginUser.getSessionToken(), txtTextColour.getText(), txtBackgroundColour.getText(),
                            txtMessage.getText(), txtImage.getText(), txtInformation.getText(), txtInformationColour.getText());
                    try {
                        Client.connectServer(temp);
                        if (Client.isRequestState()) {
                            JOptionPane.showMessageDialog(null, "Successfully edited billboard!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Fail to edit billboard!");
                        }
                    } catch (ConnectException ex) {
                        JOptionPane.showMessageDialog(null, "Connection fail.");
                        System.exit(0);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    // clear the text fields once the SQL code has been executed
                    txtBillboardName.setText("");
                    txtTextColour.setText("");
                    txtBackgroundColour.setText("");
                    txtMessage.setText("");
                    txtImage.setText("");
                    txtInformation.setText("");
                    txtInformationColour.setText("");
                    setVisible(false);
                }
            }
        });

        //create the button and define what tex it will contain
        btnBrowse = createButton("Browse an image");

        //create an actionListerner for the browse button
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //create a file chooser
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());
                jfc.setDialogTitle("Select a billboard xml file");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter bmp = new FileNameExtensionFilter("BMP", "bmp");
                FileNameExtensionFilter jpeg = new FileNameExtensionFilter("JPEG", "jpeg");
                FileNameExtensionFilter png = new FileNameExtensionFilter("PNG", "png");
                jfc.addChoosableFileFilter(bmp);
                jfc.addChoosableFileFilter(jpeg);
                jfc.addChoosableFileFilter(png);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    //Convert to base64 encoded
                    String encodstring = encodeFileBase64(selectedFile);
                    //Set the text field to the converted value
                    txtImage.setText(encodstring);
                    txtImage.setForeground(Color.black);
                    txtImage.setEditable(false);
                }
            }
        });


        //create the labels
        lblTextColour = createLabel("Text Colour:");
        lblBackgroundColour = createLabel("Background Colour:");
        lblMessage = createLabel("Message:");
        lblImage = createLabel("Image");
        lblInformation = createLabel("Information:");
        lblInformationColour = createLabel("Information Colour:");

        //create the text boxes to receive the data
        txtTextColour = createText();
        txtBackgroundColour = createText();
        txtMessage = createText();
        txtImage = createText();
        txtInformation = createText();
        txtInformationColour = createText();

        //set the content of billboard in text field
        txtTextColour.setText(Client.getEditTextColour());
        txtBackgroundColour.setText(Client.getEditBGColour());
        txtMessage.setText(Client.getEditMsg());
        txtImage.setText(Client.getEditImg());
        txtInformation.setText(Client.getEditInfo());
        txtInformationColour.setText(Client.getEditInfoColour());
        editBBConstraints.anchor = GridBagConstraints.WEST;
        editBBConstraints.insets = new Insets(10, 10, 10, 10);

        //add labels to panel
        editBBConstraints.gridx = 0;
        editBBConstraints.gridy = 0;
        editBBPanel.add(lblTextColour, editBBConstraints);
        editBBConstraints.gridy = 1;
        editBBPanel.add(lblBackgroundColour, editBBConstraints);
        editBBConstraints.gridy = 2;
        editBBPanel.add(lblMessage, editBBConstraints);
        editBBConstraints.gridy = 3;
        editBBPanel.add(lblImage, editBBConstraints);
        editBBConstraints.gridy = 4;
        editBBPanel.add(lblInformation, editBBConstraints);
        editBBConstraints.gridy = 5;
        editBBPanel.add(lblInformationColour, editBBConstraints);

        //add txtfeilds to panel
        editBBConstraints.gridx = 1;
        editBBConstraints.gridy = 0;
        editBBPanel.add(txtTextColour, editBBConstraints);
        editBBConstraints.gridy = 1;
        editBBPanel.add(txtBackgroundColour, editBBConstraints);
        editBBConstraints.gridy = 2;
        editBBPanel.add(txtMessage, editBBConstraints);
        editBBConstraints.gridy = 3;
        editBBPanel.add(txtImage, editBBConstraints);
        editBBConstraints.gridy = 4;
        editBBPanel.add(txtInformation, editBBConstraints);
        editBBConstraints.gridy = 5;
        editBBPanel.add(txtInformationColour, editBBConstraints);

        //add button to panel
        editBBConstraints.gridwidth = 1;
        editBBConstraints.insets = new Insets(5, 10, 5, 10);
        editBBConstraints.anchor = GridBagConstraints.EAST;
        editBBConstraints.gridx = 0;
        editBBConstraints.gridy = 8;
        editBBPanel.add(btnSubmit, editBBConstraints);
        editBBConstraints.gridx = 1;
        editBBPanel.add(btnBrowse, editBBConstraints);

        getContentPane().add(editBBPanel);
        //set the location of the GUI
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setLocation(width / 4, height / 4);

        //make changes and then send to GUI
        pack();
        setVisible(true);
    }

    /**
     * This function creates a JButton
     *
     * @param text the text with will be on the button
     * @return a JButton with text on it
     * @author Law
     */
    private JButton createButton(String text) {
        JButton button = new JButton();
        button.setText(text);
        return button;
    }

    /**
     * This function create a blank JTextField
     *
     * @return an empty JTextField
     * @author Law
     */
    private JTextField createText() {
        JTextField textBox = new JTextField(20);
        return textBox;
    }

    /**
     * This function create a JLabel with the title of the text
     *
     * @param text the title of the label
     * @return a JLabel with the title of text
     * @author Law
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        return label;
    }

    /**
     * This function takes a file and encodes it as base64 returning the string of the encoded value
     *
     * @param file the image file which is being converted
     * @return the base64 encoded string
     * @author Lachlan
     */
    private String encodeFileBase64(File file) {
        String encodedString = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedString = Base64.getEncoder().encodeToString(bytes);
            fileInputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    /**
     * This function determines whether a colour named using a code is valid or not
     *
     * @param textInput the input from the called text field
     * @return returns a boolean value of whether the color code is valid or not
     * @Lachlan
     */
    private boolean isColourCodeValid(String textInput) {
        boolean valid = false;
        boolean startsWithHash = false;
        String s2 = textInput.substring(1);

        if (textInput.length() == 7) {
            if (textInput.charAt(0) == '#') {
                startsWithHash = true;
            }
        }

        if (startsWithHash == true) {
            for (int i = 0; i < s2.length(); i++) {
                char c = s2.charAt(i);
                if (c != '#') {
                    if (s2.matches("[A-F]{1,}") || s2.matches("[0-9]{1,}")) {
                        valid = true;
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * This function determines whether colours named by works are valid of not
     *
     * @return a boolean value to whether all the inputted colours are valid
     * @author Lachlan
     */
    private boolean isColourValid() {
        boolean text = true;
        boolean back = true;
        boolean info = true;

        //if the textColour isn't empty
        if (txtTextColour.getText().length() > 0) {
            //see if it starts with # and then see if it a valid colour code. If so set text to true else return a valid error message
            if (txtTextColour.getText().startsWith("#")) {
                if (isColourCodeValid(txtTextColour.getText())) {
                    text = true;
                } else {
                    text = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid text colour");
                }
            }
            //else if not valid colour name return appropriate error message
            else {
                try {
                    Class.forName("java.awt.Color").getField(txtTextColour.getText().toLowerCase());
                    text = true;
                } catch (NoSuchFieldException e) {
                    text = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid text colour");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //if informationColour isn't empty
        if (txtInformationColour.getText().length() > 0) {
            //see if it starts with # and then see if it a valid colour code. If so set text to true else return a valid error message
            if (txtInformationColour.getText().startsWith("#")) {
                if (isColourCodeValid(txtInformationColour.getText())) {
                    info = true;
                } else {
                    info = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid information colour");
                }
            }
            //else if not valid colour name return appropriate error message
            else {
                try {
                    Class.forName("java.awt.Color").getField(txtInformationColour.getText().toLowerCase());
                    info = true;
                } catch (NoSuchFieldException e) {
                    info = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid information colour");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //if backgroundColour isn't empty
        if (txtBackgroundColour.getText().length() > 0) {
            //see if it starts with # and then see if it a valid colour code. If so set text to true else return a valid error message
            if (txtBackgroundColour.getText().startsWith("#")) {
                if (isColourCodeValid(txtBackgroundColour.getText())) {
                    back = true;
                } else {
                    back = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid background colour");
                }
            }
            //else if not valid colour name return appropriate error message
            else {
                try {
                    Class.forName("java.awt.Color").getField(txtBackgroundColour.getText().toLowerCase());
                    back = true;
                } catch (NoSuchFieldException e) {
                    back = false;
                    JOptionPane.showMessageDialog(null, "Please enter a valid background colour");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return text && back && info;
    }
}