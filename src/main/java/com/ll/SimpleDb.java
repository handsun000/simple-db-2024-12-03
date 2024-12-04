package com.ll;

import lombok.RequiredArgsConstructor;

import java.sql.*;

@RequiredArgsConstructor
public class SimpleDb {
    private final String host;
    private final String id;
    private final String pw;
    private final String db;

    private Connection connection;

    public void run(String query){
        String url = "jdbc:mysql://"+host+":3306/"+ db;
        try{
            connection = DriverManager.getConnection(url, id, pw);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();

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

    public void run(String query, String title, String body, boolean isBlind) {
        String url = "jdbc:mysql://"+host+":3306/"+ db;
        try{
            connection = DriverManager.getConnection(url, id, pw);
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, title);
            ps.setString(2, body);
            ps.setBoolean(3, isBlind);

            int rows = ps.executeUpdate();
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
