package main;

import java.sql.*;

public class DatabaseManager {

    String DATABASE_NAME = "game.db";

    public DatabaseManager(){
        Connection connection = null;
        try{
            // establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_NAME);
            Statement s = connection.createStatement();
            s.setQueryTimeout(30); // set timeout to 30 sec

            // create table if it doesn't exist
            String createTableStatement = "create table if not exists player (username varchar(25), PIN char(4), file_name varchar(50))";
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

    private String getSaveFileNameFromDatabase(String username, String PIN){
        String result = "";
        return result;
    }

    private void saveGameToDatabase(String username, String PIN){

    }

}
