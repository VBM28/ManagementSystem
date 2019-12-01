package login;


import db.Datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInModel {
    private Connection connection;

    public LogInModel() {
        try {
            //creating db connection
            this.connection = Datasource.getConnectionString();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        if (this.connection == null) {
            //if no connection, exit application
            System.exit(1);
        }
    }

    public boolean isDbConnected() {
        return this.connection != null;
    }

    public int isLogin(String user, String pass, String permission) throws Exception {
        //initiating statement and result set
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            //setting the parameters
            statement = this.connection.prepareStatement(Datasource.QUERY_LOGIN);
            statement.setString(1, user);
            statement.setString(2, pass);
            statement.setString(3, permission);

            //executing statement
            results = statement.executeQuery();

            //checking if there is a login record
            //if there is, return the id, else, return -1
            if (results.next()) {
                return results.getInt(1);
            } else return -1;

        } catch (SQLException e) {
            System.out.println("SQLException in LogInModel, isLogin: " + e.getMessage());
            return -1;
        } finally {
            //closing connections
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException, Couldn't close ResultSet in LoginModel, isLogin: " + e.getMessage());
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException, Couldn't close PreparedStatement in LoginModel, isLogin: " + e.getMessage());
            }
        }
    }

}
