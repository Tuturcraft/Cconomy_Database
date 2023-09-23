package fr.fuzeblocks.cconomy_database.Manager.Database;

public class DatabaseManager {
    private DbConnection moneyconnection;

    public DatabaseManager() {
        this.moneyconnection = new DbConnection(new DbCredentials("your-ip","your-login","your-pass","your-databasename",3306));
    }
    public void close() {
        this.moneyconnection.close();
    }

    public DbConnection getMoneyconnection() {
        return moneyconnection;
    }
}
