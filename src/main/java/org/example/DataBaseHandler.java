package org.example;

import java.sql.*;

public class DataBaseHandler extends ConfigForDB {
    Connection connection;
    public Connection getDBConnection() throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" +  dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return connection;
    }

    public boolean addUser(String id, String nikname, String numberPhone){
        String insert = "INSERT INTO " + Const.USER_TABEL + "(" + Const.USERS_ID + ", " + Const.USERS_NIKNAME + ", " + Const.USERS_NUMBERPHONE + ")" + " VALUES(?,? ,?)";

        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(insert);

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, nikname);
            preparedStatement.setString(3, numberPhone);

            preparedStatement.execute();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getUserInfo(String id){
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + Const.USER_TABEL + " WHERE " + Const.USERS_ID + "=?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(select);

            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}

