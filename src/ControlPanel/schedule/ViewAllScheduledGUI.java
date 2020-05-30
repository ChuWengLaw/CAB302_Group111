package ControlPanel.schedule;

import ControlPanel.Client;
import ControlPanel.Main;
import Server.Request.ListScheduleRequest;

import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;


/**
 * This class creates the GUI to be used to display all the existing
 * billboards from the database
 */
public class ViewAllScheduledGUI extends JFrame {
    private JPanel panel = new JPanel();
    private JTable table = new JTable();

    public ViewAllScheduledGUI() throws HeadlessException {
        super("All Scheduled");
        createGUI();
    }

    private void createGUI() {
        // sends request to server
        ListScheduleRequest listScheduleRequest = new ListScheduleRequest(Main.loginUser.getSessionToken());
        try {
            Client.connectServer(listScheduleRequest);
        } catch (ConnectException ex) {
            JOptionPane.showMessageDialog(null, "Connection fail.");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        setLocation(width/4,height/4);

        panel.setLayout(new BorderLayout());
        getContentPane().add(panel);

        super.setSize(500, 120);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        table = Client.getListScheduleBillboardTable();
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollpane = new JScrollPane(table);
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);
        super.setContentPane(panel);
        super.setVisible(true);
    }
}