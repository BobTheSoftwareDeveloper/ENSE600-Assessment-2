package com.bobliou.chessgame.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCDemo {

    public static void main(String[] args) {

        DBOperations dboperations = new DBOperations();
        dboperations.establishConnection();
        dboperations.createTable();
        dboperations.getQuery();
        dboperations.closeConnections();
    }
}
