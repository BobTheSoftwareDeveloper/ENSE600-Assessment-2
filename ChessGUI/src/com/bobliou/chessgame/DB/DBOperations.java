package com.bobliou.chessgame.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBOperations {

    private String url = "jdbc:derby:Chess;create=true";

    private String usernameDerby = "pdc";
    private String passwordDerby = "pdc";
    private Connection conn;

    public void establishConnection() {
        try {
            conn = DriverManager.getConnection(url, usernameDerby, passwordDerby);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    public void createTable() {
        try {
            Statement statement = conn.createStatement();
            String newTableName = "Chess";
            String sqlCreate = "create table " + newTableName
                    + " (Game_Name varchar(255), History varchar(32600))";
            statement.executeUpdate(sqlCreate);

            String sqlInsert = "insert into " + newTableName + " values("
                    + "'First Game', 'e2 e4')";
            statement.executeUpdate(sqlInsert);

            System.out.println("Table created");

        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    public void addNewGame(String gameName) {
        try {
            Statement statement = conn.createStatement();
            String sqlInsert = "insert into Chess values("
                    + "'" + gameName + "', '')";
            statement.executeUpdate(sqlInsert);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    public void addGameMoveEntry(String gameName, String move) {
        ResultSet rs = null;

        try {
            String sqlRead = "SELECT history FROM chess WHERE GAME_NAME='" + gameName + "'";
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery(sqlRead);
            rs.next();
            String history = rs.getString(1);
            history += ", " + move.trim();
            
            String sqlUpdate = "UPDATE Chess SET History = '" + history + "' WHERE GAME_NAME = '" + gameName + "'"; 
            statement.executeUpdate(sqlUpdate);
            
            System.out.println("some: " + history);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    public String getGameHistory(String gameName) {
        ResultSet rs = null;
        String history = "";

        try {
            String sqlRead = "SELECT history FROM chess WHERE GAME_NAME='" + gameName + "'";
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery(sqlRead);
            rs.next();
            history = rs.getString(1);
            System.out.println("some: " + history);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
        return history;
    }

    public String[] getGameName() {
        ResultSet rs = null;
        String[] arr = null;
        try {
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            String sqlQuery = "select game_name from chess";

            rs = statement.executeQuery(sqlQuery);
            LinkedList<String> list = new LinkedList<>();

            while (rs.next()) {
                String name = rs.getString(1);
                list.add(name);
            }
            arr = list.toArray(new String[list.size()]);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
        return arr;
    }

    public void getQuery() {
        ResultSet rs = null;

        try {
            System.out.println("getting query....");
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            String sqlQuery = "select game_name, history from chess";

            rs = statement.executeQuery(sqlQuery);
            rs.beforeFirst();
            while (rs.next()) {
                String name = rs.getString("game_name"); // 1
                String history = rs.getString(2);
                System.out.println(name + ": " + history);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
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
