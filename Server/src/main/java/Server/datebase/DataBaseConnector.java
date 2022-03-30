package Server.datebase;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DataBaseConnector {
    private static String sshUser;
    private static String sshPassword;
    private static String sshHost;
    private static int sshPort;

    private static String remoteHost;
    private static int remotePort;
    private static int localPort;

    private static String dbUser;
    private static String dbPassword;
    private static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:" + localPort + "/studs", dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init(){
        initFields();
        doSshTunnel();
    }

    private static void doSshTunnel() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException unexpected) {
            unexpected.printStackTrace();
        }
    }

    private static void initFields() {
        sshUser = PropertyManager.getProperty("sshUser");
        sshPassword = PropertyManager.getProperty("sshPassword");
        sshHost = PropertyManager.getProperty("sshHost");
        sshPort = Integer.parseInt(Objects.requireNonNull(PropertyManager.getProperty("sshPortNumber")));

        remoteHost = PropertyManager.getProperty("remoteHost");
        remotePort = Integer.parseInt(Objects.requireNonNull(PropertyManager.getProperty("remotePort")));
        localPort = Integer.parseInt(Objects.requireNonNull(PropertyManager.getProperty("localPort")));

        dbUser = PropertyManager.getProperty("dbUser");
        dbPassword = PropertyManager.getProperty("dbPassword");
    }

    public static Connection getConnection() {
        connect();
        return con;
    }
}