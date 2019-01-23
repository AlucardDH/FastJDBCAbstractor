package com.dh.fastjdbcabstractor;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Useful class to open jdbc connections
 * 
 * @author AlucardDH
 */
public class DBConnector {

    private String dbDriverClass;
    private String dbUrl;
    private String dbUser;
    private String dbPasswd;

    public DBConnector(String dbDriverClass, String dbUrl, String dbUser, String dbPasswd) {
        this.dbDriverClass = dbDriverClass;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPasswd = dbPasswd;
    }

    public DBConnector(String propertiesFile) {
        Properties prop = new Properties();

        try (InputStream input = DBConnector.class.getResourceAsStream(propertiesFile)) {

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            this.dbDriverClass = prop.getProperty("dbDriverClass");
            this.dbUrl = prop.getProperty("dbUrl");
            this.dbUser = prop.getProperty("dbUser");
            this.dbPasswd = prop.getProperty("dbPasswd");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public Connection getConnection() {
        try {
            Class.forName(dbDriverClass).newInstance();
            return (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPasswd);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
