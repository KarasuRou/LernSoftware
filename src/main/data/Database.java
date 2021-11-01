package data;

import logic.startup.Launcher;
import logic.miscellaneous.Output;

import java.sql.*;
import java.util.Arrays;

/**
 * <p>The Database class is used to write and read data from one preset SQLite-Database.</p>
 * <p>Get the Database-Connection e.g. with:</p>
 * {@code Database database = Database.getInstance();}
 * {@code database.initDBConnection();}
 * <p>Now you can use the whole Database-Connection.</p>
 */
public class Database {

    private static final Database database = new Database();
    private static Connection connection;
    private static final String DB_PATH = Launcher.APPLICATION_PATH + Launcher.FILE_SEPARATOR + "Storage.db";
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Output.exceptionWrite(e,"Fehler beim Laden des JDBC-Treibers!");
        }
    }

    private Database(){}

    /**
     * <p>This Method is the "Constructor" for the Database.</p>
     * <p>This is the only way to access the Database.</p>
     * @return {@link Database} instance
     */
    public static Database getInstance(){
        return database;
    }


    /**
     * <p>Initiate the Database Connection, should be called to access the Database.</p>
     */
    public void initDBConnection() throws SQLException {
        if (connection != null) {
            return;
        }
        Output.write("Creating Connection to Database...");
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        connection.setClientInfo("autoReconnect","true");
        if (!connection.isClosed()) {
            Output.write("Database Connection established!");
            Output.write("Path: " + DB_PATH);
            statement = connection.createStatement();
        }
    }

    /**
     * <p>Executes a select query for the Database and returns the results in a ResultSet</p>
     * @param sql SQL query
     * @return a {@linkplain ResultSet} object, which contains all results for the select-query
     * @throws SQLException if the sql query was faulty the Exception gets thrown
     */
    public ResultSet executeSelectQuery(String sql) throws SQLException {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            Output.exceptionWrite(e,"Couldn't handle DB-Query");
            throw e;
        }
    }

    /**
     * <p>Creates and returns a PreparedStatement, which should be used outside this class.</p>
     * <p>Call {@linkplain #executeInsertQuery()} ()} when all Batches are added.</p>
     * <p>Example for an Insert:</p>
     * <br>
     * <p>
     * {@code preparedStatement = database.startInsertQuery(sql);}<br>
     * {@code preparedStatement.setString(1, "Willi Winzig");}<br>
     * {@code preparedStatement.setString(2, "Willi's Wille");}<br>
     * {@code preparedStatement.setDate(3, Date.valueOf("2011-05-16"));}<br>
     * {@code preparedStatement.setInt(4, 432);}<br>
     * {@code preparedStatement.setDouble(5, 32.95);}<br>
     * {@code preparedStatement.addBatch();}<br>
     * {@code database.executeInsertQuery();}<br>
     * </p>
     * @param sql should be something like:<br>{@code "INSERT INTO books VALUES (?, ?, ?, ?, ?);"}
     * @return a PreparedStatement, which should be used to Insert new Data to the Table.
     * @throws SQLException if the sql query was faulty the Exception gets thrown
     */
    public PreparedStatement startInsertQuery(String sql) throws SQLException {
        try {
            return preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new SQLException("Couldn't handle DB-Query");
        }
    }

    /**
     * <p>Execute the insert query which was handled beforehand (not in this method).</p>
     * @return the generated ID
     */
    public int executeInsertQuery() throws SQLException {
        executePreparedStatement();
        return preparedStatement.getGeneratedKeys().getInt(1);
    }

    /**
     * <p>Creates and returns a PreparedStatement, which should be used outside this class.</p>
     * <p>Call {@linkplain #executeUpdateQuery()} when all Batches are added.</p>
     * <p>Example for an Update:</p>
     * <br>
     * <p>
     * {@code preparedStatement = database.startUpdateQuery(sql);}<br>
     * {@code preparedStatement.setString(1,"Anton's Alarm 2");}<br>
     * {@code preparedStatement.setString(2,"Anton's Alarm");}<br>
     * {@code preparedStatement.addBatch();}<br>
     * {@code database.executeUpdateQuery();}<br>
     * </p>
     * @param sql should be something like:<br>{@code "UPDATE books SET title = ? WHERE title LIKE ?"}
     * @return a PreparedStatement, which should be used to update objects in a Table
     * @throws SQLException if the sql query was faulty the Exception gets thrown
     */
    public PreparedStatement startUpdateQuery(String sql) throws SQLException {
        try {
            return preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new SQLException("Couldn't handle DB-Query");
        }
    }

    /**
     * <p>Execute the update query which was handled beforehand (not in this method).</p>
     * <p><b>NOTE:</b></p>
     * <p>Returns -1 if {@linkplain #startUpdateQuery} was not used, because no UpdateQuery was set.</p>
     * @return int - how many objects are effected
     */
    public int executeUpdateQuery() {
        return executePreparedStatement();
    }

    /**
     * <p>Creates and returns a PreparedStatement, which should be used outside this class.</p>
     * <p>Call {@linkplain #executeDeleteQuery()} when all Batches are added.</p>
     * <p>Example for delete:</p>
     * <br>
     * <p>
     * {@code preparedStatement = database.startDeleteQuery(sql);}<br>
     * {@code preparedStatement.setString(1,"Anton's Alarm");}<br>
     * {@code preparedStatement.addBatch();}<br>
     * {@code database.executeDeleteQuery();}<br>
     * </p>
     * @param sql should be something like:<br>{@code "DELETE FROM books WHERE title LIKE ?"}
     * @return a PreparedStatement, which should be used to delete objects in a Table
     * @throws SQLException if the sql query was faulty the Exception gets thrown
     */
    public PreparedStatement startDeleteQuery(String sql) throws SQLException {
        try{
            return preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new SQLException("Couldn't handle DB-Query");
        }
    }

    /**
     * <p>Execute the delete query which was handled beforehand (not in this method).</p>
     * <p><b>NOTE:</b></p>
     * <p>Returns -1 if {@linkplain #startDeleteQuery} was not used, because no DeleteQuery was set.</p>
     * @return int - how many objects are effected
     */
    public int executeDeleteQuery(){
        return executePreparedStatement();
    }

    /**
     * <p>This method should be called, to close the DB Connection.</p>
     * <p>This will lead to a clean shutdown.</p>
     */
    public void closeDBConnection(){
        try {
            if (connection != null && !connection.isClosed()) {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
                if (!statement.isClosed()) {
                    statement.close();
                }
                connection.close();
                if (connection.isClosed()) {
                    Output.write("Connection to Database closed");
                }
                connection = null;
                preparedStatement = null;
                statement = null;
            }
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
    }

    private int executePreparedStatement() {
        int returning = -1;
        try {
            if (preparedStatement == null || preparedStatement.isClosed()) {
                return returning;
            }
            connection.setAutoCommit(false);
            returning = Arrays.stream(preparedStatement.executeBatch()).sum();
            connection.setAutoCommit(true);
            preparedStatement.close();
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
        return returning;
    }
}
