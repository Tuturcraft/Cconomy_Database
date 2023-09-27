package fr.fuzeblocks.cconomy_database.Manager.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnection {
    private DbCredentials credentials;
    private Connection connection;
    public DbConnection(DbCredentials credentials) {
        this.credentials = credentials;
        this.connect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.credentials.toURI(),this.credentials.getUser(),this.credentials.getPass());
            Logger.getLogger("Cconomy").warning("Successfully connected to DB.");
        } catch (SQLException | ClassNotFoundException e) {
           System.err.println("Error when connecting to DB !");
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                return this.connection;
            }
        }
        connect();
        return this.connection;
    }
}
