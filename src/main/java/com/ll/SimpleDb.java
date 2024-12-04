package com.ll;

import lombok.RequiredArgsConstructor;

import java.sql.*;

@RequiredArgsConstructor
public class SimpleDb {
    private final String host;
    private final String id;
    private final String pw;
    private final String db;

    public void run(String query){
        String url = "jdbc:mysql://localhost:3306/"+ db;
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url, id, pw);
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

    }
}
