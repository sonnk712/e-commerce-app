package com.LTTBDD.ecommerce_app.common.config;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//    private static String DB_URL = "jdbc:mysql://192.168.1.1:3306/webservice";
//    private static String USERNAME = "root";
//    private static String PASSWORD = "123456";

//    public static Connection getConnect() {
//        Connection connection = null;
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//            System.out.println("connect successfully!");
//        } catch (SQLException | ClassNotFoundException e) {
//            System.out.println("connect failure!");
//            e.printStackTrace();
//        }
//        return connection;
//    }

public class ConnectToMysql {
    private static String ip = "192.168.1.4";
    private static String port = "1433";
    private static String db = "ecommerce_app";
    private static String un = "sa";
    private static String password = "123456";

    public static Connection connect() {
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String URL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(URL);
            System.out.println("connect success");
        } catch (SQLException e) {
            System.out.println("connect failure!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("connect failure!");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}