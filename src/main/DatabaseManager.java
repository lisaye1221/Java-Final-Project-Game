package main;

import java.sql.*;

public class DatabaseManager {

    String DATABASE_NAME = "game.db";
    String TABLE_NAME = "player";

    public DatabaseManager(){
        Connection connection = null;
        try{
            // establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_NAME);
            Statement s = connection.createStatement();
            s.setQueryTimeout(30); // set timeout to 30 sec

            // create table if it doesn't exist
            String createTableStatement = "create table if not exists " + TABLE_NAME +" (username varchar(25), PIN char(4), file_name varchar(50))";
            s.executeUpdate(createTableStatement);

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(connection != null) {
                    connection.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }

    public String getSaveFileNameFromDatabase(String username, String PIN){
        String result = "";

        Connection connection = null;
        try{
            // establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_NAME);
            Statement s = connection.createStatement();
            s.setQueryTimeout(30); // set timeout to 30 sec

            // add entry
            String query = String.format("SELECT file_name FROM %s WHERE username = '%s' AND PIN='%s'", TABLE_NAME, username, PIN);
            ResultSet rs = s.executeQuery(query);

            result = rs.getString("file_name");
            System.out.println("file name: " + result);

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(connection != null) {
                    connection.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }

        return result;
    }

    public void saveNewGameToDatabase(String usernameData, String PINData, String filenameData){
        Connection connection = null;
        try{
            // establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_NAME);
            Statement s = connection.createStatement();
            s.setQueryTimeout(30); // set timeout to 30 sec

            // add entry
            String addStatement = String.format("Insert into %s (username, PIN, file_name) Values ('%s', '%s', '%s')", TABLE_NAME, usernameData, PINData, filenameData);
            s.executeUpdate(addStatement);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(connection != null) {
                    connection.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }

}
