package com.bobliou.chessgame.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBOperations {

    String url = "jdbc:derby:Chess;create=true";

    String usernameDerby = "pdc";
    String passwordDerby = "pdc";
    Connection conn;

    public void establishConnection() {
        try {
            conn = DriverManager.getConnection(url, usernameDerby, passwordDerby);
            System.out.println(url + "   connected....");

        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void createTable() {
        try {
            Statement statement = conn.createStatement();
            String newTableName = "CAR";
            String sqlCreate = "create table " + newTableName + " (ID int,"
                    + "Brand varchar(20), Model varchar(20),"
                    + "Price int)";
            statement.executeUpdate(sqlCreate);

            String sqlInsert = "insert into " + newTableName + " values("
                    + "1, 'Toyota', 'Camry', 18000),"
                    + "("
                    + "2, 'Toyota', 'Corolla', 9800),"
                    + "("
                    + "3, 'Nissan', 'Pulsar', 6800)";
            statement.executeUpdate(sqlInsert);

            String sqlUpdateTable = "update " + newTableName + " set price=15000 "
                    + "where brand='Toyota' and model='camry'";
            statement.executeUpdate(sqlUpdateTable);

            //statement.close();
            System.out.println("Table created");

        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getQuery() {
        ResultSet rs = null;

        try {

            System.out.println(" getting query....");
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            String sqlQuery = "select model, price from car  "
                    + "where brand='Toyota'";

            rs = statement.executeQuery(sqlQuery);
            rs.beforeFirst();
            while (rs.next()) {
                String model = rs.getString("model"); // 1
                int price = rs.getInt(2);
                System.out.println(model + ":  $" + price);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        //return(rs);  
        //return(rs);  
        //return(rs);  
        //return(rs);  
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
