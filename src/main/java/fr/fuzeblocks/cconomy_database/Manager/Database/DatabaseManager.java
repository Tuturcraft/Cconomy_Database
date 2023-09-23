package fr.fuzeblocks.cconomy_database.Manager.Database;

public class DatabaseManager {
    private DbConnection moneyconnection;

    public DatabaseManager() {
        this.moneyconnection = new DbConnection(new DbCredentials("node.fuzeblocks.fr","u4_0jwkfZK9m1","lRYLFgn7cdZU2^ZiXb7O.+5i","s4_Economy",3306));
    }
    public void close() {
        this.moneyconnection.close();
    }

    public DbConnection getMoneyconnection() {
        return moneyconnection;
    }
}
