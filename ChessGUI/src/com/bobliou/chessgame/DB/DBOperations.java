package com.bobliou.chessgame.DB;

import java.sql.Connection;
import java.sql.DriverManager;
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

    /**
     * Establish the database connectino
     */
    public void establishConnection() {
        try {
            conn = DriverManager.getConnection(url, usernameDerby, passwordDerby);
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    /**
     * Create a new table
     */
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

        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    /**
     * Add a new game
     * @param gameName The game name 
     */
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

    /**
     * Add a game move to the game entry database
     * @param gameName The game name to add entry for
     * @param move The move
     */
    public synchronized void addGameMoveEntry(String gameName, String move) {
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
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
    }

    /**
     * Get game history given game name
     * @param gameName the game name to get history for
     * @return the history
     */
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
        } catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
        return history;
    }

    /**
     * Get array of game name
     * @return Array of game name
     */
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

    /**
     * Close the connection to the database.
     */
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
