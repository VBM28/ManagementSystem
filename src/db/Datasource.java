package db;

import login.LogInController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {

    public static final String DB_NAME = "db.db";


    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_LOGIN_USERNAME = "username";
    public static final String COLUMN_LOGIN_PASSWORD = "password";
    public static final String COLUMN_LOGIN_PERMISSION = "permission";
    public static final String COLUMN_LOGIN_ID = "_id";

    public static final String TABLE_MEMBERS = "members";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DOB = "dob";

    public static final String QUERY_LOGIN = "SELECT " + TABLE_LOGIN + "." + COLUMN_LOGIN_ID + " FROM " + TABLE_LOGIN + " WHERE " +
            TABLE_LOGIN + "." + COLUMN_LOGIN_USERNAME + " = ? " + "AND " + TABLE_LOGIN + "." + COLUMN_LOGIN_PASSWORD + " = ? " +
            " AND " + TABLE_LOGIN + "." + COLUMN_LOGIN_PERMISSION + " = ?";

    public static final String QUERY_MEMBERS = "SELECT * FROM " + TABLE_MEMBERS + " ORDER BY " + COLUMN_ID;

    public static final String INSERT_MEMBER = "INSERT INTO " + TABLE_MEMBERS + "(" +
            COLUMN_ID + ", " + COLUMN_FIRST_NAME + ", " + COLUMN_LAST_NAME + ", " + COLUMN_EMAIL + ", " +
            COLUMN_DOB + ") VALUES(?, ?, ?, ?, ?)";

    public static final String DELETE_MEMBER = "DELETE FROM " + TABLE_MEMBERS + " WHERE " + COLUMN_ID + " = ? AND " +
            COLUMN_FIRST_NAME + " = ? AND " + COLUMN_LAST_NAME + " = ? AND " + COLUMN_EMAIL + " = ? AND " +
            COLUMN_DOB + " = ?";
    public static final String USER_MEMBERS_DETAILS = "SELECT " + TABLE_MEMBERS + "." + COLUMN_ID + ", " + TABLE_MEMBERS + "." +
            COLUMN_FIRST_NAME + ", " + TABLE_MEMBERS + "." + COLUMN_LAST_NAME + ", " + TABLE_MEMBERS + "." + COLUMN_EMAIL +
            ", " + TABLE_MEMBERS + "." + COLUMN_DOB + " FROM " + TABLE_MEMBERS + " INNER JOIN " +
            TABLE_LOGIN + " ON " + TABLE_LOGIN + "." + COLUMN_LOGIN_ID + " = " + TABLE_MEMBERS + "." + COLUMN_ID +
            " WHERE " + TABLE_LOGIN + "." + COLUMN_LOGIN_ID + " = ?";

    private Connection connection;

    public static void main(String[] args) {
        System.out.println(USER_MEMBERS_DETAILS);
        System.out.println(QUERY_LOGIN);
    }

    private static Datasource instance = new Datasource();

    public boolean openDB() {
        //initializing connection
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("SQLException, DB connection problem: " + e.getMessage());
            return false;
        }
    }

    public void closeDB() {
        //closing connection
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Problem closing connection: " + e.getMessage());
        }
    }


    public static Connection getConnectionString() throws SQLException {

        try {
            //Connecting to DB
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(CONNECTION_STRING);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException in DataSource: " + e.getMessage());
            return null;
        }

    }

}
