package ControlPanel;

import ControlPanel.billboard.ListBillboardsGUI;
import Server.Request.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class setups a stateless connection to the server socket whenever
 * an action is performed in other billboard/user GUI classes
 */
public class Client {
    private static Object requestReply;
    private static boolean requestState;
    private static String info = "";
    private static JTable listBBTable;
    private static ArrayList<String[]> ScheduleArray;

    /**
     * Connects to server (connection read from network.props)
     * @param args The object encapsulating the data inputs to be sent to server
     */
    public static void connectServer(Object args) throws IOException, InterruptedException, ClassNotFoundException {
        // Set up socket.
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("./network.props");
        props.load(in);
        in.close();
        String host = props.getProperty("address");
        int port = Integer.parseInt(props.getProperty("port"));
        Socket socket = new Socket(host, port);

        // Set up input and output stream.
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        oos.writeObject(args);
        oos.flush();

        Object requestReply = ois.readObject();
        executeReply(requestReply);

        ois.close();
        oos.close();
    }

    public static boolean isRequestState() {
        return requestState;
    }

    private static void executeReply(Object requestReply){
        if (requestReply instanceof LoginReply){
            LoginReply loginReply = (LoginReply) requestReply;
            requestState = loginReply.isLoginState();

            if (requestState){
                Main.loginUser = loginReply.getUser();
            }
        }
        else if (requestReply instanceof SearchReply){
            SearchReply searchReply = (SearchReply) requestReply;
            requestState = searchReply.isRequestState();

            if (requestState){
                Main.editUserWin.editedUser = searchReply.getUser();
            }
        }
        else if (requestReply instanceof GernalReply){
            GernalReply gernalReply = (GernalReply) requestReply;
            requestState = gernalReply.isRequestState();
        }
        else if (requestReply instanceof ListUserReply){
            ListUserReply listUserReply = (ListUserReply) requestReply;
            Main.listUserWin.getTable(listUserReply.getTable());
        }
        else if (requestReply instanceof BBInfoReply){
            BBInfoReply bbInfoReply = (BBInfoReply) requestReply;
            info = bbInfoReply.getInformation();
        }
        else if (requestReply instanceof ListBBReply){
            ListBBReply listBBReply = (ListBBReply) requestReply;
            listBBTable = listBBReply.getTable();
        }
        else if (requestReply instanceof WeeklyScheduleReply){
            WeeklyScheduleReply ScheduleReply = (WeeklyScheduleReply) requestReply;
            ScheduleArray = ScheduleReply.getArray();
        }
    }
    public static String getInfo() { return info; }
    public static JTable getBBTable() {return listBBTable;}
    public static ArrayList<String[]> getScheduleArray() {return ScheduleArray;}
}


